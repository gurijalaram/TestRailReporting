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

/**
 * @author cfrith
 */

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
     * @param gcdParent - the gcd dropdown
     * @param gcdChild - the gcd type
     * @param gcdProperty - the gcd property
     * @return current page object
     */
    public GeometryPage selectGCDAndGCDProperty(String gcdParent, String gcdChild, String gcdProperty) {
        selectGCDType(gcdParent, gcdChild);
        selectGCDProperty(gcdProperty);
        return this;
    }

    /**
     * Selects the gcd type
     * @param gcdParent - the gcd parent type
     * @param gcdChild - the gcd type
     * @return gcd type as webelement
     */
    private GeometryPage selectGCDType(String gcdParent, String gcdChild) {
        By gcdParentElement = By.xpath("//div[@data-ap-comp='gcdTreeTable']//div[.='" + gcdParent + "']//span[@class='fa fa-caret-right']");
        pageUtils.scrollToElement(gcdParentElement, gcdTableScroller).click();

        By gcdChildElement = By.xpath("//div[@data-ap-comp='gcdTreeTable']//div[.='" + gcdChild + "']");
        pageUtils.scrollToElement(gcdChildElement, gcdTableScroller).click();
        return this;
    }

    /**
     * Selects the gcd property
     * @param gcdProperty - the gcd property
     * @return gcd property as webelement
     */
    private GeometryPage selectGCDProperty(String gcdProperty) {
        By gcd = By.xpath("//div[@data-ap-comp='artifactProperties']//td[contains(text(),'" + gcdProperty + "')]/ancestor::tr");
        pageUtils.scrollToElement(gcd, propertiesScroller).click();
        return this;
    }
}