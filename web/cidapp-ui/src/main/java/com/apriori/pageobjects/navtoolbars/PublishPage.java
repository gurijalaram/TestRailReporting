package com.apriori.pageobjects.navtoolbars;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.PersonResponse;
import com.apriori.cidappapi.utils.PeopleUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishPage extends LoadableComponent<PublishPage> {

    private static final Logger logger = LoggerFactory.getLogger(PublishPage.class);

    @FindBy(xpath = "//h5[.='Publish Scenario']")
    private WebElement headerDialog;

    @FindBy(css = "div[class='header-message'] p")
    private WebElement headerMessage;

    @FindBy(css = "div[class='checkbox-icon']")
    private WebElement lockTickBox;

    @FindBy(css = "div[class='conflict-message']")
    private WebElement conflictMessage;

    @FindBy(xpath = "//label[.='Override existing public scenario']")
    private WebElement overrideButton;

    @FindBy(xpath = "//label[.='Change Name']")
    private WebElement changeNameButton;

    @FindBy(css = "input[name='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "[id='qa-publish-form-status-select'] .apriori-select")
    private WebElement statusDropdown;

    @FindBy(css = "[id='qa-publish-form-status-select'] input")
    private WebElement statusInput;

    @FindBy(css = "[id='qa-publish-form-cost-maturity-select'] .apriori-select")
    private WebElement costMaturityDropdown;

    @FindBy(css = "[id='qa-publish-form-cost-maturity-select'] input")
    private WebElement costMaturityInput;

    @FindBy(css = "[id='qa-publish-form-assigned-to-select'] .apriori-select")
    private WebElement assigneeDropdown;

    @FindBy(css = "[id='qa-publish-form-assigned-to-select'] input")
    private WebElement assigneeInput;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private PeopleUtil peopleUtil = new PeopleUtil();
    private String root = "modal-body";

    public PublishPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(headerDialog);
    }

    /**
     * Uses type ahead to input the status
     *
     * @param status - the status
     * @return current page object
     */
    public PublishPage selectStatus(String status) {
        pageUtils.typeAheadSelect(statusDropdown, root, status);
        return this;
    }

    /**
     * Uses type ahead to input the cost maturity
     *
     * @param costMaturity - the cost maturity
     * @return current page object
     */
    public PublishPage selectCostMaturity(String costMaturity) {
        pageUtils.typeAheadSelect(costMaturityDropdown, root, costMaturity);
        return this;
    }

    /**
     * Uses type ahead to input the assignee
     *
     * @param assignee - the assignee
     * @return current page object
     */
    public PublishPage selectAssignee(UserCredentials assignee) {
        PersonResponse currentPerson = peopleUtil.getCurrentPerson(assignee);
        pageUtils.typeAheadSelect(assigneeDropdown, root, currentPerson.getGivenName() + " " + currentPerson.getFamilyName());
        return this;
    }

    /**
     * Click locked tick box
     *
     * @return current page object
     */
    public PublishPage lock() {
        pageUtils.waitForElementToAppear(lockTickBox).click();
        return this;
    }

    /**
     * Get conflict error message
     *
     * @return string
     */
    public String getConflictMessage() {
        return pageUtils.waitForElementToAppear(conflictMessage).getAttribute("textContent");
    }

    /**
     * Selects the override button
     *
     * @return current page object
     */
    public PublishPage override() {
        pageUtils.waitForElementAndClick(overrideButton);
        return this;
    }

    /**
     * Change scenario name
     *
     * @param scenarioName - scenario name
     * @return current page object
     */
    public PublishPage changeName(String scenarioName) {
        pageUtils.waitForElementAndClick(changeNameButton);
        pageUtils.waitForElementToBeClickable(scenarioNameInput).clear();
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Select the publish button
     *
     * @param cidComponentItem - the cid representation item
     * @param currentUser      - the current user
     * @param <T>              - the object type
     * @return generic page object
     */
    public <T> T publish(ComponentInfoBuilder cidComponentItem, UserCredentials currentUser, Class<T> klass) {
        modalDialogController.publish(klass);
        new ScenariosUtil().getPublishedScenarioRepresentation(cidComponentItem, "PUBLISH", true);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the continue button
     *
     * @return generic page object
     */
    public <T> T continues(Class<T> klass) {
        return modalDialogController.continues(klass);
    }

    /**
     * Select the back button
     *
     * @return generic page object
     */
    public PublishPage back() {
        modalDialogController.back();
        return this;
    }
}
