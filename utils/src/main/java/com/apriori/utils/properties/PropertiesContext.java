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

    @SneakyThrows
    private static JsonNode loadProperties() {
        return new ObjectMapper(new YAMLFactory()).readTree(
            new FileInputStream(
                FileResourceUtil.getResourceAsFile("config.yml")
            )
        );
    }

    public static String getStr(final String propertyName) {
        return get(propertyName);
    }

    public static Integer getInt(final String propertyName) {
        return Integer.valueOf(get(propertyName));
    }


    private static String get(final String propertyName) {
        return System.getProperty(propertyName,
            getFromPropertyContext(propertyName));
    }

    private static String getFromPropertyContext(String propertyName) {
        String propertyPath = convertToPropertyPathTemplate(propertyName);
        String propertyValue = propertiesContext.at(propertyPath).asText();

        if (isPropertyValueContainPropertyReference(propertyValue)) {
            for (String reference : StringUtils.substringsBetween(propertyValue, variableMarker[0], variableMarker[1])) {
                propertyValue = replacePropertyReferenceWithAppropriateValue(propertyValue, reference);
            }
        }

        return propertyValue;
    }

    private static String replacePropertyReferenceWithAppropriateValue(final String propertyValue, final String reference) {
        return propertyValue.replace(variableMarker[0] + reference + variableMarker[1], get(reference));
    }

    private static boolean isPropertyValueContainPropertyReference(String propertyValue) {
        return propertyValue.contains(variableMarker[0]) && propertyValue.contains(variableMarker[1]);
    }

    private static String convertToPropertyPathTemplate(String propertyName) {
        return "/" + propertyName.replace(".", "/");
    }
}
