package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.process.group.associations.ProcessGroupAssociationRequest;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.tests.util.ProcessGroupUtil;
import com.apriori.vds.tests.util.VDSTestUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessGroupAssociationsTest extends ProcessGroupUtil {

    private static ProcessGroupAssociation testingProcessGroupAssociation;

    @AfterClass
    public void clearTestData() {
        if(testingProcessGroupAssociation != null) {
            deleteProcessGroupAssociation(testingProcessGroupAssociation.getIdentity());
        }
    }

    @Test
    @TestRail(testCaseId = {"C8411"})
    @Description("Returns a paged set of ProcessGroupAssociation for the current user.")
    public void testGetProcessGroupAssociations() {
            getProcessGroupAssociations();
    }

    @Test
    @TestRail(testCaseId = {"C8414"})
    @Description("Get a ProcessGroupAssociation for a customer.")
    public void testGetMaterialByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PG_ASSOCIATIONS_BY_ID, ProcessGroupAssociation.class)
            .inlineVariables(getFirstGroupAssociation().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"C8412"})
    @Description("Adds a ProcessGroupAssociation for a customer.")
    public void testPostProcessGroupAssociation() {
        createProcessGroupAssociation();
    }

    @Test
    @TestRail(testCaseId = {""})
    @Description("")
    public void testDeleteProcessGroupAssociation() {
       deleteProcessGroupAssociation(
           createProcessGroupAssociation().getIdentity()
       );
    }

    @Test
    @TestRail(testCaseId = {""})
    @Description("")
    public void testPatchProcessGroupAssociation() {
        ProcessGroupAssociation processGroupAssociationToUpdate = getTestingProcessGroupAssociation();

        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()
            .processGroupIdentity(processGroupAssociationToUpdate.getIdentity())
            .processGroupName(processGroupAssociationToUpdate.getProcessGroupName())
            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.PATCH_PG_ASSOCIATIONS_BY_ID, null)
                .inlineVariables(processGroupAssociationRequest.getIdentity())
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTP2Request.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, createProcessGroupAssociationResponse.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {""})
    @Description("")
    public void testPutProcessGroupAssociation() {
        ProcessGroupAssociation processGroupAssociationToUpdate = getTestingProcessGroupAssociation();

        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()
            .processGroupIdentity(processGroupAssociationToUpdate.getIdentity())
            .processGroupName(processGroupAssociationToUpdate.getProcessGroupName())
            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.PATCH_PG_ASSOCIATIONS_BY_ID, null)
                .inlineVariables(processGroupAssociationRequest.getIdentity())
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTP2Request.build(requestEntity).put();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, createProcessGroupAssociationResponse.getStatusCode());
    }

    private static void deleteProcessGroupAssociation(final String associationId) {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.DELETE_PG_ASSOCIATIONS_BY_ID, null)
                .inlineVariables(associationId);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }

    private ProcessGroupAssociation createProcessGroupAssociation() {
        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()
            .processGroupName(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.POST_PG_ASSOCIATIONS, null)
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, createProcessGroupAssociationResponse.getStatusCode());

        return createProcessGroupAssociationResponse.getResponseEntity();
    }

    private ProcessGroupAssociation getTestingProcessGroupAssociation() {
        if(testingProcessGroupAssociation != null) {
            return testingProcessGroupAssociation;
        }

        return testingProcessGroupAssociation = createProcessGroupAssociation();
    }
}
