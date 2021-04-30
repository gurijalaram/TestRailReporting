package com.cic.tests;

import com.apriori.features.NotificationsFeature;
import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.validators.NotificationValidator;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

public class NotificationTests extends TestBase {
    private NotificationsFeature notificationsFeature;
    private NotificationValidator validator;
    private LoginPage loginPage;
    private WorkflowPage workflowPage;
    private NewWorkflowPage newWorkflowPage;
    private Map<String, Object> values;

    public NotificationTests() {
        super();
    }

    @Before
    public void setUpAndLogin() {
        loginPage = new LoginPage(driver);
        workflowPage = new WorkflowPage(driver);
        newWorkflowPage = new NewWorkflowPage(driver);
        values = new HashMap<>();
        validator = new NotificationValidator(driver);
        notificationsFeature = new NotificationsFeature(driver);

        loginPage.login();
        notificationsFeature.gotoNotifications();
    }

    @Test
    @TestRail(testCaseId = {"3951", "4875"})
    @Description("Test email Tab on the Add New Workflow Dialog")
    public void testNotificationsEmailTab() {
        values = notificationsFeature.checkEmailTab();
        validator.validateEmailTab(values);
    }
}
