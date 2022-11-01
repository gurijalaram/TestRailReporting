package com.apriori.pageobjects.pages.evaluate.materialprocess;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.inputs.PsoController;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author cfrith
 */

@Slf4j
public class MaterialProcessPage extends LoadableComponent<MaterialProcessPage> {

    @FindBy(xpath = "//button[.='Processes']")
    private WebElement processesTab;

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

    @FindBy(css = ".highcharts-xaxis-labels text")
    private List<WebElement> xAxisLabel;

    @FindBy(css = ".highcharts-series-group rect")
    private List<WebElement> chart;

    @FindBy(css = "g.highcharts-label.highcharts-data-label")
    private List<WebElement> chartPercentage;

    @FindBy(xpath = "//h6[contains(.,'Masking']/..//input[@value='defaultNoMasking']")
    private WebElement maskingDefault;

    @FindBy(xpath = "//h6[contains(.,'Number of Masked Features']/..//input[@value='none']")
    private WebElement noMasking;

    @FindBy(xpath = "//h6[contains(.,'Number of Components Per Load Bar']/..//input[@value='auto']")
    private WebElement compLoadBarDefault;

    @FindBy(xpath = "//h6[contains(.,'Number of Cavities')]/..//input[@value='optimize']")
    private WebElement optimizeMinCost;

    @FindBy(css = "[value='colorantAdded']")
    private WebElement addColorantButton;

    @FindBy(xpath = "//h6[starts-with(., 'Number of cavities  (Piece Part & Tooling Cost Driver)')]/..//input[@value='optimize']")
    private WebElement cavitiesOptimizeMinCost;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private PsoController psoController;
    private String root = "root";

    public MaterialProcessPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.psoController = new PsoController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Processes tab was not selected", processesTab.getAttribute("class").contains("active"));
        pageUtils.waitForElementToAppear(chartContainer);
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
     * Opens material utilization tab
     *
     * @return new page object
     */
    public MaterialUtilizationPage openMaterialUtilizationTab() {
        pageUtils.waitForElementAndClick(materialUtilizationTab);
        return new MaterialUtilizationPage(driver);
    }

    /**
     * Checks if tab is displayed
     *
     * @return true/false
     */
    public boolean isPartNestingTabDisplayed() {
        By byTab = By.xpath("//button[.='Part Nesting']");
        return pageUtils.isElementPresent(byTab);
    }

