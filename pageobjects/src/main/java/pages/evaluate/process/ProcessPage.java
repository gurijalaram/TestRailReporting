package main.java.pages.evaluate.process;

import main.java.pages.evaluate.SecondaryProcessPage;
import main.java.utils.PageUtils;
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

/**
 * @author cfrith
 */

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

    @FindBy(css = "g.highcharts-series rect")
    private WebElement cycleTimeChart;

    @FindBy(css = "g.highcharts-label .highcharts-text-outline")
    private List<WebElement> chartValues;

    @FindBy(css = "label[data-ap-field='processStep']")
    private WebElement processStep;

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
        pageUtils.waitForElementToAppear(alternateRoutingsButton).click();
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
        return pageUtils.waitForElementToAppear(processSelectionTable).getText();
    }

    /**
     * Gets the index position of the chart label and selects the chart based on this index
     *
     * @param process - the process
     * @return current page object
     */
    public ProcessPage selectProcessChart(String process) {
        pageUtils.waitForElementToAppear(cycleTimeChart);

        int position = IntStream.range(0, routingLabels.size())
            .filter(userInd -> routingLabels.get(userInd).getText().equals(process))
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
        for (WebElement chartValue : chartValues) {
            pageUtils.waitForElementToAppear(chartValue);
        }
        return chartValues.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}