package com.apriori.qds.tests;

import com.apriori.AuthUserContextUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestUtil;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.qds.controller.LayoutResources;
import com.apriori.qds.entity.request.layout.LayoutRequest;
import com.apriori.qds.entity.request.layout.LayoutRequestParameters;
import com.apriori.qds.entity.response.layout.LayoutResponse;
import com.apriori.qds.entity.response.layout.LayoutsResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LayoutTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static LayoutResponse layoutResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String userContext;
    private static String viewElementName;
    private static String layoutName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        layoutName = "LY" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        layoutResponse = LayoutResources.createLayout(layoutName, currentUser);
    }

    @Test
    public void createLayout() {
        softAssertions.assertThat(layoutResponse.getName()).isEqualTo(layoutName);
    }

    @Test
    public void getLayout() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT, LayoutResponse.class)
            .inlineVariables(layoutResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<LayoutResponse> getLayoutResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(getLayoutResponse.getResponseEntity().getName()).isEqualTo(layoutName);
    }

    @Test
    public void updateLayout() {
        LayoutRequest layoutRequest = LayoutRequest.builder()
            .layout(LayoutRequestParameters.builder()
                .applicationIdentity(layoutResponse.getApplicationIdentity())
                .deploymentIdentity(layoutResponse.getDeploymentIdentity())
                .installationIdentity(layoutResponse.getInstallationIdentity())
                .name(layoutResponse.getName())
                .published(true)
                .build())
            .build();
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT, LayoutResponse.class)
            .inlineVariables(layoutResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .body(layoutRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<LayoutResponse> updateLayoutResponse = HTTPRequest.build(requestEntity).patch();
        softAssertions.assertThat(updateLayoutResponse.getResponseEntity().isPublished()).isTrue();
    }

    @Test
    public void getLayouts() {

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUTS, LayoutsResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<LayoutsResponse> layoutConfigurationsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(layoutConfigurationsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }


    @After
    public void testCleanup() {
        LayoutResources.deleteLayout(layoutResponse.getIdentity(), userContext);
        softAssertions.assertAll();

    }
}
