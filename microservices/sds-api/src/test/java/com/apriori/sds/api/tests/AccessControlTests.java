package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.request.PostComponentRequest;
import com.apriori.sds.api.models.response.ScenarioItemsResponse;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AccessControlTests extends SDSTestUtil {

    @Test
    @TestRail(id = {13638})
    @Description("Access check: Get a list of all scripts for the correct and wrong user.")
    public void get() {
        final UserCredentials userCredentials = UserUtil.getUser("common");

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIOS_BY_COMPONENT_IDS, ScenarioItemsResponse.class)
                .token(userCredentials.getToken())
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
                .inlineVariables(
                    getComponentId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<ScenarioItemsResponse> responseForWrongUser = HTTPRequest.build(requestEntity).get();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(responseForWrongUser.getResponseEntity().getItems().isEmpty()).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13639})
    @Description("Access check: Update a scenario for the correct and wrong user.")
    public void update() {
        final String updatedNotes = "Automation Notes";
        final String updatedDescription = "Automation Description";
        final UserCredentials userCredentials = UserUtil.getUser("common");
        final ScenarioItem scenarioForUpdate = postTestingComponentAndAddToRemoveList();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .notes(updatedNotes)
            .description(updatedDescription)
            .updatedBy(scenarioForUpdate.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .token(userCredentials.getToken())
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
                .inlineVariables(scenarioForUpdate.getComponentIdentity(), scenarioForUpdate.getScenarioIdentity())
                .body("scenario", scenarioRequestBody)
                .expectedResponseCode(HttpStatus.SC_CONFLICT);

        HTTPRequest.build(requestEntity).patch();
    }

    @Test
    @TestRail(id = {13640})
    @Description("Access check: Delete a scenario for the correct and incorrect user.")
    public void delete() {
        final ScenarioItem componentToDeleteForTestingUser = postTestingComponentAndAddToRemoveList();
        final UserCredentials userCredentials = UserUtil.getUser("common");

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null)
                .token(userCredentials.getToken())
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
                .inlineVariables(componentToDeleteForTestingUser.getComponentIdentity(),
                    componentToDeleteForTestingUser.getScenarioIdentity())
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
        scenariosToDelete.remove(componentToDeleteForTestingUser);
    }

    @Test
    @TestRail(id = {13641})
    @Description("Access check: Publish a scenario for the correct and incorrect user.")
    public void publish() {
        final String publishScenarioName = new GenerateStringUtil().generateScenarioName();
        final ScenarioItem testingComponent = postTestingComponentAndAddToRemoveList();
        final UserCredentials userCredentials = UserUtil.getUser("common");

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(publishScenarioName)
            .override(false)
            .updatedBy(testingComponent.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .token(userCredentials.getToken())
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
                .inlineVariables(testingComponent.getComponentIdentity(), testingComponent.getScenarioIdentity())
                .body("scenario", scenarioRequestBody)
                .expectedResponseCode(HttpStatus.SC_CONFLICT);

        HTTPRequest.build(requestEntity).post();
    }
}
