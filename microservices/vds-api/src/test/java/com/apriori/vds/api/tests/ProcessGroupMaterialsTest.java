package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.api.tests.util.ProcessGroupUtil;
import com.apriori.vds.api.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ProcessGroupMaterialsTest {
    private RequestEntityUtil requestEntityUtil;
    private ProcessGroupUtil processGroupUtil;
    private VDSTestUtil vdsTestUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        processGroupUtil = new ProcessGroupUtil(requestEntityUtil);
        vdsTestUtil = new VDSTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {8129})
    @Description("Get a list of Materials for a specific customer process group.")
    public void getMaterials() {
        processGroupUtil.getProcessGroupMaterial();
    }

    @Test
    @TestRail(id = {8130})
    @Description("Get a specific Material for a customer identified by its identity.")
    public void getMaterialByIdentity() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_ID, ProcessGroupMaterial.class)
                .inlineVariables(vdsTestUtil.getDigitalFactoriesResponse().getIdentity(), processGroupUtil.getAssociatedProcessGroupIdentity(), processGroupUtil.getMaterialIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
