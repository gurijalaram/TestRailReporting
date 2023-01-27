package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class AssemblyDefaultsPage extends LoadableComponent<AssemblyDefaultsPage> {

    @FindBy(css = ".user-preferences [type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[.='Assembly Defaults']")
    private WebElement assemblyDefaultTab;

    @FindBy(css = ".assembly-default-preferences")
    private WebElement assemblyDefault;

    @FindBy(css = "[id='qa-assembly-association-strategy-preset'] .apriori-select")
    private WebElement assemblyStrategyDropdown;

    @FindBy(css = "[id='qa-assembly-association-strategy-preset'] .apriori-select [data-testid='text-overflow']")
    private WebElement assemblyStrategyDropdownValue;

    @FindBy(css = "p[data-testid='summary-message']")
    private WebElement usingDefaultMessage;

    @FindBy(css = "div[data-testid='step-cards'] div")
    private WebElement assemblyStrategyCards;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SettingsNavigation settingsNavigation;

    public AssemblyDefaultsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.settingsNavigation = new SettingsNavigation(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Multi-Body tab is not active", assemblyDefaultTab.getAttribute("class").contains("active"));
        assertTrue("Multi-Body is not displayed", assemblyDefault.isDisplayed());
    }

    /**
     * Go to tolerances default tab
     *
     * @return new page object
     */
    public ToleranceDefaultsPage goToToleranceTab() {
        return settingsNavigation.goToToleranceTab();
    }

    /**
     * Go to display default tab
     *
     * @return new page object
     */
    public DisplayPreferencesPage goToDisplayTab() {
        return settingsNavigation.goToDisplayTab();
    }

    /**
     * Go to selection tab
     *
     * @return new page object
     */
    public SelectionPage goToSelectionTab() {
        return settingsNavigation.goToSelectionTab();
    }

    /**
     * Get visibility state of Assembly Strategy Dropdown
     *
     * @return Boolean of visibility
     */
    public Boolean isAssemblyStrategyDropdownVisible() {
        return assemblyStrategyDropdown.isDisplayed();
    }

    /**
     * Select value from Assembly Strategy Dropdown
     *
     * @return This Page Object
     */
    public AssemblyDefaultsPage selectAssemblyStrategy(String strategy) {

        pageUtils.modalTypeAheadSelect(assemblyStrategyDropdown, "Assembly Association Strategy ", strategy);
        return this;
    }

    /**
     * Get currently selected Assembly Strategy
     *
     * @return String of currently selected strategy
     */
    public String getCurrentAsmStrategy() {
        return pageUtils.waitForElementToAppear(assemblyStrategyDropdownValue).getAttribute("textContent");
    }

    /**
     * Get visibility of using defaults message
     *
     * @return - Boolean of visibility state
     */
    public Boolean isUsingDefaultsMessageVisible() {
        return usingDefaultMessage.isDisplayed();
    }

    /**
     * Get message to user if no strategy selected
     *
     * @return - The use default message
     */
    public String getUsingDefaultsMessage() {
        return usingDefaultMessage.getText();
    }

    /**
     * Get number of strategy cards currently displayed
     *
     * @return - Number of cards displayed
     */
    public Integer getAsmStrategyCardCount() {
        By locator = By.cssSelector("div[data-testid='step-cards'] div");
        return driver.findElements(locator).size();
    }

    /**
     * Get workspace from specified card
     *
     * @param cardNum - The card to retrieve data from (1-index)
     * @return - The workspace text of the specified card
     */
    public String getAsmStrategyCardWorkspace(Integer cardNum) {
        WebElement cardWorkspace = driver.findElement(By.cssSelector("div[data-testid='step-cards'] div:nth-of-type(" + cardNum + ") p:nth-of-type(1)"));
        return cardWorkspace.getText();
    }

    /**
     * Get criteria from specified card
     *
     * @param cardNum - The card to retrieve data from (1-index)
     * @return - The workspace text of the specified card
     */
    public String getAsmStrategyCardCriteria(Integer cardNum) {
        WebElement cardWorkspace = driver.findElement(By.cssSelector("div[data-testid='step-cards'] div:nth-of-type(" + cardNum + ") p:nth-of-type(2)"));
        return cardWorkspace.getText();
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

}
