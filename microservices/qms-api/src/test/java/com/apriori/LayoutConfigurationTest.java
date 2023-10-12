package com.apriori;

import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ApwErrorMessage;
import com.apriori.qds.controller.LayoutResources;
import com.apriori.qds.models.response.layout.LayoutResponse;
import com.apriori.qds.models.response.layout.ViewElementResponse;
import com.apriori.qms.controller.QmsLayoutResources;
import com.apriori.qms.models.request.layout.LayoutConfigurationParameters;
import com.apriori.qms.models.request.layout.LayoutConfigurationRequest;
import com.apriori.qms.models.response.layout.LayoutConfigurationResponse;
import com.apriori.qms.models.response.layout.LayoutConfigurationsResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class LayoutConfigurationTest extends TestUtil {
    private static SoftAssertions softAssertions;
    private static LayoutResponse layoutResponse;
    private static LayoutConfigurationResponse layoutConfigurationResponse;
    private static ViewElementResponse viewElementsResponse;
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static String layoutConfigName;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        layoutConfigName = "LCN" + new GenerateStringUtil().getRandomNumbers();
        String layoutName = "LN" + new GenerateStringUtil().getRandomNumbers();
        String viewElementName = "VEN" + new GenerateStringUtil().getRandomNumbers();
        layoutResponse = LayoutResources.createLayout(layoutName, currentUser);
        viewElementsResponse = LayoutResources.createLayoutViewElement(layoutResponse.getIdentity(), viewElementName, currentUser);
        layoutConfigurationResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(layoutConfigName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
    }

    @Test
    @TestRail(id = {12538, 12545, 12544, 13113, 12887})
    @Description("Create, delete layout configuration, " +
        "verify layout that does not exist, " +
        "Update layout configuration that does not exist" +
        "delete layout configuration that does not exist")
    public void createAndDeleteLayoutConfiguration() {
        String lcName = "LCN" + new GenerateStringUtil().getRandomNumbers();
        LayoutConfigurationResponse layoutConfigurationResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(lcName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(layoutConfigurationResponse.getName()).isEqualTo(lcName);

        QmsLayoutResources.deleteLayoutConfiguration(viewElementsResponse.getIdentity(), layoutConfigurationResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage getLYCResponse = QmsLayoutResources.getLayoutConfiguration(
            viewElementsResponse.getName(),
            layoutConfigurationResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            currentUser);

        softAssertions.assertThat(getLYCResponse.getMessage()).contains("Resource 'Layout' with identity '" + layoutConfigurationResponse.getIdentity() + "' was not found");


        ApwErrorMessage deletedLYCResponse = QmsLayoutResources.updateLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(new GenerateStringUtil().getRandomNumbers(),
                layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            layoutConfigurationResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            currentUser);

        softAssertions.assertThat(deletedLYCResponse.getMessage()).contains("Resource 'Layout' with identity '" + layoutConfigurationResponse.getIdentity() + "' was not found");

        ApwErrorMessage errorMessageResponse = QmsLayoutResources.deleteLayoutConfiguration(
            viewElementsResponse.getIdentity(),
            layoutConfigurationResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            currentUser);

        softAssertions.assertThat(errorMessageResponse.getMessage()).contains("Resource 'Layout' with identity '" + layoutConfigurationResponse.getIdentity() + "' was not found");
    }

    @Test
    @TestRail(id = {12537, 13111})
    @Description("Verify that user can find all configuration for layout and get with empty configurations")
    public void getLayoutConfigurations() {
        LayoutConfigurationsResponse layoutConfigurationsResponse = QmsLayoutResources.getLayoutConfigurations(viewElementsResponse.getName(), LayoutConfigurationsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(layoutConfigurationsResponse.getItems().size()).isGreaterThan(0);

        QmsLayoutResources.deleteLayoutConfiguration(viewElementsResponse.getIdentity(), layoutConfigurationResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        LayoutConfigurationsResponse getLayoutConfigurationsResponse = QmsLayoutResources.getLayoutConfigurations(viewElementsResponse.getName(), LayoutConfigurationsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getLayoutConfigurationsResponse.getItems().size()).isEqualTo(0);

        layoutConfigurationResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(layoutConfigName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
    }

    @Test
    @TestRail(id = {12542, 12540})
    @Description("Verify that user get configuration by identity and verify layout configuration identity")
    public void getLayoutConfiguration() {
        LayoutConfigurationResponse getLycResponse = QmsLayoutResources.getLayoutConfiguration(
            viewElementsResponse.getName(),
            layoutConfigurationResponse.getIdentity(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(getLycResponse.getIdentity()).isEqualTo(layoutConfigurationResponse.getIdentity());
    }

    @Test
    @TestRail(id = {12543})
    @Description("Verify that user can update layout configuration")
    public void updateLayoutConfiguration() {
        String lycName = new GenerateStringUtil().getRandomString();
        LayoutConfigurationResponse updateLycResponse = QmsLayoutResources.updateLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(lycName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            layoutConfigurationResponse.getIdentity(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(updateLycResponse.getIdentity()).isEqualTo(layoutConfigurationResponse.getIdentity());
        softAssertions.assertThat(updateLycResponse.getName()).isEqualTo(lycName);
    }

    @Test
    @TestRail(id = {12539})
    @Issue("COL-1710")
    @Description("Create layout configuration with name that already exists")
    public void createLayoutConfigurationWithSameName() {
        ApwErrorMessage lycErrorResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(layoutConfigName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            ApwErrorMessage.class,
            HttpStatus.SC_CONFLICT,
            currentUser);

        softAssertions.assertThat(lycErrorResponse.getMessage()).contains("Layout configuration with name '" + layoutConfigName + "' already exists for Customer");
    }

    @Test
    @TestRail(id = {12541})
    @Issue("COL-1710")
    @Issue("COL-1416")
    @Description("Create layout configuration with blank name")
    public void createLayoutConfigurationWithEmptyName() {
        ApwErrorMessage lycErrorResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder("", layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            ApwErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            currentUser);

        softAssertions.assertThat(lycErrorResponse.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(id = {12881})
    @Issue("COL-1710")
    @Issue("COL-1416")
    @Description("Create layout configuration name more than 64 characters")
    public void createLayoutConfigurationNameMoreThan64() {
        ApwErrorMessage lycErrorResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(RandomStringUtils.randomAlphabetic(70), layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            ApwErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            currentUser);

        softAssertions.assertThat(lycErrorResponse.getMessage()).contains("name' should not be more than 64 characters");
    }

    @Test
    @TestRail(id = {12887})
    @Description("Delete Invalid layout configuration")
    public void deleteInvalidLayoutConfiguration() {
        ApwErrorMessage errorMessageResponse = QmsLayoutResources.deleteLayoutConfiguration(
            viewElementsResponse.getIdentity(),
            "INVALIDLYC",
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            currentUser);

        softAssertions.assertThat(errorMessageResponse.getMessage()).contains("Resource 'Layout' with identity 'INVALIDLYC' was not found");
    }

    @Test
    @TestRail(id = {13106})
    @Description("Create layout configuration with blank configuration")
    public void createLayoutConfigurationWithEmptyConfig() {
        LayoutConfigurationRequest layoutConfigurationRequestBuilder = LayoutConfigurationRequest.builder()
            .layoutConfiguration(LayoutConfigurationParameters.builder()
                .configuration("")
                .name(layoutConfigName)
                .deploymentIdentity(new GenerateStringUtil().getRandomString())
                .installationIdentity(new GenerateStringUtil().getRandomString())
                .shareable(false)
                .build())
            .build();
        ApwErrorMessage lycErrorResponse = QmsLayoutResources.createLayoutConfiguration(
            layoutConfigurationRequestBuilder,
            viewElementsResponse.getName(),
            ApwErrorMessage.class,
            HttpStatus.SC_INTERNAL_SERVER_ERROR,
            currentUser);

        softAssertions.assertThat(lycErrorResponse.getMessage()).contains("No content to map due to end-of-input");
    }

    @Test
    @TestRail(id = {13107, 13109})
    @Description("Create, Share  layout configuration and Verify layout configuration is shared")
    public void createShareAndVerifyLayoutConfiguration() {
        String lcName = "LCN" + new GenerateStringUtil().getRandomNumbers();
        LayoutConfigurationResponse lycResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(lcName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(lycResponse.getName()).isEqualTo(lcName);

        LayoutConfigurationResponse updateLycResponse = QmsLayoutResources.shareLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(lcName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), true),
            viewElementsResponse.getName(),
            lycResponse.getIdentity(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(updateLycResponse.getName()).isEqualTo(lcName);

        LayoutConfigurationResponse getLycResponse = QmsLayoutResources.getLayoutConfiguration(
            viewElementsResponse.getName(),
            lycResponse.getIdentity(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(getLycResponse.getShareable()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {13108, 13110})
    @Description("Create, UnShare  layout configuration and Verify layout configuration is shared")
    public void createAndUnShareAndVerifyLayoutConfiguration() {
        String lcName = "LCN" + new GenerateStringUtil().getRandomNumbers();
        LayoutConfigurationResponse lycResponse = QmsLayoutResources.createLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(lcName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), true),
            viewElementsResponse.getName(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(lycResponse.getName()).isEqualTo(lcName);

        LayoutConfigurationResponse updateLycResponse = QmsLayoutResources.shareLayoutConfiguration(
            QmsLayoutResources.getLayoutConfigurationRequestBuilder(lcName, layoutResponse.getDeploymentIdentity(), layoutResponse.getInstallationIdentity(), false),
            viewElementsResponse.getName(),
            lycResponse.getIdentity(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(updateLycResponse.getName()).isEqualTo(lcName);

        LayoutConfigurationResponse getLycResponse = QmsLayoutResources.getLayoutConfiguration(
            viewElementsResponse.getName(),
            lycResponse.getIdentity(),
            LayoutConfigurationResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(getLycResponse.getShareable()).isEqualTo(false);
    }


    @AfterEach
    public void testCleanup() {
        QmsLayoutResources.deleteLayoutConfiguration(viewElementsResponse.getIdentity(), layoutConfigurationResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
