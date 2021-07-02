package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.TableUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationPage {
    private static final Logger logger = LoggerFactory.getLogger(NotificationPage.class);

    @FindBy(css = "#runtime")
    private WebElement body;
    @FindBy(css = "div[title=\"Email\"]")
    private WebElement emailTab;
    @FindBy(css = "div[title=\"Attach Report\"]")
    private WebElement reportTab;
    @FindBy(css = "div[title=\"Filter\"]")
    private WebElement filterTab;
    @FindBy(css = "div[title=\"Monitoring\"]")
    private WebElement monitoringTab;
    @FindBy(css = "#runtime > div.ss-content > div.ss-list")
    private WebElement emailTemplateSelect;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-171 > div > div > span.placeholder")
    private WebElement emailTemplateField;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-171 > div > div > span.ss-arrow")
    private WebElement emailTemplateArrow;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_panel-159")
    private WebElement popup;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-173 > div > div > span.placeholder")
    private WebElement emailRecipientSelectField;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textbox-175 > table > tbody > tr > td > input")
    private WebElement emailRecipientField;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-85-grid-advanced > div.objbox > table " +
            "> tbody > tr:nth-child(4) > td:nth-child(3)")
    private WebElement emailRecipientText;
    @FindBy(css = "#runtime > div.ss-content > div.ss-list")
    private WebElement emailRecipientSelect;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-173 > div > div > span.ss-arrow")
    private WebElement emailRecipientgArrow;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textbox-178 > table > tbody > tr > td > input")
    private WebElement costRoundingField;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-180 > div > div > span.placeholder")
    private WebElement costRoundingSelectField;
    @FindBy(css = "#runtime > div.ss-content > div.ss-list")
    private WebElement costRoundingList;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-180 > div > div > span.ss-arrow")
    private WebElement costRoundingArrow;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textbox-179 > table > tbody > tr > td > input")
    private WebElement aprioriCostField;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-181 > div > div > span.placeholder")
    private WebElement aprioriCostSelectField;
    @FindBy(css = "#runtime > div.ss-content > div.ss-list")
    private WebElement aprioriCostList;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-181 > div > div > span.ss-arrow")
    private WebElement aprioriCostArrow;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_label-182 > span")
    private WebElement emailSpaceErrorLabel;
    @FindBy(css = "#runtime > div.ss-content")
    private WebElement emailContent;

    private PageUtils pageUtils;
    private TableUtils tableUtils;
    private WebDriver driver;

    public NotificationPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public enum NotificationTab {
        EMAIL,
        REPORT,
        FILTER,
        MONITORING
    }

    public enum EmailTemplate {
        NONE,
        DFMPARTSUMMARY
    }

    public enum EmailRecipientType {
        FIELD,
        CONSTANT
    }

    public enum AprioriCost {
        FULLYBURDENEDCOST,
        PIECEPARTCOST,
        MATERIALCOST
    }

    public enum CostRounding {
        YES,
        NO
    }
    
    public enum Field {
        TEMPLATE,
        RECIPIENT,
        RECIPIENTSELECT,
        RECIPIENTSELECTFIELD,
        COSTROUNDING,
        COSTROUNDINGSELECT,
        COSTROUNDINGSELECTFIELD,
        APRIORICOST,
        APRIORICOSTSELECT,
        APRIORICOSTSELECTFIELD
    }

    final int indexEmailTemplate = 2;
    final int indexEmailRecipientType = 3;
    final int indexCostRounding = 5;
    final int indexAprioriCost = 6;

    /**
     * Get details of the specified field
     * 
     * @param field The field to inspect
     * @return Field text
     */
    public Map<String, Object> getFieldDetails(Field field) {
        Map<String, Object> details = new HashMap<>();
        String text;
        List<String> options;

        switch (field) {
            case TEMPLATE:
                options = getOptions(indexEmailTemplate);
                text = emailTemplateField.getText();
                break;
            case RECIPIENT:
                options = null;
                text = emailRecipientField.getAttribute("value");
                break;
            case RECIPIENTSELECT:
                options = getOptions(indexEmailRecipientType);
                text = emailRecipientSelectField.getText();
                break;
            case RECIPIENTSELECTFIELD:
                options = null;
                text = emailRecipientSelectField.getText();
                break;
            case COSTROUNDING:
                options = null;
                text = costRoundingField.getAttribute("value");
                break;
            case COSTROUNDINGSELECT:
                options = getOptions(indexCostRounding);
                text = costRoundingSelectField.getText();
                break;
            case COSTROUNDINGSELECTFIELD:
                options = null;
                text = costRoundingSelectField.getText();
                break;
            case APRIORICOST:
                options = null;
                text = aprioriCostField.getAttribute("value");
                break;
            case APRIORICOSTSELECT:
                options = getOptions(indexAprioriCost);
                text = aprioriCostSelectField.getText();
                break;
            case APRIORICOSTSELECTFIELD:
                options = null;
                text = aprioriCostSelectField.getText();
                break;
            default:
                logger.debug("invalid field");
                return null;
        }

        details.put("options", options);
        details.put("text", text);
        return details;
    }

    /**
     * Adds email recipient(s)
     *
     * @param recipient Email recipient(s) to add
     */
    public void addEmailRecipient(String recipient) {
        pageUtils.waitForElementAndClick(emailRecipientField);
        emailRecipientField.clear();
        emailRecipientField.sendKeys(recipient);
        pageUtils.waitForElementAndClick(popup);
    }

    /**
     * Checks for email recipients csv with space error
     *
     * @return True if error is displayed
     */
    public boolean emailSpaceErrorIsDisplayed() {
        pageUtils.waitForElementAndClick(emailSpaceErrorLabel);
        return emailSpaceErrorLabel.getText().equals(Constants.EMAIL_SPACE_ERROR);
    }

    /**
     * Select an email template
     *
     * @param template The template to select
     * @return The name of the selected template
     */
    public String selectEmailTemplate(EmailTemplate template) {
        String templateName;
        switch (template) {
            case NONE:
                templateName = "None";
                break;
            case DFMPARTSUMMARY:
            default:
                templateName = "DFM Part Summary";
        }

        String selected = selectOption(Field.TEMPLATE, templateName);
        //pageUtils.waitForElementAndClick(emailTemplateArrow);
        return selected;
    }


    /**
     * Select an email recipient type
     *
     * @param recipientType The recipient type to select
     * @return The name of the selected recipient type
     */
    public String selectEmailRecipientType(EmailRecipientType recipientType) {
        String type;
        switch (recipientType) {
            case FIELD:
                type = "None";
                break;
            case CONSTANT:
            default:
                type = "Constant";
        }

        pageUtils.waitFor(5000);
        String selected = selectOption(Field.RECIPIENTSELECTFIELD, type);
        //pageUtils.waitForElementAndClick(emailRecipientgArrow);
        return selected;
    }


    /**
     * Select a cost option
     *
     * @param cost The cost option to select
     * @return The name of the selected cost option
     */
    public String selectAprioriCost(AprioriCost cost) {
        String costOption;
        switch (cost) {
            case FULLYBURDENEDCOST:
                costOption = "Fully Burden Cost";
                break;
            case MATERIALCOST:
                costOption = "Material Cost";
                break;
            case PIECEPARTCOST:
            default:
                costOption = "Piece Part Cost";
        }

        String selected = selectOption(Field.APRIORICOSTSELECTFIELD, costOption);
        return selected;
    }


    /**
     * Select an cost rounding option
     *
     * @param costRounding Yes or No to use cost rounding
     * @return The name of the selected rounding option
     */
    public String selectCostRounding(CostRounding costRounding) {
        String roundingOption;
        switch (costRounding) {
            case YES:
                roundingOption = "Yes";
                break;
            case NO:
            default:
                roundingOption = "No";
        }

        pageUtils.waitForElementAndClick(costRoundingArrow);
        pageUtils.waitForElementAndClick(costRoundingArrow);
        String selected = selectOption(Field.COSTROUNDINGSELECTFIELD, roundingOption);
        return selected;
    }

    /**
     * Gets the title of the specified tab
     *
     * @param notificationTab The tab to get the title for
     * @return Tab's title
     */
    public String getTabText(NotificationTab notificationTab) {
        WebElement tab;
        switch (notificationTab) {
            case EMAIL:
                tab = emailTab;
                break;
            case FILTER:
                tab = filterTab;
                break;
            case MONITORING:
                tab = monitoringTab;
                break;
            case REPORT:
                tab = reportTab;
                break;
            default:
                logger.debug("invalid notification tab");
                return null;
        }

        pageUtils.waitForElementAndClick(tab);
        return tab.getText();
    }


    /**
     * Get all options in a list
     *
     * @param fieldIndex the index of the field returning options for
     * @return List of options
     */
    private List<String> getOptions(int fieldIndex) {
        List<WebElement> list = (List<WebElement>)selectOption(null, fieldIndex, null);
        List<String> options = new ArrayList<>();
        for (WebElement option : list) {
            options.add(option.getAttribute("innerText"));
        }

        return options;
    }

    /**
     * Select an option from list
     * Since the targeted lists are not select elements, we can't use the built-in
     * select functionality. The lists page objects, have a random id associated
     * with it. To get around this, the random id was  removed from the css selector.
     * This leads to another issue, the lists, without the id, become redundant. This
     * is why the multiple loops and checks are present.
     *
     * @param field Selected option field element
     * @param fieldIndex The index of the list to select an option for
     * @param option Option to select
     * @return The selected option text
     */
    private Object selectOption(WebElement field, int fieldIndex, String option) {
        if (field != null && pageUtils.isElementEnabled(field)) {
            pageUtils.waitForElementAndClick(field);
        }

        int count = 1;

        // Get a list of all ss-content elements, which are the list containers
        List<WebElement> containers = body.findElements(By.cssSelector("div.ss-content"));

        // Iterate through the containers to find the container with the targeted list
        for (WebElement container : containers) {
            // Match the counter to the targeted container index
            if (count == fieldIndex) {
                // Get the list element in the container
                WebElement list = container.findElement(By.cssSelector("div.ss-list"));

                // Get a list of the options in the list
                List<WebElement> options = list.findElements(By.cssSelector("div"));

                // If the option paramerter is just return the list options
                // No option is selected
                if (option == null) {
                    return options;
                }

                // Search for the target option and click on it
                for (WebElement opt : options) {
                    if (opt.getAttribute("innerText").equalsIgnoreCase(option)) {
                        pageUtils.waitForElementAndClick(opt);
                    }
                }
                break;
            }
            count++;
        }

        return field.getText();
    }

    /**
     * Select a list option
     *
     * @param field the field to select an option for
     * @param option the option to be selected
     * @return the text in the targeted field
     */
    private String selectOption(Field field, String option) {

        int index;
        WebElement listField;
        switch (field) {
            case TEMPLATE:
                index = indexEmailTemplate;
                listField = emailTemplateField;
                break;
            case COSTROUNDINGSELECTFIELD:
                index = indexCostRounding;
                listField = costRoundingSelectField;
                break;
            case RECIPIENTSELECTFIELD:
                index = indexEmailRecipientType;
                listField = emailRecipientSelectField;
                break;
            case APRIORICOSTSELECTFIELD:
                index = indexAprioriCost;
                listField = aprioriCostSelectField;
                break;
            default:
                return null;
        }

        return  selectOption(listField, index, option).toString();
    }

}
