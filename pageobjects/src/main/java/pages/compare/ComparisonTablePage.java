package main.java.pages.compare;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComparisonTablePage extends LoadableComponent<ComparisonTablePage> {

    private final Logger logger = LoggerFactory.getLogger(ComparisonTablePage.class);

    @FindBy(css = "div[data-ap-comp='workspaceList']")
    private WebElement workspaceList;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement applyButton;

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

    @FindBy(css = "div[data-ap-comp='componentTable'] div.v-grid-scroller-vertical")
    private WebElement comparisonScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ComparisonTablePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(workspaceList);
    }

    /**
     * Find and select the comparison in the table
     * @param partName - name of the part
     * @param scenarioName - scenario name
     * @return current page object
     */
    public ComparisonTablePage selectComparison(String partName, String scenarioName) {
        By comparison = By.cssSelector("//div[@data-ap-comp='componentTable']//a[contains(@href,'" + partName + "," + scenarioName + "')]/ancestor::tr//input[@class]");
        pageUtils.scrollToElement(comparison, comparisonScroller).click();
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ComparePage apply() {
        applyButton.click();
        return new ComparePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ComparePage cancel() {
        cancelButton.click();
        return new ComparePage(driver);
    }
}