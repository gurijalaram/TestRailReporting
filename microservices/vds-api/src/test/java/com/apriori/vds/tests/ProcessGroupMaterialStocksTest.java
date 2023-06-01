package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.List;

public class ProcessGroupMaterialStocksTest extends ProcessGroupUtil {
    @Test
    @TestRail(testCaseId = {"8191"})
    @Description("Get a list of MaterialStocks for a specific material.")
    public void getMaterialStocks() {
        getProcessGroupMaterialStocks();
    }

    @Test
    @TestRail(testCaseId = {"8192"})
    @Description("Get a specific MaterialStock for a material identified by its identity.")
    public void getMaterialStocksByIdentity() {
        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = getMaterialsStocksWithItems();

        getMaterialStockById(processGroupMaterialsStocks);
    }



}
