package com.apriori.sds.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.AssociationRequest;
import com.apriori.sds.entity.response.ScenarioAssociation;
import com.apriori.sds.entity.response.ScenarioAssociationsItems;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ScenarioAssociationsTest extends SDSTestUtil {
    private static Item testingRollUp;
    private static ScenarioAssociation testingAssociation;

    @BeforeClass
    public static void init() {
        postAssociationForTestingRollup();
    }


    @AfterClass
    public static void clearTestData() {
        if(testingAssociation != null) {
            removeTestingAssociation(testingAssociation.getIdentity());
        }
    }

    @Test
    @TestRail(testCaseId = {"6928"})
    @Description("Get scenario associations for a given scenario matching a specified query.")
    public void getAssociationsTest() {
        this.getAssociations();
    }

    @Test
    @TestRail(testCaseId = {"6929"})
    @Description("Get the current representation of a scenario assocation.")
    public void getAssociationsByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioAssociation.class)
                .inlineVariables(
                    getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(), getFirstAssociation().getIdentity()
                );

        ResponseWrapper<ScenarioAssociation> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8422"})
    @Description("Add a scenario association to a scenario.")
    public void postAssociationTest() {
        postAssociationForTestingRollup();
    }

    @Test
    @TestRail(testCaseId = {"8424"})
    @Description("Update an existing scenario association.")
    public void patchScenarioAssociation() {
        final Integer updatedOccurrences = 2;

        RequestEntity request = RequestEntityUtil.initWithApUserContext(SDSAPIEnum.PATCH_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioAssociation.class)
            .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(), getFirstAssociation().getIdentity())
            .body("association", AssociationRequest.builder().scenarioIdentity(getScenarioId())
                .occurrences(updatedOccurrences)
                .createdBy(getTestingRollUp().getComponentCreatedBy())
                .build());

        final ResponseWrapper<ScenarioAssociation> response = HTTPRequest.build(request).patch();
        final ScenarioAssociation scenarioAssociation = response.getResponseEntity();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        assertEquals("Occurrences should be updated", scenarioAssociation.getOccurrences(), updatedOccurrences);
    }

    @Test
    @TestRail(testCaseId = {"8423"})
    @Description("Remove an existing scenario association.")
    public void deleteScenarioAssociation() {
        removeTestingAssociation(postAssociationForTestingRollup().getIdentity());
        testingAssociation = null;
    }

    private static ScenarioAssociation postAssociationForTestingRollup() {

        if(testingAssociation != null) {
            return testingAssociation;
        }

        RequestEntity request = RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_ASSOCIATION_BY_COMPONENT_SCENARIO_IDS, ScenarioAssociation.class)
            .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity())
            .body("association", AssociationRequest.builder().scenarioIdentity(getTestingRollUp().getScenarioIdentity())
                .occurrences(1)
                .createdBy(getTestingRollUp().getComponentCreatedBy())
                .build());

        final ResponseWrapper<ScenarioAssociation> response = HTTPRequest.build(request).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, response.getStatusCode());

        return testingAssociation = response.getResponseEntity();
    }

    private ScenarioAssociation getFirstAssociation() {
        List<ScenarioAssociation> scenarioAssociations = this.getAssociations();

        Assert.assertNotEquals("To get Scenario Association, response should contain it.", 0, scenarioAssociations.size());
        return scenarioAssociations.get(0);
    }

    private List<ScenarioAssociation> getAssociations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioAssociationsItems.class)
                .inlineVariables(
                    getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity()
                );

        ResponseWrapper<ScenarioAssociationsItems> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity().getItems();
    }

    private static void removeTestingAssociation(String associationIdentity) {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.DELETE_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS, null)
                .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(),
                    associationIdentity);

        ResponseWrapper<String> response = HTTPRequest.build(requestEntity).delete();

        assertEquals(String.format("The association %s, was not removed", associationIdentity),
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());
    }

    public static Item getTestingRollUp() {
        if (testingRollUp != null) {
            return testingRollUp;
        }

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "AutomationRollup";
        return testingRollUp = postRollUp(componentName, scenarioName);
    }
}
