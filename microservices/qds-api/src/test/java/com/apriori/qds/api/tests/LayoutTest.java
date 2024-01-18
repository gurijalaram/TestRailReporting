package com.apriori.qds.api.tests;

import com.apriori.qds.api.controller.LayoutResources;
import com.apriori.qds.api.enums.QDSAPIEnum;
import com.apriori.qds.api.models.request.layout.LayoutRequest;
import com.apriori.qds.api.models.request.layout.LayoutRequestParameters;
import com.apriori.qds.api.models.response.layout.LayoutResponse;
import com.apriori.qds.api.models.response.layout.LayoutsResponse;
import com.apriori.qds.api.utils.QdsApiTestUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class LayoutTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static LayoutResponse layoutResponse;
    private UserCredentials currentUser = UserUtil.getUser();
    private static String userContext;
    private static String layoutName;

    @BeforeEach
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QDSAPIEnum.LAYOUT, LayoutResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QDSAPIEnum.LAYOUT, LayoutResponse.class)
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

        RequestEntity requestEntity = RequestEntityUtil_Old.init(QDSAPIEnum.LAYOUTS, LayoutsResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<LayoutsResponse> layoutConfigurationsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(layoutConfigurationsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }


    @AfterEach
    public void testCleanup() {
        LayoutResources.deleteLayout(layoutResponse.getIdentity(), userContext);
        softAssertions.assertAll();
    }
}
