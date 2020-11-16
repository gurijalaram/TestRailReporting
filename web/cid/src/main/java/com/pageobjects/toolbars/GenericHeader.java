package com.pageobjects.toolbars;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.compare.SaveAsPage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.evaluate.RevertPage;
import com.pageobjects.pages.explore.AssignPage;
import com.pageobjects.pages.explore.ComparisonPage;
import com.pageobjects.pages.explore.DeletePage;
import com.pageobjects.pages.explore.FileUploadPage;
import com.pageobjects.pages.explore.ScenarioNotesPage;
import com.pageobjects.pages.explore.ScenarioPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author kpatel
 */

public class GenericHeader extends PageHeader {

    private static final Logger logger = LoggerFactory.getLogger(GenericHeader.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    private WebElement newFileDropdown;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton']")
    private WebElement publishButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertButton;

    @FindBy(css = "button[data-ap-comp='deleteScenarioButton']")
    private WebElement deleteButton;

    @FindBy(xpath = "//a[text()='Actions']")
    private WebElement actionsDropdown;

    @FindBy(css = "button[data-ap-comp='editScenarioButton'] span")
    private WebElement editButton;

    @FindBy(css = "button[data-ap-comp='saveComparisonAsButton']")
    private WebElement saveComparisonAsButton;

    @FindBy(css = "button[data-ap-comp='newComponentButton']")
    private WebElement componentButton;

    @FindBy(css = "button[data-ap-comp='saveAsButton']")
    private WebElement scenarioButton;

    @FindBy(css = "button[data-ap-comp='newComparisonButton']")
    private WebElement comparisonButton;

    @FindBy(css = "button[data-ap-comp='toggleLockButton']")
    private WebElement lockToggleButton;

    @FindBy(css = "button[data-ap-comp='reloadButton']")
    private WebElement cadModelButton;

    @FindBy(css = "button[data-ap-comp='assignScenarioButton']")
    private WebElement assignButton;

    @FindBy(css = "button[data-ap-comp='updateAdminInfoButton']")
    private WebElement scenarioNotesButton;

    @FindBy(css = "button[data-ap-comp='partCostReportButton']")
    private WebElement partCostButton;

    @FindBy(css = "button[data-ap-comp='costComparisonReportButton']")
    private WebElement comparisonReportButton;

    @FindBy(css = "input[type='file']")
    private WebElement fileInput;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GenericHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Checks delete button is displayed
     *
     * @return visibility of button
     */
    public boolean isDeleteButtonPresent() {
        pageUtils.waitForElementAndClick(newFileDropdown);
        return deleteButton.isDisplayed();
    }

    /**
     * Collective method to upload a file then select OK
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param className    - the class name
     * @return new page object
     */
    public <T> T uploadFileAndOk(String scenarioName, File filePath, Class<T> className) {
        return uploadFile(scenarioName, filePath).selectOkButton(className);
    }

    /**
     * Collective method to upload a file then select Cancel
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param className    - the class name
     * @return new page object
     */
    public <T> T uploadFileAndCancel(String scenarioName, File filePath, Class<T> className) {
        return uploadFile(scenarioName, filePath).selectCancelButton(className);
    }

    /**
     * Selects the file dropdown and enters file details
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @return current page object
     */
    public FileUploadPage uploadFile(String scenarioName, File filePath) {
        pageUtils.waitForElementAndClick(newFileDropdown);
        pageUtils.waitForElementAndClick(componentButton);
        return new FileUploadPage(driver).inputFileDetails(scenarioName, filePath);
    }

    /**
     * Uploads a cad file
     *
     * @param filename - the file name
     * @return new page object
     */
    public EvaluatePage updateCadFile(File filename) {
        pageUtils.waitForElementAndClick(actionsDropdown);
        for (int sendFile = 0; sendFile < 4; sendFile++) {
            fileInput.sendKeys(filename.getAbsolutePath().replace("%5c", File.separator));
        }
        pageUtils.waitForElementAndClick(actionsDropdown);
        return new EvaluatePage(driver);
    }

    /**
     * Selects new scenario button
     *
     * @return new page object
     */
    public ScenarioPage createNewScenario() {
        pageUtils.waitForElementAndClick(newFileDropdown);
        pageUtils.waitForElementAndClick(scenarioButton);
        return new ScenarioPage(driver);
    }

    /**
     * Selects new comparison button
     *
     * @return new page object
     */
    public ComparisonPage createNewComparison() {
        pageUtils.waitForElementAndClick(newFileDropdown);
        pageUtils.waitForElementAndClick(comparisonButton);
        return new ComparisonPage(driver);
    }

    /**
     * Lock/Unlocks a scenario
     *
     * @return current page object
     */
    public GenericHeader toggleLock() {
        pageUtils.javaScriptClick(actionsDropdown);
        pageUtils.waitForElementAndClick(lockToggleButton);
        return this;
    }

    /**
     * Gets the locked status
     *
     * @return true/false
     */
    public boolean isActionLockedStatus(String status) {
        By lockToggle = By.xpath(String.format("//button[.='%s']", status));
        return pageUtils.waitForElementToAppear(lockToggle).isDisplayed();
    }

    /**
     * Selects the actions button
     *
     * @return current page object
     */
    public GenericHeader selectActions() {
        pageUtils.javaScriptClick(actionsDropdown);
        return this;
    }

    /**
     * Selects assign scenario
     *
     * @return new page object
     */
    public AssignPage selectAssignScenario() {
        pageUtils.javaScriptClick(actionsDropdown);
        pageUtils.waitForElementAndClick(assignButton);
        return new AssignPage(driver);
    }

    /**
     * Selects scenario info and notes
     *
     * @return new page object
     */
    public ScenarioNotesPage selectScenarioInfoNotes() {
        pageUtils.javaScriptClick(actionsDropdown);
        pageUtils.waitForElementAndClick(scenarioNotesButton);
        return new ScenarioNotesPage(driver);
    }

    /**
     * Publish the scenario
     *
     * @return new page object
     */
    public <T> T publishScenario(Class<T> className) {
        clickPublishButton();
        return PageFactory.initElements(driver, className);
    }

    /**
     * Publish the scenario
     *
     * @param status       - the status dropdown
     * @param costMaturity - the cost maturity dropdown
     * @param assignee     - the assignee
     * @return new page object
     */
    public PublishPage publishScenario(String status, String costMaturity, String assignee) {
        clickPublishButton();
        new PublishPage(driver).selectStatus(status)
            .selectCostMaturity(costMaturity)
            .selectAssignee(assignee);
        return new PublishPage(driver);
    }

    /**
     * Checks the element attribute is empty before clicking
     */
    private void clickPublishButton() {
        pageUtils.checkElementAttributeEmpty(publishButton, "title");
        pageUtils.waitForElementAndClick(publishButton);
    }

    /**
     * Edits the scenario
     *
     * @return new page object
     */
    public <T> T editScenario(Class<T> className) {
        pageUtils.waitForElementAndClick(editButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Deletes the scenario
     *
     * @return new page object
     */
    public DeletePage delete() {
        pageUtils.checkElementAttributeEmpty(deleteButton, "title");
        pageUtils.waitForElementAndClick(deleteButton);
        return new DeletePage(driver);
    }

    /**
     * Selects the revert button
     *
     * @return new page object
     */
    public RevertPage revert() {
        pageUtils.checkElementAttributeEmpty(revertButton, "title");
        pageUtils.waitForElementAndClick(revertButton);
        return new RevertPage(driver);
    }

    /**
     * Selects the save as button
     *
     * @return new page object
     */
    public SaveAsPage saveAs() {
        pageUtils.checkElementAttributeEmpty(saveComparisonAsButton, "title");
        pageUtils.waitForElementAndClick(saveComparisonAsButton);
        return new SaveAsPage(driver);

    }
}