package pageobjects.pages.evaluate;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.toolbars.EvaluatePanelToolbar;

public class CostDetailsPage extends EvaluatePanelToolbar {

    private final Logger logger = LoggerFactory.getLogger(CostDetailsPage.class);

    @FindBy(css = "[data-ap-comp='costResultChartArea']")
    private WebElement costResultChart;

    @FindBy(css = "[class='table tree-table']")
    private WebElement resultsTree;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    @FindBy(css = "[data-ap-field='pieceCost']")
    private WebElement totalVariableCosts;

    @FindBy(css = "[data-ap-field='periodOverhead']")
    private WebElement indirectOverhead;

    @FindBy(css = "[data-ap-field='sgaCost']")
    private WebElement sganda;

    @FindBy(css = "[data-ap-field='margin']")
    private WebElement margin;

    @FindBy(css = "[data-ap-field='totalCost']")
    private WebElement piecePartCost;

    @FindBy(xpath = "(//span[@data-ap-field='capitalInvestment'])[1]")
    private WebElement totalCapitalInvestments;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CostDetailsPage(WebDriver driver) {
        super(driver);
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
        pageUtils.waitForElementToAppear(costResultChart);
        pageUtils.waitForElementToAppear(resultsTree);
    }

    /**
     * Expands the dropdown
     *
     * @param dropdown - the dropdown to be selected
     * @return current page object
     */
    public CostDetailsPage expandDropdown(String dropdown) {
        By caret = By.xpath("//span[contains(text(), '" + dropdown + "')]/..");
        pageUtils.waitForElementToAppear(caret);

        if (driver.findElement(caret).getAttribute("aria-expanded").contains("true")) {
            return this;
        } else {
            if (driver.findElement(caret).getAttribute("aria-expanded").contains("false")) {
                driver.findElement(caret).click();
            }
        }
        return this;
    }

    /**
     * Gets the cost
     *
     * @param contributor The contributor needs to be exact. Inspect the DOM as there may be spaces
     * @return string
     */
    public String getCostContribution(String contributor) {
        By costInfo = By.xpath("//td[.='" + contributor + "']/following-sibling::td");
        //TODO add in ability to scroll when BA-890 is fixed pageUtils.scrollToElement(costInfo, verticalScroller);
        return driver.findElement(costInfo).getText();
    }

    /**
     * Gets Total Variable Costs
     *
     * @return double
     */
    public double getTotalVariableCosts() {
        pageUtils.waitForElementToAppear(totalVariableCosts);
        return Double.parseDouble(totalVariableCosts.getText());
    }

    /**
     * Gets Indirect Overhead
     *
     * @return double
     */
    public double getIndirectOverhead() {
        pageUtils.waitForElementToAppear(indirectOverhead);
        return Double.parseDouble(indirectOverhead.getText());
    }

    /**
     * Gets SG&A
     *
     * @return double
     */
    public double getSGandA() {
        pageUtils.waitForElementToAppear(sganda);
        return Double.parseDouble(sganda.getText());
    }

    /**
     * Gets Margin
     *
     * @return double
     */
    public double getMargin() {
        pageUtils.waitForElementToAppear(margin);
        return Double.parseDouble(margin.getText());
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
     * Gets Piece Part Cost as String
     *
     * @return String
     */
    public String getPiecePartCostString() {
        By locator = By.xpath("(//span[@title='691.48'])[2]");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Gets Total Capital Investments
     *
     * @return String
     */
    public String getTotalCapitalInvestments() {
        pageUtils.waitForElementToAppear(totalCapitalInvestments);
        return totalCapitalInvestments.getAttribute("title");
    }
}