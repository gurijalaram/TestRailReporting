package com.apriori.cid.ui.pageobjects.navtoolbars;

import com.apriori.cid.api.models.response.PersonResponse;
import com.apriori.cid.api.utils.PeopleUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class PublishPage extends LoadableComponent<PublishPage> {

    @FindBy(xpath = "//h5[.='Publish Scenario']")
    private WebElement headerDialog;

    @FindBy(css = "div[class='header-message'] p")
    private WebElement headerMessage;

    @FindBy(css = "[id='qa-publish-form-lock']")
    private WebElement lockTickBox;

    @FindBy(css = "div[data-testid='alert-messaging']")
    private WebElement conflictMessage;

    @FindBy(css = "[role='dialog'] [data-testid='alert-messaging']")
    private WebElement alertMessage;

    @FindBy(css = "input[value='override']")
    private WebElement overrideButton;

    @FindBy(css = ".radio-button-group-field [value='changeName']")
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

    @FindBy(xpath = "//button[.='Close']")
    private WebElement closeButton;

    @FindBy(css = ".scenario-group-operations-success-message h5")
    private WebElement publishingMessage;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;
    private PeopleUtil peopleUtil = new PeopleUtil();

    public PublishPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
        pageUtils.typeAheadSelect(statusDropdown, "qa-publish-form-status-select", status);
        return this;
    }

    /**
     * Uses type ahead to input the cost maturity
     *
     * @param costMaturity - the cost maturity
     * @return current page object
     */
    public PublishPage selectCostMaturity(String costMaturity) {
        pageUtils.typeAheadSelect(costMaturityDropdown, "qa-publish-form-cost-maturity-select", costMaturity);
        return this;
    }

    /**
     * Uses type ahead to input the assignee
     *
     * @param assignee - the assignee
     * @return current page object
     */
    public PublishPage selectAssignee(String assignee) {
        typeAheadSelectAssignee(assigneeDropdown, "qa-publish-form-assigned-to-select", assignee);
        return this;
    }

    /**
     * Interacts with a dropdown and input the relevant info
     *
     * @param assigneeDropdown - the selector
     * @param root             - the bottom level of the locator. this is the page the element is located on eg. can be in a modal dialog
     * @param assignee     - the locator value
     * @return current page object
     */
    private void typeAheadSelectAssignee(WebElement assigneeDropdown, String root, String assignee) {
        if (!pageUtils.waitForElementToAppear(By.xpath(String.format("//div[@id='%s']//div[@class]", root))).getAttribute("textContent").equals(assignee)) {
            pageUtils.waitForElementAndClick(assigneeDropdown);
            pageUtils.waitForElementToAppear(By.cssSelector("[id='qa-publish-form-assigned-to-select'] .apriori-select input")).sendKeys(assignee.split(" ")[0]);
            pageUtils.waitForElementAndClick(By.xpath(String.format("//div[@id='%s']//div[.='%s']//div[@id]", root, assignee)));
        }
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
     * Gets assembly association alert
     *
     * @return string
     */
    public String getAssociationAlert() {
        return pageUtils.waitForElementToAppear(alertMessage).getText();
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
        pageUtils.clearValueOfElement(scenarioNameInput);
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
     * @param <T>              - the object type
     * @return generic page object
     */
    public <T> T publish(ComponentInfoBuilder cidComponentItem, Class<T> klass) {
        modalDialogController.publish(klass);
        pageUtils.waitForElementsToNotAppear(By.cssSelector("div[data-testid='loader']"));
        new ScenariosUtil().getScenarioActioned(cidComponentItem, "PUBLISH", true);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the publish button
     *
     * @param <T> - the object type
     * @return generic page object
     */
    public <T> T publish(Class<T> klass) {
        modalDialogController.publish(klass);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the continue button
     *
     * @return generic page object
     */
    public <T> T clickContinue(Class<T> klass) {
        return modalDialogController.clickContinue(klass);
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

    /**
     * Close
     *
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }

    /**
     * Get publishing message
     *
     * @return string
     */
    public String getPublishingMessage() {
        return pageUtils.waitForElementToAppear(publishingMessage).getAttribute("textContent");
    }
}
