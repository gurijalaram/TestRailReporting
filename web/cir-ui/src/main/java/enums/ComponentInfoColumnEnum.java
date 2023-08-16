package enums;

public enum ComponentInfoColumnEnum {

    QUANTITY("Qty"),
    PROCESS_GROUP("Process Group"),
    VPE("VPE"),
    LAST_SAVED("Last Saved"),
    LAST_COSTED("Last Costed"),
    CYCLE_TIME("Cycle Time (s)"),
    PIECE_PART_COST("Piece Part Cost (USD)"),
    FULLY_BURDENED_COST("Fully Burdened Cost (USD)"),
    CAPITAL_INVESTMENT("Capital Investment (USD)");

    private final String columnName;

    ComponentInfoColumnEnum(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}