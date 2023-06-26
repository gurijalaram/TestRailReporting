package enums;

public enum QueryDefinitionFieldType {
    CONTAINS("LIKE"),
    EQUAL("EQ"),
    GREATER_THAN("GT"),
    GREATER_THAN_EQUAL("GE"),
    NOT_EQUAL("NE"),
    BETWEEN("BETWEEN"),
    NOT_BETWEEN("NOTBETWEEN"),
    LESS_THAN("LT"),
    LESS_THAN_EQUAL("LE");

    private final String queryDefinitionFieldType;

    QueryDefinitionFieldType(String ciField) {
        queryDefinitionFieldType = ciField;
    }

    public String getQueryDefinitionFieldType() {
        return queryDefinitionFieldType;
    }
}
