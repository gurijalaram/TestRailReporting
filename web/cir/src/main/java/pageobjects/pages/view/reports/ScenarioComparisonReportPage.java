package pageobjects.pages.view.reports;

import com.apriori.pageobjects.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ScenarioComparisonReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(AssemblyDetailsReportPage.class);

    @FindBy(xpath = "//span[contains(text(), 'FULLY')]/../following-sibling::td[2]")
    private WebElement firstFbc;

    @FindBy(xpath = "//span[contains(text(), 'FULLY')]/../following-sibling::td[3]")
    private WebElement secondFbc;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ScenarioComparisonReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Get first or second Fbc Value
     * @param getFirst boolean
     * @return BigDecimal
     */
    public BigDecimal getFbcValue(boolean getFirst) {
        WebElement elementToUse = getFirst ? firstFbc : secondFbc;
        pageUtils.waitForElementToAppear(elementToUse);
        return new BigDecimal(elementToUse.getText());
    }

    /**
     * Gets Scenarios to Compare Name
     * @param index int
     * @return String
     */
    public String getScenariosToCompareName(int index) {
        By locator = By.xpath(String.format("(//div[@title='Scenarios to Compare']//ul)[1]/li[%s]", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Waits for scenario filter to take effect
     */
    public void waitForScenarioFilter() {
        for (int i = 1; i < 11; i++) {
            By locator = By.xpath(
                    String.format(
                            "((//div[@title='Scenarios to Compare']//ul)[1]/li[contains(@title, 'Initial')])[%s]", i)
            );
            pageUtils.waitForElementToAppear(locator);
        }
    }
}