    /**
     * Selects the process dropdown
     *
     * @param filter - the filter
     * @return current page object
     */
    public MaterialProcessPage selectDropdown(String filter) {
        pageUtils.optionsTypeAheadSelect(processDropdown, root, filter);
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
     * Go to stock tab
     *
     * @return new page object
     */
    public StockPage openStockTab() {
        pageUtils.waitForElementAndClick(stockTab);
        return new StockPage(driver);
    }

    /**
     * Selects the bar chart
     *
     * @param axisLabel - the label on the x axis
     * @return current page object
     */
    public MaterialProcessPage selectBarChart(String axisLabel) {
        pageUtils.waitForElementToAppear(By.cssSelector(".highcharts-data-labels"));
        int position = IntStream.range(0, xAxisLabel.size()).filter(x -> xAxisLabel.get(x).getText().equals(axisLabel)).findFirst().getAsInt();
        pageUtils.actionClick(chart.get(position));
        return this;
    }

    /**
     * Gets the process percentage
     *
     * @param axisLabel - the label on the x axis
     * @return list string
     */
    public List<String> getProcessPercentage(String axisLabel) {
        int position = IntStream.range(0, xAxisLabel.size()).filter(x -> xAxisLabel.get(x).getText().equals(axisLabel)).findFirst().getAsInt();
        return chartPercentage.stream().map(x -> pageUtils.waitForElementToAppear(chartPercentage.get(position)).getAttribute("textContent")).collect(Collectors.toList());
    }

    /**
     * Selects material default
     *
     * @return current page object
     */
    public MaterialProcessPage selectAverageWallThickness() {
        pageUtils.waitForElementAndClick(psoController.buildLocator("Average Wall Thickness", "default"));
        return this;
    }

    /**
     * Input material override
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputAverageWallThickness(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Average Wall Thickness", "user"), psoController.inputLocator("Average Wall Thickness"), value);
        return this;
    }

    /**
     * Gets average wall thickness
     *
     * @return string
     */
    public double getAverageWallThickness() {
        return Double.parseDouble(psoController.inputLocator("Average Wall Thickness").getAttribute("value"));
    }

    /**
     * Select case depth
     *
     * @return current page object
     */
    public MaterialProcessPage selectCaseDepth() {
        pageUtils.waitForElementAndClick(psoController.buildLocator("Case Depth Selection", "default"));
        return this;
    }

    /**
     * Inputs case depth
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputCaseDepth(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Case Depth Selection", "userOverride"), psoController.inputLocator("Case Depth Selection"), value);
        return this;
    }

    /**
     * Gets value for overridden PSO
     *
     * @param pso - the pso
     * @return double
     */
    public double getOverriddenPso(String pso) {
        return psoController.getOverriddenPso(pso);
    }

    /**
     * Select masking
     *
     * @return current page object
     */
    public MaterialProcessPage selectMasking() {
        pageUtils.waitForElementAndClick(maskingDefault);
        return this;
    }

    /**
     * Inputs masking
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputMasking(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Masking", "userOverride"), psoController.inputLocator("Masking"), value);
        return this;
    }

    /**
     * Checks if masking is selected
     *
     * @return true/false
     */
    public boolean isNoMaskingSelected() {
        return !pageUtils.waitForElementToAppear(noMasking).getAttribute("checked").equals("null");
    }

    /**
     * Gets painted batch size
     *
     * @return double
     */
    public double getPaintedBatchSize() {
        return Double.parseDouble(psoController.inputLocator("Painted Batch Size").getAttribute("value"));
    }

    /**
     * Gets number of components per paint cart
     *
     * @return double
     */
    public double getComponentsPaintCart() {
        return Double.parseDouble(psoController.inputLocator("Number of Components Per Paint Cart").getAttribute("value"));
    }

    /**
     * Select components per load bar
     *
     * @return current page object
     */
    public MaterialProcessPage selectCompLoadBar() {
        pageUtils.waitForElementAndClick(compLoadBarDefault);
        return this;
    }

    /**
     * Inputs component per load bar
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputCompLoadBar(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Number of Components Per Load Bar", "user"), psoController.inputLocator("Number of Components Per Load Bar"), value);
        return this;
    }

    /**
     * Select defined value
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage selectNumberOfCavitiesDropdown(String value) {
        pageUtils.waitForElementAndClick(psoController.buildLocator("Number of cavities  (Piece Part & Tooling Cost Driver)", "user"));
        pageUtils.optionsTypeAheadSelect(psoController.dropdownLocator("User defined value"), "User defined value", value);
        return this;
    }

    /**
     * Select mold material
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage selectMoldMaterial(String value) {
        pageUtils.optionsTypeAheadSelect(psoController.dropdownLocator("Mold Material"), "Mold Material", value);
        return this;
    }

    /**
     * Get mold material
     *
     * @return string
     */
    public String getMoldMaterial() {
        return psoController.dropdownLocator("Mold Material").getAttribute("textContent");
    }

    /**
     * Get Part tolerance
     *
     * @return string
     */
    public String getPartTolerance() {
        return psoController.dropdownLocator("Part Tolerance").getAttribute("textContent");
    }

    /**
     * Select tolerance
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage selectTolerances(String value) {
        pageUtils.optionsTypeAheadSelect(psoController.dropdownLocator("Part Tolerance"), "Part Tolerance", value);
        return this;
    }

    /**
     * Gets defined value
     *
     * @return integer
     */
    public int getDefinedValue() {
        return Integer.parseInt(psoController.dropdownLocator("User defined value").getAttribute("textContent"));
    }

    /**
     * Input nominal override
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage overrideWallThickness(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Nominal Wall Thickness  (Piece Part Cost Driver)", "userOverride"),
            psoController.inputLocator("Nominal Wall Thickness  (Piece Part Cost Driver)"), value);
        return this;
    }

    /**
     * Input material regrind
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputMaterialRegrind(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Material Regrind Allowance (Piece Part Cost Driver)", "userDefinedMode"),
            psoController.inputLocator("Material Regrind Allowance (Piece Part Cost Driver)"), value);
        return this;
    }

    /**
     * Add colourant
     *
     * @return current page object
     */
    public MaterialProcessPage selectAddColorantButton() {
        pageUtils.waitForElementAndClick(addColorantButton);
        return this;
    }

    /**
     * Select optimize minimum cost
     *
     * @return current page object
     */
    public MaterialProcessPage selectOptimizeMinCost() {
        pageUtils.waitForElementAndClick(optimizeMinCost);
        return this;
    }

    /**
     * Checks optimize is selected
     *
     * @return true/false
     */
    public boolean isOptimizeMinCostSelected() {
        return !pageUtils.waitForElementToAppear(optimizeMinCost).getAttribute("checked").equals("null");
    }

    /**
     * Checks colorant is selected
     *
     * @return true/false
     */
    public boolean isColorantSelected() {
        return !pageUtils.waitForElementToAppear(addColorantButton).getAttribute("checked").equals("null");
    }

    /**
     * Input bundle count
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputBundleCount(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Bundle Sawing", "userOverride"),
            psoController.inputLocator("Bundle Sawing"), value);
        return this;
    }

    /**
     * Input material allowance
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputMaterialAllowance(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Material Allowance (Piece Part Cost Driver)", "userOverride"),
            psoController.inputLocator("Material Allowance (Piece Part Cost Driver)"), value);
        return this;
    }

    /**
     * Input cooling time
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputCoolingTime(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Cooling Time", "user"),
            psoController.inputLocator("Cooling Time"), value);
        return this;
    }

    /**
     * Input colour charge
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputColorCharge(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Colorant   (Piece Part Cost Driver)", "userDefined"),
            psoController.inputLocator("Colorant   (Piece Part Cost Driver)"), value);
        return this;
    }


    /**
     * Select optimize minimum cost
     *
     * @return current page object
     */
    public MaterialProcessPage selectCavitiesOptimizeMinCost() {
        pageUtils.waitForElementAndClick(cavitiesOptimizeMinCost);
        return this;
    }

    /**
     * Checks optimize is selected
     *
     * @return true/false
     */
    public boolean isCavitiesOptimizeMinCostSelected() {
        return !pageUtils.waitForElementToAppear(cavitiesOptimizeMinCost).getAttribute("checked").equals("null");
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

    /**
     * Get Part tolerance
     *
     * @return string
     */
    public String getPartOrientation() {
        return psoController.dropdownLocator("Orientation").getAttribute("textContent");
    }

    /**
     * Select tolerance
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage selectPartOrientation(String value) {
        pageUtils.optionsTypeAheadSelect(psoController.dropdownLocator("Orientation"), "Orientation", value);
        return this;
    }

    /**
     * Get Part tolerance
     *
     * @return string
     */
    public String getGrainDirection() {
        return psoController.dropdownLocator("Direction").getAttribute("textContent");
    }

    /**
     * Select tolerance
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage selecGrainDirection(String value) {
        pageUtils.optionsTypeAheadSelect(psoController.dropdownLocator("Direction"), "Direction", value);
        return this;
    }

    /**
     * Input Minimum Recommended Hole Diameter count
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputMinimumRecommendedHoleDiameter(String value) {
        psoController.inputOverrideValue(psoController.buildLocator("Minimum Recommended Hole Diameter", "userOverride"),
            psoController.inputLocator("Minimum Recommended Hole Diameter"), value);
        return this;
    }
}
