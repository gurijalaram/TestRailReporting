package com.pageobjects.pages.evaluate.inputs;

import com.apriori.utils.PageUtils;

import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class MoreInputsPage extends EvaluatePanelToolbar {

    private final Logger LOGGER = LoggerFactory.getLogger(MoreInputsPage.class);

    @FindBy(css = ".details-viewport-part .panel-details")
    private WebElement inputTable;

    @FindBy(css = "input[data-ap-field='cadFileName']")
    private WebElement cadFileInput;

    @FindBy(css = "input[data-ap-field='batchSizeOverridden']")
    private WebElement batchSizeCheckbox;

    @FindBy(css = "input[data-ap-field='batchSizeOverride']")
    private WebElement batchSizeInput;

    @FindBy(css = "button[data-ap-nav-dialog='showVpeSelection']")
    private WebElement vpeSelection;

    @FindBy(css = "input[data-ap-comp='mayBeMachined']")
    private WebElement machinedRadioButton;

    @FindBy(css = "input[data-ap-comp='notMachined']")
    private WebElement notMachinedRadioButton;

    @FindBy(css = "input[data-ap-field='TestString.value']")
    private WebElement testStringInput;

    @FindBy(css = "input[data-ap-field='TestNumber.value']")
    private WebElement testNumberInput;

    @FindBy(css = "input[data-ap-field='TestDate.value']")
    private WebElement testDateInput;

    @FindBy(css = "select[data-ap-field='TestUser.value']")
    private WebElement testUserSelect;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public MoreInputsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(inputTable);
    }

    /**
     * Gets name of cad file
     *
     * @return name of cad file
     */
    public String getCadFileName() {
        pageUtils.waitForElementToAppear(cadFileInput);
        return cadFileInput.getAttribute("value");
    }

    /**
     * Selects the batch size checkbox
     *
     * @return the current page object
     */
    public MoreInputsPage selectBatchBox() {
        batchSizeCheckbox.click();
        return this;
    }

    /**
     * Input batch size
     *
     * @param batchSize - the batch size
     * @return current page object
     */
    public MoreInputsPage enterBatchInput(String batchSize) {
        pageUtils.clearInput(batchSizeInput);
        batchSizeInput.sendKeys(batchSize);
        return this;
    }

    /**
     * Selects vpe button
     *
     * @return new page object
     */
    public VPESelectionPage selectVPEButton() {
        vpeSelection.click();
        return new VPESelectionPage(driver);
    }

    /**
     * Selects the machined/not machined checkbox
     *
     * @param option - true/false
     * @return current page object
     */
    public MoreInputsPage setMachined(boolean option) {
        if (option) {
            machinedRadioButton.click();
        } else {
            notMachinedRadioButton.click();
        }
        return this;
    }

    /**
     * Enter test string
     *
     * @param testString - the test string
     * @return current page object
     */
    public MoreInputsPage enterTestString(String testString) {
        pageUtils.clearInput(testStringInput);
        testStringInput.sendKeys(testString);
        return this;
    }

    /**
     * Enter test number
     *
     * @param testNumber - the test number
     * @return current page object
     */
    public MoreInputsPage enterTestNumber(String testNumber) {
        pageUtils.clearInput(testNumberInput);
        testNumberInput.sendKeys(testNumber);
        return this;
    }

    /**
     * Enter test date
     *
     * @param testDate - the test date
     * @return current page object
     */
    public MoreInputsPage enterTestDate(String testDate) {
        pageUtils.clearInput(testDateInput);
        testDateInput.sendKeys(testDate);
        return this;
    }

    /**
     * Selects the user
     *
     * @param testUser - the user
     * @return current page object
     */
    public MoreInputsPage selectTestUser(String testUser) {
        new Select(testUserSelect).selectByVisibleText(testUser);
        return this;
    }

    /**
     * Gets batch size from UI
     *
     * @return batch size
     */
    public String getBatchSize() {
        pageUtils.waitForElementAppear(batchSizeInput);
        return batchSizeInput.getAttribute("value");
    }
}