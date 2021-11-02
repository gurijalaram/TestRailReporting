package com.apriori.pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ExploreTabToolbar extends MainNavigationBar {

    @FindBy(id = "start-comparison-button")
    private WebElement startComparison;

    @FindBy(css = ".deployment-connection-info")
    private WebElement deploymentInfo;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ExploreTabToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the Start comparison button
     *
     * @return current page object
     */
    public ExploreTabToolbar selectStartComparison() {
        pageUtils.waitForElementAndClick(startComparison);
        return this;
    }

    /**
     * Get the Deployment connection info
     *
     * @return String
     */
    public String getDeploymentInfo() {
        return pageUtils.waitForElementToAppear(deploymentInfo).getAttribute("textContent");
    }
}
