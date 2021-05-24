package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterialsItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ProcessGroupMaterialsTest extends VDSTestUtil {

    private final DigitalFactory digitalFactory = this.getDigitalFactoriesResponse();
    private final String digitalFactoryIdentity = digitalFactory.getIdentity();
    private final String processGroupIdentity = digitalFactory.getProcessGroupAssociations().getBarTubeFab().getProcessGroupIdentity();

    @Test
    @TestRail(testCaseId = {"8129"})
    @Description("Get a list of Materials for a specific customer process group.")
    public void getMaterials() {
        this.getProcessGroupMaterials();
    }

    @Test
    @TestRail(testCaseId = {"8130"})
    @Description("Get a specific Material for a customer identified by its identity.")
    public void getMaterialByIdentity() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterial.class)
                .inlineVariables(Arrays.asList(digitalFactoryIdentity, processGroupIdentity, this.getProcessGroupMaterials().getIdentity()));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    private ProcessGroupMaterial getProcessGroupMaterials() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_BY_DF_AND_PG_IDs, ProcessGroupMaterialsItems.class)
                .inlineVariables(Arrays.asList(digitalFactoryIdentity, processGroupIdentity));

        final ResponseWrapper<ProcessGroupMaterialsItems> processGroupMaterialsItems = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            processGroupMaterialsItems.getStatusCode()
        );

        List<ProcessGroupMaterial> processGroupMaterials = processGroupMaterialsItems.getResponseEntity().getItems();

        Assert.assertNotEquals(0, processGroupMaterials.size());

        return processGroupMaterials.get(0);
    }
}
