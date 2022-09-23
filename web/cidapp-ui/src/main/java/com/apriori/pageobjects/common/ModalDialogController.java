package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ModalDialogController {

    @FindBy(id = "secondary-process-select-all-btn")
    private WebElement selectAllButton;

    @FindBy(id = "secondary-process-clear-all-btn")
    private WebElement deselectAllButton;

    @FindBy(id = "secondary-process-reset-btn")
    private WebElement resetButton;

    @FindBy(id = "secondary-process-reset-btn")
    private WebElement clearAllButton;

    @FindBy(css = "button[title='Expand all']")
    private WebElement expandAllButton;

    @FindBy(css = "button[title='Collapse all']")
    private WebElement collapseAllButton;

    @FindBy(xpath = "//div[@role='dialog']//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[@id='styled-routings-list']/following-sibling::div//button[.='Cancel']")
    private WebElement cancel;

    @FindBy(xpath = "//form //button[.='Try Again']")
    private WebElement tryAgainButton;

    @FindBy(xpath = "//form //button[.='Publish']")
    private WebElement publishButton;

    @FindBy(xpath = "//form //button[.='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//form //button[.='Back']")
    private WebElement backButton;

    @FindBy(xpath = "//form //button[.='Save']")
    private WebElement saveButton;

    @FindBy(css = "button[aria-label='Close']")
    private WebElement closePanel;

    @FindBy(xpath = "//form //button[.='Cost']")
    private WebElement costButton;

    @FindBy(xpath = "//form //button[.='Apply & Cost']")
    private WebElement applyCostButton;

    @FindBy(xpath = "//div[@class='content']//button[.='Back']")
    private WebElement backResourceButton;

    @FindBy(xpath = "//form //button[.='Close']")
    private WebElement closeButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ModalDialogController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(WebElement button, Class<T> klass) {
        pageUtils.waitForElementAndClick(button);
        pageUtils.waitForElementsToNotAppear(By.xpath("//h5[.='Preferences']"));
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Selects the submit button
     *
     * @return current page object
     */
    public ModalDialogController submit(WebElement button) {
        pageUtils.waitForElementAndClick(button);
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancelButton(Class<T> klass) {
        pageUtils.waitForElementAndClick(cancel);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T submitButton(WebElement button, Class<T> klass) {
        pageUtils.waitForElementAndClick(button);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the try again button
     *
     * @return generic page object
     */
    public <T> T tryAgain(Class<T> klass) {
        pageUtils.waitForElementAndClick(tryAgainButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the publish button
     *
     * @return generic page object
     */
    public <T> T publish(Class<T> klass) {
        pageUtils.waitForElementAndClick(publishButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the continue button
     *
     * @return generic page object
     */
    public <T> T clickContinue(Class<T> klass) {
        pageUtils.waitForElementAndClick(continueButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select the back button
     *
     * @return generic page object
     */
    public ModalDialogController back() {
        pageUtils.waitForElementAndClick(backButton);
        return this;
    }

    /**
     * Select the save button
     *
     * @return generic page object
     */
    public <T> T save(Class<T> klass) {
        pageUtils.waitForElementAndClick(saveButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Select all
     *
     * @return current page object
     */
    public ModalDialogController selectAll() {
        pageUtils.waitForElementAndClick(selectAllButton);
        return this;
    }

    /**
     * Deselect all
     *
     * @return current page object
     */
    public ModalDialogController deselectAll() {
        pageUtils.waitForElementAndClick(deselectAllButton);
        return this;
    }

    /**
     * Reset
     *
     * @return current page object
     */
    public ModalDialogController reset() {
        pageUtils.waitForElementAndClick(resetButton);
        return this;
    }

    /**
     * Expand all
     *
     * @return current page object
     */
    public ModalDialogController expandAll() {
        pageUtils.waitForElementAndClick(expandAllButton);
        return this;
    }

    /**
     * Collapse all
     *
     * @return current page object
     */
    public ModalDialogController collapseAll() {
        pageUtils.waitForElementAndClick(collapseAllButton);
        return this;
    }

    /**
     * Cost
     *
     * @return current page object
     */
    public <T> T cost(Class<T> klass) {
        pageUtils.waitForElementAndClick(costButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Apply and Cost
     *
     * @return current page object
     */
    public <T> T applyCost(Class<T> klass) {
        pageUtils.waitForElementAndClick(applyCostButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Close
     *
     * @return current page object
     */
    public <T> T close(Class<T> klass) {
        pageUtils.waitForElementAndClick(closeButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Gets error message about element was not found
     *
     * @return text of error message
     */
    public String getNotFoundMessage() {
        By message = By.cssSelector("span.message");
        pageUtils.waitForElementToAppear(message);
        return driver.findElement(message).getText();
    }

    /**
     * Clicks on Back button
     *
     * @return generic page object
     */
    public <T> T backFromError(Class<T> className) {
        pageUtils.waitForElementAndClick(backResourceButton);
        return PageFactory.initElements(driver, className);
    }
}
