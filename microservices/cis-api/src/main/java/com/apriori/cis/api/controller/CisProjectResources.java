package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;

import java.util.HashMap;
import java.util.List;

public class CisProjectResources {

    /**
     * Create bid package project
     *
     * @param projectAttributesMap - project attributes
     * @param itemsList     - project item list
     * @param usersList     - project users list
     * @param currentUser          - UserCredentials
     * @param <T>                - response class type
     * @return response class object
     */
    public static <T> T createProject(HashMap<String, String> projectAttributesMap, List<BidPackageItemRequest> itemsList, List<BidPackageProjectUserParameters> usersList, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        BidPackageProjectRequest projectRequest = getProjectRequestBuilder(projectAttributesMap, itemsList, usersList);
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.PROJECTS, responseClass)
            .body(projectRequest)
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
    private static BidPackageProjectRequest getProjectRequestBuilder(HashMap<String, String> projectAttributesMap, List<BidPackageItemRequest> projectItemsList,
                                                                    List<BidPackageProjectUserParameters> projectUsersList) {
        String projectName = projectAttributesMap.getOrDefault("projectName", new GenerateStringUtil().getRandomString());
        String projectDisplayName = projectAttributesMap.getOrDefault("projectDisplayName", new GenerateStringUtil().getRandomString());
        String projectDescription = projectAttributesMap.getOrDefault("projectDescription", new GenerateStringUtil().getRandomString());
        String projectStatus = projectAttributesMap.getOrDefault("projectStatus", "OPEN");
        String projectType = projectAttributesMap.getOrDefault("projectType", "INTERNAL");

        BidPackageProjectRequest projectRequest = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/BidPackageProjectRequest.json"), BidPackageProjectRequest.class);
        BidPackageProjectParameters project = projectRequest.getProject();
        project.setName(projectName);
        project.setDisplayName(projectDisplayName);
        project.setDescription(projectDescription);
        project.setStatus(projectStatus);
        project.setType(projectType);
        project.setItems(projectItemsList);
        project.setUsers(projectUsersList);
        return projectRequest;
    }
}