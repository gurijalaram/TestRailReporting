package com.apriori.qms.api.controller;

import com.apriori.qms.api.enums.QMSAPIEnum;
import com.apriori.qms.api.models.request.layout.LayoutConfigurationParameters;
import com.apriori.qms.api.models.request.layout.LayoutConfigurationRequest;
import com.apriori.qms.api.utils.QmsApiTestUtils;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

/**
 * The type Qms layout resources.
 */
public class QmsLayoutResources {

    /**
     * Create layout configuration.
     *
     * @param <T>                        the type parameter
     * @param layoutConfigRequestBuilder the layout config request builder
     * @param viewElementName            the view element name
     * @param responseClass              the response class
     * @param httpStatus                 the http status
     * @param currentUser                the current user
     * @return the response entity
     */
    public static <T> T createLayoutConfiguration(LayoutConfigurationRequest layoutConfigRequestBuilder, String viewElementName, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATIONS, responseClass)
            .inlineVariables(viewElementName)
            .body(layoutConfigRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update layout configuration.
     *
     * @param <T>                         the type parameter
     * @param layoutConfigRequestBuilder  the layout config request builder
     * @param viewElementName             the view element name
     * @param layoutConfigurationIdentity the layout configuration identity
     * @param responseClass               the response class
     * @param httpStatus                  the http status
     * @param currentUser                 the current user
     * @return the response entity
     */
    public static <T> T updateLayoutConfiguration(LayoutConfigurationRequest layoutConfigRequestBuilder, String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .body(layoutConfigRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Share layout configuration.
     *
     * @param <T>                         the type parameter
     * @param layoutConfigRequestBuilder  the layout config request builder
     * @param viewElementName             the view element name
     * @param layoutConfigurationIdentity the layout configuration identity
     * @param responseClass               the response class
     * @param httpStatus                  the http status
     * @param currentUser                 the current user
     * @return the response entity
     */
    public static <T> T shareLayoutConfiguration(LayoutConfigurationRequest layoutConfigRequestBuilder, String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION_SHARE, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .body(layoutConfigRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete layout configuration.
     *
     * @param <T>                         the type parameter
     * @param viewElementName             the view element name
     * @param layoutConfigurationIdentity the layout configuration identity
     * @param responseClass               the response class
     * @param httpStatus                  the http status
     * @param currentUser                 the current user
     * @return the response entity
     */
    public static <T> T deleteLayoutConfiguration(String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets layout configurations.
     *
     * @param <T>             the type parameter
     * @param viewElementName the view element name
     * @param responseClass   the response class
     * @param httpStatus      the http status
     * @param currentUser     the current user
     * @return the layout configurations
     */
    public static <T> T getLayoutConfigurations(String viewElementName, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATIONS, responseClass)
            .inlineVariables(viewElementName)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets layout configuration.
     *
     * @param <T>                         the type parameter
     * @param viewElementName             the view element name
     * @param layoutConfigurationIdentity the layout configuration identity
     * @param responseClass               the response class
     * @param httpStatus                  the http status
     * @param currentUser                 the current user
     * @return the layout configuration
     */
    public static <T> T getLayoutConfiguration(String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets layout configuration request builder.
     *
     * @param layoutConfigName     the layout config name
     * @param deploymentIdentity   the deployment identity
     * @param installationIdentity the installation identity
     * @param isShareable          the is shareable
     * @return the layout configuration request builder
     */
    public static LayoutConfigurationRequest getLayoutConfigurationRequestBuilder(String layoutConfigName, String deploymentIdentity, String installationIdentity, Boolean isShareable) {
        return LayoutConfigurationRequest.builder()
            .layoutConfiguration(LayoutConfigurationParameters.builder()
                .configuration(QmsLayoutResources.getLayoutConfigurationRequestData().getLayoutConfiguration().getConfiguration())
                .name(layoutConfigName)
                .deploymentIdentity(deploymentIdentity)
                .installationIdentity(installationIdentity)
                .shareable(isShareable)
                .build())
            .build();
    }

    private static LayoutConfigurationRequest getLayoutConfigurationRequestData() {
        return new TestDataService().getTestData("LayoutConfigurationRequestData.json", LayoutConfigurationRequest.class);
    }
}
