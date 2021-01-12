package com.pageobjects.pages.evaluate.process;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.pageobjects.toolbars.EvaluatePanelToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author cfrith
 */

public class ProcessRoutingPage extends EvaluatePanelToolbar {

    private final Logger LOGGER = LoggerFactory.getLogger(ProcessRoutingPage.class);

    @FindBy(css = "div[data-ap-comp='processCycleTime']")
    private WebElement routingTable;

    @FindBy(css = ".highcharts-xaxis-labels tspan")
    private List<WebElement> routingLabels;

    @FindBy(css = "select[data-ap-field='chartSelectionField']")
    private WebElement contributionsDropdown;

    @FindBy(css = "button[data-ap-comp='alternateRoutingsButton']")
    private WebElement alternateRoutingsButton;

    @FindBy(css = "[data-ap-comp='processCycleTime'] button[data-ap-comp='secondaryTreatmentsButton']")
    private WebElement secTreatementsButton;

    @FindBy(css = "[data-ap-scope='processSelection'] .table")
    private WebElement processSelectionTable;

    @FindBy(css = "g.highcharts-series rect")
    private List<WebElement> cycleTimeCharts;

    @FindBy(css = "g.highcharts-label .highcharts-text-outline")
    private List<WebElement> chartValues;

    @FindBy(css = "g.highcharts-label .highcharts-text-outline")
    private WebElement chartValue;

    @FindBy(css = "label[data-ap-field='processStep']")
    private WebElement processStep;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    @FindBy(xpath = "//label[.='Options']")
    private WebElement optionsTab;

    @FindBy(css = "[data-ap-field='machineName']")
    private WebElement machineName;

    @FindBy(css = ".costing-out-of-date-title")
    private List<WebElement> outOfDateMsg;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    @FindBy(css = "[data-ap-field='cycleTime']")
    private WebElement cycleTime;

    @FindBy(css = "[data-ap-field='totalCost']")
    private WebElement piecePartCost;

    @FindBy(css = "[data-ap-field='fullyBurdenedCost']")
    private WebElement fullyBurdenedCost;

    @FindBy(css = "[data-ap-field='capitalInvestment']")
    private WebElement capitalInvestment;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessRoutingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(routingTable);
        pageUtils.waitForElementsToAppear(cycleTimeCharts);
    }

    /**
     * Selects the contribution dropdown
     *
     * @param contribution - the contribution
     * @return current page object
     */
    public ProcessRoutingPage selectContribution(String contribution) {
        new Select(contributionsDropdown).selectByVisibleText(contribution);
        return this;
    }

    /**
     * Selects the routing button
     *
     * @return current page object
     */
    public RoutingsPage selectRoutingsButton() {
        pageUtils.waitForElementAndClick(alternateRoutingsButton);
        return new RoutingsPage(driver);
    }

    /**
     * Select the secondary process button
     *
     * @return new page object
     */
    public SecondaryProcessPage selectSecondaryProcessButton() {
        pageUtils.waitForElementToAppear(secTreatementsButton).click();
        return new SecondaryProcessPage(driver);
    }

    /**
     * Gets list of routing labels
     *
     * @return list of strings
     */
    public List<String> getRoutingLabels() {
        return routingLabels.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Gets details of process selection table
     *
     * @return list as string
     */
    public String getSelectionTableDetails() {
        return Arrays.toString(pageUtils.waitForElementToAppear(processSelectionTable).getText().split("\n"));
    }

    /**
     * Gets the index position of the chart label and selects the chart based on this index
     *
     * @param process - the process
     * @return current page object
     */
    public ProcessRoutingPage selectProcessChart(String process) {
        pageUtils.waitForElementToAppear(chartValue);

        int position = IntStream.range(0, routingLabels.size())
            .filter(label -> routingLabels.get(label).getText().equals(process))
            .findFirst().getAsInt() + 1;

        By chart = By.cssSelector(String.format("g.highcharts-series rect:nth-of-type(%s", position));
        pageUtils.actionClick(driver.findElement(chart));
        return this;
    }

    /**
     * Gets the process percentage value
     *
     * @return chart values as string list
     */
    public List<String> getProcessPercentage() {
        pageUtils.waitForElementToAppear(chartValue);
        return chartValues.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    /**
     * Selects the option tab
     *
     * @return new page object
     */
    public ProcessSetupOptionsPage selectOptions() {
        pageUtils.waitForElementAndClick(optionsTab);
        return new ProcessSetupOptionsPage(driver);
    }

    /**
     * Get the machine name
     *
     * @return string
     */
    public String getMachineName() {
        By machine = By.cssSelector("[data-ap-field='machineName']");
        pageUtils.waitForElementToAppear(machine);
        return driver.findElement(machine).getAttribute("innerText");
    }

    /**
     * Checks if the routing out of date message appears
     *
     * @return true/false
     */
    public boolean isRoutingOutOfDateDisplayed() {
        return pageUtils.checkElementVisibleByBoolean(outOfDateMsg);
    }

    /**
     * Gets cycle time count
     *
     * @return double
     */
    public double getCycleTime() {
        pageUtils.waitForElementToAppear(cycleTime);
        return Double.parseDouble(cycleTime.getText());
    }

    /**
     * Gets Piece Part Cost
     *
     * @return double
     */
    public double getPiecePartCost() {
        pageUtils.waitForElementToAppear(piecePartCost);
        return Double.parseDouble(piecePartCost.getText());
    }

    /**
     * Gets Fully Burdened Cost
     *
     * @return double
     */
    public double getFullyBurdenedCost() {
        pageUtils.waitForElementToAppear(fullyBurdenedCost);
        return Double.parseDouble(fullyBurdenedCost.getText());
    }

    /**
     * Gets Total Capital Investments
     *
     * @return double
     */
    public double getCapitalInvestments() {
        pageUtils.waitForElementToAppear(capitalInvestment);
        return Double.parseDouble(capitalInvestment.getText());
    }
}