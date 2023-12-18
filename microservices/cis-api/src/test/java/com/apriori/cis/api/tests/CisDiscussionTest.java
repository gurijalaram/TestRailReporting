package com.apriori.cis.api.tests;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.api.controller.CisDiscussionResources;
import com.apriori.cis.api.models.request.discussion.InternalDiscussionRequest;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.cis.api.models.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import jdk.jfr.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CisDiscussionTest extends CISTestUtil {

    private static ComponentInfoBuilder componentInfoBuilder;
    private SoftAssertions softAssertions;

    @BeforeAll
    public static void beforeClass() {
        componentInfoBuilder = new ScenariosUtil().postAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING));
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {13286, 13287, 13290})
    @Description("Create Internal discussion, Get Internal Discussion and Delete internal discussion")
    public void createGetAndDeleteInternalDiscussion() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = CisDiscussionResources.createInternalDiscussion(CisDiscussionResources.getDiscussionRequestBuilder(),
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_CREATED, componentInfoBuilder.getUser());

        softAssertions.assertThat(scenarioDiscussionResponse.getAttributes().getSubject()).isEqualTo("2MM-LOW-001b");
        softAssertions.assertThat(scenarioDiscussionResponse.getIdentity()).isNotEmpty();

        ScenarioDiscussionResponse internalDiscussionResponse = CisDiscussionResources.getInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(internalDiscussionResponse.getComponentIdentity()).isEqualTo(componentInfoBuilder.getComponentIdentity());
        softAssertions.assertThat(internalDiscussionResponse.getScenarioIdentity()).isEqualTo(componentInfoBuilder.getScenarioIdentity());
        softAssertions.assertThat(internalDiscussionResponse.getIdentity()).isEqualTo(scenarioDiscussionResponse.getIdentity());

        CisDiscussionResources.deleteInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            componentInfoBuilder.getUser());
    }

    @Test
    @TestRail(id = {13289, 13288})
    @Description("Create Internal discussion, Update Discussion, Get All discussions and Delete discussion")
    public void createUpdateAndDeleteInternalDiscussion() {
        InternalDiscussionRequest internalDiscussionRequest = CisDiscussionResources.getDiscussionRequestBuilder();
        String updatedDescription = new GenerateStringUtil().getRandomString();
        ScenarioDiscussionResponse scenarioDiscussionResponse = CisDiscussionResources.createInternalDiscussion(internalDiscussionRequest,
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_CREATED, componentInfoBuilder.getUser());

        softAssertions.assertThat(scenarioDiscussionResponse.getAttributes().getSubject()).isEqualTo("2MM-LOW-001b");
        softAssertions.assertThat(scenarioDiscussionResponse.getIdentity()).isNotEmpty();

        internalDiscussionRequest.getDiscussion().setDescription(updatedDescription);
        ScenarioDiscussionResponse updateDiscussionResponse = CisDiscussionResources.updateInternalDiscussion(internalDiscussionRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(updateDiscussionResponse.getDescription()).isEqualTo(updatedDescription);

        ScenarioDiscussionsResponse getInternalDiscussionsResponse = CisDiscussionResources.getInternalDiscussions(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            ScenarioDiscussionsResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(getInternalDiscussionsResponse.getItems().size()).isGreaterThan(0);

        CisDiscussionResources.deleteInternalDiscussion(updateDiscussionResponse.getComponentIdentity(),
            updateDiscussionResponse.getScenarioIdentity(),
            updateDiscussionResponse.getIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            componentInfoBuilder.getUser());
    }

    @Test
    @TestRail(id = {14151})
    @Description("Create internal discussion with invalid component identity, scenario identity and type")
    public void createInternalDiscussionWithInvalid() {
        CisErrorMessage cisInvalidCidErrorResponse = CisDiscussionResources.createInternalDiscussion(CisDiscussionResources.getDiscussionRequestBuilder(),
            "INVALID",
            componentInfoBuilder.getScenarioIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST, componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisDiscussionResources.createInternalDiscussion(CisDiscussionResources.getDiscussionRequestBuilder(),
            componentInfoBuilder.getComponentIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST, componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        InternalDiscussionRequest internalDiscussionRequest = CisDiscussionResources.getDiscussionRequestBuilder();
        internalDiscussionRequest.getDiscussion().setType("INVALID");;
        CisErrorMessage cisInvalidTypeErrorResponse = CisDiscussionResources.createInternalDiscussion(internalDiscussionRequest,
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_INTERNAL_SERVER_ERROR, componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidTypeErrorResponse.getMessage()).contains("INVALID");
    }

    @Test
    @TestRail(id = {14361})
    @Description("get internal discussion with invalid component identity, scenario identity and discussion identity")
    public void getInternalDiscussionWithInvalid() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = CisDiscussionResources.createInternalDiscussion(CisDiscussionResources.getDiscussionRequestBuilder(),
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_CREATED, componentInfoBuilder.getUser());

        softAssertions.assertThat(scenarioDiscussionResponse.getAttributes().getSubject()).isEqualTo("2MM-LOW-001b");
        CisErrorMessage cisInvalidCidErrorResponse = CisDiscussionResources.getInternalDiscussion("INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisDiscussionResources.getInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidTypeErrorResponse = CisDiscussionResources.getInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidTypeErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14362})
    @Description("delete internal discussion with invalid component identity, scenario identity and discussion identity")
    public void deleteInternalDiscussionWithInvalid() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = CisDiscussionResources.createInternalDiscussion(CisDiscussionResources.getDiscussionRequestBuilder(),
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_CREATED, componentInfoBuilder.getUser());

        softAssertions.assertThat(scenarioDiscussionResponse.getAttributes().getSubject()).isEqualTo("2MM-LOW-001b");
        CisErrorMessage cisInvalidCidErrorResponse = CisDiscussionResources.deleteInternalDiscussion("INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisDiscussionResources.deleteInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidTypeErrorResponse = CisDiscussionResources.deleteInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidTypeErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14363})
    @Description("update internal discussion with invalid component identity, scenario identity and discussion identity")
    public void updateInternalDiscussionWithInvalid() {
        InternalDiscussionRequest internalDiscussionRequest = CisDiscussionResources.getDiscussionRequestBuilder();
        String updatedDescription = new GenerateStringUtil().getRandomString();
        ScenarioDiscussionResponse scenarioDiscussionResponse = CisDiscussionResources.createInternalDiscussion(internalDiscussionRequest,
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_CREATED, componentInfoBuilder.getUser());

        softAssertions.assertThat(scenarioDiscussionResponse.getAttributes().getSubject()).isEqualTo("2MM-LOW-001b");
        CisErrorMessage cisInvalidCidErrorResponse = CisDiscussionResources.updateInternalDiscussion(internalDiscussionRequest,
            "INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisDiscussionResources.updateInternalDiscussion(internalDiscussionRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidDidErrorResponse = CisDiscussionResources.updateInternalDiscussion(internalDiscussionRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidDidErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14364})
    @Description("get internal discussions with invalid component identity and scenario identity")
    public void getInternalDiscussionsWithInvalid() {
        CisErrorMessage cisInvalidCidErrorResponse = CisDiscussionResources.getInternalDiscussions("INVALID",
            componentInfoBuilder.getScenarioIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisDiscussionResources.getInternalDiscussions(componentInfoBuilder.getComponentIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14596})
    @Description("get user discussions")
    public void getUserDiscussions() {
        ScenarioDiscussionsResponse userDiscussionsResponse = CisDiscussionResources.getUserDiscussions(
            ScenarioDiscussionsResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(userDiscussionsResponse.getItems().size()).isGreaterThan(0);
    }

    @AfterEach
    public void testClean() {
        softAssertions.assertAll();
    }

    @AfterAll
    public static void afterClass() {
        new ScenariosUtil().deleteScenario(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity(), componentInfoBuilder.getUser());
    }
}
