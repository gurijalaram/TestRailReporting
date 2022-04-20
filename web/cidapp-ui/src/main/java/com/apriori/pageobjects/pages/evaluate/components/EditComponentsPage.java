package com.apriori.pageobjects.pages.evaluate.components;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class EditComponentsPage extends LoadableComponent<EditComponentsPage> {

    @FindBy(css = ".scenario-conflicts-form")
    private WebElement conflictsForm;

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
        assertTrue("Edit Scenario dialog is not displayed", conflictsForm.isDisplayed());
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
     * Close
     *
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        return modalDialogController.close(klass);
    }

    /**
     * Select the continue button
     *
     * @return generic page object
     */
    public <T> T modalContinue(Class<T> klass) {
        return modalDialogController.modalContinue(klass);
    }
}
