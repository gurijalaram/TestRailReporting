package pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.common.PanelController;
import pageobjects.pages.help.HelpDocPage;

/**
 * @author cfrith
 */

public class CostDetailsPage extends LoadableComponent<CostDetailsPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(CostDetailsPage.class);

    @FindBy(css = "g[class='highcharts-series-group']")
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
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
    public double getDropdownValue(String label) {
        By value = By.xpath(String.format("//div[@class='cost-result-list']//div[normalize-space(text())='%s']/..//div[@class='summary-amount']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(value).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Gets the cost contribution
     *
     * @param label - cost contribution
     * @return double
     */
    public double getCostContribution(String label) {
        By costDropdown = By.xpath(String.format("//div[@class='collapse show']//span[normalize-space(text())='%s']/..//span[@class='property-value']", label));
        return Double.parseDouble(pageUtils.waitForElementToAppear(costDropdown).getAttribute("textContent").replaceAll("[^0-9?!\\.]", ""));
    }

    /**
     * Checks the specified contribution is displayed
     *
     * @param label - the label
     * @param value - the value
     * @return true/false
     */
    public boolean isCostContributionDisplayed(String label, String value) {
        By costResult = By.xpath(String.format("//div[@class='collapse show']//span[normalize-space(text())='%s']/..//span[.='%s']", label, value));
        pageUtils.waitForElementToAppear(costResult);
        return driver.findElement(costResult).isDisplayed();
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
