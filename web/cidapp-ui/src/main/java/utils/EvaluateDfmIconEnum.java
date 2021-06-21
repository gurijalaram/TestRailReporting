package utils;

public enum EvaluateDfmIconEnum {

    CRITICAL("var(--red-light)"),
    HIGH("var(--yellow-light)"),
    MEDIUM("var(--cyan-light)"),
    LOW("var(--green-light)"),
    UNKNOWN("var(--gray-500)");

    private final String icon;

    EvaluateDfmIconEnum(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
