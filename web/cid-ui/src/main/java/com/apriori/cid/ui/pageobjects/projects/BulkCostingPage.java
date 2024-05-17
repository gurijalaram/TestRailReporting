package com.apriori.cid.ui.pageobjects.projects;


import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;

public class BulkCostingPage extends LoadableComponent<BulkCostingPage> {
    @FindBy(xpath = "//button[contains(@class,'bcm-sub-header-delete-button')]")
    private WebElement deleteButton;
    @FindBy(xpath = "//button[contains(@type,'submit')][contains(.,'Confirm')]")
    private WebElement confirmDeleteButton;
    @FindBy(xpath = "//div[contains(@data-testid,'bulk-analysis-explorer')]]")
    private WebElement bulkAnalysisPage;
    @FindBy(xpath = "//span[contains(.,'Edit')]")
    private WebElement editButton;
    @FindBy(xpath = "//h2[contains(.,'Edit')]")
    private WebElement editPage;
    @FindBy(xpath = "//button[contains(@aria-selected,'true')][contains(.,'Successes')]")
    private WebElement isEditedSuccessfully;
    @FindBy(xpath = "//div[contains(@class,'MuiPaper-root MuiPaper-elevation MuiPaper-rounded MuiPaper-elevation24')]")
    private WebElement removeConfirmationLabel;
    @FindBy(id = "qa-input-row-delete-new")
    private WebElement removeButton;
    @FindBy(xpath = "//button[contains(@class,'MuiButtonBase-root MuiButton-root')][@data-testid = 'primary-button']")
    private WebElement removeScenarioButton;
    @FindBy(xpath = "//button[contains(.,'Close')]")
    private WebElement closeButton;
    @FindBy(id = "qa-input-row-set-inputs")
    private WebElement setInputsButton;
    @FindBy(xpath = "//h2[contains(.,'Set Inputs')]")
    private WebElement setInputsLabel;
    @FindBy(xpath = "//div[@class = 'select-field select-field-process-group-name form-group']/div[1]")
    private WebElement processGroupDropdown;
    @FindBy(xpath = "//div[@class = 'select-field select-field-vpe-name form-group digital-factory-select-field']/div[1]")
    private WebElement digitalFactoryDropdown;
    @FindBy(xpath = "//input[@name = 'annualVolume']")
    private WebElement annualVolumeInput;
    @FindBy(xpath = "//input[@name = 'productionLife']")
    private WebElement yearsInput;
    @FindBy(xpath = "//button[contains(.,'Save')]")
    private WebElement saveButton;
    @FindBy(xpath = "//a[@href = '/bulk-analysis']")
    private WebElement bulkAnalysisTab;
    private PageUtils pageUtils;
    private WebDriver driver;

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        driver.getCurrentUrl().contains("/bulk-analysis");
        pageUtils.isElementDisplayed(bulkAnalysisPage);
    }

    public BulkCostingPage(WebDriver driver) {
        super();
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * verify if the list of worksheet is present in the bulk analysis page
     *
     * @return boolean
     */
    public boolean isListOfWorksheetsPresent() {
        List<WebElement> listOfWorksheetItems =
            pageUtils.waitForElementsToAppear(By.xpath("//div[@data-testid = 'table-body']/div"));
        return !(listOfWorksheetItems.isEmpty());
    }

    /**
     * select and delete worksheet
     *
     * @param nameBulkAnalysis - name of the worksheet
     * @return page object
     */
    public BulkCostingPage selectAndDeleteSpecificBulkAnalysis(String nameBulkAnalysis) {
        String xpath = "//div[contains(@class,'table-row')][contains(.,'" + nameBulkAnalysis + "')]/descendant::span";
        WebElement checkBox2 = pageUtils.waitForElementToBeClickable(By.xpath(xpath));
        pageUtils.waitForElementAndClick(checkBox2);
        pageUtils.waitForElementAndClick(deleteButton);
        pageUtils.waitForElementAndClick(confirmDeleteButton);
        return this;
    }

    /**
     * enter bulk analysis
     *
     * @param nameBulkAnalysis - name of the worksheet
     * @return page object
     */
    public BulkCostingPage enterSpecificBulkAnalysis(String nameBulkAnalysis) {
        String xpath = "//div[contains(.,'" + nameBulkAnalysis + "')][@data-testid = 'text-overflow']";
        WebElement link = pageUtils.waitForElementToBeClickable(By.xpath(xpath));
        pageUtils.waitForElementAndClick(link);
        return this;
    }

    /**
     * select First Part InWork Sheet
     *
     * @return page object
     */
    public BulkCostingPage selectFirstPartInWorkSheet() {
        List<WebElement> checkboxes = pageUtils
            .waitForSpecificElementsNumberToAppear(By.xpath("//div[contains(@class,'table-cell checkbox-cell')]/descendant::span"), 2);
        WebElement checkbox = checkboxes.get(1);
        pageUtils.waitForElementAndClick(checkbox);
        return this;
    }

    /**
     * click edit button
     *
     * @return page object
     */
    public BulkCostingPage clickEditButton() {
        pageUtils.waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Check if worksheet is not present on UI
     *
     * @param worksheetName - name of the worksheet
     * @return the boolean value
     */
    public boolean isWorksheetNotPresent(String worksheetName) {
        String xpath = "//div[contains(.,'" + worksheetName + "')][contains(@data-testid,'text-overflow')]";
        pageUtils.waitForElementsToNotAppear(By.xpath(xpath));
        return !(pageUtils.isElementDisplayed(By.xpath(xpath)));
    }

    /**
     * is on the Edit page
     *
     * @return boolean value
     */
    public boolean isOnEditPage() {
        pageUtils.waitForElementAppear(editPage);
        return pageUtils.isElementDisplayed(editPage);
    }

    /**
     * is the edit was successful
     *
     * @return boolean value
     */
    public boolean isEditSuccessful() {
        pageUtils.waitForElementAppear(isEditedSuccessfully);
        return pageUtils.isElementDisplayed(isEditedSuccessfully);
    }

    /**
     * is inputRow is displayed
     *
     * @return boolean
     */
    public boolean isInputRowDisplayed(String inputRowName) {
        String xpath = "//span[contains(.,'" + inputRowName + "')]";
        WebElement inputRow = pageUtils.waitForElementToAppear(By.xpath(xpath));
        pageUtils.waitForElementAppear(inputRow);
        return pageUtils.isElementDisplayed(inputRow);
    }

    /**
     * get remove button state
     *
     * @return String if the button is disabled
     */
    public String getRemoveButtonState(String attributeValue) {
        pageUtils.waitForElementAppear(removeButton);
        pageUtils.checkElementAttribute(removeButton, "aria-label", attributeValue);
        return removeButton.getAttribute("aria-label");
    }

    /**
     * Clicks on remove button and get confirmation text
     *
     * @return String text on the modal
     */
    public String clickOnRemoveButtonAngGetConfirmationText() {
        pageUtils.waitForElementAndClick(removeButton);
        return pageUtils.waitForElementAppear(removeConfirmationLabel).getText();
    }

    /**
     * click on remove scenario button on confirmation screen
     *
     * @return this object
     */
    public BulkCostingPage clickOnRemoveScenarioButtonOnConfirmationScreen() {
        pageUtils.waitForElementAndClick(removeScenarioButton);
        pageUtils.waitForElementAndClick(closeButton);
        return this;
    }

    /**
     * check if the scenario name is displayed on the page
     *
     * @param scenario - scenario name
     * @return boolean value
     */
    public boolean isScenarioPresentOnPage(String scenario) {
        return driver.getPageSource().contains(scenario);
    }

    /**
     * get expected set input button state
     *
     * @param attributeValue - expected string in attribute
     * @return String abut the state of the button
     */
    public String getSetInputButtonState(String attributeValue) {
        pageUtils.waitForElementAppear(setInputsButton);
        pageUtils.checkElementAttribute(setInputsButton, "aria-label", attributeValue);
        return setInputsButton.getAttribute("aria-label");
    }

    /**
     * click on the button Set Inputs
     *
     * @return this
     */
    public BulkCostingPage clickSetInputsButton() {
        pageUtils.waitForElementAndClick(setInputsButton);
        return this;
    }

    /**
     * check if the Set Inputs window is present
     *
     * @return boolean value
     */
    public boolean isScenarioPresentOnPage() {
        return pageUtils.waitForWebElement(setInputsLabel);
    }

    /**
     * select a value for process group dropdown
     *
     * @param value selected value
     * @return this
     */
    public BulkCostingPage selectDropdownProcessGroup(String value) {
        pageUtils.waitForElementAndClick(processGroupDropdown);
        String xpath = "//div[contains(.,'" + value + "')][contains(@class,'text-overflow option-content')]";
        WebElement webElement = pageUtils.waitForElementToAppear(By.xpath(xpath));
        pageUtils.waitForElementAndClick(webElement);
        return this;
    }

    /**
     * select a value for digital factory dropdown
     *
     * @param value selected value
     * @return this
     */
    public BulkCostingPage selectDropdownDigitalFactory(String value) {
        pageUtils.waitForElementAndClick(digitalFactoryDropdown);
        String xpath = "//div[contains(.,'" + value + "')][contains(@class,'text-overflow option-content')]";
        WebElement webElement = pageUtils.waitForElementToAppear(By.xpath(xpath));
        pageUtils.waitForElementAndClick(webElement);
        return this;
    }

    /**
     * sent values into the input field annual volume
     *
     * @param value send value
     * @return this
     */
    public BulkCostingPage typeIntoAnnualVolume(String value) {
        pageUtils.waitForElementAndClick(annualVolumeInput);
        annualVolumeInput.sendKeys(value);
        return this;
    }

    /**
     * sent values into the input field: Years
     *
     * @param value send value
     * @return this
     */
    public BulkCostingPage typeIntoYears(String value) {
        pageUtils.waitForElementAndClick(yearsInput);
        yearsInput.sendKeys(value);
        return this;
    }

    /**
     * click on the save button in the set inputs
     *
     * @return this
     */
    public BulkCostingPage clickOnSaveButtonOnSetInputs() {
        pageUtils.waitForElementAndClick(saveButton);
        return this;
    }

    /**
     * click on the close button in the set inputs
     *
     * @return this
     */
    public BulkCostingPage clickOnCloseButtonOnSetInputs() {
        pageUtils.waitForElementAndClick(closeButton);
        return this;
    }

    /**
     * check if element is displayed in the page
     *
     * @param text text to check on the page
     * @return boolean
     */
    public boolean isElementDisplayedOnThePage(String text) {
        String xpath = "//div[contains(@aria-label,'" + text + "')]";
        WebElement element = pageUtils.waitForElementToAppear(By.xpath(xpath));
        return pageUtils.isElementDisplayed(element);
    }

    /**
     * check if on bulk analysis page
     *
     * @return boolean
     */
    public boolean isOnBulkAnalysisPage() {
        pageUtils.waitForElementAppear(bulkAnalysisTab);
        return pageUtils.isElementDisplayed(bulkAnalysisTab);
    }


}
