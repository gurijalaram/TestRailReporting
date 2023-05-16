package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.SustainabilityInfo;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.List;

public class SustainabilityMaterialsTests extends ProcessGroupUtil {
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"24098"})
    @Description("Get materials endpoint returns sustainability info")
    public void verifySustainabilityFieldsForMaterials() {
        List<ProcessGroupMaterial> materials = getProcessGroupMaterial();
        materials.stream().forEach(material -> assertSustainabilityInfoInMaterials(material.getSustainabilityInfo()));

        ResponseWrapper<ProcessGroupMaterial> materialById = getMaterialById();
        assertSustainabilityInfoInMaterials(materialById.getResponseEntity().getSustainabilityInfo());
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"24099"})
    @Description("Get material stocks endpoint returns sustainability info")
    public void verifySustainabilityInfoForMaterialStocks() {
        List<ProcessGroupMaterialStock> materialStocks = getProcessGroupMaterialStocks();
        materialStocks.stream().forEach(stock -> assertMaterialStocksSustainabilityInfo(stock.getSustainabilityInfo()));

        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = getMaterialsStocksWithItems();
        ResponseWrapper<ProcessGroupMaterialStock> stockById = getMaterialStockById(processGroupMaterialsStocks);
        assertMaterialStocksSustainabilityInfo(stockById.getResponseEntity().getSustainabilityInfo());
        soft.assertAll();
    }


    private ResponseWrapper<ProcessGroupMaterial> getMaterialById() {
        RequestEntity materialById =
            RequestEntityUtil.init(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterial.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity())
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