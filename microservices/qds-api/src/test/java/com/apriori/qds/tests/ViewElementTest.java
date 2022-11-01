package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.qds.controller.LayoutResources;
import com.apriori.qds.entity.request.layout.ViewElementRequest;
import com.apriori.qds.entity.request.layout.ViewElementRequestConfig;
import com.apriori.qds.entity.request.layout.ViewElementRequestParameters;
import com.apriori.qds.entity.response.layout.LayoutResponse;
import com.apriori.qds.entity.response.layout.ViewElementsResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ViewElementTest extends TestUtil {

    private static String viewElementName;
    private static String layoutIdentity;
    private static ResponseWrapper<ViewElementsResponse> viewElementResponse;
    private static ResponseWrapper<LayoutResponse> layoutResponse;
    UserCredentials userCredentials = UserUtil.getUser();
    private static SoftAssertions softAssertions;
    private static String userContext;
    private static String layoutName;


    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        viewElementName = "VEN" + new GenerateStringUtil().getRandomNumbers();
        layoutName = "LY" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail());
        layoutResponse = LayoutResources.createLayout(layoutName, userContext);
        viewElementResponse = LayoutResources.createLayoutViewElement(layoutResponse.getResponseEntity().getIdentity(), "TEST123456", userContext);

    }

    @Test
    public void createLayoutViewElements() {
        softAssertions.assertThat(viewElementResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(viewElementResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    public void updateLayoutViewElements() {
        ViewElementRequest viewElementRequest = ViewElementRequest.builder()
            .viewElement(ViewElementRequestParameters.builder()
                .name(viewElementResponse.getResponseEntity().getItems().get(0).getName())
                .configuration(ViewElementRequestConfig.builder()
                    .foo("Test")
                    .build())
                .build())
            .build();
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT_VIEW_ELEMENT, ViewElementsResponse.class)
            .inlineVariables(viewElementResponse.getResponseEntity().getItems().get(0).getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .body(viewElementRequest)
            .apUserContext(userContext);

        viewElementResponse = HTTPRequest.build(requestEntity).patch();
        softAssertions.assertThat(viewElementResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(viewElementResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    public void getLayoutViewElements() {

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT_VIEW_ELEMENTS, ViewElementsResponse.class)
            .inlineVariables(layoutResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        viewElementResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(viewElementResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(viewElementResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        ResponseWrapper<String> deleteLayoutViewElementResponse = LayoutResources.deleteLayoutViewElement(layoutResponse.getResponseEntity().getIdentity(),
            viewElementResponse.getResponseEntity().getItems().get(0).getIdentity(), userContext);
        ResponseWrapper<String> deleteLayoutResponse = LayoutResources.deleteLayout(layoutResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertThat(deleteLayoutViewElementResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertThat(deleteLayoutResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
