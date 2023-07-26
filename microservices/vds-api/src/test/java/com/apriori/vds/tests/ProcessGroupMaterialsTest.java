package com.apriori.vds.tests;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ProcessGroupMaterialsTest extends ProcessGroupUtil {

    @Test
    @TestRail(id = {8129})
    @Description("Get a list of Materials for a specific customer process group.")
    public void getMaterials() {
        getProcessGroupMaterial();
    }

    @Test
    @TestRail(id = {8130})
    @Description("Get a specific Material for a customer identified by its identity.")
    public void getMaterialByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterial.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
