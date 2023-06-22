package com.apriori.util.test;

import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


/**
 * Contain tests for the basic functionality of @{@link PropertiesContext}
 */
@Slf4j
public class PropertiesContextTest {

    @Test
    public void testGetGlobalProperty() {
        final String globalProperty = PropertiesContext.get("global.schema_base_path");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(globalProperty).isNotEmpty();
        softAssertions.assertAll();
    }

    @Test
    public void testGetEnvironmentProperty() {
        final String envValue = PropertiesContext.get("env");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(envValue).isNotEmpty();
        softAssertions.assertAll();
    }

    @Test
    public void testGetEnvironmentPropertyWithReferences() {
        final String deploymentUrlPart = PropertiesContext.get("${${deployment}.url_part}");
        final String env = PropertiesContext.get("env");
        final String version = PropertiesContext.get("version");
        final String awsRegion = PropertiesContext.get("aws_region");

        final String expectedCasCoreServiceValue = String.format("https://cas-api.%s.%s.apriori.net/", awsRegion, env);
        final String expectedBcsDeploymentValue = String.format("https://bcs.%s-v%s.%s.apriori.net/", awsRegion, version, deploymentUrlPart);

        final String specificEnvPropertyCoreServiceValue = PropertiesContext.get("cas.api_url");
        final String specificEnvDeploymentValue = PropertiesContext.get("bcs.api_url");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(specificEnvPropertyCoreServiceValue).isEqualTo(expectedCasCoreServiceValue);
        softAssertions.assertThat(specificEnvDeploymentValue).isEqualTo(expectedBcsDeploymentValue);
        softAssertions.assertAll();
    }

    @Test
    public void testGetSystemProperty() {
        final String PROPERTY_KEY_CODE_TEMPLATE = "int-core.my_test_property";
        final String PROPERTY_KEY_ENV_TEMPLATE = "int-core_my_test_property";
        final String PROPERTY_VALUE = "propertyValue";

        System.setProperty(PROPERTY_KEY_ENV_TEMPLATE, PROPERTY_VALUE);

        final String receivedPropertyValue = PropertiesContext.get(PROPERTY_KEY_CODE_TEMPLATE);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(receivedPropertyValue).isEqualTo(PROPERTY_VALUE);
        softAssertions.assertAll();
    }

    @Test
    public void testGetDefaultProperty() {
        final String PROPERTY_KEY = "fms.api_url";
        final String value = PropertiesContext.get(PROPERTY_KEY);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(value).isNotEmpty();
        softAssertions.assertAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissedProperty() {
        PropertiesContext.get("NOT.EXISTING.PROPERTY");
    }

    @Test
    public void testToValidatePropertyReferences() {
        final String referenceToValidateValue = PropertiesContext.get("${customer}.${${customer}.aws_account_type}.token_subject");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(referenceToValidateValue).isNotEmpty();
        softAssertions.assertAll();

        log.info("Property value: {}", referenceToValidateValue);
    }
}
