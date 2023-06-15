package enums;

public enum QueryDefinitionFields {
    PART_NUMBER("partNumber"),
    REVISION_NUMBER("revisionNumber"),
    CUSTOM_STRING("UDA1"),
    CUSTOM_NUMBER("UDA2"),
    CUSTOM_DATE("UDA3"),
    CUSTOM_MULTI("UDA4"),
    STRING1("String1"),
    INTEGER1("Integer1"),
    REAL_NUMBER1("RealNumber1"),
    DATE_TIME1("DateTime1"),
    EMAIL("StrEmail2");


    private final String queryDefinitionField;

    QueryDefinitionFields(String ciField) {
        queryDefinitionField = ciField;
    }

    public String getQueryDefinitionField() {
        return queryDefinitionField;
    }
}
