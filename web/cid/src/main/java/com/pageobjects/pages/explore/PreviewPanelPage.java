package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mparker
 */

public class PreviewPanelPage extends LoadableComponent<PreviewPanelPage> {

    private final Logger logger = LoggerFactory.getLogger(PreviewPanelPage.class);

    @FindBy(css = "[data-ap-field='totalCost']")
    private WebElement totalCost;

    @FindBy(css = "[data-ap-field='fullyBurdenedCost']")
    private WebElement fullyBurdenedCost;

    @FindBy(css = "[data-ap-field='capitalInvestment']")
    private WebElement totalCapitalInvestment;

    @FindBy(css = "[data-ap-comp='costResultChartArea']")
    private WebElement previewCostChart;

    @FindBy(css = "[data-ap-comp='thumbnail']")
    private WebElement thumbnailImage;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PreviewPanelPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(previewCostChart);
    }

    /**
     * Gets piece part cost
     *
     * @return string
     */
    public String getPiecePartCost() {
        return getAttribute(totalCost);
    }

    /**
     * Gets piece part cost
     *
     * @return string
     */
    public String getFullyBurdenedCost() {
        return getAttribute(fullyBurdenedCost);
    }

    /**
     * Gets piece part cost
     *
     * @return string
     */
    public String getTotalCapitalInvestment() {
        return getAttribute(totalCapitalInvestment);
    }

    /**
     * Refactored method to check element attribute
     *
     * @param locator - the locator
     * @return true/false
     */
    private String getAttribute(WebElement locator) {
        pageUtils.waitForElementToAppear(locator);
        return locator.getAttribute("innerText");
    }

    /**
     * Checks if image is displayed
     *
     * @return true/false
     */
    public boolean isImageDisplayed() {
        return pageUtils.isElementDisplayed(thumbnailImage);
    }
}
