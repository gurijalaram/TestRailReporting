package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.myuser.MyUserPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

@Slf4j
public class LeftHandNavigationBar extends CisHeaderBar {



    @FindBy(xpath = "//button[@data-testid='non-collapsed']")
    private WebElement hamburgerIcon;

    @FindBy(xpath = "//div[@data-testid='list-subitem-text-left-menu.subTitle.parts-and-assemblies']")
    private WebElement btnPartsAndAssemblies;

    @FindBy(xpath = "//div[@data-testid='avatar']")
    private WebElement userIcon;

    public LeftHandNavigationBar(WebDriver driver) {
        this(driver, log);
    }

    public LeftHandNavigationBar(WebDriver driver, Logger logger) {
        super(driver, logger);

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

}
