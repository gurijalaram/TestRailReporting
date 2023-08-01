package com.apriori;

import com.apriori.testrail.TestRail;
import com.apriori.util.ProcessGroupUtil;
import com.apriori.vds.entity.response.process.group.materials.stock.ProcessGroupMaterialStock;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import java.util.List;

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
