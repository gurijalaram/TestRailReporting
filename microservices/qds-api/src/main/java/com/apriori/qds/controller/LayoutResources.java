package com.apriori.qds.controller;

import com.apriori.qds.entity.request.layout.LayoutConfigRequest;
import com.apriori.qds.entity.request.layout.LayoutConfigRequestParameters;
import com.apriori.qds.entity.request.layout.LayoutRequest;
import com.apriori.qds.entity.request.layout.LayoutRequestParameters;
import com.apriori.qds.entity.request.layout.ViewElementRequest;
import com.apriori.qds.entity.request.layout.ViewElementRequestConfig;
import com.apriori.qds.entity.request.layout.ViewElementRequestParameters;
import com.apriori.qds.entity.response.layout.LayoutResponse;
import com.apriori.qds.entity.response.layout.ViewElementResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

public class LayoutResources {

    public static LayoutResponse createLayout(String layoutName, UserCredentials currentUser) {
        LayoutRequest layoutRequest = LayoutRequest.builder()
            .layout(LayoutRequestParameters.builder()
                .applicationIdentity("AN" + layoutName)
                .deploymentIdentity("DI" + layoutName)
                .installationIdentity("II" + layoutName)
                .name(layoutName)
                .published(false)
                .build())
            .build();
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUTS, LayoutResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .body(layoutRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (LayoutResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    public static ResponseWrapper<String> deleteLayout(String layoutIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT, null)
            .inlineVariables(layoutIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    public static ResponseWrapper<String> deleteLayoutViewElement(String layoutIdentity, String viewElementIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT_VIEW_ELEMENT, null)
            .inlineVariables(layoutIdentity, viewElementIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    public static RequestEntity getLayoutConfigurationRequestEntity(String viewElementName, String layoutConfigName, UserCredentials currentUser) {
        LayoutConfigRequest layoutConfigurationRequest = new TestDataService().getTestData("LayoutConfigurationRequestData.json", LayoutConfigRequest.class);
        layoutConfigurationRequest.setLayoutConfiguration(LayoutConfigRequestParameters.builder()
            .configuration(String.format(layoutConfigurationRequest.getLayoutConfiguration().getConfiguration(), RandomStringUtils.randomNumeric(3)))
            .build());
        layoutConfigurationRequest.getLayoutConfiguration().setName(layoutConfigName);
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATIONS, LayoutResponse.class)
            .inlineVariables(viewElementName)
            .body(layoutConfigurationRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);
        return requestEntity;
    }

    public static ViewElementResponse createLayoutViewElement(String layoutIdentity, String viewElementName, UserCredentials currentUser) {
        ViewElementRequest viewElementRequest = ViewElementRequest.builder()
            .viewElement(ViewElementRequestParameters.builder()
                .name(viewElementName)
                .configuration(ViewElementRequestConfig.builder()
                    .foo("bar")
                    .build())
                .build())
            .build();
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.LAYOUT_VIEW_ELEMENTS, ViewElementResponse.class)
            .inlineVariables(layoutIdentity)
            .headers(QdsApiTestUtils.setUpHeader())
            .body(viewElementRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (ViewElementResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

}
