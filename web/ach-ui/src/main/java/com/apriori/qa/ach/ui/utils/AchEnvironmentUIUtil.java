package com.apriori.qa.ach.ui.utils;


import com.apriori.login.CommonLoginPageImplementation;
import com.apriori.pageobjects.customeradmin.CustomerAdminPage;
import com.apriori.pageobjects.header.ReportsHeader;
import com.apriori.pageobjects.homepage.AdminHomePage;
import com.apriori.pageobjects.messages.MessagesPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.properties.PropertiesContext;
import com.apriori.qa.ach.ui.pageobjects.applications.AppStreamPage;
import com.apriori.testconfig.TestBaseUI;

import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Customer environment util class
 * Contains methods with base functionality for customer environments tests
 */
public class AchEnvironmentUIUtil extends TestBaseUI {

    protected final String deploymentName = PropertiesContext.get("${deployment}.name");

    private static final Map<String, Class<? extends LoadableComponent>> APPLICATIONS_CLASS = new LinkedHashMap<>() {{
            put("aP Admin", AdminHomePage.class);
            put("aP Analytics", ReportsHeader.class);
            put("aP Connect", WorkflowHome.class);
            put("aP Design", CommonLoginPageImplementation.class);
            put("aP Pro", AppStreamPage.class);
            put("aP Workspace", MessagesPage.class);
            put("Customer Admin", CustomerAdminPage.class);
            put("Electronics Data Collection", CommonLoginPageImplementation.class);
        }};

    /**
     * Specify PageObject type based on an application name
     * @param applicationName
     * @return
     */
    protected Class<? extends LoadableComponent> getPageObjectTypeByApplicationName(final String applicationName) {
        if (APPLICATIONS_CLASS.containsKey(applicationName)) {
            return APPLICATIONS_CLASS.get(applicationName);
        }

        throw new IllegalArgumentException("Application to open is not supported. Application name:" + applicationName);

    }
}