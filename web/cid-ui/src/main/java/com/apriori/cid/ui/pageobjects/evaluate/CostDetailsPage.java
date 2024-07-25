package com.apriori.cid.ui.pageobjects.evaluate;

import com.apriori.cid.ui.pageobjects.common.PanelController;
import com.apriori.cid.ui.pageobjects.help.HelpDocPage;
import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author cfrith
 */

public class CostDetailsPage extends LoadableComponent<CostDetailsPage> {

    private static final Logger logger = LoggerFactory.getLogger(CostDetailsPage.class);

    @FindBy(css = "g[class='recharts-cartesian-grid']")
    private WebElement costResultChart;

    @FindBy(id = "qa-cost-result-chart-option-select")
    private WebElement costResultDropdown;

    @FindBy(css = ".yAxis g[class='recharts-layer recharts-cartesian-axis-tick'] [orientation='left']")
    private List<WebElement> exAxisLabel;

    @FindBy(css = ".recharts-bar g[class='recharts-layer recharts-label-list'] tspan")
    private List<WebElement> barValues;

    @FindBy(css = "div[class='cost-result-list']")
    private WebElement costResults;

    @FindBy(css = "div[data-testid='apriori-alert']")
    private WebElement alertBar;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public CostDetailsPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(costResultChart);
        pageUtils.waitForElementToAppear(costResults);
    }

    /**
     * Visibility of Alert Bar
     *
     * @return - Boolean of alert visibility
     */
    public Boolean isAlertBarDisplayed() {
        return pageUtils.isElementDisplayed(alertBar);
    }

    /**
     * Get Alert Text
     *
     * @return - Text displayed in alert bar
     */
    public String alertBarText() {
        pageUtils.waitForElementAppear(alertBar);
        return alertBar.getText();
    }

    /**
     * Expands the cost dropdown
     *
     * @param costDetails - the cost dropdown
     * @return current page object
     */
    public CostDetailsPage expandDropDown(String costDetails) {
        String[] costDropdowns = costDetails.split(",");

        for (String costDropdown : costDropdowns) {
            By dropdown = By.xpath(String.format("//div[@class='cost-result-list']//div[normalize-space(text())='%s']", costDropdown.trim()));
            pageUtils.waitForElementToAppear(dropdown);
            pageUtils.waitForElementAndClick(dropdown);
        }
        return this;
    }

    /**
     * Gets the value of the dropdown label
     *
     * @param label - the label
     * @return double
     */
    public double getCostSumValue(String label) {
        By value = By.xpath(String.format("//div[normalize-space(text())='%s']/following-sibling::div", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(value).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets the value of the dropdown label as a String
     *
     * @param label - the label
     * @return String
     */
    public String getCostSumValueAsString(String label) {
        By value = By.xpath(String.format("//div[normalize-space(text())='%s']/following-sibling::div", label));
        return pageUtils.waitForElementToAppear(value).getAttribute("textContent").replaceAll("[^0-9?!\\.]", "");
    }

    /**
     * Gets the cost contribution value
     *
     * @param label - cost contribution
     * @return double
     */
    public double getCostContributionValue(String label) {
        return Double.parseDouble(getCostContribution(label).replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets the cost contribution value as a String
     *
     * @param label - cost contribution
     * @return String
     */
    public String getCostContributionValueText(String label) {
        return getCostContribution(label).replaceAll("[^0-9?!\\.]", "");
    }

    /**
     * Get the cost contribution as displayed
     *
     * @param label - the label
     * @return string
     */
    public String getCostContribution(String label) {
        By costResult = By.xpath(String.format("//div[@class='collapse show']//span[normalize-space(text())='%s']/following-sibling::span", label));
        return pageUtils.waitForElementToAppear(costResult).getAttribute("textContent");
    }

    /**
     * Selects the Cost Result dropdown
     *
     * @param filter - the filter
     * @return current page object
     */
    public CostDetailsPage selectDropdown(String filter) {
        pageUtils.typeAheadSelect(costResultDropdown, filter);
        pageUtils.waitForElementsToAppear(barValues);
        return this;
    }

    /**
     * Gets the value of the Bar
     *
     * @param axisLabel - the labels on the bar chart
     * @return list string
     */
    public Double getBarValue(String axisLabel) {
        int position = IntStream.range(0, exAxisLabel.size()).filter(x -> exAxisLabel.get(x).getText().equals(axisLabel)).findFirst().getAsInt();
        return Double.parseDouble(pageUtils.waitForElementToAppear(barValues.get(position)).getAttribute("textContent"));
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
