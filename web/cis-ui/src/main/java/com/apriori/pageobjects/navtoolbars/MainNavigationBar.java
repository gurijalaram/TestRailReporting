package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class MainNavigationBar extends LoadableComponent<MainNavigationBar> {

    @FindBy(xpath = "//button[.='Explore']")
    private WebElement exploreButton;

    @FindBy(xpath = "//button[.='Compare']")
    private WebElement compareButton;

    @FindBy(id = "qa-header-help-button")
    private WebElement helpDropdown;

    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    private PageUtils pageUtils;
    private WebDriver driver;

    public MainNavigationBar(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(helpDropdown);
    }

    /**
     * Navigates to the explore page
     *
     * @return new page object
     */
    public ExplorePage clickExplore() {
        pageUtils.waitForElementAndClick(exploreButton);
        return new ExplorePage(driver);
    }

    /**
     * Navigates to the compare page
     *
     * @return new page object
     */
    public ComparePage clickCompare() {
        pageUtils.waitForElementAndClick(compareButton);
        return new ComparePage(driver);
    }

    public HelpPage clickHelpdropdown() {
        pageUtils.waitForElementAndClick(helpDropdown);
        return new HelpPage(driver);
    }
}
