package com.util;

import com.apriori.edc.utils.EDCResources;
import com.apriori.utils.web.driver.TestBase;

public class EdcUiTestUtil extends TestBase {

    public void deleteBom(String endpoint) {
        EDCResources.deleteBillOfMaterial(endpoint);
    }
}
