package com.apriori.pageobjects.header;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.RevertPage;
import com.apriori.pageobjects.pages.explore.AssignPage;
import com.apriori.pageobjects.pages.explore.ComparisonPage;
import com.apriori.pageobjects.pages.explore.DeletePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.pageobjects.pages.explore.ScenarioNotesPage;
import com.apriori.pageobjects.pages.explore.ScenarioPage;
import com.apriori.pageobjects.utils.PageUtils;

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

    private static Logger logger = LoggerFactory.getLogger(GenericHeader.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    private WebElement newFileDropdown;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton'] .fa")
    private WebElement publishButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertButton;

    @FindBy(css = "button[data-ap-comp='deleteScenarioButton']")
    private WebElement deleteButton;

    @FindBy(css = ".dropdown .glyphicons-settings")
    private WebElement actionsDropdown;

    @FindBy(css = "button[data-ap-comp='editScenarioButton']")
    private WebElement editButton;

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
     * Collective method to upload a file
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @return current page object
     */
    public EvaluatePage uploadFile(String scenarioName, File filePath) {
        pageUtils.waitForElementAndClick(newFileDropdown);
        pageUtils.waitForElementAndClick(componentButton);
        return new FileUploadPage(driver).uploadFile(scenarioName, filePath);
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
        pageUtils.waitForElementAndClick(actionsDropdown);
        pageUtils.waitForElementAndClick(lockToggleButton);
        return this;
    }

    /**
     * Gets the locked status
     *
     * @return true false
     */
    public Boolean isActionLockedStatus(String status) {
        return pageUtils.checkElementAttribute(lockToggleButton, "innerText", status);
    }

    /**
     * Selects the actions button
     *
     * @return current page object
     */
    public GenericHeader selectActions() {
        pageUtils.waitForElementAndClick(actionsDropdown);
        return this;
    }

    /**
     * Selects assign scenario
     *
     * @return new page object
     */
    public AssignPage selectAssignScenario() {
        actionsDropdown.click();
        assignButton.click();
        return new AssignPage(driver);
    }

    /**
     * Selects scenario info and notes
     *
     * @return new page object
     */
    public ScenarioNotesPage selectScenarioInfoNotes() {
        pageUtils.waitForElementAndClick(actionsDropdown);
        pageUtils.waitForElementAndClick(scenarioNotesButton);
        return new ScenarioNotesPage(driver);
    }

    /**
     * Publish the scenario
     *
     * @return new page object
     */
    public ExplorePage publishScenario() {
        pageUtils.checkElementAttributeEmpty(publishButton, "title");
        pageUtils.waitForElementAndClick(publishButton);
        new PublishPage(driver).selectPublishButton();
        return new ExplorePage(driver);
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
        pageUtils.waitForElementAndClick(publishButton);
        new PublishPage(driver).selectStatus(status)
            .selectCostMaturity(costMaturity)
            .selectAssignee(assignee);
        return new PublishPage(driver);
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
        pageUtils.waitForElementAndClick(revertButton);
        return new RevertPage(driver);
    }
}