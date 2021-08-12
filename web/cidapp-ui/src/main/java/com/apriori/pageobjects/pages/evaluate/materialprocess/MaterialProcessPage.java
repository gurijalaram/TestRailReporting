package com.apriori.pageobjects.pages.evaluate.materialprocess;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
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
import java.util.stream.IntStream;

/**
 * @author cfrith
 */

@Slf4j
public class MaterialProcessPage extends LoadableComponent<MaterialProcessPage> {

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

    @FindBy(css = ".highcharts-xaxis-labels text")
    private List<WebElement> xAxisLabel;

    @FindBy(css = ".highcharts-series-group rect")
    private List<WebElement> chart;

    @FindBy(xpath = "//h6[.='Average Wall Thickness']/..//input[@value='default']")
    private WebElement averageThicknessDefault;

    @FindBy(xpath = "//h6[.='Average Wall Thickness']/..//input[@value='user']")
    private WebElement averageThicknessUser;

    @FindBy(xpath = "//h6[.='Average Wall Thickness']/..//input[@type='number']")
    private WebElement averageThickInput;

    @FindBy(xpath = "//h6[.='Case Depth Selection']/..//input[@value='default']")
    private WebElement caseDepthDefault;

    @FindBy(xpath = "//h6[.='Case Depth Selection']/..//input[@value='user']")
    private WebElement caseDepthUser;

    @FindBy(xpath = "//h6[.='Case Depth Selection']/..//input[@type='number']")
    private WebElement caseDepthInput;

    @FindBy(xpath = "//h6[.='Masking']/..//input[@value='defaultNoMasking']")
    private WebElement maskingDefault;

    @FindBy(xpath = "//h6[.='Masking']/..//input[@value='userOverride']")
    private WebElement maskingUser;

    @FindBy(xpath = "//h6[.='Masking']/..//input[@type='number']")
    private WebElement maskingInput;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private ProcessOptionsController processOptionsController;

    public MaterialProcessPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.processOptionsController = new ProcessOptionsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * Go to stock tab
     *
     * @return new page object
     */
    public StockPage goToStockTab() {
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
        int position = IntStream.range(0, xAxisLabel.size()).filter(x -> xAxisLabel.get(x).getText().equals(axisLabel)).findFirst().getAsInt();
        chart.forEach(x -> pageUtils.waitForElementAndClick(chart.get(position)));
        return this;
    }

    /**
     * Selects material default
     *
     * @return current page object
     */
    public MaterialProcessPage selectAverageWallThickness() {
        pageUtils.waitForElementAndClick(averageThicknessDefault);
        return this;
    }

    /**
     * Input material override
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputAverageWallThickness(String value) {
        pageUtils.waitForElementAndClick(averageThicknessUser);
        pageUtils.clearInput(averageThickInput);
        averageThickInput.sendKeys(value);
        return this;
    }

    /**
     * Gets average wall thickness
     *
     * @return string
     */
    public String getAverageWallThickness() {
        return pageUtils.waitForElementToAppear(averageThickInput).getAttribute("value");
    }

    /**
     * Select case depth
     *
     * @return current page object
     */
    public MaterialProcessPage selectCaseDepth() {
        pageUtils.waitForElementAndClick(caseDepthDefault);
        return this;
    }

    /**
     * Inputs case depth
     *
     * @param value - the value
     * @return current page object
     */
    public MaterialProcessPage inputCaseDepth(String value) {
        pageUtils.waitForElementAndClick(caseDepthUser);
        pageUtils.clearInput(caseDepthInput);
        caseDepthInput.sendKeys(value);
        return this;
    }

    /**
     * Gets case depth
     *
     * @return string
     */
    public double getCaseDepth() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(caseDepthInput).getAttribute("value"));
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
        pageUtils.waitForElementAndClick(maskingUser);
        pageUtils.clearInput(maskingUser);
        maskingUser.sendKeys(value);
        return this;
    }

    /**
     * Gets case depth
     *
     * @return string
     */
    public double getMasking() {
        return Double.parseDouble(pageUtils.waitForElementToAppear(maskingInput).getAttribute("value"));
    }
//    /**
//     * Selects default value radio button
//     *
//     * @return current page object
//     */
//    public MaterialProcessPage selectDefaultValue(String label) {
//        processOptionsController.selectDefaultValue(label);
//        return this;
//    }
//
//    /**
//     * Inputs override value
//     *
//     * @param value - the value
//     * @return current page object
//     */
//    public MaterialProcessPage inputOverride(String label, String value) {
//        processOptionsController.inputOverride(label, value);
//        return this;
//    }
//
//    /**
//     * Select masking
//     *
//     * @return current page object
//     */
//    public MaterialProcessPage selectMasking() {
//        processOptionsController.selectMasking(maskingButton);
//        return this;
//    }
//
//    /**
//     * Select masked features
//     *
//     * @return current page object
//     */
//    public MaterialProcessPage selectMaskedFeatures() {
//        processOptionsController.selectMaskedFeatures(maskingFeaturesButton);
//        return this;
//    }
//
//    /**
//     * Select masked input
//     *
//     * @param value        - the value
//     * @return current page object
//     */
//    public MaterialProcessPage inputMaskedFeatures(String value) {
//       processOptionsController.inputMaskedFeatures(maskingInput, value);
//       return this;
//    }
//
//    /**
//     * Gets masking input
//     * @return string
//     */
//    public String getMaskedFeatures() {
//        return processOptionsController.getMaskedFeatures(maskingInput);
//    }
//
//    /**
//     * Gets override input
//     * @return string
//     */
//    public String getOverride() {
//        return processOptionsController.getOverride(overrideInput);
//    }

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
