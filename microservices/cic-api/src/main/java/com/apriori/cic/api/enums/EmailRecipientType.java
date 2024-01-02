package com.apriori.cic.api.enums;

public enum EmailRecipientType {
    FIELD("Field"),
    CONSTANT("Constant");

    private final String emailRecipientType;

    EmailRecipientType(String st) {
        emailRecipientType = st;
    }

    public String getEmailRecipientType() {
        return emailRecipientType;
    }
}
