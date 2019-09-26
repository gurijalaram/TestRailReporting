package com.apriori.pageobjects.pages.evaluate.designguidance.investigation;

import com.apriori.pageobjects.utils.PageUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ThreadingPage extends LoadableComponent<ThreadingPage> {

    private final Logger logger = LoggerFactory.getLogger(ThreadingPage.class);

    @FindBy(css = "div[data-ap-comp='threadEditor'] .modal-content")
    private WebElement threadDialog;

    @FindBy(css = "label[data-ap-field='cadThreaded']")
    private WebElement threadCADText;

    @FindBy(css = "select[data-ap-field='threadGcd']")
    private WebElement threadDropdown;

    @FindBy(css = "input[data-ap-field='threadLength']")
    private WebElement lengthInput;

    @FindBy(css = "div[data-ap-comp='threadEditor'] button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "div[data-ap-comp='threadEditor'] button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ThreadingPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(threadDialog);
        pageUtils.waitForElementToAppear(threadDropdown);
    }

    /**
     * Selects the thread dropdown option
     *
     * @param option - the option
     * @return current page object
     */
    public ThreadingPage selectThreadDropdown(String option) {
        pageUtils.selectDropdownOption(threadDropdown, option);
        return this;
    }

    /**
     * Enters the thread length
     *
     * @param length - the thread length
     * @return current page object
     */
    public ThreadingPage enterThreadLength(String length) {
        pageUtils.waitForElementToAppear(lengthInput).clear();
        lengthInput.sendKeys(length);
        lengthInput.sendKeys(Keys.TAB);
        return this;
    }

    /**
     * Removes the thread length
     *
     * @return current page object
     */
    public ThreadingPage removeThreadLength() {
        pageUtils.waitForElementToAppear(lengthInput).click();
        lengthInput.clear();
        return this;
    }

    /**
     * Gets the thread length
     *
     * @return - the thread length
     */
    public Boolean getThreadLength(String text) {
        return pageUtils.checkElementAttribute(lengthInput, "value", text);
    }

    /**
     * Selects the apply button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T apply(Class<T> className) {
        pageUtils.javaScriptClick(applyButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public InvestigationPage cancel() {
        cancelButton.click();
        return new InvestigationPage(driver);
    }
}