package com.apriori.pageobjects.pages.evaluate.process;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcessPage extends LoadableComponent<ProcessPage> {

    private final Logger logger = LoggerFactory.getLogger(ProcessPage.class);

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

    @FindBy(css = "div[data-ap-field='machineVisible']")
    private WebElement processInfo;

    @FindBy(css = "div[data-ap-comp='costingResult']")
    private WebElement processContributions;

    @FindBy(css = ".panel .glyphicon-remove")
    private WebElement closePanelButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ProcessPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(routingTable);
        pageUtils.waitForElementsToAppear(cycleTimeCharts);
    }

    /**
     * Selects the contribution dropdown
     *
     * @param contribution - the contribution
     * @return current page object
     */
    public ProcessPage selectContribution(String contribution) {
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
    public String[] getSelectionTableDetails() {
        return pageUtils.waitForElementToAppear(processSelectionTable).getText().split("\n");
    }

    /**
     * Gets the index position of the chart label and selects the chart based on this index
     *
     * @param process - the process
     * @return current page object
     */
    public ProcessPage selectProcessChart(String process) {
        pageUtils.waitForElementToAppear(chartValue);

        int position = IntStream.range(0, routingLabels.size())
            .filter(label -> routingLabels.get(label).getText().equals(process))
            .findFirst().getAsInt() + 1;

        WebElement chart = driver.findElement(By.cssSelector("g.highcharts-series rect:nth-of-type(\n" + position));
        pageUtils.actionClick(chart);
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
     * Closes the processes panel
     *
     * @return current page object
     */
    public EvaluatePage closeProcessPanel() {
        pageUtils.waitForElementAndClick(closePanelButton);
        return new EvaluatePage(driver);
    }

    /**
     * Gets the process info
     *
     * @param text - the text
     * @return true/false
     */
    public Boolean getProcessInfo(String text) {
        return pageUtils.checkElementAttribute(processInfo, "innerText", text);
    }

    /**
     * Gets the process contribution
     *
     * @param text - the text
     * @return true/false
     */
    public Boolean getProcessContributions(String text) {
        return pageUtils.checkElementAttribute(processContributions, "innerText", text);
    }
}