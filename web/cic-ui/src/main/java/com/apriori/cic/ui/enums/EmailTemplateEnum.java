package com.apriori.cic.ui.enums;

public enum EmailTemplateEnum {
    DFM_PART_SUMMARY("DFM Part Summary"),
    CONFIGURABLE_SUMMARY("Configurable Summary"),
    NONE("None");

    private String emailTemplate;

    EmailTemplateEnum(String name) {
        this.emailTemplate = name;
    }

    /**Get connector name
     *
     * @return String
     */
    public String getEmailTemplate() {
        return this.emailTemplate;
    }
}
