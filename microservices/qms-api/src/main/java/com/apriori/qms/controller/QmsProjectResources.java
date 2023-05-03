package com.apriori.qms.controller;

import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectProfile;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.request.bidpackage.CommentReminder;
import com.apriori.qms.entity.request.bidpackage.EmailReminder;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

import utils.QmsApiTestUtils;

import java.time.LocalDateTime;
import java.util.List;

public class QmsProjectResources {

    /**
     * ProjectRequestBuilder
     *
     * @param projectName        the project name
     * @param projectDescription the project description
     * @param itemsList          the items list
     * @param usersList          the users list
     * @return BidPackageProjectRequest project request builder
     */
    public static BidPackageProjectRequest getProjectRequestBuilder(String projectName, String projectDescription, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList) {
        return BidPackageProjectRequest.builder()
            .project(BidPackageProjectParameters.builder()
                .name(projectName)
                .displayName(projectName)
                .dueAt(LocalDateTime.now().plusDays(10))
                .description(projectDescription)
                .status("COMPLETED")
                .type("INTERNAL")
                .projectProfile(BidPackageProjectProfile.builder()
                    .emailReminder(EmailReminder.builder()
                        .active(true)
                        .startDuration("P1DT5M")
                        .frequencyValue("R2/P1D")
                        .build())
                    .commentReminder(CommentReminder.builder()
                        .active(true)
                        .startDuration("P1D")
                        .frequencyValue("R/P4H")
                        .build())
                    .build())
                .items(itemsList)
                .users(usersList)
                .build())
            .build();
    }

    /**
     * Create project
     *
     * @param <T>                Response class type
     * @param projectName        Unique project name
     * @param projectDescription Unique project description
     * @param itemsList          the items list
     * @param usersList          the users list
     * @param responseClass      Expected response class
     * @param httpStatus         Expected http status code
     * @param currentUser        UserCredentials
     * @return response class object
     */
    public static <T> T createProject(String projectName, String projectDescription, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getProjectRequestBuilder(projectName, projectDescription, itemsList, usersList);
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, responseClass)
            .body(projectRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get list of all projects
     *
     * @param responseClass expected response class
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @param <T>           response class type
     * @return Response class object
     */
    public static <T> T getProjects(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECTS, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
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

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets project items.
     *
     * @param <T>           the type parameter
     * @param responseClass the response class
     * @param httpStatus    the http status
     * @param currentUser   the current user
     * @return the project items
     */
    public static <T> T getProjectItems(String projectIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT_ITEMS, responseClass)
            //            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .inlineVariables(projectIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets project item.
     *
     * @param <T>             the type parameter
     * @param projectIdentity the project identity
     * @param responseClass   the response class
     * @param httpStatus      the http status
     * @param currentUser     the current user
     * @return the project item
     */
    public static <T> T getProjectItem(String projectIdentity, String projectItemIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.PROJECT_ITEM, responseClass)
            .inlineVariables(projectIdentity, projectItemIdentity)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }
}