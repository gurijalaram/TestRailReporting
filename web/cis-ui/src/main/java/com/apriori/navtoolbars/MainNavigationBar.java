package com.apriori.navtoolbars;

import com.apriori.EagerPageComponent;
import com.apriori.pageobjects.compare.ComparePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.help.HelpPage;
import com.apriori.pageobjects.myuser.MyUserPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

@Slf4j
public class MainNavigationBar extends EagerPageComponent<MainNavigationBar> {

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//button[.='Compare']")
    private WebElement compareButton;

    @FindBy(id = "qa-header-help-button")
    private WebElement helpDropdown;

    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    public MainNavigationBar(WebDriver driver) {
        this(driver, log);
    }

    public MainNavigationBar(WebDriver driver, Logger logger) {
        super(driver, logger);

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(helpDropdown);
    }

    /**
     * Navigates to the explore page
     *
     * @return new page object
     */
    public ExplorePage clickExplore() {
        getPageUtils().waitForElementAndClick(exploreButton);
        return new ExplorePage(getDriver());
    }

    /**
     * Navigates to the compare page
     *
     * @return new page object
     */
    public ComparePage clickCompare() {
        getPageUtils().waitForElementAndClick(compareButton);
        return new ComparePage(getDriver());
    }

    /**
     * Clicks on the Help dropdown
     *
     * @return new page object
     */
    public HelpPage clickHelpDropdown() {
        getPageUtils().waitForElementAndClick(helpDropdown);
        return new HelpPage(getDriver());
    }

    /**
     * Clicks on the User dropdown
     *
     * @return new page object
     */
    public MyUserPage clickUserDropdown() {
        getPageUtils().waitForElementAndClick(userDropdown);
        return new MyUserPage(getDriver());
    }
}
