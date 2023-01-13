package com.apriori.pageobjects.pages.settings;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class MultiBodyPage extends LoadableComponent<MultiBodyPage> {

    @FindBy(css = ".user-preferences [type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[.='Multi-Body']")
    private WebElement multiBodyTab;

    @FindBy(css = ".multi-body-preferences")
    private WebElement multiBody;

    @FindBy(css = "[id='qa-assembly-association-strategy-preset'] .apriori-select")
    private WebElement assemblyStrategyDropdown;

    @FindBy(css = "[id='qa-assembly-association-strategy-preset'] .apriori-select [data-testid='text-overflow']")
    private WebElement assemblyStrategyDropdownValue;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;
    private SettingsNavigation settingsNavigation;

    public MultiBodyPage(WebDriver driver) {
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
        assertTrue("Multi-Body tab is not active", multiBodyTab.getAttribute("class").contains("active"));
        assertTrue("Multi-Body is not displayed", multiBody.isDisplayed());
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
    public Boolean isAssemblyStrategyDropdownVisible() { return assemblyStrategyDropdown.isDisplayed(); }

    /**
     * Select value from Assembly Strategy Dropdown
     *
     * @return This Page Object
     */
    public MultiBodyPage selectAssemblyStrategy(String strategy) {

        pageUtils.modalTypeAheadSelect(assemblyStrategyDropdown, "Assembly Strategy", strategy);
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
