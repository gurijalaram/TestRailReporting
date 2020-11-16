package com.pageobjects.pages.evaluate.materialutilization;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.materialutilization.stock.StockPage;
import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class MaterialUtilizationPage extends EvaluatePanelToolbar {

    private final Logger logger = LoggerFactory.getLogger(MaterialUtilizationPage.class);

    @FindBy(css = ".panel.panel-details")
    private WebElement panelDetails;

    @FindBy(css = "div[data-ap-comp='materialDetails']")
    private WebElement materialTable;

    @FindBy(css = "a[href='#materialDetailsTab']")
    private WebElement materialUtilizationTab;

    @FindBy(css = "a[href='#stockDetailsTab']")
    private WebElement stockTab;

    @FindBy(css = "li[id='partNestingTabBookmark']")
    private WebElement partNestingTab;

    @FindBy(css = "[data-ap-comp=materialProperties]")
    private WebElement materialPropertiesTree;

    @FindBy(css = "input[data-ap-field='materialUnitCostFlag']")
    private WebElement basicInfoCheckbox;

    @FindBy(css = "input[data-ap-field='materialUnitCostOverride']")
    private WebElement basicInfoInput;

    @FindBy(css = "input[data-ap-field='utilizationFlag']")
    private WebElement utilizationCheckBox;

    @FindBy(css = "input[data-ap-field='utilizationOverride']")
    private WebElement utilizationInput;

    @FindBy(css = "div[id='materialTable']")
    private WebElement materialTableInfo;

    @FindBy(xpath = "//span[contains(text(), 'Utilization')]/..")
    private WebElement utilizationsTabToggle;

    @FindBy(xpath = "//span[contains(text(), 'Material Properties')]/..")
    private WebElement materialPropertiesTabToggle;

    private WebDriver driver;
    private PageUtils pageUtils;

    public MaterialUtilizationPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(basicInfoCheckbox);
        pageUtils.waitForElementToAppear(panelDetails);
    }

    /**
     * Opens the material & utilization tab
     *
     * @return new page object
     */
    public MaterialUtilizationPage goToMaterialUtilizationTab() {
        expandPanel();
        materialUtilizationTab.click();
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Opens the stock tab
     *
     * @return new page object
     */
    public StockPage goToStockTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(stockTab);
        return new StockPage(driver);
    }

    /**
     * Opens the part nesting tab
     *
     * @return new page object
     */
    public PartNestingPage goToPartNestingTab() {
        expandPanel();
        pageUtils.waitForElementAndClick(partNestingTab);
        return new PartNestingPage(driver);
    }

    /**
     * Gets material information
     * The fully qualified name of the property must be entered as the locator looks for an exact match eg. "(kg / m^3)"
     *
     * @param info
     * @return string
     */
    public String getMaterialInfo(String info) {
        expandPanel();
        pageUtils.waitForElementToAppear(materialPropertiesTree);
        By propertiesInfo = By.xpath("//table[@class='table material-grid'] //td[.='" + info + "']/following-sibling::td");
        return driver.findElement(propertiesInfo).getAttribute("innerText");
    }

    /**
     * Gets the button as a webelement
     *
     * @return the button as webelement
     */
    public String getPartNestingButton() {
        pageUtils.waitForElementToAppear(By.xpath("//td[.='Name']"));
        return partNestingTab.getAttribute("class");
    }

    /**
     * Selects info check box
     *
     * @return current page object
     */
    public MaterialUtilizationPage selectInfoCheckBox() {
        basicInfoCheckbox.click();
        return this;
    }

    /**
     * Enter basic override value
     *
     * @param value - the value as string
     * @return current page object
     */
    public MaterialUtilizationPage enterBasicOverride(String value) {
        pageUtils.waitForElementToBeClickable(basicInfoInput);
        pageUtils.clearInput(basicInfoInput);
        basicInfoInput.sendKeys(value);
        return this;
    }

    /**
     * Select utilization check box
     *
     * @return current page object
     */
    public MaterialUtilizationPage selectUtilizationCheckBox() {
        utilizationCheckBox.click();
        return this;
    }

    /**
     * Enter utilization override value
     *
     * @param value - the value as string
     * @return current page object
     */
    public MaterialUtilizationPage enterUtilizationOverride(String value) {
        pageUtils.waitForElementToBeClickable(utilizationInput);
        pageUtils.clearInput(utilizationInput);
        utilizationInput.sendKeys(value);
        return this;
    }

    /**
     * Gets the info in the material table
     *
     * @return material table info as string
     */
    public String getMaterialTableInfo() {
        return pageUtils.waitForElementToAppear(materialTableInfo).getText();
    }

    /**
     * Gets string value which states if utilization panel is expanded or not
     *
     * @return string of utilization panel class name
     */
    public String utilizationPanelExpanded() {
        return utilizationsTabToggle.getAttribute("className");
    }

    /**
     * Toggles Utilization panel (open or closed)
     *
     * @return current page object
     */
    public MaterialUtilizationPage toggleUtilizationPanel() {
        utilizationsTabToggle.click();
        return this;
    }

    /**
     * Gets string value which states if material panel is expanded or not
     *
     * @return string of material properties panel class name
     */
    public String materialPanelExpanded() {
        return materialPropertiesTabToggle.getAttribute("className");
    }

    /**
     * Toggles Material Properties panel (open or closed)
     *
     * @return current page object
     */
    public MaterialUtilizationPage toggleMaterialPropertiesPanel() {
        materialPropertiesTabToggle.click();
        return this;
    }
}