package com.apriori.sds.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.AssociationRequest;
import com.apriori.sds.entity.response.CostingTemplate;
import com.apriori.sds.util.SDSRequestEntityUtil;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ScenarioAssociationsTest extends SDSTestUtil {

    private static Item testingRollUp;
    private static List<String> assemblyIdToDelete = new ArrayList<>();

    @AfterClass
    public static void clearTestingData() {
        assemblyIdToDelete.forEach(ScenarioAssociationsTest::removeTestingAssociation);
    }

    @Test
    @TestRail(testCaseId = {"6928"})
    @Description("Find scenario associations for a given scenario matching a specified query.")
    @Ignore
    public void getAssociationsTest() {
        ResponseWrapper<Void> response = this.getAssociations();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    // TODO z: add association identity
    @Test
    @TestRail(testCaseId = {"6929"})
    @Description("Get the current representation of a scenario assocation.")
    public void getAssociationsByIdentity() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, null)
                .inlineVariables(
                    getComponentId(), getScenarioId(), getFirstAssociation().getIdentity()
                );

        ResponseWrapper<Void> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    public void postAssociationTest() {
        this.postAssociation();
    }

    // TODO z: finish it
    @Test
    public void patchScenarioAssociation() {
        RequestEntity request = SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_ASSOCIATION_BY_COMPONENT_SCENARIO_IDS, null)
            .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity())
            .body("association", AssociationRequest.builder().scenarioIdentity(getScenarioId())
                .occurrences(1)
                .createdBy(getTestingRollUp().getComponentCreatedBy())
                .build());

        ResponseWrapper<Void> response = HTTP2Request.build(request).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, response.getStatusCode());
    }

    // TODO z: fix id
    @Test
    public void deleteScenarioAssociation() {
        removeTestingAssociation(postAssociation().toString());
    }


    private Void postAssociation() {
        RequestEntity request = SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_ASSOCIATION_BY_COMPONENT_SCENARIO_IDS, null)
            .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity())
            .body("association", AssociationRequest.builder().scenarioIdentity(getScenarioId())
                .occurrences(1)
                .createdBy(getTestingRollUp().getComponentCreatedBy())
                .build());

        ResponseWrapper<Void> response = HTTP2Request.build(request).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, response.getStatusCode());

        return response.getResponseEntity();
    }

    private CostingTemplate getFirstAssociation() {
        List<CostingTemplate> costingTemplates = null;// = this.getAssociations().getResponseEntity();
        Assert.assertNotEquals("To get CostingTemplate, response should contain it.", 0, costingTemplates.size());

        return costingTemplates.get(0);
    }

    private ResponseWrapper<Void> getAssociations() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS, null)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        return HTTP2Request.build(requestEntity).get();
    }

    private static void removeTestingAssociation(String associationIdentity) {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.DELETE_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS, null)
                .inlineVariables(getTestingRollUp().getComponentIdentity(), getTestingRollUp().getScenarioIdentity(),
                    associationIdentity);

        ResponseWrapper<String> response = HTTP2Request.build(requestEntity).delete();

        assertEquals(String.format("The association %s, was not removed", associationIdentity),
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());
    }

    public static Item getTestingRollUp() {
        if (testingRollUp != null) {
            return testingRollUp;
        }

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "test.Initial.ap";
        return testingRollUp = postComponent(componentName, scenarioName, ProcessGroupEnum.ROLL_UP);
    }
}
