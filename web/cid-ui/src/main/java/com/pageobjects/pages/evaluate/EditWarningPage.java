package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

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

public class EditWarningPage extends LoadableComponent<EditWarningPage> {

    private static final Logger logger = LoggerFactory.getLogger(EditWarningPage.class);

    @FindBy(css = "div[data-ap-comp='editScenarioConflict'] .modal-content")
    private WebElement dialog;

    @FindBy(css = "input[data-ap-comp='overwrite']")
    private WebElement overwriteRadioButton;

    @FindBy(css = "input[data-ap-comp='saveAsNew']")
    private WebElement scenarioRadioButton;

    @FindBy(css = "input[data-ap-field='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(css = "div[data-ap-comp='editScenarioConflict'] button.btn.btn-primary")
    private WebElement continueButton;

    @FindBy(css = "div[data-ap-comp='editScenarioConflict'] button.btn.btn-default")
    private WebElement cancelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public EditWarningPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(dialog);
    }

    /**
     * Selects overwrite radio button
     *
     * @return current page object
     */
    public EditWarningPage selectOverwrite() {
        pageUtils.waitForElementAndClick(overwriteRadioButton);
        return this;
    }

    /**
     * Selects save as new scenario radio button
     *
     * @return current page object
     */
    public EditWarningPage selectSaveAsNew() {
        pageUtils.waitForElementAndClick(scenarioRadioButton);
        return this;
    }

    /**
     * Enters new scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public EditWarningPage inputNewScenarioName(String scenarioName) {
        scenarioNameInput.clear();
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the continue button
     *
     * @return new page object
     */
    public <T> T selectContinue(Class<T> klass) {
        pageUtils.waitForElementAndClick(continueButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage cancel() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }
}