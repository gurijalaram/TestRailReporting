package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
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

    @FindBy(xpath = "//div[@class='modal-content']//button[@class='btn btn-primary'][.='Submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Publish']")
    private WebElement publishButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Back']")
    private WebElement backButton;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Save']")
    private WebElement saveButton;

    @FindBy(css = "button[aria-label='Close']")
    private WebElement closePanel;

    @FindBy(xpath = "//div[@class='modal-content']//button[.='Cost']")
    private WebElement costButton;

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
    public <T> T submit(Class<T> klass) {
        pageUtils.waitForElementAndClick(submitButton);
        return PageFactory.initElements(driver, klass);
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
    public <T> T continues(Class<T> klass) {
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
        pageUtils.waitForElementAppear(costButton);
        return PageFactory.initElements(driver, klass);
    }
}
