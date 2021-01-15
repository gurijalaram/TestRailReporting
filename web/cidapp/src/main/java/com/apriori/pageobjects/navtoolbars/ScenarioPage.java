package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioPage extends LoadableComponent<ScenarioPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(ScenarioPage.class);

    @FindBy(css = ".modal-title")
    private WebElement dialogTitle;

    @FindBy(xpath = "//label[contains(text(),'Create')]/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement scenarioDropdown;

    @FindBy(css = "input[name='scenarioName']")
    private WebElement scenarioNameInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ScenarioPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(dialogTitle);
        pageUtils.waitForElementAppear(scenarioNameInput);
    }

    /**
     * Enter scenario name
     *
     * @param scenarioName - the scenario name
     * @return current page object
     */
    public ScenarioPage enterScenarioName(String scenarioName) {
        scenarioNameInput.clear();
        scenarioNameInput.sendKeys(scenarioName);
        return this;
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}
