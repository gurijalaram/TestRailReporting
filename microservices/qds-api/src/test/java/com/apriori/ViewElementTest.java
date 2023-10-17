package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.qds.controller.LayoutResources;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.models.request.layout.ViewElementRequest;
import com.apriori.qds.models.request.layout.ViewElementRequestConfig;
import com.apriori.qds.models.request.layout.ViewElementRequestParameters;
import com.apriori.qds.models.response.layout.LayoutResponse;
import com.apriori.qds.models.response.layout.ViewElementResponse;
import com.apriori.qds.models.response.layout.ViewElementsResponse;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ViewElementTest extends TestUtil {

    private static String viewElementName;
    private static String layoutIdentity;
    private static ViewElementResponse viewElementResponse;
    private static LayoutResponse layoutResponse;
    private UserCredentials currentUser = UserUtil.getUser();
    private static SoftAssertions softAssertions;
    private static String userContext;
    private static String layoutName;


    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        viewElementName = "VEN" + new GenerateStringUtil().getRandomNumbers();
        layoutName = "LY" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        layoutResponse = LayoutResources.createLayout(layoutName, currentUser);
        viewElementResponse = LayoutResources.createLayoutViewElement(layoutResponse.getIdentity(), viewElementName, currentUser);

    }

    @Test
    public void createLayoutViewElements() {
        softAssertions.assertThat(viewElementResponse.getName()).isNotNull();
    }

    @Test
    public void updateLayoutViewElements() {
        ViewElementRequest viewElementRequest = ViewElementRequest.builder()
            .viewElement(ViewElementRequestParameters.builder()
                .name(viewElementResponse.getName())
                .configuration(ViewElementRequestConfig.builder()
                    .foo("Test")
                    .build())
                .build())
            .build();
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT_VIEW_ELEMENT, ViewElementsResponse.class)
            .inlineVariables(viewElementResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .body(viewElementRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        viewElementResponse = (ViewElementResponse) HTTPRequest.build(requestEntity).patch().getResponseEntity();
        softAssertions.assertThat(viewElementResponse.getName()).isNotNull();
    }

    @Test
    public void getLayoutViewElements() {

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT_VIEW_ELEMENTS, ViewElementsResponse.class)
            .inlineVariables(layoutResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        viewElementResponse = (ViewElementResponse) HTTPRequest.build(requestEntity).patch().getResponseEntity();
        softAssertions.assertThat(viewElementResponse.getName()).isNotNull();
    }

    @AfterEach
    public void testCleanup() {
        LayoutResources.deleteLayoutViewElement(layoutResponse.getIdentity(),
            viewElementResponse.getIdentity(), userContext);
        LayoutResources.deleteLayout(layoutResponse.getIdentity(), userContext);
        softAssertions.assertAll();
    }
}
