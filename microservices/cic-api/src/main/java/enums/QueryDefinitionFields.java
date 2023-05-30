package enums;

public enum QueryDefinitionFields {
    PART_NUMBER("partNumber"),
    REVISION_NUMBER("revisionNumber"),
    CUSTOM_STRING("UDA1"),
    CUSTOM_NUMBER("UDA2"),
    CUSTOM_DATE("UDA3"),
    CUSTOM_MULTI("UDA4");

    private final String queryDefinitionField;

    QueryDefinitionFields(String ciField) {
        queryDefinitionField = ciField;
    }

    public String getQueryDefinitionField() {
        return queryDefinitionField;
    }
}
