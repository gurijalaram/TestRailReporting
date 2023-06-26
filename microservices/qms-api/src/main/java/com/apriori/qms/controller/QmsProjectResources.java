package com.apriori.qms.controller;

import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.user.UserCredentials;

import utils.QmsApiTestUtils;

import java.util.List;

/**
 * The type Qms project resources.
 */
public class QmsProjectResources {

    /**
     * Gets project request builder.
     *
     * @param projectName        the project name
     * @param projectDescription the project description
     * @param itemsList          the items list
     * @param usersList          the users list
     * @return the project request builder
     */
    public static BidPackageProjectRequest getProjectRequestBuilder(String projectName, String projectDescription, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList) {
        BidPackageProjectRequest projectRequest = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/QmsProjectRequest.json"), BidPackageProjectRequest.class);
        projectRequest.getProject().setName("PN" + projectName);
        projectRequest.getProject().setDisplayName("DN" + projectName);
        projectRequest.getProject().setDescription("PD" + projectDescription);
        projectRequest.getProject()
            .setDueAt(DateUtil.getDateDaysAfter(10, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));
        projectRequest.getProject().setItems(itemsList);
        projectRequest.getProject().setUsers(usersList);
        return projectRequest;
    }

    /**
     * Create project.
     *
     * @param <T>                the type parameter
     * @param projectName        the project name
     * @param projectDescription the project description
     * @param itemsList          the items list
     * @param usersList          the users list
     * @param responseClass      the response class
     * @param httpStatus         the http status
     * @param currentUser        the current user
     * @return the response entity
     */
    public static <T> T createProject(String projectName, String projectDescription, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getProjectRequestBuilder(projectName, projectDescription, itemsList, usersList);
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
}