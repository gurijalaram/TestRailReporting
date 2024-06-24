package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.enums.ProjectStatusEnum;
import com.apriori.cis.api.enums.ProjectTypeEnum;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.api.models.request.bidpackage.ProjectItemNotificationRequest;
import com.apriori.cis.api.models.request.bidpackage.ProjectNotificationRequest;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;

import java.util.List;

public class CisProjectResources extends CISTestUtil {

    /**
     * Create bid package project
     *
     * @param projectAttributesMap - project attributes
     * @param itemsList            - project item list
     * @param usersList            - project users list
     * @param currentUser          - UserCredentials
     * @param <T>                  - response class type
     * @return response class object
     */
    public static <T> T createProject(BidPackageProjectRequest projectRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.PROJECTS, responseClass)
            .body(projectRequestBuilder)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get Project using project identity
     *
     * @param projectIdentity - Identity
     * @param responseClass   - expected response class
     * @param httpStatus      - expected http status code
     * @param currentUser     - UserCredentials
     * @param <T>             - response class type
     * @return - response class object
     */
    public static <T> T getProject(String projectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.PROJECT, responseClass)
            .inlineVariables(projectIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get projects notification count
     *
     * @param projectNotificationRequest - ProjectNotificationRequest Builder
     * @param responseClass              - expected response class
     * @param httpStatus                 - expected http status code
     * @param currentUser                - UserCredentials
     * @param <T>                        - expected class type
     * @return - response class object
     */
    public static <T> T getProjectNotifications(ProjectNotificationRequest projectNotificationRequest, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.PROJECT_NOTIFICATIONS, responseClass)
            .body(projectNotificationRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get Project Item Notifications count
     *
     * @param projectIdentity                - ProjectIdentity
     * @param projectItemNotificationRequest - ProjectItemNotification Request
     * @param responseClass                  - expected response class
     * @param httpStatus                     - expected http status code
     * @param currentUser                    - UserCredentials
     * @param <T>                            - expected response class type
     * @return - expected class object
     */
    public static <T> T getProjectItemNotifications(String projectIdentity, ProjectItemNotificationRequest projectItemNotificationRequest, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.PROJECT_ITEM_NOTIFICATIONS, responseClass)
            .inlineVariables(projectIdentity)
            .body(projectItemNotificationRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get project request builder
     *
     * @param projectAttributesMap - project attributes
     * @param projectItemsList     - project item list
     * @param projectUsersList     - project users list
     * @return BidPackageProjectRequest
     */
    public static BidPackageProjectRequest getProjectRequestBuilder(String projectName, ProjectStatusEnum projectStatus,
                                                                    ProjectTypeEnum projectType, List<BidPackageItemRequest> projectItemsList,
                                                                    List<BidPackageProjectUserParameters> projectUsersList) {
        BidPackageProjectRequest projectRequest = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/BidPackageProjectRequest.json"), BidPackageProjectRequest.class);
        BidPackageProjectParameters project = projectRequest.getProject();
        project.setName(projectName);
        project.setDisplayName(new GenerateStringUtil().getRandomStringSpecLength(12));
        project.setDescription(new GenerateStringUtil().getRandomStringSpecLength(12));
        project.setStatus(projectStatus.getProjectStatus());
        project.setType(projectType.getProjectType());
        project.setItems(projectItemsList);
        project.setUsers(projectUsersList);
        projectRequest.setProject(project);
        return projectRequest;
    }
}