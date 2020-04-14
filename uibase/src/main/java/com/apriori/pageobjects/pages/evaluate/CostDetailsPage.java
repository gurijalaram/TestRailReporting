package com.apriori.pageobjects.pages.evaluate;

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

    private final Logger logger = LoggerFactory.getLogger(CostDetailsPage.class);


    @FindBy(css = "[data-ap-comp='costResultChartArea']")
    private WebElement costResultChart;

    @FindBy(css = "[class='table tree-table']")
    private WebElement resultsTree;

    @FindBy(css = ".details-viewport-part .glyphicon-question-sign")
    private WebElement helpButton;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public CostDetailsPage(WebDriver driver) {
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
     * Clicks the help button
     *
     * @return the current page object
     */
    public CostDetailsPage clickHelp() {
        pageUtils.waitForElementAndClick(helpButton);
        return this;
    }

    public String getChildPageTitle() {
        return pageUtils.windowHandler().getTitle();
        // TODO remove code duplication
    }
}
