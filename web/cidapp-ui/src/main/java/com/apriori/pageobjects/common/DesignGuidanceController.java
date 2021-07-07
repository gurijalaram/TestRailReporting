package com.apriori.pageobjects.common;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class DesignGuidanceController {

    @FindBy(css = ".table-head .checkbox-icon")
    private WebElement gcdCheckbox;

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
        List<WebElement> cells = driver.findElements(By.xpath(String.format("//div[normalize-space()='%s']/..//div[@role='cell']", issue.trim())));
        return pageUtils.waitForElementToAppear(cells.get(column).findElement(By.cssSelector(".cell-text"))).getAttribute("textContent");
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

    /**
     * Selects dropdown
     *
     * @param issueType - the issue type
     * @return current page object
     */
    public DesignGuidanceController selectDropdown(String issueType) {
        String[] issue = issueType.split(",");

        Arrays.stream(issue).map(x -> pageUtils.waitForElementToAppear(getBy(x.trim())).findElement(By.cssSelector("svg[data-icon='chevron-down']")))
            .forEach(x -> pageUtils.scrollWithJavaScript(x, true).click());
        return this;
    }

    /**
     * Selects all gcd checkbox
     *
     * @return current page object
     */
    public DesignGuidanceController selectAllGcd() {
        if (!getCheckboxStatus().contains("check")) {
            pageUtils.waitForElementAndClick(gcdCheckbox);
        }
        return this;
    }

    /**
     * Deselects all gcd checkbox
     *
     * @return current page object
     */
    public DesignGuidanceController deSelectAllGcd() {
        if (!getCheckboxStatus().equals("square")) {
            pageUtils.waitForElementAndClick(gcdCheckbox);
        }
        return this;
    }

    /**
     * Gets status of header gcd checkbox
     *
     * @return string
     */
    private String getCheckboxStatus() {
        return pageUtils.waitForElementToAppear(gcdCheckbox.findElement(By.cssSelector("svg"))).getAttribute("data-icon");
    }
}
