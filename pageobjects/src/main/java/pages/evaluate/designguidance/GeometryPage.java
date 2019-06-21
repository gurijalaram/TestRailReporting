package main.java.pages.evaluate.designguidance;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeometryPage extends LoadableComponent<GeometryPage> {

    private final Logger logger = LoggerFactory.getLogger(GeometryPage.class);

    @FindBy(css = "div[data-ap-comp='gcdTreeTable']")
    private WebElement gcdTable;

    @FindBy(css = "div[data-ap-comp='gcdTreeTable'] div.v-grid-scroller-vertical")
    private WebElement gcdTableScroller;

    @FindBy(css = "div[data-ap-comp='artifactProperties'] div.v-grid-scroller-vertical")
    private WebElement propertiesScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GeometryPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(gcdTable);
    }

    /**
     * Selects both gcd and gcd property
     * @param gcdType - the gcd type
     * @param gcdProperty - the gcd property
     * @return current page object
     */
    public GeometryPage selectGCDAndGCDProperty(String gcdType, String gcdProperty) {
        selectGCDType(gcdType).click();
        selectGCDProperty(gcdProperty).click();
        return this;
    }

    /**
     * Selects the gcd type
     * @param gcdType - the gcd type
     * @return gcd type as webelement
     */
    private WebElement selectGCDType(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='gcdTreeTable']//div[contains(text(),'" + gcdType + "')]");
        return pageUtils.scrollToElement(gcd, gcdTableScroller);
    }

    /**
     * Selects the gcd property
     * @param gcdProperty - the gcd property
     * @return gcd property as webelement
     */
    private WebElement selectGCDProperty(String gcdProperty) {
        By gcd = By.xpath("//div[@data-ap-comp='artifactProperties']//td[contains(text(),'" + gcdProperty + "')]");
        return pageUtils.scrollToElement(gcd, propertiesScroller);
    }
}