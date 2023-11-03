package com.apriori.pageobjects.evaluate;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.help.HelpDocPage;

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

public class CostDetailsPage extends LoadableComponent<CostDetailsPage> {

    private static final Logger logger = LoggerFactory.getLogger(CostDetailsPage.class);

    @FindBy(css = "g[class='recharts-cartesian-grid']")
    private WebElement costResultChart;

    @FindBy(css = "div[class='cost-result-list']")
    private WebElement costResults;

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
