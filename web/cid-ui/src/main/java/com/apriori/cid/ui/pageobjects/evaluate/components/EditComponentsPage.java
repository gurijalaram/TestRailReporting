package com.apriori.cid.ui.pageobjects.evaluate.components;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cid.ui.pageobjects.common.ModalDialogController;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class EditComponentsPage extends LoadableComponent<EditComponentsPage> {

    @FindBy(css = "[role='dialog']")
    private WebElement conflictsForm;

    @FindBy(css = "[data-testid='apriori-alert'] [data-testid='alert-messaging']")
    private WebElement conflictsFormMessage;

    @FindBy(css = "input[value='override']")
    private WebElement overrideButton;

    @FindBy(css = "input[value='changeName']")
    private WebElement renameButton;

    @FindBy(css = "input[name='scenarioName']")
    private WebElement scenarioNameInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public EditComponentsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
        //Don't really need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(pageUtils.waitForElementToAppear(conflictsForm).isDisplayed(), "Edit Scenarios dialog is not displayed");
    }

    /**
     * Gets text in conflicts form
     *
     * @return string
     */
    public String getConflictForm() {
        return pageUtils.waitForElementToAppear(conflictsFormMessage).getAttribute("textContent");
    }

    /**
     * Selects override scenario button
     *
     * @return current page object
     */
    public EditComponentsPage overrideScenarios() {
        pageUtils.waitForElementAndClick(overrideButton);
        return this;
    }

    /**
     * Selects rename scenario radio button
     *
     * @return current page object
     */
    public EditComponentsPage renameScenarios() {
        pageUtils.waitForElementAndClick(renameButton);
        return this;
    }

    /**
     * Inputs new scenario name
     *
     * @param scenarioName
     * @return current page object
     */
    public EditComponentsPage enterScenarioName(String scenarioName) {
        pageUtils.clearValueOfElement(scenarioNameInput);
        scenarioNameInput.sendKeys(scenarioName);
        return this;
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
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T clickCancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
