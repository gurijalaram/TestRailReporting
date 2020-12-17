package com.pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishWarningPage extends LoadableComponent<PublishWarningPage> {

    private final Logger logger = LoggerFactory.getLogger(PublishWarningPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(css = "input[data-ap-field='scenarioName']")
    private WebElement inputField;

    @FindBy(css = "button.btn-primary")
    private WebElement continueButton;

    @FindBy(css = "button.gwt-Button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "[data-ap-comp='overwrite']")
    private WebElement overwriteButton;

    @FindBy(css = "[data-ap-comp='saveAsNew']")
    private WebElement newScenarioButton;

    @FindBy(css = "div.panel-body div.gwt-Label")
    private WebElement cannotPublishDuePrivateScenarioText;

    @FindBy(xpath = "//span[@class='warning-icon']/..")
    private WebElement cannotPublishDueLockedComparisonText;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PublishWarningPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(dialogTitle);
    }

    /**
     * Enter scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public PublishWarningPage enterNewScenarioName(String scenarioName) {
        pageUtils.waitForElementToAppear(dialogTitle);
        pageUtils.waitForElementAndClick(inputField);
        inputField.clear();
        inputField.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the publish button
     *
     * @return new page object
     */
    public PublishPage selectContinueButton() {
        pageUtils.waitForElementAndClick(continueButton);
        return new PublishPage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public EvaluatePage selectCancelButton() {
        cancelButton.click();
        return new EvaluatePage(driver);
    }

    /**
     * Selects the overwrite button
     *
     * @return current page object
     */
    public PublishWarningPage selectOverwriteOption() {
        pageUtils.waitForElementAndClick(overwriteButton);
        return this;
    }

    /**
     * Selects the Publish as new scenario option
     *
     * @return current page object
     */
    public PublishWarningPage selectPublishAsNew() {
        pageUtils.waitForElementAndClick(newScenarioButton);
        return this;
    }

    /**
     * Gets a message about comparison is locked
     *
     * @return the text as String
     */
    public String getLockedComparisonText() {
        return pageUtils.waitForElementToAppear(cannotPublishDueLockedComparisonText).getText();
    }

    /**
     * Gets a message about scenario is private
     *
     * @return the text as String
     */
    public String getPrivateScenarioText() {
        return pageUtils.waitForElementToAppear(cannotPublishDuePrivateScenarioText).getText();
    }
}
