package com.apriori.vds.api.tests;

import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.models.response.process.group.materials.stock.ProcessGroupMaterialStock;
import com.apriori.vds.api.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class ProcessGroupMaterialStocksTest extends ProcessGroupUtil {
    @Test
    @TestRail(id = {8191})
    @Description("Get a list of MaterialStocks for a specific material.")
    public void getMaterialStocks() {
        ProcessGroupUtil.getProcessGroupMaterialStocks();
    }

    @Test
    @TestRail(id = {8192})
    @Description("Get a specific MaterialStock for a material identified by its identity.")
    public void getMaterialStocksByIdentity() {
        List<ProcessGroupMaterialStock> processGroupMaterialsStocks = ProcessGroupUtil.getMaterialsStocksWithItems();

        ProcessGroupUtil.getMaterialStockById(processGroupMaterialsStocks);
    }
}
