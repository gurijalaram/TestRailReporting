package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialsStocksItems;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ProcessGroupMaterialStocksTest extends VDSTestUtil {
    @Test
    @TestRail(testCaseId = {"8191"})
    @Description("Get a list of MaterialStocks for a specific material.")
    public void getMaterialStocks() {
        this.getMaterialsStocks();
    }

    @Test
    @TestRail(testCaseId = {"8192"})
    @Description("Get a specific MaterialStock for a material identified by its identity.")
    public void getMaterialStocksByIdentity() {
        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = this.getMaterialsStocks();
        Assert.assertNotEquals("To get Material Stock, response should contain it.", 0, processGroupMaterialsStocks.size());

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialStock.class)
                .inlineVariables(
                    getDigitalFactoryIdentity(),
                    getAssociatedProcessGroupIdentity(),
                    getMaterialIdentity(),
                    processGroupMaterialsStocks.get(0).getIdentity()
                );
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    private List<ProcessGroupMaterialStock> getMaterialsStocks() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, ProcessGroupMaterialsStocksItems.class)
                .inlineVariables(getDigitalFactoryIdentity(), getAssociatedProcessGroupIdentity(), getMaterialIdentity());

        ResponseWrapper<ProcessGroupMaterialsStocksItems> processGroupMaterialStocksResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, processGroupMaterialStocksResponse.getStatusCode());


        return processGroupMaterialStocksResponse.getResponseEntity().getItems();
    }

}