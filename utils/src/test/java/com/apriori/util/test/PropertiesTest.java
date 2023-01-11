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
public class PropertiesTest {

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
        final String specificEnvPropertyValue = PropertiesContext.get("${env}.bcs.api_url");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(specificEnvPropertyValue).isEqualTo("https://bcs-http.na-1-v22-1.qa-test.apriori.net/");
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
