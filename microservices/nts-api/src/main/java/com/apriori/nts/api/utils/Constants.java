package com.apriori.nts.api.utils;

import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Constants {
    public static final String EMAIL_SUBJECT = PropertiesContext.get("env") + "_NTSSendEmail_";
    public static final String EMAIL_CONTENT = "This is an automated email sent from the QA Automation Project.";
    public static final String EMAIL_CONTENT_W_ATTACHMENT = EMAIL_CONTENT + " It should contain an attachment";
}
