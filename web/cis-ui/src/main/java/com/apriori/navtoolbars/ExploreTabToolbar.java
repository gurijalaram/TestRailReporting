package com.apriori.navtoolbars;

import com.apriori.common.CisComponentTableActions;
import com.apriori.pageobjects.explore.ExplorePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class ExploreTabToolbar extends MainNavigationBar {

    @FindBy(id = "start-comparison-button")
    private WebElement startComparison;

    @FindBy(css = ".deployment-connection-info")
    private WebElement deploymentInfo;

    @FindBy(css = ".icon-button-group .disabled")
    private WebElement disabledStartComparison;

    private CisComponentTableActions componentTableActions;

    public ExploreTabToolbar(WebDriver driver) {
        super(driver, log);
        this.componentTableActions = new CisComponentTableActions(driver);
    }

    /**
     * Clicks the Start comparison button
     *
     * @return current page object
     */
    public CompareTabToolbar clickStartComparison() {
        getPageUtils().waitForElementToAppear(startComparison);
        if (getPageUtils().isElementEnabled(startComparison)) {
            getPageUtils().waitForElementAndClick(startComparison);
        }
        return new CompareTabToolbar(getDriver());
    }

    /**
     * Get the Deployment connection info
     *
     * @return String
     */
    public String getDeploymentInfo() {
        return getPageUtils().waitForElementToAppear(deploymentInfo).getAttribute("textContent");
    }

    /**
     * Check if the Start comparison button is enabled
     *
     * @return boolean
     */
    public boolean isStartComparisonEnabled() {
        return getPageUtils().waitForElementToAppear(disabledStartComparison).isEnabled();
    }

    /**
     * Search for component
     *
     * @param componentName -the component
     * @return new page object
     */
    public ExplorePage clickSearch(String componentName) {
        componentTableActions.clickSearch(componentName);
        return new ExplorePage(getDriver());
    }

    /**
     * Get the value of the start comparison on the page
     *
     * @return - String
     */
    public String getStartComparisonText() {
        return getPageUtils().waitForElementToAppear(startComparison).getAttribute("textContent");
    }
}
