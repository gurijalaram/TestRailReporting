package com.apriori.pageobjects;

import com.apriori.objects.WorkflowSchedule;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewWorkflowPage {
    private static final Logger logger = LoggerFactory.getLogger(NewWorkflowPage.class);

    @FindBy(css = "#Daily")
    private WebElement newWorkflowPopup;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83")
    private WebElement newWorkflowModal;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_label-12")
    private WebElement createNewWorkflow;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textbox-148 > table > tbody > tr > td > input")
    private WebElement newWorflowNameField;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_textarea-152 > div > textarea")
    private WebElement newWorkflowDescription;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list")
    private WebElement newWorkflowConnectorSelection;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154-bounding-box")
    private WebElement newWorkflowConnectorDropDown;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154")
    private WebElement newWorkflowConnectorDropDownOpen;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-92 > button")
    private WebElement newWorkflowNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-189 > button")
    private  WebElement queryAddRowButton;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(2) > div:nth-child(2)")
    private WebElement ciConnectFieldSelection;
    @FindBy(id = "CIC_CostingInputCell_MU-5_DrowpdownWidget-4")
    private WebElement mappingRuleDD;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(3)")
    private WebElement mappingRuleSelection;
    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list > div:nth-child(1)")
    private WebElement valueSelection;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-108 > button")
    private WebElement queryPageNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-104 > button")
    private WebElement queryPageNextButtonDisabled;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-103 > button")
    private WebElement queryPagePreviousButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-43 > button")
    private WebElement notificationNextButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-109 > button")
    private WebElement notificationPreviousButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_crontabBuilderWidget-115")
    private WebElement scheduleTabs;
    @FindBy(id = "Minute")
    private WebElement scheduleMinutes;
    @FindBy(id = "Hourly")
    private WebElement scheduleHourly;
    @FindBy(id = "Daily")
    private WebElement scheduleDaily;
    @FindBy(id = "Monthly")
    private WebElement scheduleMonthly;
    @FindBy(id = "Weekly")
    private WebElement scheduleWeekly;
    @FindBy(id = "Yearly")
    private WebElement scheduleYearly;
    @FindBy(id = "msMinutesTab")
    private WebElement minutesTab;
    @FindBy(id = "MinutesInput")
    private WebElement minutesInput;
    @FindBy(id = "msHourlyTab")
    private WebElement hourlyTab;
    @FindBy(css = "input[name='HourlyRadio'][value='1']")
    private WebElement everyHour;
    @FindBy(css = "input[name='HourlyRadio'][value='2']")
    private WebElement atHourMinutes;
    @FindBy(id = "HoursInput")
    private WebElement hoursInput;
    @FindBy(id = "AtHours")
    private WebElement atHours;
    @FindBy(id = "AtMinutes")
    private WebElement atMinutes;
    @FindBy(id = "msDailyTab")
    private WebElement dailyTab;
    @FindBy(id = "DaysInput")
    private WebElement daysInput;
    @FindBy(css = "input[name='DailyRadio][value='1']")
    private WebElement everyDays;
    @FindBy(css = "input[name='DailyRadio'][value='2']")
    private WebElement dayHourMinutes;
    @FindBy(id = "DailyHours")
    private WebElement dailyHours;
    @FindBy(id = "DailyMinutes")
    private WebElement dailyMinutes;
    @FindBy(id = "msWeeklyTab")
    private WebElement weeklyTab;
    @FindBy(id = "WeeksInput")
    private WebElement weeksInput;
    @FindBy(css = "checkbox[value='MON']")
    private WebElement weeklyMonday;
    @FindBy(css = "checkbox[value='TUE']")
    private WebElement weeklyTuesday;
    @FindBy(css = "checkbox[value='WED']")
    private WebElement weeklyWednesday;
    @FindBy(css = "checkbox[value='THU']")
    private WebElement weeklyThursday;
    @FindBy(css = "checkbox[value='FRI']")
    private WebElement weeklyFriday;
    @FindBy(css = "checkbox[value='SAT']")
    private WebElement weeklySaturday;
    @FindBy(css = "checkbox[value='SUN']")
    private WebElement weeklySunday;
    @FindBy(id = "WeeklyHours")
    private WebElement weeklyHours;
    @FindBy(id = "WeeklyMinutes")
    private WebElement weeklyMinutes;
    @FindBy(id = "msMonthlyTab")
    private WebElement monthlyTab;
    @FindBy(css = "input[name='MonthlyRadio'][value='1']")
    private WebElement dayOfMonth;
    @FindBy(css = "input[name='MonthlyRadio'][value='2']")
    private WebElement everyMonth;
    @FindBy(id = "MonthInput")
    private WebElement monthInput;
    @FindBy(id = "DayOfMOnthInput")
    private WebElement dayOfMonthInput;
    @FindBy(id = "WeekDay")
    private WebElement weekDay;
    @FindBy(id = "DayInWeekOrder")
    private  WebElement dayInWeekOrder;
    @FindBy(id = "EveryMonthInput")
    private WebElement everyMonthInput;
    @FindBy(id = "MonthlyHours")
    private WebElement monthlyHours;
    @FindBy(id = "MonthlyMinutes")
    private WebElement monthlyMinutes;
    @FindBy(id = "msYearlyTab")
    private WebElement yearlyTab;
    @FindBy(css = "input[name='YearlyRadio'][value='1']")
    private WebElement optMonthOfYear;
    @FindBy(css = "input[name='YearlyRadio'][value='2']")
    private WebElement everyYear;
    @FindBy(id = "YearInput")
    private WebElement yearInput;
    @FindBy(id = "MonthsOfYear")
    private WebElement monthsOfYear;
    @FindBy(id = "DayOrderInYear")
    private WebElement dayOrderInYear;
    @FindBy(id = "DayWeekForYear")
    private WebElement dayWeekForYear;
    @FindBy(id = "MonthsOfYear2")
    private WebElement monthsOfYear2;
    @FindBy(id = "YearlyHours")
    private WebElement yearlyHours;
    @FindBy(id = "YearlyMinutes")
    private WebElement yearlyMinutes;
    @FindBy(id = "root_pagemashupcontainer-1_navigation-83-popup_checkbox-149-input")
    private WebElement newWorkflowEnabledCB;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-183 > button")
    private WebElement newWorkflowCancelButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_0 > div.rule-filter-container > select")
    private WebElement queryDropDown;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_0 > div.rule-value-container" +
            " > input")
    private WebElement queryValue;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110")
    private WebElement queryPopup;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-104 > button")
    private WebElement queryNext;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-171")
    private WebElement emailDropDown;
    @FindBy(css = "#runtime > div.ss-content.ss-37701.ss-open > div.ss-list > div:nth-child(2)")
    private WebElement emailTemplateSelection;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-288 > button")
    private WebElement saveButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-186 > button")
    private WebElement notificationCancelButton;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_label-220 > span")
    private WebElement errorNameWithUnsupportedCharacters;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_button-183 > button")
    private WebElement detailCancelButton;

    private String ciConnectFieldCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-3";
    private String valueDDCss = "#CIC_CostingInputCell_MU-[ID]_DrowpdownWidget-20";
    private String dayOfWeekCss = "input[value='[DAY]']";

    private WebDriver driver;
    private PageUtils pageUtils;

    // Query Definitions
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_group_0 > div.rules-group-body > div")
    private WebElement rulesList;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_group_0 > div.rules-group-header > div.btn-group.pull-right.group-actions")
    private WebElement ruleButtons;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_0 > div" +
            ".rule-filter-container > select")
    private WebElement filterSelect;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_0 > div" +
            ".rule-operator-container > select")
    private WebElement operatorSelect;
    @FindBy(css = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_group_0 > div.rules-group-body > div")
    private WebElement rulesListContainer;

    private String deleteButtonCss = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_" +
            "[RULE_NUMBER] > div.rule-header > div > button";
    private String valueFieldCss = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_[RULE_NUMBER] > " +
            "div.rule-value-container > input";
    private String filterSelectCss = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_" +
            "[RULE_NUMBER] > div.rule-filter-container > select";
    private String operatorSelectCss = "#root_pagemashupcontainer-1_navigation-83-popup_QueryBuilder-110_rule_" +
            "[RULE_NUMBER] > div.rule-operator-container > select";



    private WebElement addRuleButton;
    private WebElement addGroupButton;

    /**
     * Newworkflow modal tabs
     */
    public enum Tab {
        DETAILS,
        QUERY,
        COSTING,
        NOTIFICATION,
        PUBLISH
    }

    public enum NavigationButton {
        NEXT,
        PREVIOUS
    }

    public NewWorkflowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
    }

    /**
     * Checks to see for name with unsupported character error
     *
     * @return True, if the error exists
     */
    public boolean checkForUnsupportedCharacterErrors() {
        pageUtils.waitForElementAppear(errorNameWithUnsupportedCharacters);
        return errorNameWithUnsupportedCharacters.getText()
                .equalsIgnoreCase(Constants.ERROR_NAME_WITH_UNSUPPORTED_SPECIAL_CHARS);
    }

    /**
     * Checks if the next button is clickable
     *
     * @param tab Current tab
     * @return True if the next button is clickable
     */
    public Boolean isNextButtonClickable(Tab tab) {
        WebElement nextButton;
        switch (tab) {
            case DETAILS:
                nextButton = newWorkflowNextButton;
                break;
            case QUERY:
                nextButton = queryPageNextButton;
                break;
            case NOTIFICATION:
                nextButton = notificationNextButton;
                break;
            default:
                logger.debug("Invalid tab provided");
                return null;
        }

        return pageUtils.isElementClickable(nextButton);
    }

    /**
     *  Determine if the rules list is scrollable
     *
     * @return True if rules list is scrollable
     */
    public boolean rulesScrollBarExists() {
        return UIUtils.scrollBarExists(rulesListContainer);
    }

    /**
     * Add n number of empty rules
     *
     * @param numberOfRules Number of rules to add
     */
    public void addEmptyRules(int numberOfRules) {
        addRuleButton = ruleButtons.findElement(By.cssSelector("button[data-add='rule']"));
        for (int i = 1; i <= numberOfRules; i++) {
            pageUtils.waitForElementAndClick(addRuleButton);
        }
    }

    /**
     * Determine if Add Group button exists
     *
     * @return true if button exists
     */
    public boolean groupsButtonExists() {
        addGroupButton = ruleButtons.findElement(By.cssSelector("button[data-add='group']"));
        return pageUtils.isElementDisplayed(addGroupButton);
    }

    /**
     * Cancel the new workflow
     *
     * @param tab The current tab
     */
    public void cancelNewWorkflow(Tab tab) {
        WebElement cancelButton;
        switch (tab) {
            case DETAILS:
                cancelButton = detailCancelButton;
                break;
            case NOTIFICATION:
                cancelButton = notificationCancelButton;
                break;
            default:
                return;
        }

        pageUtils.waitForElementAndClick(cancelButton);
    }

    /**
     * Delete the last rule
     *
     * @return Number of rules
     */
    public int deleteRule() {
        Integer ruleNumber = getNumberOfRules() - 1;
        String deleteRule = deleteButtonCss.replace("[RULE_NUMBER]", ruleNumber.toString());
        WebElement deleteButton = driver.findElement(By.cssSelector(deleteRule));
        pageUtils.waitForElementAndClick(deleteButton);
        return  getNumberOfRules();
    }

    /**
     *  Add a new rule
     *
     * @return number of rules
     */
    public int addRule() {
        addRuleButton = ruleButtons.findElement(By.cssSelector("button[data-add='rule']"));
        pageUtils.waitForElementAndClick(addRuleButton);
        int ruleNumber = getNumberOfRules() - 1;

        Map<String, WebElement> elements;
        elements = getQueryElements(ruleNumber, false);
        fillQueryDefinitions(elements.get("filter"), null, elements.get("value"), false);
        elements = getQueryElements(ruleNumber, true);
        fillQueryDefinitions(elements.get("filter"), null, elements.get("value"), false);

        return getNumberOfRules();
    }

    /**
     * Get the number of rules displayed on the Query Definition tab
     *
     * @return Number of Rules
     */
    public int getNumberOfRules() {
        pageUtils.waitForElementToBeClickable(rulesList);
        List<WebElement> rules = rulesList.findElements(By.cssSelector("div.rule-container"));
        return rules.size();
    }

    /**
     * returns the next/previous buttons based on the current  tab
     *
     * @return Next & Previous buttons
     */
    public Map<String, WebElement> getButtons(Tab tab) {
        pageUtils.waitFor(2000);
        WebElement nextButton;
        WebElement previousButton;

        switch (tab) {
            case DETAILS:
                previousButton = null;
                nextButton = newWorkflowNextButton;
                break;
            case QUERY:
                previousButton = queryPagePreviousButton;
                nextButton = queryPageNextButton;
                break;
            case NOTIFICATION:
                previousButton = notificationPreviousButton;
                nextButton = notificationNextButton;
                break;
            default:
                logger.debug("Invalid tab");
                return null;
        }

        Map<String, WebElement> buttons = new HashMap<>();
        buttons.put("next", nextButton);
        buttons.put("previous", previousButton);
        return buttons;
    }

    /**
     * Determines if the filter field exists
     *
     * @return True if field exists
     */
    public boolean queryFilterExists() {
        pageUtils.waitForElementAppear(filterSelect);
        return pageUtils.isElementDisplayed(filterSelect);
    }

    /**
     * Determines if the operator field exists
     *
     * @return True if field exists
     */
    public boolean queryOperatorExists() {
        return pageUtils.isElementDisplayed(operatorSelect);
    }

    /**
     * Determines if the value field exists
     *
     * @return True if field exists
     */
    public boolean queryValueExists() {
        return pageUtils.isElementDisplayed(queryValue);
    }

    /**
     * The disabled next button is a different web element than an enabled next
     * button. This is a check for the dissbled version
     *
     * @return True if the disabled next button exists
     */
    public boolean isQueryNextButtonDisabled() {
        return pageUtils.isElementDisplayed(queryPageNextButtonDisabled);
    }

    /**
     * Gets the current state of a navigation button
     *
     * @param tab The new wrokflow tab that contains the button
     * @param button The navigation button to check state for
     * @return map of the button's states
     */
    public Map<String, Boolean> getNavigationButtonState(Tab tab, NavigationButton button) {
        Map<String, Boolean> buttonStates = new HashMap<>();
        WebElement navigationButton = getButtons(tab).get(button.toString().toLowerCase());
        buttonStates.put("exists", pageUtils.isElementDisplayed(navigationButton));
        buttonStates.put("enabled", pageUtils.isElementEnabled(navigationButton));
        return buttonStates;
    }

    /**
     * Does a "New Workflow" pop exist
     *
     * @return new workflow popup exists
     */
    public boolean modalExists() {
        return newWorkflowModal.isDisplayed();
    }

    /**
     * Get the "New Workflow" popup label
     *
     * @return popup label
     */
    public String getLabel() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(createNewWorkflow));
        return createNewWorkflow.getText();

    }

    /**
     *
     * @param name The name of the new workflow
     */
    public void fillWorkflowNameField(String name) {
        pageUtils.waitForElementAndClick(newWorflowNameField);
        newWorflowNameField.sendKeys(name);
    }

    /**
     *
     * @return The name field exists
     */
    public boolean nameFieldExists() {
        pageUtils.waitForElementAppear(newWorflowNameField);
        return pageUtils.isElementDisplayed(newWorflowNameField);
    }

    /**
     *
     * @param description Description of the new workflow
     */
    public void fillWorkflowDescriptionField(String description) {
        pageUtils.waitForElementAndClick(newWorkflowDescription);
        newWorkflowDescription.sendKeys(description);
    }

    /**
     *
     * @return Description field exists
     */
    public boolean descriptionFiledExistis() {
        return pageUtils.isElementDisplayed(newWorkflowDescription);
    }

    /**
     * Select an exisiing connector
     */
    public void selectWorkflowConnector() {
        pageUtils.waitForElementAndClick(newWorkflowConnectorDropDown);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);

        List<WebElement> options = newWorkflowConnectorSelection.findElements(By.cssSelector("div"));
        for (WebElement option : options) {
            if (option.getText().equalsIgnoreCase(Constants.NWF_CONNECTOR)) {
                pageUtils.waitForElementAndClick(option);
                return;
            }
        }

        // A hard wait is needed here due to timing issues with the "Conector Dropdown"
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
    }

    /**
     *
     * @return Connector dropdown exists
     */
    public boolean connectorDropdownExists() {
        return pageUtils.isElementDisplayed(newWorkflowConnectorDropDown);
    }

    /**
     *
     * @return Value map with the state of each schedule tab
     */
    public Map<String, Boolean> schedulesExist() {
        Map<String, Boolean> values = new HashMap<>();
        values.put("minutes", pageUtils.isElementDisplayed(minutesTab));
        values.put("hourly", pageUtils.isElementDisplayed(hourlyTab));
        values.put("daily", pageUtils.isElementDisplayed(dailyTab));
        values.put("weekly", pageUtils.isElementDisplayed(weeklyTab));
        values.put("monthly", pageUtils.isElementDisplayed(monthlyTab));
        values.put("yearly", pageUtils.isElementDisplayed(yearlyTab));

        return values;
    }

    /**
     * Create a basic workflow with just a name & connector
     *
     * @param name workflow name
     * @param iteration If creating more than one workflow, iteration is the current workflow count.
     *                   For example, if you intend to create 4 new workflows and you are creating your third
     *                   workflow, the iteration would be 3.
     */
    public void createNewWorkflow(String name, int iteration) {
        createNewWorkflow(name, null, true, false, null, iteration);
    }

    /**
     * Create a basic new work flow
     *
     * @param name The name of of the new workflow
     * @param description Workflow description. If null no description will be added
     * @param iteration If creating more than one workflow, iteration is the current workflow count.
     *                  For example, if you intend to create 4 new workflows and you are creating your third
     *                  workflow, the iteration would be 3.
     */
    public void createNewWorkflow(String name, String description, int iteration) {
        createNewWorkflow(name, description, true, false, null, iteration);

    }

    /**
     * Create a new workflow
     *
     * @param name
     * @param description
     * @param selectConnector If true, a connector will be selected
     * @param isSetSchedule If true a schedule will be set
     * @param schedule Scheduling information
     * @param iteration
     */
    public void createNewWorkflow(String name, String description, boolean selectConnector, boolean isSetSchedule,
                                  WorkflowSchedule schedule, int iteration) {
        fillDetails(name, description, selectConnector, isSetSchedule, schedule);
        pageUtils.waitForElementAndClick(newWorkflowNextButton);
        fillQueryDefinitions();
        fillCostingInputs(iteration);
        pageUtils.waitForElementAndClick(notificationNextButton);
        pageUtils.waitForElementAndClick(saveButton);
    }

    /**
     * Fill in information on the new workflow details tab
     *
     * @param name Name of the workflow
     * @param description Description of workflow
     * @param selectConnector If true a connector will be selected
     * @param isSetSchedule If true, a schedule will be set
     * @param schedule Schedule information
     */
    public void fillDetails(String name, String description, boolean selectConnector,
                                        boolean isSetSchedule, WorkflowSchedule schedule) {
        // Fill Details page
        if (name != null) {
            fillWorkflowNameField(name);
        }

        if (description != null) {
            fillWorkflowDescriptionField(description);
        }

        if (selectConnector) {
            selectWorkflowConnector();
        }

        if (isSetSchedule) {
            setSchedule(schedule);
        }
    }

    /**
     * Set a schedule on the Details tab on a New Workflow dialogue
     *
     * @param schedule Schedule object with a target schedule and corresponding values
     */
    public void setSchedule(WorkflowSchedule schedule) {
        switch (schedule.getSchedule()) {
            case MINUTES:
                scheduleMinutes(schedule);
                break;
            case HOUR:
                scheduleHourly(schedule);
                break;
            case DAILY:
                scheduleDaily(schedule);
                break;
            case WEEKLY:
                scheduleWeekly(schedule);
                break;
            case MONTHLY:
                scheduleMonthly(schedule);
                break;
            case YEARLY:
                scheduleYearly(schedule);
                break;
            default:
                logger.debug("Incorrect schedule type");
        }
    }

    /**
     * Fill in detail information and navigate to the Query Definition tab
     *
     * @param name Workflow name
     * @return NewWorkflowPage object
     */
    public NewWorkflowPage gotoQueryDefinitions(String name) {
        fillDetails(name, null, true, false, null);
        pageUtils.waitForElementAndClick(newWorkflowNextButton);
        return this;
    }

    /**
     * Fill in Query definition information
     *
     * @return NewWorkflowPage object
     */
    public NewWorkflowPage gotoCosting() {
        fillQueryDefinitions(true);
        return this;
    }

    /**
     * Fill in Costing information
     *
     * @return NewWorkflowPage object
     */
    public NewWorkflowPage gotoNotifications() {
        pageUtils.waitForElementAndClick(queryPageNextButton);
        return this;
    }

    /**
     * Creates a css webelement string with specified a specified id
     *
     * @param css Marked up webelement css string
     * @param interation The number of times the New Workflow popup has been called
     * @return
     */
    private WebElement getIncrementedElement(String css, int interation) {
        Integer id = interation * 5;
        String elementCss = css.replace("[ID]", id.toString());
        WebElement webElement = driver.findElement(By.cssSelector(elementCss));
        return webElement;
    }

    /**
     * Select the first query filter
     */
    public void selectQueryFilter() {
        pageUtils.waitForElementToBeClickable(queryDropDown);
        Select opt = new Select(queryDropDown);
        opt.selectByIndex(1);
    }

    /**
     * Fills in the Query Definition fields with DEFAULT values
     */
    public void fillQueryDefinitions() {
        fillQueryDefinitions(true);
    }

    /**
     * Fills in the Query Definition fields with DEFAULT values
     */
    public void fillQueryDefinitions(boolean clickNext) {
        fillQueryDefinitions(queryDropDown, null, queryValue, clickNext);

    }

    /**
     * Fill in the query definition fields
     *
     * @param filter The filter object to use
     * @param operator If not null, the operator drop down to use
     * @param value The value to enter into the value field
     * @param clickNext If true, the next button will be clicked. The clickNext parameter would be set to false
     *                 for most feature specific tests, like check fields or add a rule. No need to click next
     *                  because the tests are validated on the Query Definitions page.
     *                  The clickNext parameter would be true for flow tests such as Create and Edit. I'll add this
     *                  information to the javadoc so it's clearer why the clickNext parameter exists.
     */
    public void fillQueryDefinitions(WebElement filter, WebElement operator, WebElement value, boolean clickNext) {
        pageUtils.waitForElementToBeClickable(filter);
        Select optFilter = new Select(filter);
        optFilter.selectByIndex(1);

        if (operator != null) {
            pageUtils.waitForElementToBeClickable(operator);
            Select optOperator = new Select(operator);
            optOperator.selectByIndex(1);
        }

        if (value != null) {
            pageUtils.waitForElementAndClick(value);
            value.sendKeys(Constants.DEFAULT_PART_ID);
        }

        pageUtils.waitForElementAndClick(queryPopup);

        if (clickNext) {
            pageUtils.waitForElementAndClick(queryNext);
        }
    }

    /**
     * Fill in the Costing Inputs fields with DEFAULT values
     *
     * @param iteration The number of times the New Workflow popup has been called
     */
    private void fillCostingInputs(int iteration) {
        pageUtils.waitForElementAndClick(queryAddRowButton);
        WebElement ciConnectField = getIncrementedElement(ciConnectFieldCss, iteration);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        pageUtils.waitForElementAndClick(ciConnectField);
        pageUtils.waitForElementAndClick(ciConnectFieldSelection);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        WebElement valueDD = getIncrementedElement(valueDDCss, iteration);
        pageUtils.waitForElementAndClick(valueDD);
        pageUtils.waitForElementAndClick(valueSelection);
        pageUtils.waitForElementAndClick(queryPageNextButton);
    }

    /**
     * Add minutes schedule
     *
     * @param schedule
     */
    private void scheduleMinutes(WorkflowSchedule schedule) {
        pageUtils.waitForElementAndClick(minutesTab);
        pageUtils.waitForElementAndClick(minutesInput);
        minutesInput.clear();
        minutesInput.sendKeys(schedule.getNumberOfMinutes().toString());
    }

    /**
     * Add hourly schedule
     *
     * @param schedule
     */
    private void scheduleHourly(WorkflowSchedule schedule) {
        pageUtils.waitForElementAndClick(hourlyTab);
        if (schedule.isSelectEveryHour()) {
            pageUtils.waitForElementAndClick(everyHour);
            pageUtils.waitForElementAndClick(hoursInput);
            hoursInput.clear();
            hoursInput.sendKeys(schedule.getNumberOfHours().toString());
        } else {
            pageUtils.clickOnOffScreenElement(atHourMinutes);
            pageUtils.selectDropdownOption(atHours, schedule.getStartHour());
            pageUtils.selectDropdownOption(atMinutes, schedule.getStartMinutes());
        }
    }

    /**
     * Add daily schedule
     *
     * @param schedule
     */
    private void scheduleDaily(WorkflowSchedule schedule) {
        pageUtils.waitForElementAndClick(dailyTab);
        if (schedule.isSelectEveryDay()) {
            pageUtils.waitForElementAndClick(daysInput);
            daysInput.clear();
            daysInput.sendKeys(schedule.getNumberOfDays().toString());
        } else {
            pageUtils.clickOnOffScreenElement(dayHourMinutes);
        }

        pageUtils.selectDropdownOption(dailyHours, schedule.getStartHour());
        pageUtils.selectDropdownOption(dailyMinutes, schedule.getStartMinutes());
    }

    /**
     * Build css string for Weekly day checkbox
     *
     * @param day day of week
     * @return string with day abbreviation
     */
    private String getDayOfWeekCss(String day) {
        String dayAbbreviation;
        switch (day.toUpperCase()) {
            case "MONDAY":
                dayAbbreviation = "MON";
                break;
            case "TUESDAY":
                dayAbbreviation = "TUE";
                break;
            case "WEDNESDAY":
                dayAbbreviation = "WED";
                break;
            case "THURSDAY":
                dayAbbreviation = "THU";
                break;
            case "FRIDAY":
                dayAbbreviation = "FRI";
                break;
            case "SATURDAY":
                dayAbbreviation = "SAT";
                break;
            case "SUNDAY":
                dayAbbreviation = "SUN";
                break;
            default:
                dayAbbreviation = "MON";
        }

        return dayOfWeekCss.replace("[DAY]", dayAbbreviation);
    }

    /**
     * Add weekly schedule
     *
     * @param schedule
     */
    private void scheduleWeekly(WorkflowSchedule schedule) {
        pageUtils.waitForElementAndClick(weeklyTab);
        WebElement dayOfWeek =
                driver.findElement(By.cssSelector(getDayOfWeekCss(schedule.getWeekDays().toString())));
        pageUtils.waitForElementAndClick(dayOfWeek);
        pageUtils.selectDropdownOption(weeklyMinutes, schedule.getStartMinutes());
        pageUtils.selectDropdownOption(weeklyHours, schedule.getStartHour());
    }

    /**
     * Add monthly schedule
     *
     * @param schedule
     */
    private void scheduleMonthly(WorkflowSchedule schedule) {
        pageUtils.waitForElementAndClick(monthlyTab);
        if (schedule.isSelectDayInEveryMonth()) {
            pageUtils.waitForElementAndClick(dayOfMonth);
            pageUtils.waitForElementAndClick(dayOfMonthInput);
            dayOfMonthInput.clear();
            dayOfMonthInput.sendKeys(schedule.getDayOfMonth().toString());
            pageUtils.waitForElementAndClick(monthInput);
            monthInput.clear();
            monthInput.sendKeys(schedule.getNumberOfMonths().toString());
        } else {
            pageUtils.clickOnOffScreenElement(everyMonth);
            pageUtils.selectDropdownOption(weekDay, schedule.getMonthlyOccurance());
            pageUtils.selectDropdownOption(dayInWeekOrder, schedule.getWeekDay());
            pageUtils.waitForElementAndClick(everyMonthInput);
            everyMonthInput.clear();
            everyMonthInput.sendKeys(schedule.getNumberOfMonths().toString());
        }

        pageUtils.selectDropdownOption(monthlyHours, schedule.getStartHour());
        pageUtils.selectDropdownOption(monthlyMinutes, schedule.getStartMinutes());
    }

    /**
     * Add yearly schedule
     *
     * @param schedule
     */
    private void scheduleYearly(WorkflowSchedule schedule) {
        pageUtils.waitForElementAndClick(yearlyTab);
        if (schedule.isSelectEveryYear()) {
            pageUtils.waitForElementAndClick(optMonthOfYear);
            pageUtils.selectDropdownOption(monthsOfYear, schedule.getMonth());
            pageUtils.waitForElementAndClick(yearInput);
            yearInput.clear();
            yearInput.sendKeys(schedule.getDayOfMonth().toString());
        } else {
            // The function fails here for some reason, even with using wait for and click
            pageUtils.waitFor(2000);
            pageUtils.waitForElementAndClick(everyYear);
            pageUtils.waitForElementAndClick(yearInput);
            pageUtils.selectDropdownOption(dayOrderInYear, schedule.getMonthlyOccurance());
            pageUtils.selectDropdownOption(dayWeekForYear, schedule.getWeekDay());
            pageUtils.selectDropdownOption(monthsOfYear2, schedule.getMonth());
        }

        pageUtils.selectDropdownOption(yearlyHours, schedule.getStartHour());
        pageUtils.selectDropdownOption(yearlyMinutes, schedule.getStartMinutes());

    }

    /**
     * return page objects based on rule number
     *
     * @param ruleNumber The row number of the rule, if new rule then last rule number + 1
     * @param filterSelected True if a filter for this rule number has been selected
     * @return Filter, Operator and Value elements
     */
    private Map<String, WebElement> getQueryElements(Integer ruleNumber, boolean filterSelected) {
        Map<String, WebElement> elements = new HashMap<>();
        String filterSelectString = filterSelectCss.replace("[RULE_NUMBER]", ruleNumber.toString());
        String operatorSelectString = operatorSelectCss.replace("[RULE_NUMBER]", ruleNumber.toString());
        String valueFieldString = valueFieldCss.replace("[RULE_NUMBER]", ruleNumber.toString());

        WebElement filterSelectEle = driver.findElement(By.cssSelector(filterSelectString));

        WebElement operatorSelectEle = null;
        WebElement valueFieldEle = null;
        if (filterSelected) {
            operatorSelectEle = driver.findElement(By.cssSelector(operatorSelectString));
            valueFieldEle = driver.findElement(By.cssSelector(valueFieldString));
        }

        elements.put("filter", filterSelectEle);
        elements.put("operator", operatorSelectEle);
        elements.put("value", valueFieldEle);
        return elements;

    }
}
