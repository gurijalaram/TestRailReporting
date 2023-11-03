package com.apriori.edc.ui.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EdcUiResources {

    /**
     * Get Bom Id from url
     *
     * @param currentUrl Current url
     * @return String
     */
    public static String getBillOfMaterialsId(String currentUrl) {
        return currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
    }
}
