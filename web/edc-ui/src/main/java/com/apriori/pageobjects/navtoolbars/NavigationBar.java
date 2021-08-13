package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.navtoolbars.help.AboutUsPage;
import com.apriori.pageobjects.navtoolbars.help.HelpPage;
import com.apriori.pageobjects.navtoolbars.myuser.MyUserPage;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class NavigationBar extends LoadableComponent<NavigationBar> {


    @FindBy(css = ".help-dropdown")
    private WebElement helpDropdown;

    @FindBy(css = "[data-icon='info-circle']")
    private WebElement aboutButton;

    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    @FindBy(css = "[data-icon='sign-out-alt']")
    private WebElement logoutButton;

    @FindBy(css = "[data-icon='user")
    private WebElement myProfile;

    private PageUtils pageUtils;
    private WebDriver driver;

    public NavigationBar(WebDriver driver) {
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
     * Selects the help dropdown and go to Help
     *
     * @retun new page object
     * @return
     */
        public HelpPage clickHelpDropdown() {
                pageUtils.waitForElementAndClick(helpDropdown);
               return new HelpPage(driver);
           }

    /**
     * Select About button
     *
     * @return new page object
     */
    public AboutUsPage clickAbout() {
            pageUtils.waitForElementAndClick(helpDropdown);
            pageUtils.waitForElementAndClick(aboutButton);
            return new AboutUsPage(driver);
       }

    /**
     * Click on the User dropdown
     * @return
     */
    public MyUserPage clickUserDropdown() {
            pageUtils.waitForElementAndClick(userDropdown);
            pageUtils.waitForElementAndClick(myProfile);
            return new MyUserPage(driver);
        }
}

