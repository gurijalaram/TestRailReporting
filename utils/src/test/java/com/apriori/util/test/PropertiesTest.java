package com.apriori.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.apriori.utils.email.GraphEmailService;
import com.apriori.utils.email.response.EmailMessage;
import com.apriori.utils.pdf.PDFDocument;
import com.apriori.utils.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.IOException;


/**
 * Contain tests for the basic functionality of @{@link PropertiesContext}
 */
@Slf4j
public class PropertiesTest {

    @Test
    public void testEncrypt() throws IOException {
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

        assertNotNull("Global property should exist.", globalProperty);
    }

    @Test
    public void testGetEnvironmentProperty() {
        final String envValue = PropertiesContext.get("env");
        final String specificEnvPropertyValue = PropertiesContext.get("${env}.bcs.api_url");
//        final String specificEnvPropertyValue = PropertiesContext.get("https://bcs-http.${default.aws_region}-v22-1.${${env}.env_url_specification}.apriori.net/");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(specificEnvPropertyValue).isEqualTo("https://bcs-http.na-1-v22-1.update_this_value_in_environment_file.apriori.net/");
        softAssertions.assertThat(specificEnvPropertyValue).isEqualTo("https://bcs-http.na-1-v22-1.qa-test.apriori.net/");
        softAssertions.assertThat(specificEnvPropertyValue).isEqualTo("https://bcs-http.na-1-v23-1.qa-test.apriori.net/");

        softAssertions.assertAll();
//        assertFalse("Environment property should exist.", envValue.isEmpty());
//        assertFalse("Specific environment property should exist.", specificEnvPropertyValue.isEmpty());
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
