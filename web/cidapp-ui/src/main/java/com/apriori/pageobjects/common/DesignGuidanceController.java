package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Slf4j
public class DesignGuidanceController {

    private WebDriver driver;
    private PageUtils pageUtils;

    public DesignGuidanceController(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets element By
     *
     * @param element - the element
     * @return By
     */
    public By getBy(String element) {
        return By.xpath(String.format("//div[normalize-space(text())='%s']/..", element));
    }

    /**
     * Gets column
     *
     * @return string
     */
    public String getColumn(String issue, int column) {
        List<WebElement> cells = driver.findElements(By.xpath(String.format("//div[.='%s']/..//div[@role='cell']", issue.trim())));
        return cells.get(column).findElement(By.cssSelector(".cell-text")).getAttribute("textContent");
    }

    /**
     * Selects gcd
     *
     * @param gcd - the gcd
     * @return current page object
     */
    public DesignGuidanceController selectGcd(String gcd) {
        By byGcd = getBy(gcd);
        pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(byGcd), true).click();
        return this;
    }
}
