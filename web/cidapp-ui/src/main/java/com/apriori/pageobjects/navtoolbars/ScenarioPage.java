package com.apriori.pageobjects.navtoolbars;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class ScenarioPage extends LoadableComponent<ScenarioPage> {

    @FindBy(css = "[role='dialog'] .dialog-title")
    private WebElement dialogTitle;

    @FindBy(xpath = "//label[contains(text(),'Create')]/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement scenarioDropdown;

    @FindBy(css = "input[name='scenarioName']")
    private WebElement scenarioNameInput;

    @FindBy(xpath = "//form //button[.='Submit']")
    private WebElement submitButton;

    @FindBy(css = ".invalid-feedback-for-scenario-name")
    private WebElement invalidScenarioNameMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ScenarioPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(dialogTitle);
        pageUtils.waitForElementToAppear(scenarioNameInput);
    }

    /**
     * Enter scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ScenarioPage enterScenarioName(String scenarioName) {
        pageUtils.clearValueOfElement(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(submitButton, klass);
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
     * Gets text in error form
     *
     * @return string
     */
    public String getErrorMessage() {
        return pageUtils.waitForElementToAppear(invalidScenarioNameMessage).getText();
    }
}
