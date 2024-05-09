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
     * Check if worksheet is present on UI
     *
     * @param worksheetName - name of the worksheet
     * @return the boolean value
     */
    public boolean isWorksheetIsPresent(String worksheetName) {
        pageUtils.waitFor(2000);
        return driver.getPageSource().contains(worksheetName);
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
        pageUtils.waitForElementsToChangeAttributeValue(removeButton, "aria-label", attributeValue);
        return removeButton.getAttribute("aria-label");
    }

    /**
     * click on remove button
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

    public boolean IfScenarioIsPresentOnPage(String scenario) {
        return driver.getPageSource().contains("scenario");
    }

}
