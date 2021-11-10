package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.CisComponentTableActions;
import com.apriori.pageobjects.pages.explore.ExplorePage;
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

    @FindBy(css = ".icon-button-group .disabled")
    private WebElement disabledStartComparison;

    private WebDriver driver;
    private PageUtils pageUtils;
    private CisComponentTableActions componentTableActions;

    public ExploreTabToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.componentTableActions = new CisComponentTableActions(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the Start comparison button
     *
     * @return current page object
     */
    public ExploreTabToolbar clickStartComparison() {
        pageUtils.waitForElementToAppear(startComparison);
        if (pageUtils.isElementEnabled(startComparison)) {
            pageUtils.waitForElementAndClick(startComparison);
        }
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

    /**
     * Check if the Start comparison button is enabled
     *
     * @return boolean
     */
    public boolean isStartComparisonEnabled() {
        return pageUtils.waitForElementToAppear(disabledStartComparison).isEnabled();
    }

    /**
     * Search for component
     *
     * @param componentName -the component
     * @return new page object
     */
    public ExplorePage clickSearch(String componentName) {
        componentTableActions.clickSearch(componentName);
        return new ExplorePage(driver);
    }
}
