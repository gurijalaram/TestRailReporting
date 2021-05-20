package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialsStocksItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ProcessGroupMaterialStocksTest extends ProcessGroupMaterialsTest {

    private final String materialIdentity = this.getProcessGroupMaterial().getIdentity();

    @Test
    @TestRail(testCaseId = {"8191"})
    @Description("Get a list of MaterialStocks for a specific material.")
    public void getMaterialStocks() {
        this.getMaterialsStock();
    }

    @Test
    @TestRail(testCaseId = {"8192"})
    @Description("Get a specific MaterialStock for a material identified by its identity.")
    @Ignore
    public void getMaterialStocksByIdentity() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SPECIFIC_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, null)
                .inlineVariables(Arrays.asList(
                    digitalFactoryIdentity,
                    processGroupIdentity,
                    materialIdentity,
                    this.getMaterialsStock().getIdentity()
                ));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    private ProcessGroupMaterialStock getMaterialsStock() {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_MATERIALS_STOCKS_BY_DF_PG_AND_MATERIAL_IDs, null)
                .inlineVariables(Arrays.asList(digitalFactoryIdentity, processGroupIdentity, materialIdentity));

        ResponseWrapper<ProcessGroupMaterialsStocksItems> processGroupMaterialStocksResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, processGroupMaterialStocksResponse.getStatusCode());

        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = processGroupMaterialStocksResponse.getResponseEntity().getItems();

        Assert.assertNotEquals("To get Material Stock, response should contain it.", 0, processGroupMaterialsStocks.size());

        return processGroupMaterialsStocks.get(0);
    }

}
