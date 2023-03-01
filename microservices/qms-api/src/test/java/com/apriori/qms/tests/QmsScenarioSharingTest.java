package com.apriori.qms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsComponentResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QmsScenarioSharingTest extends TestUtil {

    public static ScenarioItem scenarioItem;
    private static SoftAssertions softAssertions;
    private UserCredentials currentUser;
    private static String userContext;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
    }

    @Test
    @TestRail(testCaseId = {"15474"})
    @Description("get component Details by identity")
    public void addProjectUser() {
        ProjectUserParameters projectUserParameters = ProjectUserParameters.builder()
            .email(currentUser.getEmail()).build();
        QmsComponentResources.addProjectUser(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            projectUserParameters, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15475"})
    @Description("get project users ")
    public void getScenarioProjectUsers() {
        ScenarioProjectUserResponse responseWrapper = QmsScenarioDiscussionResources.getScenarioProjectUsers(
            scenarioItem.getComponentIdentity(),scenarioItem.getScenarioIdentity(),currentUser);
        softAssertions.assertThat(responseWrapper.size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}