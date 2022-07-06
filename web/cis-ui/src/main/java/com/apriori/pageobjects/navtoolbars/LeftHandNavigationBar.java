package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.LetNavigationBarController;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class LeftHandNavigationBar extends CisHeaderBar {



    @FindBy(xpath = "//button[@data-testid='non-collapsed']")
    private WebElement hamburgerIcon;

    @FindBy(xpath = "//div[@data-testid='list-subitem-text-left-menu.subTitle.parts-and-assemblies']")
    private WebElement btnPartsAndAssemblies;

    @FindBy(xpath = "//div[@data-testid='avatar']")
    private WebElement userIcon;

    @FindBy(xpath = "//div[@data-testid='list-subitem-left-menu.subTitle.dashboard']")
    private WebElement btnDashboard;

    @FindBy(xpath = "//img[@data-testid='main-logo']")
    private WebElement aprioriLogo;

    @FindBy(xpath = "//img[@data-testid='collapsed-logo']")
    private WebElement collapsedAprioriLogo;


    private LetNavigationBarController letNavigationBarController;

    public LeftHandNavigationBar(WebDriver driver) {
        this(driver, log);
    }

    public LeftHandNavigationBar(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.letNavigationBarController = new LetNavigationBarController(driver);

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets the navigation panel state
     *
     * @return String
     */
    public String getNavigationPanelDefaultState() {
        return getPageUtils().waitForElementToAppear(hamburgerIcon).getAttribute("data-testid");
    }

    /**
     * Collapse the navigation panel
     *
     * @return current page object
     */

    public LeftHandNavigationBar collapseNavigationPanel() {
        getPageUtils().waitForElementToAppear(hamburgerIcon).click();
        return this;
    }

    /**
     * Navigate to parts and assemblies page
     *
     * @return new page object
     */

    public PartsAndAssembliesPage clickPartsAndAssemblies() {
        getPageUtils().waitForElementToAppear(btnPartsAndAssemblies).click();
        return new PartsAndAssembliesPage(getDriver());
    }

    /**
     * Click the user icon
     *
     * @return current page object
     */

    public LeftHandNavigationBar clickUserIcon() {
        getPageUtils().waitForElementAndClick(userIcon);
        return this;
    }

    /**
     * Navigate to dashboard page
     *
     * @return new page object
     */
    public LeftHandNavigationBar clickDashBoard() {
        getPageUtils().waitForElementAndClick(btnDashboard);
        return this;
    }

    /**
     * Checks if aPriori logo displayed
     *
     * @return true/false
     */
    public boolean isaPrioriLogoDisplayed() {
        getPageUtils().waitForElementToAppear(aprioriLogo);
        return getPageUtils().isElementDisplayed(aprioriLogo);
    }

    /**
     * Checks if dashboard navbar item displayed
     *
     * @return true/false
     */
    public boolean isDashboardBtnDisplayed() {
        getPageUtils().waitForElementToAppear(btnDashboard);
        return getPageUtils().isElementDisplayed(btnDashboard);
    }

    /**
     * Get Dashboard state as default selected
     *
     * @return String
     */
    public String getDashBoardBtnDefaultState() {
        return getPageUtils().waitForElementToAppear(btnDashboard).getAttribute("class");
    }

    /**
     * Checks if  collapsed aPriori logo displayed
     *
     * @return true/false
     */
    public boolean isCollapsedAprioriLogoDisplayed() {
        return getPageUtils().isElementDisplayed(collapsedAprioriLogo);
    }

    /**
     * Get Nav bar items on each section
     *
     * @return list of string
     */
    public List<String> getItemsOfSections(String section) {
        return letNavigationBarController.getItemsOfSections(section);
    }

}
