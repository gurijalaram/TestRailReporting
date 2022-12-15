package com.apriori.qms.controller;

import com.apriori.qms.entity.request.layout.LayoutConfigurationParameters;
import com.apriori.qms.entity.request.layout.LayoutConfigurationRequest;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

import utils.QmsApiTestUtils;

public class QmsLayoutResources {

    private static final String CLOUD_CONTEXT = "6C1F8C1D4D757FLINC95H3A37GFDL6LCI5N41J8M416FBJBK";

    /**
     * create layout configuration for layout view element name
     *
     * @param layoutConfigRequestBuilder - LayoutConfigRequest data builder
     * @param viewElementName            View Element Name
     * @param responseClass              expected Response class
     * @param httpStatus                 expected http status code
     * @param currentUser                UserCredentials
     * @param <T>                        response class type
     * @return Response class object
     */
    @SuppressWarnings("unchecked")
    public static <T> T createLayoutConfiguration(LayoutConfigurationRequest layoutConfigRequestBuilder, String viewElementName, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATIONS, responseClass)
            .inlineVariables(viewElementName)
            .body(layoutConfigRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * update layout configuration for layout view element name
     *
     * @param layoutConfigRequestBuilder  LayoutConfigRequest data builder
     * @param layoutConfigurationIdentity Layout Configuration identity
     * @param viewElementName             View Element Name
     * @param responseClass               expected Response class
     * @param httpStatus                  expected http status code
     * @param currentUser                 UserCredentials
     * @param <T>                         response class type
     * @return Response class object
     */
    @SuppressWarnings("unchecked")
    public static <T> T updateLayoutConfiguration(LayoutConfigurationRequest layoutConfigRequestBuilder, String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .body(layoutConfigRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Share layout configuration for layout view element name
     *
     * @param layoutConfigRequestBuilder  LayoutConfigRequest data builder
     * @param layoutConfigurationIdentity Layout Configuration identity
     * @param viewElementName             View Element Name
     * @param responseClass               expected Response class
     * @param httpStatus                  expected http status code
     * @param currentUser                 UserCredentials
     * @param <T>                         response class type
     * @return Response class object
     */
    @SuppressWarnings("unchecked")
    public static <T> T shareLayoutConfiguration(LayoutConfigurationRequest layoutConfigRequestBuilder, String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION_SHARE, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .body(layoutConfigRequestBuilder)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Delete layout configuration by identity
     *
     * @param layoutConfigurationIdentity Layout Configuration identity
     * @param viewElementName             View Element Name
     * @param responseClass               expected Response class
     * @param httpStatus                  expected http status code
     * @param currentUser                 UserCredentials
     * @param <T>                         response class type
     * @return Response class object
     */
    @SuppressWarnings("unchecked")
    public static <T> T deleteLayoutConfiguration(String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }

    /**
     * get list of all layout configurations
     *
     * @param viewElementName View Element Name
     * @param responseClass   expected Response class
     * @param httpStatus      expected http status code
     * @param currentUser     UserCredentials
     * @param <T>             response class type
     * @return Response class object
     */
    @SuppressWarnings("unchecked")
    public static <T> T getLayoutConfigurations(String viewElementName, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATIONS, responseClass)
            .inlineVariables(viewElementName)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get layout configurtation by view element name and layout configuration identity
     *
     * @param viewElementName             view Element Name
     * @param layoutConfigurationIdentity layout configuration identity
     * @param responseClass               expected Response class
     * @param httpStatus                  expected http status code
     * @param currentUser                 UserCredentials
     * @param <T>                         response class type
     * @return Response class object
     */
    @SuppressWarnings("unchecked")
    public static <T> T getLayoutConfiguration(String viewElementName, String layoutConfigurationIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.VIEW_ELEMENT_LAYOUT_CONFIGURATION, responseClass)
            .inlineVariables(viewElementName, layoutConfigurationIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Build Layout configuration request data builder from json
     *
     * @param layoutConfigName     unique layout configuration name
     * @param deploymentIdentity   layout deployment identity captured from layout response api
     * @param installationIdentity layout installation identity captured from layout response api
     * @param isShareable          boolean true or false
     * @return LayoutConfigurationRequest
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

    /**
     * returns deserialized layout configuration request json data file
     *
     * @return LayoutConfigurationRequest
     */
    private static LayoutConfigurationRequest getLayoutConfigurationRequestData() {
        return new TestDataService().getTestData("LayoutConfigurationRequestData.json", LayoutConfigurationRequest.class);
    }
}
