package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.process.group.associations.ProcessGroupAssociationRequest;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociation;
import com.apriori.vds.entity.response.process.group.associations.ProcessGroupAssociationsItems;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.tests.util.VDSTestUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ProcessGroupAssociationsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"C8411"})
    @Description("Returns a paged set of ProcessGroupAssociation for the current user.")
    public void testGetProcessGroupAssociations() {
            this.getProcessGroupAssociations();
    }

    @Test
    @TestRail(testCaseId = {"C8414"})
    @Description("Get a ProcessGroupAssociation for a customer.")
    public void testGetMaterialByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PG_ASSOCIATIONS_BY_ID, ProcessGroupAssociation.class)
            .inlineVariables(this.getFirstGroupAssociation().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"C8412"})
    @Description("dds a ProcessGroupAssociation for a customer.")
    public void testPostProcessGroupAssociation() {
        ProcessGroupAssociationRequest processGroupAssociationRequest = ProcessGroupAssociationRequest
            .builder()

            .build();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.POST_PG_ASSOCIATIONS, null)
            .body("processGroupAssociation", processGroupAssociationRequest);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).post().getStatusCode());
    }
}
