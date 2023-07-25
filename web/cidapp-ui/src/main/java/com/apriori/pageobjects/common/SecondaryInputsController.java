package com.apriori.pageobjects.common;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryInputsController {

    private static final Logger logger = LoggerFactory.getLogger(SecondaryInputsController.class);

    private WebDriver driver;
    private PageUtils pageUtils;

    public SecondaryInputsController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Enters the batch size
     *
     * @param batchSizeInput - the batch size input
     * @param batchSize      - the batch size
     * @return current page object
     */
    public SecondaryInputsController enterBatchSize(WebElement batchSizeInput, String batchSize) {
        batchSizeInput.clear();
        batchSizeInput.sendKeys(Keys.DELETE);
        batchSizeInput.sendKeys(batchSize);
        return this;
    }

    /**
     * Selects the secondary vpe dropdown
     *
     * @param secVpeDropdown - the secondary vpe dropdown
     * @param secVpe         - the secondary vpe
     * @return current page object
     */
    public SecondaryInputsController selectSecondaryVpe(WebElement secVpeDropdown, String secVpe) {
        pageUtils.waitForElementAndClick(secVpeDropdown);
        By svp = By.cssSelector(String.format("button[value='%s']", secVpe));
        pageUtils.scrollWithJavaScript(driver.findElement(svp), true).click();
        return this;
    }
}
