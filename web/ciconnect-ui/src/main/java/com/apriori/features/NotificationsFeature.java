package com.apriori.features;

import com.apriori.pageobjects.LoginPage;
import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.NotificationPage;
import com.apriori.pageobjects.WorkflowPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Constants;
import utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

public class NotificationsFeature {
    private Map<String, Object> values;
    private NewWorkflowPage newWorkflowPage;
    private WebDriver driver;
    private LoginPage loginPage;
    private WorkflowPage workflowPage;
    private NotificationPage notificationPage;

    public NotificationsFeature(WebDriver driver) {
        this.driver = driver;
        newWorkflowPage = new NewWorkflowPage(driver);
        loginPage = new LoginPage(driver);
        workflowPage = new WorkflowPage(driver);
        notificationPage = new NotificationPage(driver);
    }

    public Map<String, Object> checkEmailTab() {
        values = new HashMap<>();
        Map<String, WebElement> buttons;
        String title = notificationPage.getTabText(NotificationPage.NotificationTab.EMAIL);
        values.put("title", title);

        String template;
        template = notificationPage.selectEmailTemplate(NotificationPage.EmailTemplate.DFMPARTSUMMARY);
        values.put("dfm-template", template);
        buttons = newWorkflowPage.getButtons(NewWorkflowPage.Tab.NOTIFICATION);
        values.put("before-fields-filled", newWorkflowPage.isNextButtonClickable(NewWorkflowPage.Tab.NOTIFICATION));

        Map<String, Object> recipientTypes =
                notificationPage.getFieldDetails(NotificationPage.Field.RECIPIENTSELECT);
        values.put("recipient-options", recipientTypes.get("options"));
        values.put("default-recipient", recipientTypes.get("text"));
        String recipientType;
        recipientType = notificationPage.selectEmailRecipientType(NotificationPage.EmailRecipientType.CONSTANT);
        values.put("recipient-type-constant", recipientType);
        notificationPage.addEmailRecipient(Constants.USER_EMAIL);
        String recipient = notificationPage.getFieldDetails(NotificationPage.Field.RECIPIENT)
                .get("text").toString();
        values.put("recipient", recipient);

        notificationPage.addEmailRecipient(Constants.EMAIL_RECIPIENTS_WITH_SPACE);
        values.put("error-displayed", notificationPage.emailSpaceErrorIsDisplayed());
        notificationPage.addEmailRecipient(Constants.EMAIL_RECIPIENTS);
        recipient = notificationPage.getFieldDetails(NotificationPage.Field.RECIPIENT)
                .get("text").toString();
        values.put("recipient-csv", recipient);

        Map<String, Object> rounding = notificationPage.getFieldDetails(NotificationPage.Field.COSTROUNDINGSELECT);
        values.put("rounding-options", rounding.get("options"));
        values.put("default-rounding", rounding.get("text"));
        values.put("rounding-label", notificationPage.getFieldDetails(NotificationPage.Field.COSTROUNDING).get("text"));
        String roundingSelected;
        roundingSelected = notificationPage.selectCostRounding(NotificationPage.CostRounding.NO);
        values.put("rounding-no", roundingSelected);

        Map<String, Object> cost = notificationPage.getFieldDetails(NotificationPage.Field.APRIORICOSTSELECT);
        values.put("cost-options", cost.get("options"));
        values.put("default-cost", cost.get("text"));
        values.put("cost-label", notificationPage.getFieldDetails(NotificationPage.Field.APRIORICOST).get("text"));
        String costSelected;
        costSelected = notificationPage.selectAprioriCost(NotificationPage.AprioriCost.MATERIALCOST);
        values.put("cost-material", costSelected);

        buttons = newWorkflowPage.getButtons(NewWorkflowPage.Tab.NOTIFICATION);
        values.put("after-fields-filled", newWorkflowPage.isNextButtonClickable(NewWorkflowPage.Tab.NOTIFICATION));
        newWorkflowPage.cancelNewWorkflow(NewWorkflowPage.Tab.NOTIFICATION);

        workflowPage.newWorkflow();
        gotoNotifications();
        newWorkflowPage.getButtons(NewWorkflowPage.Tab.NOTIFICATION);
        buttons = newWorkflowPage.getButtons(NewWorkflowPage.Tab.NOTIFICATION);
        values.put("after-none-selected", newWorkflowPage.isNextButtonClickable(NewWorkflowPage.Tab.NOTIFICATION));

        // TODO: Determine if Scrolling is supported

        return values;
    }

    /**
     * Navigate to the notifications tab
     */
    public void gotoNotifications() {
        String name = UIUtils.saltString(Constants.DEFAULT_WORKFLOW_NAME);
        workflowPage.newWorkflow();
        newWorkflowPage.gotoQueryDefinitions(name)
                .gotoCosting()
                .gotoNotifications();
    }
}
