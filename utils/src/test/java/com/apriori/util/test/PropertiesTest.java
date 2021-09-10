package com.apriori.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.apriori.utils.properties.PropertiesContext;

import org.junit.Test;

/**
 * Contain tests for the basic functionality of @{@link PropertiesContext}
 */
public class PropertiesTest {


    @Test
    public void testGetGlobalProperty() {
        final String globalProperty = PropertiesContext.get("global.schema_base_path");

        assertNotNull("Global property should exist.", globalProperty);
    }

    @Test
    public void testGetEnvironmentProperty() {
        final String envValue = PropertiesContext.get("env");
        final String specificEnvPropertyValue = PropertiesContext.get("${env}.secret_key");

        assertFalse("Environment property should exist.", envValue.isEmpty());
        assertFalse("Specific environment property should exist.", specificEnvPropertyValue.isEmpty());
    }

    @Test
    public void testGetSystemProperty() {
        final String PROPERTY_KEY_CODE_TEMPLATE = "int-core.my_test_property";
        final String PROPERTY_KEY_ENV_TEMPLATE = "int-core_my_test_property";
        final String PROPERTY_VALUE = "propertyValue";

        System.setProperty(PROPERTY_KEY_ENV_TEMPLATE, PROPERTY_VALUE);

        final String receivedPropertyValue = PropertiesContext.get(PROPERTY_KEY_CODE_TEMPLATE);

        assertEquals("Property value should be the same", receivedPropertyValue, PROPERTY_VALUE);
    }

    @Test
    public void testGetDefaultProperty() {
        final String PROPERTY_KEY = "${env}.fms.api_url";

        assertFalse("Property value should be the same",
            PropertiesContext.get(PROPERTY_KEY).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissedProperty() {
        PropertiesContext.get("NOT.EXISTING.PROPERTY");
    }

}
