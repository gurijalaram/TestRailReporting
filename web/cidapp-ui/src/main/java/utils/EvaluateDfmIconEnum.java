package utils;

public enum EvaluateDfmIconEnum {

    CRITICAL("var(--red)"),
    HIGH("var(--yellow)"),
    MEDIUM("var(--cyan)"),
    LOW("var(--green)"),
    UNKNOWN("var(--gray)");

    private final String icon;

    EvaluateDfmIconEnum(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
