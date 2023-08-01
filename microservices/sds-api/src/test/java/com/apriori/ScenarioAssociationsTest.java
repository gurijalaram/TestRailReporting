package com.apriori;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.entity.response.ScenarioItem;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.AssociationRequest;
import com.apriori.sds.entity.response.ScenarioAssociation;
import com.apriori.sds.entity.response.ScenarioAssociationsItems;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ScenarioAssociationsTest extends SDSTestUtil {
    private static ScenarioItem testingRollUp;
    private static ScenarioAssociation testingAssociation;

    @AfterAll
    public static void clearTestData() {
        if (testingAssociation != null) {
            removeTestingAssociation(testingAssociation.getIdentity());
        }
    }

    @Test
    @TestRail(id = {6928})
    @Description("Get scenario associations for a given scenario matching a specified query.")
    public void getAssociationsTest() {
        this.getAssociations();
    }

    @Test
    @TestRail(id = {6929})
    @Description("Get the current representation of a scenario assocation.")
    public void getAssociationsByIdentity() {
        postAssociationForTestingRollup();
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioAssociation.class)
                .inlineVariables(
                    getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(), getFirstAssociation().getIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {8422})
    @Description("Add a scenario association to a scenario.")
    public void postAssociationTest() {
        postAssociationForTestingRollup();
    }

    @Test
    @TestRail(id = {8424})
    @Description("Update an existing scenario association.")
    public void patchScenarioAssociation() {
        final Integer updatedOccurrences = 2;

        RequestEntity request = RequestEntityUtil.init(SDSAPIEnum.PATCH_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioAssociation.class)
            .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(), getFirstAssociation().getIdentity())
            .body("association", AssociationRequest.builder().scenarioIdentity(getScenarioId())
                .occurrences(updatedOccurrences)
                .createdBy(getTestingRollUp().getComponentCreatedBy())
                .build())
            .expectedResponseCode(HttpStatus.SC_OK);

        final ResponseWrapper<ScenarioAssociation> response = HTTPRequest.build(request).patch();
        final ScenarioAssociation scenarioAssociation = response.getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(scenarioAssociation.getOccurrences()).isEqualTo(updatedOccurrences);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {8423})
    @Description("Remove an existing scenario association.")
    public void deleteScenarioAssociation() {
        removeTestingAssociation(postAssociationForTestingRollup().getIdentity());
        testingAssociation = null;
    }

    private static ScenarioAssociation postAssociationForTestingRollup() {

        if (testingAssociation != null) {
            return testingAssociation;
        }

        RequestEntity request = RequestEntityUtil.init(SDSAPIEnum.POST_ASSOCIATION_BY_COMPONENT_SCENARIO_IDS, ScenarioAssociation.class)
            .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity())
            .headers(getContextHeaders())
            .body("association", AssociationRequest.builder().scenarioIdentity(getTestingRollUp().getScenarioIdentity())
                .occurrences(1)
                .excluded(false)
                .createdBy(getTestingRollUp().getComponentCreatedBy())
                .build())
            .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<ScenarioAssociation> response = HTTPRequest.build(request).post();

        return testingAssociation = response.getResponseEntity();
    }

    private ScenarioAssociation getFirstAssociation() {
        List<ScenarioAssociation> scenarioAssociations = this.getAssociations();

        assertNotEquals(0, scenarioAssociations.size(), "To get Scenario Association, response should contain it.");
        return scenarioAssociations.get(0);
    }

    private List<ScenarioAssociation> getAssociations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioAssociationsItems.class)
                .inlineVariables(
                    getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioAssociationsItems> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity().getItems();
    }

    private static void removeTestingAssociation(String associationIdentity) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.DELETE_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS, null)
                .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(),
                    associationIdentity)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);;

        HTTPRequest.build(requestEntity).delete();
    }

    public static ScenarioItem getTestingRollUp() {
        if (testingRollUp != null) {
            return testingRollUp;
        }

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "AutomationRollup";
        return testingRollUp = postRollUp(componentName, scenarioName);
    }
}
