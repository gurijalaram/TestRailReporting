package com.apriori.cic.enums;

/**
 * enum to build query parameters for PLM WC Search API
 */
public enum PlmPartsSearch {
    PLM_WC_PART_NAME_STARTS_WITH("startswith(name,'%s')"),
    PLM_WC_PART_NAME_ENDS_WITH("endswith(name,'%s')"),
    PLM_WC_PART_NUMBER_EQ("number eq '%s':"),
    PLM_WC_PART_TYPE_ID("typeId:"),
    PLM_WC_PART_FILTER("$filter:");

    private final String filterKey;

    PlmPartsSearch(String key) {
        this.filterKey = key;
    }

    public String getFilterKey() {
        return this.filterKey;
    }
}
