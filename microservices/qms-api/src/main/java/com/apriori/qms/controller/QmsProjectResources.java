package com.apriori.qms.controller;

import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectNotificationRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.KeyValueException;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;
import utils.QmsApiTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        QueryParams queryParams = new QueryParams();

        List<String[]> paramKeyValue = Arrays.stream(paramKeysValues).map(o -> o.split(","))
            .collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        try {
            paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValue);
        }

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, BidPackageProjectsResponse.class)
            .queryParams(queryParams.use(paramMap))
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