package com.apriori.vds.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.process.group.associations.ProcessGroupAssociationRequest;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProcessGroupAssociationsTest extends ProcessGroupUtil {

    private static ProcessGroupAssociation testingProcessGroupAssociation;
    private static ProcessGroupAssociation backupProcessGroupAssociation;

    @BeforeClass
    public static void initTestingData() {
        backupProcessGroupAssociation = getFirstGroupAssociation();
        deleteProcessGroupAssociation(backupProcessGroupAssociation.getIdentity());
    }


    @AfterClass
    public static void clearTestDataAndUploadDefault() {
        if (testingProcessGroupAssociation != null) {
            deleteProcessGroupAssociation(testingProcessGroupAssociation.getIdentity());
        }

        createProcessGroupAssociation(
            convertAssociationToRequest(backupProcessGroupAssociation)
        );
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

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"C8412"})
    @Description("Adds a ProcessGroupAssociation for a customer.")
    public void testPostProcessGroupAssociation() {
        getTestingProcessGroupAssociation();
    }

    @Test
    @TestRail(testCaseId = {"C8415"})
    @Description("DELETEs a ProcessGroupAssociation for a customer.")
    public void testDeleteProcessGroupAssociation() {
        deleteProcessGroupAssociation(
            getTestingProcessGroupAssociation().getIdentity()
        );
    }

    @Test
    @TestRail(testCaseId = {"C8416"})
    @Description("Updates a ProcessGroupAssociation for a customer.")
    public void testPatchProcessGroupAssociation() {
        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()
            .defaultVpeIdentity(backupProcessGroupAssociation.getDefaultVpeIdentity())
            .defaultVpeName(backupProcessGroupAssociation.getDefaultVpeName())
            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.PATCH_PG_ASSOCIATIONS_BY_ID, ProcessGroupAssociation.class)
                .inlineVariables(getTestingProcessGroupAssociation().getIdentity())
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createProcessGroupAssociationResponse.getStatusCode());

        ProcessGroupAssociation updatedProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();
        assertEquals("VPE identity should be updated", updatedProcessGroupAssociation.getDefaultVpeIdentity(), backupProcessGroupAssociation.getDefaultVpeIdentity());
        assertEquals("VPE name should be updated", updatedProcessGroupAssociation.getDefaultVpeName(), backupProcessGroupAssociation.getDefaultVpeName());
        assertNotNull("Updated By should present", updatedProcessGroupAssociation.getUpdatedBy());
        assertNotNull("Updated At should present", updatedProcessGroupAssociation.getUpdatedAt());
    }

    @Test
    @TestRail(testCaseId = {"C8413"})
    @Description("Adds or Replaces a ProcessGroupAssociation for a customer. ")
    public void testPutCreateProcessGroupAssociation() {
        ProcessGroupAssociation processGroupAssociationToUpdate = getTestingProcessGroupAssociation();

        deleteProcessGroupAssociation(processGroupAssociationToUpdate.getIdentity());

        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()
            .processGroupIdentity(processGroupAssociationToUpdate.getIdentity())
            .processGroupName(processGroupAssociationToUpdate.getProcessGroupName())
            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.PUT_PG_ASSOCIATIONS, ProcessGroupAssociation.class)
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).put();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createProcessGroupAssociationResponse.getStatusCode());

        ProcessGroupAssociation createdProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();

        assertNotEquals("Created association should have another identifier.", createdProcessGroupAssociation.getIdentity(),
            processGroupAssociationToUpdate.getIdentity()
        );

        testingProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();
    }

    @Test
    @TestRail(testCaseId = {"C9038"})
    @Description("Replaces a ProcessGroupAssociation for a customer. ")
    public void testPutUpdateProcessGroupAssociation() {
        ProcessGroupAssociation processGroupAssociationToUpdate = getTestingProcessGroupAssociation();

        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()
            .identity(processGroupAssociationToUpdate.getIdentity())
            .processGroupIdentity(processGroupAssociationToUpdate.getIdentity())
            .processGroupName(processGroupAssociationToUpdate.getProcessGroupName())
            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.PUT_PG_ASSOCIATIONS, ProcessGroupAssociation.class)
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).put();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createProcessGroupAssociationResponse.getStatusCode());

        ProcessGroupAssociation updatedProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();

        assertEquals("Updated association should have the same identifier.", updatedProcessGroupAssociation.getIdentity(),
            processGroupAssociationToUpdate.getIdentity()
        );
    }

    private static void deleteProcessGroupAssociation(final String associationId) {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.DELETE_PG_ASSOCIATIONS_BY_ID, null)
                .inlineVariables(associationId);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());

        testingProcessGroupAssociation = null;
    }

    private static ProcessGroupAssociation createProcessGroupAssociation(final ProcessGroupAssociationRequest processGroupAssociationRequest) {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.POST_PG_ASSOCIATIONS, ProcessGroupAssociation.class)
                .body("processGroupAssociation", processGroupAssociationRequest);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createProcessGroupAssociationResponse.getStatusCode());

        return createProcessGroupAssociationResponse.getResponseEntity();
    }

    private static ProcessGroupAssociationRequest convertAssociationToRequest(ProcessGroupAssociation backupProcessGroupAssociation) {
        return ProcessGroupAssociationRequest
            .builder()
            .createdBy(backupProcessGroupAssociation.getCreatedBy())
            .defaultVpeIdentity(backupProcessGroupAssociation.getDefaultVpeIdentity())
            .defaultVpeName(backupProcessGroupAssociation.getDefaultVpeName())
            .processGroupIdentity(backupProcessGroupAssociation.getProcessGroupIdentity())
            .processGroupName(backupProcessGroupAssociation.getProcessGroupName())
            .build();
    }

    private ProcessGroupAssociationRequest initCustomProcessGroupAssociationRequest() {
        return ProcessGroupAssociationRequest
            .builder()
            .processGroupIdentity(backupProcessGroupAssociation.getProcessGroupIdentity())
            .processGroupName(backupProcessGroupAssociation.getProcessGroupName())
            .build();
    }

    private ProcessGroupAssociation getTestingProcessGroupAssociation() {
        if (testingProcessGroupAssociation != null) {
            return testingProcessGroupAssociation;
        }

        return testingProcessGroupAssociation = createProcessGroupAssociation(
            initCustomProcessGroupAssociationRequest()
        );
    }
}
