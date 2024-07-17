package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.api.models.response.process.group.materials.SustainabilityInfo;
import com.apriori.vds.api.models.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.api.tests.util.ProcessGroupUtil;
import com.apriori.vds.api.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class SustainabilityMaterialsTests extends ProcessGroupUtil {
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = {24098})
    @Description("Get materials endpoint returns sustainability info")
    public void verifySustainabilityFieldsForMaterials() {
        List<ProcessGroupMaterial> materials = ProcessGroupUtil.getProcessGroupMaterial();
        materials.stream().forEach(material -> assertSustainabilityInfoInMaterials(material.getSustainabilityInfo()));

        ResponseWrapper<ProcessGroupMaterial> materialById = getMaterialById();
        assertSustainabilityInfoInMaterials(materialById.getResponseEntity().getSustainabilityInfo());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {24099})
    @Description("Get material stocks endpoint returns sustainability info")
    public void verifySustainabilityInfoForMaterialStocks() {
        List<ProcessGroupMaterialStock> materialStocks = ProcessGroupUtil.getProcessGroupMaterialStocks();
        materialStocks.stream().forEach(stock -> assertMaterialStocksSustainabilityInfo(stock.getSustainabilityInfo()));

        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = ProcessGroupUtil.getMaterialsStocksWithItems();
        ResponseWrapper<ProcessGroupMaterialStock> stockById = ProcessGroupUtil.getMaterialStockById(processGroupMaterialsStocks);
        assertMaterialStocksSustainabilityInfo(stockById.getResponseEntity().getSustainabilityInfo());
        soft.assertAll();
    }


    private ResponseWrapper<ProcessGroupMaterial> getMaterialById() {
        RequestEntity materialById =
            requestEntityUtil.init(VDSAPIEnum.SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_ID, ProcessGroupMaterial.class)
                .inlineVariables(VDSTestUtil.getDigitalFactoryIdentity(), ProcessGroupUtil.getAssociatedProcessGroupIdentity(), ProcessGroupUtil.getMaterialIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(materialById).get();
    }

    private void assertSustainabilityInfoInMaterials(SustainabilityInfo sustainabilityInfo) {
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("identity");
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("hexBarCarbonEmissionsFactor");
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("rectangularBarCarbonEmissionsFactor");
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("roundBarCarbonEmissionsFactor");
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("roundTubeCarbonEmissionsFactor");
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("squareBarCarbonEmissionsFactor");
    }

    private void assertMaterialStocksSustainabilityInfo(SustainabilityInfo sustainabilityInfo) {
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("identity");
        soft.assertThat(sustainabilityInfo).hasFieldOrProperty("carbonEmissionsFactor");
    }
}