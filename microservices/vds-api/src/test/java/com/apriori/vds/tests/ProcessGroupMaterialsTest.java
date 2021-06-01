package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Arrays;

public class ProcessGroupMaterialsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8129"})
    @Description("Get a list of Materials for a specific customer process group.")
    public void getMaterials() {
        this.getProcessGroupMaterial();
    }

    @Test
    @TestRail(testCaseId = {"8130"})
    @Description("Get a specific Material for a customer identified by its identity.")
    public void getMaterialByIdentity() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterial.class)
                .inlineVariables(Arrays.asList(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity(), getMaterialIdentity()));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }
}
