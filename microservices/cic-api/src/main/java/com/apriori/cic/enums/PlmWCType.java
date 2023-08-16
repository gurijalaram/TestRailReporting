package com.apriori.cic.enums;

/**
 * This enum is to supply data for PLM WC API
 */
public enum PlmWCType {
    PLM_WC_PART_TYPE("wt.part.WTPart"),
    PLM_WC_DOC_TYPE("wt.doc.WTDocument");

    private final String partType;

    PlmWCType(String partType) {
        this.partType = partType;
    }

    public String getPartType() {
        return this.partType;
    }
}
