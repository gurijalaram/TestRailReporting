package enums;

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
    PLM_MULTI_REVISION("MultiSpecificRevision");

    private final String plmPartType;

    PlmPartDataType(String partDataType) {
        this.plmPartType = partDataType;
    }

    public String getPlmPartDataType() {
        return this.plmPartType;
    }
}
