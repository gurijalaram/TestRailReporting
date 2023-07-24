package com.apriori.pageobjects.pages.settings;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class SettingsNavigation {

    @FindBy(xpath = "//button[.='Tolerance Defaults']")
    private WebElement toleranceTab;

    @FindBy(xpath = "//button[.='Assembly Defaults']")
    private WebElement assemblyDefaultsTab;

    @FindBy(xpath = "//button[.='Production Defaults']")
    private WebElement productionsTab;

    @FindBy(xpath = "//button[.='Selection']")
    private WebElement selectionTab;

    @FindBy(xpath = "//button[.='Display Preferences']")
    private WebElement displayTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public SettingsNavigation(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Go to display tab
     *
     * @return new page object
     */
    public DisplayPreferencesPage goToDisplayTab() {
        pageUtils.waitForElementAndClick(displayTab);
        return new DisplayPreferencesPage(driver);
    }

    /**
     * Go to production default tab
     *
     * @return new page object
     */
    public ProductionDefaultsPage goToProductionTab() {
        pageUtils.waitForElementAndClick(productionsTab);
        return new ProductionDefaultsPage(driver);
    }

    /**
     * Go to selection tab
     *
     * @return new page object
     */
    public SelectionPage goToSelectionTab() {
        pageUtils.waitForElementAndClick(selectionTab);
        return new SelectionPage(driver);
    }

    /**
     * Go to Multi-Body tab
     *
     * @return new page object
     */
    public AssemblyDefaultsPage goToAssemblyDefaultsTab() {
        pageUtils.waitForElementAndClick(assemblyDefaultsTab);
        return new AssemblyDefaultsPage(driver);
    }

    /**
     * Go to tolerances default tab
     *
     * @return new page object
     */
    public ToleranceDefaultsPage goToToleranceTab() {
        pageUtils.waitForElementAndClick(toleranceTab);
        return new ToleranceDefaultsPage(driver);
    }
}
