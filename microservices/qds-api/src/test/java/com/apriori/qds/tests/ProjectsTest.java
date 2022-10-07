package com.apriori.qds.tests;

import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.request.projects.Project;
import com.apriori.qds.entity.request.projects.ProjectRequest;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qds.entity.response.projects.ProjectsResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectsTest {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String userContext;
    private static String projectName;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
    }

    @Test
    public void createProject() {
        ProjectRequest projectRequest = ProjectRequest.builder()
            .project(Project.builder()
                .name(projectName)
                .description(projectName)
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT, ProjectsResponse.class)
            .inlineVariables(bidPackageResponse.getResponseEntity().getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .body(projectRequest)
            .apUserContext(userContext);

        ResponseWrapper<ProjectsResponse> ProjectsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(ProjectsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(ProjectsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    public void getProjects() {

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.PROJECTS, ProjectsResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<ProjectsResponse> ProjectsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(ProjectsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(ProjectsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        //  ResponseWrapper<String> responseWrapper = LayoutResources.deleteLayout(layoutResponse.getResponseEntity().getIdentity(), userContext);
        //   softAssertions.assertThat(responseWrapper.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();

    }
}
