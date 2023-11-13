package com.apriori.qds.api.controller;

import com.apriori.qds.api.enums.QDSAPIEnum;
import com.apriori.qds.api.models.request.layout.LayoutRequest;
import com.apriori.qds.api.models.request.layout.LayoutRequestParameters;
import com.apriori.qds.api.models.request.layout.ViewElementRequest;
import com.apriori.qds.api.models.request.layout.ViewElementRequestConfig;
import com.apriori.qds.api.models.request.layout.ViewElementRequestParameters;
import com.apriori.qds.api.models.response.layout.LayoutResponse;
import com.apriori.qds.api.models.response.layout.ViewElementResponse;
import com.apriori.qds.api.utils.QdsApiTestUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

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