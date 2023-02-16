package com.apriori.util.test;

import com.apriori.utils.email.GraphEmailService;
import com.apriori.utils.email.response.EmailMessage;
import com.apriori.utils.pdf.PDFDocument;
import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Contain tests for the basic functionality of @{@link PropertiesContext}
 */
@Slf4j
public class PropertiesContextTest {

    // TODO gurijalaram16
    // this test should be moved from this place
    @Ignore
    @Test
    public void testEncrypt() {
        SoftAssertions softAssertions = new SoftAssertions();
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments("ap-int123456");
        PDFDocument pdfDocument = emailMessage.emailMessageAttachment().getFileAttachment();
        log.info(pdfDocument.getDocumentContents());
        softAssertions.assertThat(pdfDocument).isNotNull();
        softAssertions.assertAll();
    }

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
        final String PROPERTY_KEY = "${env}.fms.api_url";
        final String value = PropertiesContext.get(PROPERTY_KEY);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(value).isNotEmpty();
        softAssertions.assertAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissedProperty() {
        PropertiesContext.get("NOT.EXISTING.PROPERTY");
    }

}
