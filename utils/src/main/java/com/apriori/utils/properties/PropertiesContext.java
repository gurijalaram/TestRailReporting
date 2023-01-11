package com.apriori.utils.properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Stack;

@Slf4j
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

    @SneakyThrows
    private static JsonNode loadProperties(final String fileName) {
        return mapper.readTree(
            ClassLoader.getSystemClassLoader().getResourceAsStream(fileName)
        );
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
     * Get property from configurations files.
     *  see {@link configurations}
     *
     * @param propertyName
     * @return
     */
    public static String get(final String propertyName) {
        return Optional.ofNullable(
            System.getProperty(convertToSystemPropertyTemplate(propertyName))
        ).orElseGet(
            () -> getFromPropertyContext(convertToPropertyPathTemplate(propertyName))
        );
    }

    private static String getFromPropertyContext(String propertyName) {
        final String readyToWorkPropertyPath = parsePropertyOnReferencesPresentsAndGetValue(propertyName);

        String propertyValue = propertiesContext.at(readyToWorkPropertyPath).asText();

        if (StringUtils.isBlank(propertyValue)) {
            propertyValue = getDefaultValueThrowExceptionIfMissed(readyToWorkPropertyPath);
        }

        if (isPropertyContainPropertyReference(propertyValue)) {
            return parsePropertyOnReferencesPresentsAndGetValue(propertyValue);
        }

        return propertyValue;
    }

    private static String getDefaultValueThrowExceptionIfMissed(String propertyPath) {
        String propertyValue = parsePropertyOnReferencesPresentsAndGetValue(
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

    private static String parsePropertyOnReferencesPresentsAndGetValue(String propertyPath) {
        if (isPropertyContainPropertyReference(propertyPath)) {

            Stack<StringBuilder> propertiesDividedPathAndReferences = divideReferencesAndPropertyPath(propertyPath);
            String finalPath = combineReferencesValuesAndPropertyPath(propertiesDividedPathAndReferences);

            return parsePropertyOnReferencesPresentsAndGetValue(finalPath);
        }

        return propertyPath;
    }

    private static String combineReferencesValuesAndPropertyPath(Stack<StringBuilder> propertiesDividedPathAndReferences) {
        StringBuilder finalPath = new StringBuilder();

        for (StringBuilder pathPart : propertiesDividedPathAndReferences) {
            String property = pathPart.toString();

            // If stack element is reference, get the value by this reference
            if (isPropertyContainPropertyReference(property)) {
                String workingPath = updateReferenceToWorkingPath(property);
                String value = getValueByWorkingPath(workingPath);

                if(StringUtils.isEmpty(value)) {
                    log.warn("Property: {}, doesn't exist for the current environment. Getting the default property.", property);
                    value = updateToDefaultPathAndGetValue(workingPath);
                }

                finalPath.append(value);
                continue;
            }

            finalPath.append(property);
        }

        return finalPath.toString();
    }

    private static Stack<StringBuilder> divideReferencesAndPropertyPath(String propertyPath) {
        Stack<StringBuilder> parsedProperties = new Stack<StringBuilder>() {{
            add(new StringBuilder());
        }};

        for (char propertySymbol : propertyPath.toCharArray()) {
            // if it is reference start, create a new stack element and start recording into this
            // else append symbol to existing stack element
            if (propertySymbol == '$') {
                parsedProperties.add(new StringBuilder(String.valueOf(propertySymbol)));
            } else {
                parsedProperties.peek().append(propertySymbol);

                // if it is reference end, create a new stack element for the next data
                if (propertySymbol == '}') {
                    parsedProperties.add(new StringBuilder());
                }
            }
        }

        return parsedProperties;
    }

    private static String updateToDefaultPathAndGetValue(String property) {
        final String defaultPropertyPath = updateToDefaultPropertyPath(property);
        final String propertyValue =  propertiesContext.at(defaultPropertyPath).asText();

        if(StringUtils.isEmpty(propertyValue)) {
            log.error("Default property: {} doesn't exist. Please add required property into default section from global-config.yml file \n" +
                "or add required property into appropriate <environment>-config.yml file.", defaultPropertyPath);
            throw new IllegalArgumentException();
        }

        return propertyValue;
    }

    private static String getValueByWorkingPath(final String property) {
        return propertiesContext.at(property).asText();
    }

    private static String updateReferenceToWorkingPath(String reference) {
       return "/" + StringUtils.substringBetween(reference.replace(".", "/"), variableMarker[0], variableMarker[1]);
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
