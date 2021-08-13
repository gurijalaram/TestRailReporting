package com.apriori.utils.properties;

import com.apriori.utils.FileResourceUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;

public class PropertiesContext {

    private static final String[] variableMarker = {"${", "}"};
    private static final JsonNode propertiesContext;

    static {
        propertiesContext = loadProperties();
    }

    /**
     * Get property from config.yml file.
     * @param propertyName
     * @return
     */
    public static String get(final String propertyName) {
        return System.getProperty(propertyName,
            getFromPropertyContext(propertyName));
    }

    @SneakyThrows
    private static JsonNode loadProperties() {
        return new ObjectMapper(new YAMLFactory()).readTree(
            new FileInputStream(
                FileResourceUtil.getResourceAsFile("config.yml")
            )
        );
    }

    private static String getFromPropertyContext(String propertyName) {
        String propertyPath = parsePropertyOnReferencesPresents(
            convertToPropertyPathTemplate(propertyName)
        );

        return parsePropertyOnReferencesPresents(
            propertiesContext.at(propertyPath).asText()
        );
    }

    private static String parsePropertyOnReferencesPresents(String propertyPath) {
        if (isPropertyContainPropertyReference(propertyPath)) {
            for (String reference : StringUtils.substringsBetween(propertyPath, variableMarker[0], variableMarker[1])) {
                propertyPath = replacePropertyReferenceWithAppropriateValue(propertyPath, reference);
            }
        }

        return propertyPath;
    }

    private static String replacePropertyReferenceWithAppropriateValue(final String propertyValue, final String reference) {
        return propertyValue.replace(variableMarker[0] + reference + variableMarker[1], get(reference));
    }

    private static boolean isPropertyContainPropertyReference(String propertyValue) {
        return propertyValue.contains(variableMarker[0]) && propertyValue.contains(variableMarker[1]);
    }

    private static String convertToPropertyPathTemplate(String propertyName) {
        return "/" + propertyName.replace(".", "/");
    }
}
