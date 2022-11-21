package com.apriori.vds.tests;

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
import org.assertj.core.api.SoftAssertions;
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
            RequestEntityUtil.init(VDSAPIEnum.GET_PG_ASSOCIATIONS_BY_ID, ProcessGroupAssociation.class)
                .inlineVariables(getFirstGroupAssociation().getIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
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
            RequestEntityUtil.init(VDSAPIEnum.PATCH_PG_ASSOCIATIONS_BY_ID, ProcessGroupAssociation.class)
                .inlineVariables(getTestingProcessGroupAssociation().getIdentity())
                .body("processGroupAssociation", processGroupAssociationRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).patch();

        ProcessGroupAssociation updatedProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedProcessGroupAssociation.getDefaultVpeIdentity()).isEqualTo(backupProcessGroupAssociation.getDefaultVpeIdentity());
        softAssertions.assertThat(updatedProcessGroupAssociation.getDefaultVpeName()).isEqualTo(backupProcessGroupAssociation.getDefaultVpeName());
        softAssertions.assertThat(updatedProcessGroupAssociation.getUpdatedBy()).isNotNull();
        softAssertions.assertThat(updatedProcessGroupAssociation.getUpdatedAt()).isNotNull();
        softAssertions.assertAll();
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
            RequestEntityUtil.init(VDSAPIEnum.PUT_PG_ASSOCIATIONS, ProcessGroupAssociation.class)
                .body("processGroupAssociation", processGroupAssociationRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).put();

        ProcessGroupAssociation createdProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();


        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(createdProcessGroupAssociation.getIdentity()).isNotEqualTo(processGroupAssociationToUpdate.getIdentity());
        softAssertions.assertAll();

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
            RequestEntityUtil.init(VDSAPIEnum.PUT_PG_ASSOCIATIONS, ProcessGroupAssociation.class)
                .body("processGroupAssociation", processGroupAssociationRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);


        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).put();

        ProcessGroupAssociation updatedProcessGroupAssociation = createProcessGroupAssociationResponse.getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedProcessGroupAssociation.getIdentity())
            .isEqualTo(processGroupAssociationToUpdate.getIdentity());
        softAssertions.assertAll();
    }

    private static void deleteProcessGroupAssociation(final String associationId) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.DELETE_PG_ASSOCIATIONS_BY_ID, null)
                .inlineVariables(associationId)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();

        testingProcessGroupAssociation = null;
    }

    private static ProcessGroupAssociation createProcessGroupAssociation(final ProcessGroupAssociationRequest processGroupAssociationRequest) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.POST_PG_ASSOCIATIONS, ProcessGroupAssociation.class)
                .body("processGroupAssociation", processGroupAssociationRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<ProcessGroupAssociation> createProcessGroupAssociationResponse = HTTPRequest.build(requestEntity).post();

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
