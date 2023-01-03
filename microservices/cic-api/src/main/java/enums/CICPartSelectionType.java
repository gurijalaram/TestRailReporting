package enums;

public enum CICPartSelectionType {
    QUERY("QUERY"),
    REST("REST");

    private final String partSelectionType;

    CICPartSelectionType(String st) {
        partSelectionType = st;
    }

    public String getPartSelectionType() {
        return partSelectionType;
    }
}
