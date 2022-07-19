package com.apriori.sds.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.entity.response.ScenarioItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import jdk.jfr.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class AccessControlTests extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"13638"})
    @Description("Access check: Get a list of all scripts for the correct and wrong user.")
    public void read() {
        ResponseWrapper<ScenarioItemsResponse> responseFromCorrectUser = this.getScenariosByToken(testingUser);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseFromCorrectUser.getStatusCode());

        ResponseWrapper<ScenarioItemsResponse> responseFromWrongUser = this.getScenariosByToken(UserUtil.getUser());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_UNAUTHORIZED, responseFromWrongUser.getStatusCode());
    }

    private ResponseWrapper<ScenarioItemsResponse> getScenariosByToken(final UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIOS_BY_COMPONENT_IDS, ScenarioItemsResponse.class)
                .token(userCredentials.getToken())
                .inlineVariables(
                    getComponentId()
                );
        return HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"13639"})
    @Description("Access check: Update a scenario for the correct and wrong user.")
    public void update() {
        final String updatedNotes = "Automation Notes";
        final String updatedDescription = "Automation Description";
        final ScenarioItem scenarioForUpdate = postTestingComponentAndAddToRemoveList();

        final SoftAssertions softAssertions = new SoftAssertions();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .notes(updatedNotes)
            .description(updatedDescription)
            .updatedBy(scenarioForUpdate.getCreatedBy())
            .build();

        Scenario updatedScenarioWithWrongUser = this.updateScenarioByUser(
            HttpStatus.SC_FORBIDDEN, scenarioRequestBody, scenarioForUpdate, testingUser
        );

        Scenario updatedScenarioWithCorrectUser = this.updateScenarioByUser(
            HttpStatus.SC_OK, scenarioRequestBody, scenarioForUpdate, testingUser
        );

        softAssertions.assertThat(updatedScenarioWithCorrectUser.getNotes()).isEqualTo(updatedNotes);
        softAssertions.assertThat(updatedScenarioWithCorrectUser.getDescription()).isEqualTo(updatedDescription);
    }

    private Scenario updateScenarioByUser(int expectedStatus, PostComponentRequest scenarioRequestBody, ScenarioItem scenarioForUpdate, UserCredentials testingUser) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(scenarioForUpdate.getComponentIdentity(), scenarioForUpdate.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> response = HTTPRequest.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(expectedStatus, response.getStatusCode());

        return response.getResponseEntity();
    }

    @Test
    @TestRail(testCaseId = {"13640"})
    @Description("Access check: Delete a scenario for the correct and incorrect user.")
    public void delete() {
        final ScenarioItem componentToDeleteForTestingUser = postTestingComponentAndAddToRemoveList();

        ResponseWrapper<String> responseForWrongUser = removeTestingScenarioByUser(componentToDeleteForTestingUser.getComponentIdentity(),
            componentToDeleteForTestingUser.getScenarioIdentity(),
            UserUtil.getUser());
        assertEquals(String.format("The component with scenario %s is removed, but it shouldn't be", componentToDeleteForTestingUser.getScenarioIdentity()),
            HttpStatus.SC_FORBIDDEN, responseForWrongUser.getStatusCode());

        ResponseWrapper<String> responseForCorrectUser = removeTestingScenarioByUser(componentToDeleteForTestingUser.getComponentIdentity(),
            componentToDeleteForTestingUser.getScenarioIdentity(),
            testingUser);
        assertEquals(String.format("The component with scenario %s, was not removed", componentToDeleteForTestingUser.getScenarioIdentity()),
            HttpStatus.SC_NO_CONTENT, responseForCorrectUser.getStatusCode());

        scenariosToDelete.remove(componentToDeleteForTestingUser);
    }

    public ResponseWrapper<String> removeTestingScenarioByUser(final String componentId, final String scenarioId, final UserCredentials userCredentials) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null)
                .inlineVariables(componentId, scenarioId);

        return HTTPRequest.build(requestEntity).delete();
    }

    @Test
    @TestRail(testCaseId = {"13641"})
    @Description("Access check: Publish a scenario for the correct and incorrect user.")
    public void publish() {
        ResponseWrapper<Scenario> responseFromCorrectUser = this.publishScenario(testingUser);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseFromCorrectUser.getStatusCode());

        ResponseWrapper<Scenario> responseFromWrongUser = this.publishScenario(UserUtil.getUser());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_FORBIDDEN, responseFromWrongUser.getStatusCode());
    }

    private ResponseWrapper<Scenario> publishScenario(final UserCredentials userCredentials) {
        final String publishScenarioName = new GenerateStringUtil().generateScenarioName();
        final ScenarioItem testingComponent = postTestingComponentAndAddToRemoveList();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(publishScenarioName)
            .override(false)
            .updatedBy(testingComponent.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .token(userCredentials.getToken())
                .inlineVariables(testingComponent.getComponentIdentity(), testingComponent.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        return HTTPRequest.build(requestEntity).post();
    }
}
