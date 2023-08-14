package com.apriori.qms.controller;

import com.apriori.DateFormattingUtils;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.DateUtil;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.qms.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectNotificationRequest;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.utils.KeyValueUtil;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

/**
 * The type Qms project resources.
 */
public class QmsProjectResources {
    /**
     * Gets project request builder.
     *
     * @param projectAttributesMap the project attributes map
     * @param projectItemsList     the project items list
     * @param projectUsersList     the project users list
     * @param currentUser          the current user
     * @return the project request builder
     */
    public static BidPackageProjectRequest getProjectRequestBuilder(HashMap<String, String> projectAttributesMap, List<BidPackageItemRequest> projectItemsList,
                                                                    List<BidPackageProjectUserParameters> projectUsersList, UserCredentials currentUser) {
        String projectName = projectAttributesMap.getOrDefault("projectName", new GenerateStringUtil().getRandomString());
        String projectDisplayName = projectAttributesMap.getOrDefault("projectDisplayName", new GenerateStringUtil().getRandomString());
        String projectDescription = projectAttributesMap.getOrDefault("projectDescription", new GenerateStringUtil().getRandomString());
        String projectStatus = projectAttributesMap.getOrDefault("projectStatus", "COMPLETED");
        String projectOwner = projectAttributesMap.getOrDefault("projectOwner", new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        String projectOwnerUserIdentity = projectAttributesMap.getOrDefault("projectOwnerUserIdentity", "N/A");
        String projectDueAt = projectAttributesMap.getOrDefault("projectDueAt", DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));

        BidPackageProjectRequest projectRequest = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/QmsProjectRequest.json"), BidPackageProjectRequest.class);
        BidPackageProjectParameters project = projectRequest.getProject();
        project.setName(projectName);
        project.setDisplayName(projectDisplayName);
        project.setDescription(projectDescription);
        project.setDueAt(projectDueAt);
        project.setOwner(projectOwner);
        project.setOwnerUserIdentity(projectOwnerUserIdentity);
        project.setStatus(projectStatus);
        project.setItems(projectItemsList);
        project.setUsers(projectUsersList);
        return projectRequest;
    }

    /**
     * Create project.
     *
     * @param <T>           the type parameter
     * @param itemsList     the items list
     * @param usersList     the users list
     * @param responseClass the response class
     * @param httpStatus    the http status
     * @param currentUser   the current user
     * @return the response entity
     */
    public static <T> T createProject(HashMap<String, String> projectAttributesMap, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getProjectRequestBuilder(projectAttributesMap, itemsList, usersList, currentUser);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets projects.
     *
     * @param <T>           the type parameter
     * @param responseClass the response class
     * @param httpStatus    the http status
     * @param currentUser   the current user
     * @return the projects
     */
    public static <T> T getProjects(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }


    /**
     * Gets project.
     *
     * @param <T>             the type parameter
     * @param projectIdentity the project identity
     * @param responseClass   the response class
     * @param httpStatus      the http status
     * @param currentUser     the current user
     * @return the project
     */
    public static <T> T getProject(String projectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT, responseClass)
            .inlineVariables(projectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets project items.
     *
     * @param <T>             the type parameter
     * @param projectIdentity the project identity
     * @param responseClass   the response class
     * @param httpStatus      the http status
     * @param currentUser     the current user
     * @return the project items
     */
    public static <T> T getProjectItems(String projectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT_ITEMS, responseClass)
            .inlineVariables(projectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets project item.
     *
     * @param <T>                 the type parameter
     * @param projectIdentity     the project identity
     * @param projectItemIdentity the project item identity
     * @param responseClass       the response class
     * @param httpStatus          the http status
     * @param currentUser         the current user
     * @return the project item
     */
    public static <T> T getProjectItem(String projectIdentity, String projectItemIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT_ITEM, responseClass)
            .inlineVariables(projectIdentity, projectItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets filtered projects.
     *
     * @param currentUser     the current user
     * @param paramKeysValues the param keys values
     * @return the filtered projects
     */
    public static BidPackageProjectsResponse getFilteredProjects(UserCredentials currentUser, String... paramKeysValues) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, BidPackageProjectsResponse.class)
            .queryParams(new KeyValueUtil().keyValue(paramKeysValues, ","))
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageProjectsResponse> filteredProjectsResponse = HTTPRequest.build(requestEntity).get();
        return filteredProjectsResponse.getResponseEntity();
    }

    /**
     * Retrieve project notifications.
     *
     * @param <T>                 the type parameter
     * @param notificationRequest the notification request
     * @param responseClass       the response class
     * @param httpStatus          the http status
     * @param currentUser         the current user
     * @return the response entity
     */
    public static <T> T retrieveProjectNotifications(BidPackageProjectNotificationRequest notificationRequest, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT_NOTIFICATION_COUNT, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(notificationRequest)
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }
}