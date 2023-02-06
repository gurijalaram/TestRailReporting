package com.apriori.utils.properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

@Slf4j
public class PropertiesContext {

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static final JsonNode propertiesContext;

    private static final String ENVIRONMENT_NAME;
    private static final String DEFAULT_PROPERTIES_KEY = "/default";
    private static final String[] variableMarker = {"${", "}"};

    static {

        final JsonNode globalPropertiesContext = loadProperties("configurations/global-config.yml");
        final JsonNode envPropertiesContext = loadProperties("configurations/environments-config.yml");
        final JsonNode customersPropertiesContext = loadProperties("configurations/customers-config.yml");

        propertiesContext = mergePropertiesIntoOneContext(globalPropertiesContext, envPropertiesContext, customersPropertiesContext);

        ENVIRONMENT_NAME = propertiesContext.at("/env").asText();
    }

    @SneakyThrows
    private static JsonNode loadProperties(final String fileName) {
        InputStream fileData = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);

        if (fileData == null) {
            log.error("Property file {} was not found. Please check the utils/resource folder on file presence", fileName);
            throw new IllegalArgumentException();
        }

        return mapper.readTree(
            fileData
        );
    }

    public static JsonNode mergePropertiesIntoOneContext(JsonNode... propertiesFiles) {
        ObjectNode objectNode = mapper.createObjectNode();

        for (int i = 0; i < propertiesFiles.length; i++) {
            objectNode.setAll((ObjectNode) propertiesFiles[i]);
        }

        return objectNode;
    }

    /**
     * Get property from configurations files.
     * see {@link configurations}
     *
     * @param propertyName
     * @return
     */
    public static String get(String propertyName) {
        // try to find system property
        String propertyValue = System.getProperty(convertToSystemPropertyTemplate(propertyName));

        propertyName = convertToPropertyPathTemplate(propertyName);

        // try to find property
        if (StringUtils.isEmpty(propertyValue)) {
            propertyValue = getFromPropertyContext(propertyName);
        }

        // try to find environment property
        if (StringUtils.isEmpty(propertyValue) && !propertyName.startsWith("/" + ENVIRONMENT_NAME) ) {
            if (!propertyName.startsWith("/${env}")) {
                propertyName = ENVIRONMENT_NAME + propertyName;
            }

            propertyValue = getEnvironmentValue(propertyName);
        }

        // try to find default property
        if (StringUtils.isEmpty(propertyValue)) {
            propertyValue = getDefaultValueThrowExceptionIfMissed(propertyName);
        }

        return propertyValue;

    }

    private static String getFromPropertyContext(String propertyPath) {

        propertyPath = convertToPropertyPathTemplate(propertyPath);

        if (isPropertyContainPropertyReference(propertyPath)) {
            propertyPath = findAndReplaceVariables(propertyPath);
        }

        if (propertyPath.equals("/" + ENVIRONMENT_NAME)) {
            return ENVIRONMENT_NAME;
        }

        String propertyValue = propertiesContext.at(propertyPath).asText();

        if (isPropertyContainPropertyReference(propertyValue)) {
            propertyValue = findAndReplaceVariables(propertyValue);
        }

        return propertyValue;
    }

    private static String getEnvironmentValue(String propertyPath) {
        log.info("PropertyContext: getting environment property by path: {}", propertyPath);
        return getFromPropertyContext(propertyPath);
    }

    private static String findAndReplaceVariables(final String key) {
        String result = key;
        Integer startIndex;

        do {
            startIndex = result.lastIndexOf(variableMarker[0]);

            int endIndex = result.indexOf(variableMarker[1], startIndex);

            if (endIndex == -1) {
                break;
            }

            String keyToReplace = result.substring(startIndex, endIndex + 1);
            String varName = keyToReplace.replaceAll("[${}]", "");
            String content = get(varName);
            result = result.replace(keyToReplace, content);

        } while (startIndex != -1);

        return result;
    }

    private static String getDefaultValueThrowExceptionIfMissed(String propertyPath) {
        final String propertyPathToGet = updatePropertyPathWithAStartKey(DEFAULT_PROPERTIES_KEY,
            convertToPropertyPathTemplate(propertyPath)
        );
        log.info("PropertyContext: getting default property by path: {}", propertyPathToGet);

        String propertyValue = getFromPropertyContext(propertyPathToGet);

        if (StringUtils.isBlank(propertyValue)) {
            throw new IllegalArgumentException(
                String.format("Property with path: %s not present.", propertyPath)
            );
        }

        return propertyValue;
    }

    private static String updatePropertyPathWithAStartKey(String startKey, String readyToWorkPropertyPath) {
        return startKey +
            readyToWorkPropertyPath.substring(
                StringUtils.ordinalIndexOf(readyToWorkPropertyPath, "/", 2)
            );
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
