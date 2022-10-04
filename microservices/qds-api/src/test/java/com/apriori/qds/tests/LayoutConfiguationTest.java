package com.apriori.qds.tests;

import com.apriori.qds.controller.LayoutResources;
import com.apriori.qds.entity.response.layout.LayoutResponse;
import com.apriori.qds.entity.response.layout.LayoutsResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LayoutConfiguationTest {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<LayoutResponse> layoutConfigurationResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String userContext;
    private static String viewElementName;
    private static String layoutConfigName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        layoutConfigName = "LCN" + RandomStringUtils.randomNumeric(6);
        // layoutConfigurationResponse = HTTPRequest.build(LayoutResources.getLayoutConfigurationRequestEntity(viewElementName, layoutConfigName)).post();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
    }

    @Test
    public void getLayouts() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUTS, LayoutsResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<LayoutsResponse> layoutConfigurationsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(layoutConfigurationsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(layoutConfigurationsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    public void createLayoutConfiguration() {
        softAssertions.assertThat(layoutConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(layoutConfigurationResponse.getResponseEntity().getName()).isEqualTo(layoutConfigName);
        softAssertions.assertAll();
    }

    @Test
    public void shareLayoutConfiguration() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, LayoutResponse.class)
            .inlineVariables(viewElementName, layoutConfigurationResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader("authorizationKey"));
        layoutConfigurationResponse = HTTPRequest.build(requestEntity).patch();
        softAssertions.assertThat(layoutConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(layoutConfigurationResponse.getResponseEntity().getName()).isEqualTo(layoutConfigName);
        softAssertions.assertAll();
    }

    @Test
    public void updateLayoutConfiguration() {
        ResponseWrapper<LayoutResponse> layoutConfigurationResponse;
        layoutConfigurationResponse = HTTPRequest.build(LayoutResources.getLayoutConfigurationRequestEntity(viewElementName, layoutConfigName)).patch();
        softAssertions.assertThat(layoutConfigurationResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void getLayoutConfigurations() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, LayoutsResponse.class)
            .inlineVariables(viewElementName, layoutConfigurationResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader("authorizationKey"));

        ResponseWrapper<LayoutsResponse> layoutConfigurationsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(layoutConfigurationsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(layoutConfigurationsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    public void getLayoutConfiguration() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, LayoutsResponse.class)
            .inlineVariables(viewElementName, layoutConfigurationResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader("authorizationKey"));

        ResponseWrapper<LayoutsResponse> layoutConfigurationsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(layoutConfigurationsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(layoutConfigurationsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
    /*  RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, LayoutsResponse.class)
            .inlineVariables(viewElementName, layoutConfigurationResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader("authorizationKey"));

        ResponseWrapper<LayoutsResponse> layoutConfigurationsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(layoutConfigurationsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);*/
    }
}
