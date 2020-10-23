package pageobjects.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostDetailsPage extends LoadableComponent<CostDetailsPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(CostDetailsPage.class);

    @FindBy(css = "g[class='highcharts-series-group']")
    private WebElement costResultChart;

    @FindBy(css = "div[class='cost-result-list']")
    private WebElement costResults;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CostDetailsPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(costResultChart);
        pageUtils.waitForElementToAppear(costResults);
    }

    /**
     * Expands the cost dropdown
     * @param costDetails - the cost dropdown
     * @return current page object
     */
    public CostDetailsPage expandDropDown(String costDetails) {
        By costDropdown = By.xpath(String.format("//div[normalize-space(text())='%s']", costDetails));
        pageUtils.waitForElementToAppear(costDropdown);
        pageUtils.waitForElementAndClick(costDropdown);
        return this;
    }

    /**
     * Gets the cost contribution
     * @param costContribution - cost contribution
     * @return string
     */
    public String getCostContribution(String costContribution) {
        By costDropdown = By.xpath(String.format("//div[normalize-space(text())='%s']/..//div[@class='right']", costContribution));
        return pageUtils.waitForElementToAppear(costDropdown).getText();
    }
}
