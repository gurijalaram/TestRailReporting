package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
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

public class MaterialProcessPage extends LoadableComponent<MaterialProcessPage> {

    private static final Logger logger = LoggerFactory.getLogger(MaterialProcessPage.class);

    @FindBy(css = ".tab.active [data-icon='clock']")
    private WebElement processesTabActive;

    @FindBy(css = "div[dir='ltr']")
    private WebElement chartContainer;

    @FindBy(css = "[id='qa-process-totals-section'] div.left")
    private WebElement totalChevron;

    @FindBy(css = "[id='qa-process-details-section'] div.left")
    private WebElement processDetailsChevron;

    @FindBy(css = "[id='qa-process-options-section'] div.left")
    private WebElement optionsChevron;

    @FindBy(xpath = "//button[.='Material Utilization']")
    private WebElement materialUtilizationTab;

    @FindBy(xpath = "//button[.='Process']")
    private WebElement processResultTab;

    @FindBy(xpath = "//button[.='Options']")
    private WebElement optionsTab;

    @FindBy(xpath = "//button[.='Stock']")
    private WebElement stockTab;

    @FindBy(xpath = "//button[.='Part Nesting']")
    private WebElement partNestingTab;

    @FindBy(css = ".process-routing-chart-column .apriori-select")
    private WebElement processDropdown;

    @FindBy(css = "[value='defaultBatchSetupTime']")
    private WebElement defaultValueButton;

    @FindBy(css = "[value='userDefinedBatchSetupTime']")
    private WebElement overrideButton;

    @FindBy(css = ".process-setup-option-form-group [type='number']")
    private WebElement overrideInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public MaterialProcessPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(processesTabActive);
        pageUtils.waitForElementAppear(chartContainer);
    }

    /**
     * Selects the process dropdown
     *
     * @param filter - the filter
     * @return current page object
     */
    public MaterialProcessPage selectDropdown(String filter) {
        pageUtils.typeAheadSelect(processDropdown, filter);
        return this;
    }

    /**
     * Gets total result
     *
     * @param label - the label
     * @return double
     */
    public double getTotalResult(String label) {
        By costResult = getBy(label);
        return Double.parseDouble(pageUtils.waitForElementToAppear(costResult).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Selects the process tab
     *
     * @return current page object
     */
    public MaterialProcessPage selectProcessTab() {
        pageUtils.waitForElementAndClick(processResultTab);
        return this;
    }

    /**
     * Selects the options tab
     *
     * @return current page object
     */
    public MaterialProcessPage selectOptionsTab() {
        pageUtils.waitForElementAndClick(optionsTab);
        return this;
    }

    /**
     * Get process result
     *
     * @param label - the label
     * @return string
     */
    public String getProcessResult(String label) {
        By processResult = getBy(label);
        return pageUtils.waitForElementToAppear(processResult).getAttribute("textContent");
    }

    /**
     * Gets by label
     *
     * @param label - the label
     * @return by
     */
    private By getBy(String label) {
        return By.xpath(String.format("//span[.='%s']/following-sibling::span[@class='property-value']", label));
    }

    /**
     * Opens material utilization tab
     *
     * @return new page object
     */
    public MaterialUtilizationPage openMaterialUtilizationTab() {
        pageUtils.waitForElementAndClick(materialUtilizationTab);
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Go to stock tab
     *
     * @return new page object
     */
    public StockPage goToStockTab() {
        pageUtils.waitForElementAndClick(stockTab);
        return new StockPage(driver);
    }

    /**
     * Selects default value radio button
     *
     * @return current page object
     */
    public MaterialProcessPage selectDefaultValue() {
        pageUtils.waitForElementAndClick(defaultValueButton);
        return this;
    }

    /**
     * Selects override radio button
     *
     * @return current page object
     */
    public MaterialProcessPage selectOverride() {
        pageUtils.waitForElementAndClick(overrideButton);
        return this;
    }

    /**
     * Inputs override value
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputOverride(String value) {
        pageUtils.clearInput(overrideInput);
        overrideInput.sendKeys(value);
        return this;
    }

    /**
     * Go to Part Nesting tab
     *
     * @return new page object
     */
    public PartNestingPage openPartNestingTab() {
        pageUtils.waitForElementAndClick(partNestingTab);
        return new PartNestingPage(driver);
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }
}
