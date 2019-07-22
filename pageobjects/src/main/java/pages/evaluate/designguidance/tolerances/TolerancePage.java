package main.java.pages.evaluate.designguidance.tolerances;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TolerancePage extends LoadableComponent<TolerancePage> {

    private final Logger logger = LoggerFactory.getLogger(TolerancePage.class);

    @FindBy(css = "div[data-ap-comp='tolerancesTable']")
    private WebElement toleranceTable;

    @FindBy(css = "div[data-ap-comp='tolerancesTable'] .v-grid-scroller-vertical")
    private WebElement toleranceScroller;

    @FindBy(css = "div[id='tolerancesTab'] .edit-tolerances-btn")
    private WebElement editToleranceButton;

    @FindBy(css = "div[data-ap-comp='tolerancesDetailsTable'] .v-grid-scroller-vertical")
    private WebElement detailsScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public TolerancePage(WebDriver driver) {
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

    }

    /**
     * Selects both tolerance and gcd
     * @param toleranceType - the tolerance type
     * @param gcdType - the gcd type
     * @return current page object
     */
    public TolerancePage selectToleranceTypeAndGCD(String toleranceType, String gcdType) {
        selectToleranceType(toleranceType);
        selectGCD(gcdType);
        return this;
    }

    /**
     * Selects the tolerance type.  Selection is based on exact match so unit must be specified eg. Flatness (mm)
     * @param toleranceType - the tolerance type
     * @return current page object
     */
    private TolerancePage selectToleranceType(String toleranceType) {
        By tolerance = By.xpath("//div[@data-ap-comp='tolerancesTable']//td[.='" + toleranceType + "']");
        pageUtils.scrollToElement(tolerance,toleranceScroller);
        return this;
    }

    /**
     * Selects the gcd.  Selection is based on exact match
     * @param gcdType - the gcd type
     * @return current page object
     */
    private TolerancePage selectGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='tolerancesDetailsTable']//td[.='" + gcdType + "']");
        pageUtils.scrollToElement(gcd, detailsScroller);
        return this;
    }

    //TODO : class is not implemented
//    private ToleranceEditPage editTolerance() {
    private Object editTolerance() {
        editToleranceButton.click();
//        return new ToleranceEditPage(driver);
        return null;
    }
}