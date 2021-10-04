package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.ProcessGroupMaterial;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialsStocksItems;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ProcessGroupMaterialStocksTest extends ProcessGroupUtil {
    @Test
    @TestRail(testCaseId = {"8191"})
    @Description("Get a list of MaterialStocks for a specific material.")
    public void getMaterialStocks() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialsStocksItems.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity());

        ResponseWrapper<ProcessGroupMaterialsStocksItems> processGroupMaterialStocksResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, processGroupMaterialStocksResponse.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8192"})
    @Description("Get a specific MaterialStock for a material identified by its identity.")
    public void getMaterialStocksByIdentity() {
        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = this.getMaterialsStocksWithItems();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialStock.class)
                .inlineVariables(
                    getDigitalFactoryIdentity(),
                    getAssociatedProcessGroupIdentity(),
                    getMaterialIdentity(),
                    processGroupMaterialsStocks.get(0).getIdentity()
                );
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    private List<ProcessGroupMaterialStock> getMaterialsStocksWithItems() {
        for (ProcessGroupMaterial material : getProcessGroupMaterial()) {
            RequestEntity requestEntity =
                RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialsStocksItems.class)
                    .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), material.getIdentity());

            ResponseWrapper<ProcessGroupMaterialsStocksItems> processGroupMaterialStocksResponse = HTTPRequest.build(requestEntity).get();
            validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, processGroupMaterialStocksResponse.getStatusCode());

            List<ProcessGroupMaterialStock> processGroupMaterialStocks = processGroupMaterialStocksResponse.getResponseEntity().getItems();

            if (!processGroupMaterialStocks.isEmpty()) {
                return processGroupMaterialStocks;
            }
        }

        Assert.fail("Materials don't contain materials stocks");

        return null;
    }


}
