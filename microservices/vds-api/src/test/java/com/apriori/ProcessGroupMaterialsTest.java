package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;
import com.apriori.util.ProcessGroupUtil;
import com.apriori.util.VDSTestUtil;
import com.apriori.vds.enums.VDSAPIEnum;
import com.apriori.vds.models.response.process.group.materials.ProcessGroupMaterial;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class ProcessGroupMaterialsTest extends ProcessGroupUtil {

    @Test
    @TestRail(id = {8129})
    @Description("Get a list of Materials for a specific customer process group.")
    public void getMaterials() {
        ProcessGroupUtil.getProcessGroupMaterial();
    }

    @Test
    @TestRail(id = {8130})
    @Description("Get a specific Material for a customer identified by its identity.")
    public void getMaterialByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterial.class)
                .inlineVariables(VDSTestUtil.getDigitalFactoryIdentity(), ProcessGroupUtil.getAssociatedProcessGroupIdentity(), ProcessGroupUtil.getMaterialIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
