package com.apriori.cic.ui.enums;

public enum EmailConfigSummaryEnum {
    COST_ROUNDING("Cost Rounding"),
    SCENARIO_LINK("Scenario Link"),
    FIELD_1("Field 1"),
    FIELD_2("Field 2"),
    FIELD_3("Field 3"),
    FIELD_4("Field 4"),
    FIELD_5("Field 5");

    private String emailConfigSummaryField;

    EmailConfigSummaryEnum(String name) {
        this.emailConfigSummaryField = name;
    }

    /**Get email configuration summary template field name
     *
     * @return String
     */
    public String getEmailConfigSummaryField() {
        return this.emailConfigSummaryField;
    }
}
