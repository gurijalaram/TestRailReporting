package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ProcessGroupMaterialsTest extends ProcessGroupUtil {

    @Test
    @TestRail(testCaseId = {"8129"})
    @Description("Get a list of Materials for a specific customer process group.")
    public void getMaterials() {
        getProcessGroupMaterial();
    }

    @Test
    @TestRail(testCaseId = {"8130"})
    @Description("Get a specific Material for a customer identified by its identity.")
    public void getMaterialByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterial.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());
    }
}
