package com.apriori.pages.workflows.schedule.details;

import com.apriori.pages.CICBasePage;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

/**
 * Details part page in new workflow or edit workflow process
 */
public class DetailsPart extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(DetailsPart.class);

    public static final String PARENT_WEBELEMENT = "div[id^='root_pagemashupcontainer-1_navigation-']";

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_textbox-148'] > table > tbody > tr > td > input")
    public WebElement worflowNameField;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_label-220'] > span")
    private WebElement workflowNameErrorLbl;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_textarea-152'] > div > textarea")
    private WebElement workflowDescription;

    @FindBy(css = "#runtime > div.ss-content.ss-open > div.ss-list")
    private WebElement workflowConnectorSelection;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_DrowpdownWidget-154-bounding-box']")
    private WebElement workflowConnectorDropDown;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_DrowpdownWidget-154']")
    private WebElement workflowConnectorDropDownOpen;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_DrowpdownWidget-154'] > div > div > span.ss-arrow > span.arrow-down")
    private WebElement connectorDropdownArrowDownImg;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_DrowpdownWidget-154'] > div > div > span.ss-arrow > span.arrow-up")
    private WebElement connectorDropdownArrowUpImg;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_DrowpdownWidget-399']")
    private WebElement connectorComponentDropDown;

    @FindBy(css = "input[id^=root_pagemashupcontainer-1_navigation-][id$=-popup_checkbox-149-input]")
    private WebElement checkboxEnabled;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_button-92'] > button")
    private WebElement workflowDetailsNextButton;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_checkbox-393'] > label > span")
    private WebElement workflowQDReturnOnlyCheckbox;

    @FindBy(css = PARENT_WEBELEMENT + "[id$='-popup_button-189'] > button")
    private WebElement costingInputAddRowButton;

    @FindBy(css = "div.ss-content.ss-open> div.ss-search > input")
    private WebElement searchConnectorTxtElement;

    @FindBy(css = "#msMinutesTab")
    private WebElement minutesTab;
    @FindBy(css = "#MinutesInput")
    private WebElement minutesInput;
    @FindBy(css = "#msHourlyTab")
    private WebElement hourlyTab;
    @FindBy(css = "input[name='HourlyRadio'][value='1']")
    private WebElement everyHour;
    @FindBy(css = "input[name='HourlyRadio'][value='2']")
    private WebElement atHourMinutes;
    @FindBy(css = "#HoursInput")
    private WebElement hoursInput;
    @FindBy(id = "AtHours")
    private WebElement atHours;
    @FindBy(id = "AtMinutes")
    private WebElement atMinutes;
    @FindBy(css = "#msDailyTab")
    private WebElement dailyTab;
    @FindBy(css = "#DaysInput")
    private WebElement daysInput;
    @FindBy(css = "input[name='DailyRadio][value='1']")
    private WebElement everyDays;
    @FindBy(css = "input[name='DailyRadio'][value='2']")
    private WebElement dayHourMinutes;
    @FindBy(id = "DailyHours")
    private WebElement dailyHours;
    @FindBy(id = "DailyMinutes")
    private WebElement dailyMinutes;
    @FindBy(css = "#msWeeklyTab")
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
    @FindBy(css = "#msMonthlyTab")
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
    private WebElement dayInWeekOrder;
    @FindBy(id = "EveryMonthInput")
    private WebElement everyMonthInput;
    @FindBy(id = "MonthlyHours")
    private WebElement monthlyHours;
    @FindBy(id = "MonthlyMinutes")
    private WebElement monthlyMinutes;
    @FindBy(css = "#msYearlyTab")
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

    private String dayOfWeekCss = "input[value='[DAY]']";
    private String connectFieldDDCss = "div[class^='ss-content ss-'][class$='ss-open'] div[class='ss-list']";

    public DetailsPart(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(worflowNameField);
    }

    /**
     * @param name The name of the new workflow
     */
    public DetailsPart enterWorkflowNameField(String name) {
        pageUtils.waitForElementAndClick(worflowNameField);
        if (!worflowNameField.getAttribute("value").isEmpty()) {
            worflowNameField.clear();
        }
        worflowNameField.sendKeys(name + Keys.TAB);
        return this;
    }


    /**
     * @param description Description of the new workflow
     */
    public DetailsPart enterWorkflowDescriptionField(String description) {
        pageUtils.waitForElementAndClick(workflowDescription);
        workflowDescription.sendKeys(description);
        return this;
    }

    /**
     *
     */
    public DetailsPart selectEnabledCheckbox(String checkboxState) {
        if (checkboxState.toString().equals("on")) {
            if (!checkboxEnabled.isSelected()) {
                pageUtils.waitForElementAndClick(checkboxEnabled);
            }
        }

        if (checkboxState.toString().equals("off")) {
            if (checkboxEnabled.isSelected()) {
                pageUtils.waitForElementAndClick(checkboxEnabled);
            }
        }
        return this;
    }

    /**
     * Select an existing connector
     */
    public DetailsPart selectWorkflowConnector(String connectorName) {
        pageUtils.waitForElementAndClick(workflowConnectorDropDown);
        pageUtils.waitForElementToAppear(searchConnectorTxtElement);
        searchConnectorTxtElement.sendKeys(connectorName);
        this.selectValueFromDDL(0, connectorName);
        pageUtils.waitForElementToBeClickable(connectorComponentDropDown);
        return this;
    }


    /**
     * Click New Workflow button
     *
     * @return NewEditWorkflow page object
     */
    public Object clickWFDetailsNextBtn() {
        pageUtils.waitForElementToAppear(workflowDetailsNextButton);
        pageUtils.waitForElementToBeClickable(workflowDetailsNextButton);
        Object object = null;
        if (!workflowDetailsNextButton.isEnabled()) {
            logger.warn("Next button in Query Definitions Page is not enabled");
            return new QueryDefinitions(driver);
        }
        pageUtils.waitForElementAndClick(workflowDetailsNextButton);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        if (pageUtils.isElementDisplayed(workflowQDReturnOnlyCheckbox)) {
            object = new QueryDefinitions(driver);
        } else if (pageUtils.isElementDisplayed(costingInputAddRowButton)) {
            object = new CostingInputsPart(driver);
        }
        return object;
    }


    /**
     * Set a schedule on the Details tab on a New Workflow dialogue
     *
     * @param schedule Schedule object with a target schedule and corresponding values
     */
    public DetailsPart setSchedule(WorkflowSchedule schedule) {
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
        return this;
    }

    /**
     * getter for Minutes tab
     *
     * @return WebElement
     */
    public WebElement getMinutesTab() {
        return minutesTab;
    }

    /**
     * getter for Hourly tab
     *
     * @return WebElement
     */
    public WebElement getHourlyTab() {
        return hourlyTab;
    }

    /**
     * getter for Daily tab
     *
     * @return WebElement
     */
    public WebElement getDailyTab() {
        return dailyTab;
    }

    /**
     * getter for Weekly tab
     *
     * @return WebElement
     */
    public WebElement getWeeklyTab() {
        return weeklyTab;
    }

    /**
     * getter for Monthly tab
     *
     * @return WebElement
     */
    public WebElement getMonthlyTab() {
        return monthlyTab;
    }

    /**
     * getter for Yearly tab
     *
     * @return WebElement
     */
    public WebElement getYearlyTab() {
        return yearlyTab;
    }

    /**
     * getter WorkflowNameErrorLbl web element
     *
     * @return WebElement
     */
    public WebElement getWorkflowNameErrorLbl() {
        return workflowNameErrorLbl;
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
            driver.findElement(By.cssSelector(getDayOfWeekCss(schedule.getWeekDay().toString())));
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
            pageUtils.waitForElementAndClick(everyYear);
            pageUtils.selectDropdownOption(dayOrderInYear, schedule.getMonthlyOccurance());
            pageUtils.selectDropdownOption(dayWeekForYear, schedule.getWeekDay());
            pageUtils.selectDropdownOption(monthsOfYear2, schedule.getMonth());
        }

        pageUtils.selectDropdownOption(yearlyHours, schedule.getStartHour());
        pageUtils.selectDropdownOption(yearlyMinutes, schedule.getStartMinutes());

    }
}
