package com.apriori.cic.ui.utils;

import com.apriori.shared.util.http.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Properties;

@Slf4j
public class Constants {

    public static final String DEFAULT_USER_NAME_KEY = System.getProperty("username");
    public static final String DEFAULT_PASSWORD_KEY = System.getProperty("password");
    public static final String DEFAULT_BASE_URL_KEY = "url";
    public static final String DEFAULT_ENVIRONMENT_KEY = "env";
    public static final String DEFAULT_ENVIRONMENT_VALUE = "qa-cid-perf";
    // Login Credentials
    public static final String USER_EMAIL = "qa-automation-1@apriori.com";
    public static final String SECOND_USER_EMAIL = "";
    public static final String SECOND_USER_PASSWORD = "";
    //Notification Email
    public static final String EMAIL_RECIPIENTS = USER_EMAIL.concat(",scrowe@apriori.com");
    public static final String EMAIL_RECIPIENTS_WITH_SPACE = USER_EMAIL.concat(",  scrowe@apriori.com");
    public static final String EMAIL_SPACE_ERROR = "Recipient field should not contain spaces.";
    public static final String EMAIL_TEMPLATE_DFC = "DFM Part Summary";
    public static final String EMAIL_APRIORICOST_FULLY = "Fully Burdened Cost";
    public static final String EMAIL_APRIORICOST_MATERIAL = "Material Cost";
    public static final String EMAIL_APRIORICOST_PART = "Piece Part Cost";
    public static final String COST_ROUNDING_LABEL = "Cost Rounding";
    public static final String APRIORI_COST_LABEL = "aPriori Cost";
    public static final String EMAIL_RECIPIENT_FIELD = "Field";
    public static final String EMAIL_RECIPIENT_CONSTANT = "Constant";
    //New Workflow Connector - need to be connected to PLM
    //public static final String NWF_CONNECTOR = "automation - aP Internal - WC";
    public static final String NWF_CONNECTOR = "automation - NA - WC";

    //Default part id
    public static final String DEFAULT_PART_ID = "12345";
    //Default wait in millisecond
    public static final int DEFAULT_WAIT = 3000;
    //Default worklflow list size
    public static final int DEFAULT_PAGE_SIZE = 25;
    public static final String MAX_PAGE_SIZE_25_TEXT = "25 rows per page";
    public static final String MAX_PAGE_SIZE_100_TEXT = "100 rows per page";
    public static final String DEFAULT_ROW_RANGE = "Rows1 - 25";
    public static final String NEXT_ROW_RANGE = "Rows26 - 50";
    //Default worflow names
    public static final String DEFAULT_WORKFLOW_NAME = "0   0   0   0   0   Automation Workflow";
    public static final String DEFAULT_EDITED_WORKFLOW_NAME = "0   0   0   0   Automation Workflow - Updated";
    public static final String DEFAULT_NAME_WITH_NUMBER = "0   0   0   0   0   1 Worflow Automation";
    public static final String DEFAULT_NAME_UPPER_CASE = "0   0   0   0   0   A Upper Workflow Automation";
    public static final String DEFAULT_NAME_LOWER_CASE = "0   0   0   0   0   a Lower Workflow Automation";
    public static final String ERROR_NAME_WITH_UNSUPPORTED_SPECIAL_CHARS =
        "Name should only contain spaces and the following characters: a-zA-Z0-9-_";
    public static final String DEFAULT_WORKFLOW_DESCRIPTION = "This workflow was created by automation";
    //Workflow field boundaries
    public static final int MAXIMUM_WORKFLOW_NAME_CHARACTERS = 64;
    public static final int MINIMAL_WORKFLOW_NAME_CHARACTERS = 1;
    public static final int MAXIMUM_WORKFLOW_DESCRIPTION_CHARACTERS = 255;
    //Query Definition
    public static final String DEFAULT_RULE_FILTER = "partNumber";
    public static final String DEFAULT_RULE_OPERATOR = "not_equal";
    private static final Properties PROPERTIES = new Properties();
    private static final File INPUT_STREAM;
    private static final String ROW_RANGE = "Rows1-25";
    public static String environment;
    private static String baseUrl;


    static {
        environment = System.getProperty(DEFAULT_ENVIRONMENT_KEY) == null ? DEFAULT_ENVIRONMENT_VALUE : System.getProperty(DEFAULT_ENVIRONMENT_KEY);

        INPUT_STREAM = FileResourceUtil.getResourceAsFile("cic-ui-" + environment + ".properties");

    }
}
