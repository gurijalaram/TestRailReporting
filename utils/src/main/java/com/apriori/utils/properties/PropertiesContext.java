package com.apriori.utils.properties;

import com.apriori.utils.FileResourceUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.util.Optional;

public class PropertiesContext {

    private static final String DEFAULT_PROPERTIES_KEY = "/default";

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
        return Optional.ofNullable(
            System.getProperty(convertToSystemPropertyTemplate(propertyName))
        ).orElseGet(
            () -> getFromPropertyContext(convertToPropertyPathTemplate(propertyName))
        );
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
        String readyToWorkPropertyPath = parsePropertyOnReferencesPresents(propertyName);

        String propertyValue = parsePropertyOnReferencesPresents(
            propertiesContext.at(readyToWorkPropertyPath).asText()
        );

        if (StringUtils.isBlank(propertyValue)) {
            propertyValue = getDefaultValueThrowExceptionIfMissed(readyToWorkPropertyPath);
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

    private static String convertToSystemPropertyTemplate(String propertyName) {
        return propertyName.replace(".", "_");
    }

    private static String convertToPropertyPathTemplate(String propertyName) {
        return "/" + propertyName.replace(".", "/");
    }
}
