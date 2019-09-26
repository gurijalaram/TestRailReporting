package com.apriori.pageobjects.pages.evaluate.designguidance.tolerances;

import com.apriori.pageobjects.utils.PageUtils;
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
        selectToleranceType(toleranceType).click();
        selectGCD(gcdType).click();
        return this;
    }

    /**
     * Selects the tolerance type.  Selection is based on exact match so unit must be specified eg. Flatness (mm)
     * @param toleranceType - the tolerance type
     * @return the tolerance as webelement
     */
    private WebElement selectToleranceType(String toleranceType) {
        By tolerance = By.xpath("//div[@data-ap-comp='tolerancesTable']//td[contains(text(),'" + toleranceType + "')]/ancestor::tr");
        return pageUtils.scrollToElement(tolerance,toleranceScroller);
    }

    /**
     * Selects the gcd.  Selection is based on exact match
     * @param gcdType - the gcd type
     * @return the gcd as webelement
     */
    private WebElement selectGCD(String gcdType) {
        By gcd = By.xpath("//div[@data-ap-comp='tolerancesDetailsTable']//td[.='" + gcdType + "']/ancestor::tr");
        return pageUtils.scrollToElement(gcd, detailsScroller);
    }

    /**
     * Selects the edit button
     * @return current page object
     */
    private ToleranceEditPage editTolerance() {
        editToleranceButton.click();
        return new ToleranceEditPage(driver);
    }
}