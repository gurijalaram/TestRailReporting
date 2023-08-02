package com.apriori.cic.enums;

/**
 * This enum is used to get data from cloud based on plm data type
 */
public enum PlmPartDataType {
    PLM_PARTIAL("Partial"),
    PLM_MAPPED("Mapped"),
    PLM_NOT_MAPPED("NotMapped"),
    PLM_INVALID_VPE("InvalidVpe"),
    PLM_INVALID_PG("InvalidProcessGroup"),
    PLM_INVALID_MATERIAL("InvalidMaterial"),
    PLM_INVALID_UDA_FORMAT("InvalidUdaFormat"),
    PLM_INVALID_UDA_VALUE("InvalidUdaValue"),
    PLM_MULTI_REVISION_PARTS("MultiRevWithParts"),
    PLM_MULTI_REVISION_NO_PARTS("MultiRevWithNoParts"),
    PLM_MULTI_REVISION("MultiSpecificRevision"),
    PLM_PART_WITH_DATE("PartWithDate34"),
    PLM_PART_WITH_INTEGER("PartWithInt35"),
    PLM_PART_WITH_REAL("PartWithReal36"),
    PLM_PART_WITH_STRING("PartWithStr37"),
    PLM_PART_PUBLISH_GENERATED("PublishGenerated"),
    PLM_PART_PUBLISH_CONSTANT("PublishConstant");

    private final String plmPartType;

    PlmPartDataType(String partDataType) {
        this.plmPartType = partDataType;
    }

    public String getPlmPartDataType() {
        return this.plmPartType;
    }
}
