package enums;

public enum MassMetricEnum {
    FINISH_MASS("Finish Mass"),
    ROUGH_MASS("Rough Mass");

    private final String massMetricName;

    MassMetricEnum(String massMetricName) {
        this.massMetricName = massMetricName;
    }

    /**
     * Gets mass metric name
     *
     * @return String
     */
    public String getMassMetricName() {
        return this.massMetricName;
    }
}