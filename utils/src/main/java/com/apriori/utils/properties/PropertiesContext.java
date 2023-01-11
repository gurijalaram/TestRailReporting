package com.apriori.utils.properties;

import com.apriori.utils.FileResourceUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;

public class PropertiesContext {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static final JsonNode propertiesContext;

    private static final String DEFAULT_PROPERTIES_KEY = "/default";
    private static final String[] variableMarker = {"${", "}"};

    static {
        final JsonNode globalPropertiesContext = loadProperties("configurations/global-config.yml");
        final String envName = globalPropertiesContext.at("/env").asText();

        final JsonNode envPropertiesContext = loadProperties("configurations/environments/" + envName + "-config.yml");

        propertiesContext = mergeCollection(globalPropertiesContext, envPropertiesContext);
    }

    public static JsonNode mergeCollection(JsonNode globalProperties, JsonNode envProperties) {
        ObjectNode objectNode = mapper.createObjectNode();

        if (globalProperties != null) {
            globalProperties.fields().forEachRemaining(kv -> objectNode.set(kv.getKey(), kv.getValue()));
        }

        if (envProperties != null) {
            envProperties.fields().forEachRemaining(kv -> objectNode.set(kv.getKey(), kv.getValue()));
        }

        return objectNode;
    }

    /**
     * Get property from config.yml file.
     *
     * @param propertyName
     * @return
     */
    public static String get(final String propertyName) {
        return Optional.ofNullable(
            System.getProperty(convertToSystemPropertyTemplate(propertyName))
        ).orElseGet(
//            () -> getFromPropertyContext(convertToPropertyPathTemplate(propertyName))
            () -> getFromPropertyContext(convertToPropertyPathTemplate(propertyName))
        );
    }

    @SneakyThrows
    private static JsonNode loadProperties(final String fileName) {
        // TODO z: add
        return mapper.readTree(
//            new FileInputStream(
            ClassLoader.getSystemClassLoader().getResourceAsStream(fileName)
//            )
        );
    }

    private static String getFromPropertyContext(String propertyName) {
        final String readyToWorkPropertyPath = parsePropertyOnReferencesPresents(propertyName);
        String propertyValue = propertiesContext.at(readyToWorkPropertyPath).asText();

        if (StringUtils.isBlank(propertyValue)) {
            propertyValue = getDefaultValueThrowExceptionIfMissed(convertToPropertyPathTemplate(readyToWorkPropertyPath));
        }

        return propertyValue;
    }

    private static String getDefaultValueThrowExceptionIfMissed(String propertyPath) {
        String propertyValue = parsePropertyOnReferencesPresents(
            propertiesContext.at(
                updateToDefaultPropertyPath(propertyPath)
            ).asText()
        );

        if (StringUtils.isBlank(propertyValue)) {
            throw new IllegalArgumentException(
                String.format("Property with path: %s not present.", propertyPath)
            );
        }

        return propertyValue;
    }

    private static String updateToDefaultPropertyPath(String readyToWorkPropertyPath) {
        return DEFAULT_PROPERTIES_KEY +
            readyToWorkPropertyPath.substring(
                StringUtils.ordinalIndexOf(readyToWorkPropertyPath, "/", 2)
            );
    }

    private static String parsePropertyOnReferencesPresents(String propertyPath) {
//        propertyPath = convertToPropertyPathTemplate(propertyPath);

        if (isPropertyContainPropertyReference(propertyPath)) {

            Stack<StringBuilder> propertiesReferencesQueue = new Stack<>();
            propertiesReferencesQueue.add(new StringBuilder());

            char[] propertyToParse = propertyPath.toCharArray();

            for (char element : propertyToParse) {
                if (element == '$') {
                    propertiesReferencesQueue.add(new StringBuilder(String.valueOf(element)));
                } else {
                    propertiesReferencesQueue.peek().append(element);
                    if (element == '}') {
                        propertiesReferencesQueue.add(new StringBuilder());
                    }
                }
            }

            StringBuilder finalPath = new StringBuilder();

            for (StringBuilder pathPart : propertiesReferencesQueue) {

                String property = pathPart.toString();
                if (isPropertyContainPropertyReference(property)) {
//                    finalPath.append(replacePropertyReferenceWithAppropriateValue(property));
                    String value = replacePropertyReferenceWithAppropriateValue(property);
                    if(StringUtils.isEmpty(value)) {
                        value = getDefaultValue(property);
                    }
                    finalPath.append(value);
                    continue;
                }

                finalPath.append(property);
            }

            return parsePropertyOnReferencesPresents(finalPath.toString());
//            return getFromPropertyContext(finalPath.toString());

//            for (String reference : StringUtils.substringsBetween(propertyPath, variableMarker[0], variableMarker[1])) {
//                propertyPath = replacePropertyReferenceWithAppropriateValue(propertyPath, reference);
//            }

        }

        return propertyPath;
    }

    private static String getDefaultValue(String property) {
        return propertiesContext.at( updateToDefaultPropertyPath("/" + StringUtils.substringBetween(property.replace(".", "/"), variableMarker[0], variableMarker[1]))).asText();

    }

    private static String replacePropertyReferenceWithAppropriateValue(final String reference) {
//        return propertyValue.replace(variableMarker[0] + reference + variableMarker[1], get(reference));
        return propertiesContext.at("/" + StringUtils.substringBetween(reference.replace(".", "/"), variableMarker[0], variableMarker[1])).asText();
    }

    private static boolean isPropertyContainPropertyReference(String propertyValue) {
        return propertyValue.contains(variableMarker[0]) && propertyValue.contains(variableMarker[1]);
    }

    private static String convertToSystemPropertyTemplate(String propertyName) {
        return propertyName.replace(".", "_");
    }

    private static String convertToPropertyPathTemplate(String propertyName) {
        propertyName = propertyName.replace(".", "/");

        return StringUtils.startsWith(propertyName, "/") ? propertyName : "/" + propertyName;
    }
}
