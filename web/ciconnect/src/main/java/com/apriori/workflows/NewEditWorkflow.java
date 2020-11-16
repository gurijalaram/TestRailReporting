package com.apriori.workflows;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewEditWorkflow extends LoadableComponent<NewEditWorkflow> {

    private final Logger logger = LoggerFactory.getLogger(Schedule.class);

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-92'] > button")
    private WebElement detailsNextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-104'] > button > span:nth-of-type(3)")
    private WebElement queryNextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-108'] > button > span:nth-of-type(3)")
    private WebElement costingInputsNextBtn;

    @FindBy(xpath = "//div[@id='root_pagemashupcontainer-1_navigation-83-popup_button-92']/button[@disabled='']")
    private WebElement disabledNextBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_textbox-148'] > table > tbody > tr > td > input")
    private WebElement inputName;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_textarea-152'] > div > textarea")
    private WebElement inputDescription;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_DrowpdownWidget-154'] > div > div")
    private WebElement connectorDropdown;

    @FindBy(css = "div[class^='ss-content ss-'][class$=' ss-open'] > div > input[type='search']")
    private WebElement connectorDropdownSearch;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_navigation-83-popup_button-43'] > button")
    private WebElement saveButton;

    private WebDriver driver;
    private PageUtils pageUtils;
    private Schedule schedule;

    public NewEditWorkflow(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.schedule = new Schedule(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
    }

    /**
     * Click Next button on Details tab of New Edit Workflow Modal
     */
    public NewEditWorkflow clickDetailsNextBtn() {
        pageUtils.waitForElementAndClick(detailsNextBtn);
        return this;
    }

    /**
     * Click Next button on Query tab of New Edit Workflow Modal
     */
    public NewEditWorkflow clickQueryNextBtn() {
        pageUtils.waitForElementAndClick(queryNextBtn);
        return this;
    }

    /**
     * Click Next btn on Query tab of New Edit Workflow Modal
     */
    public NewEditWorkflow clickCostingInputsNextBtn() {
        pageUtils.waitForElementAndClick(costingInputsNextBtn);
        return this;
    }

    /**
     * Input Workflow name
     *
     * @param workflowName - Workflow name to be entered
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow inputWorkflowName(String workflowName) {
        pageUtils.waitForElementToAppear(detailsNextBtn);
        inputName.sendKeys(workflowName);
        return new NewEditWorkflow(driver);
    }

    /**
     * Select connector
     *
     * @param connectorName - Name of connector to be selected
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow selectConnector(String connectorName) {
        pageUtils.waitForElementAndClick(connectorDropdown);

        By connectorToClick = By.xpath(String.format("//div[contains(text(), '%s')]", connectorName));
        pageUtils.waitForElementAndClick(connectorToClick);
        return new NewEditWorkflow(driver);
    }

    /**
     * Select query CI Connect field
     *
     * @param ruleNumber - Rule number, posistion of rule in list
     * @param fieldName  - Field name, CI Connect field to be used in query
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow selectQueryCIConnectField(int ruleNumber, String fieldName) {
        WebElement ciConnectField = driver.findElement(By.cssSelector(String.format(
            "select[name*='rule_%s_filter']", ruleNumber)));
        pageUtils.waitForElementToAppear(ciConnectField);
        Select queryField = new Select(ciConnectField);
        queryField.selectByVisibleText(fieldName);
        return new NewEditWorkflow(driver);
    }

    /**
     * Enter query argument
     *
     * @param ruleNumber   - Rule number, position of rule in list of query rules
     * @param ruleArgument - argument to be entered
     * @return NewEditWorkflow page object
     */
    public NewEditWorkflow enterQueryArgument(int ruleNumber, String ruleArgument) {
        WebElement inputField = driver.findElement(By.cssSelector(String.format(
            "input[name*='rule_%s_value_0']", ruleNumber)));
        pageUtils.waitForElementAndClick(inputField);
        pageUtils.clearInput(inputField);
        inputField.sendKeys(ruleArgument);
        return new NewEditWorkflow(driver);
    }

    /**
     * Click save button on New Edit Workflow modal
     *
     * @return new GenericWorkflow page object
     */
    public Schedule clickSaveButton() {

        int expectedRows = getNumberOfRows();
        pageUtils.waitForElementAndClick(saveButton);
        waitForExpectedRowCount(expectedRows);
        return new Schedule(driver);
    }

    /**
     * Get number of rows in Schedule tab table
     *
     * @return numRows  - number of rows in table
     */
    public int getNumberOfRows() {
        return schedule.getNumberOfRows();
    }

    /**
     * Wait for expected row count
     *
     * @param expectedRows - integer value expected rows
     */
    public void waitForExpectedRowCount(int expectedRows) {
        schedule.waitForExpectedRowCount(expectedRows);
    }

}