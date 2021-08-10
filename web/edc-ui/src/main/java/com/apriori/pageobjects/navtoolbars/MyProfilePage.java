package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyProfilePage extends LoadableComponent<MyProfilePage> {

    private static final Logger logger = LoggerFactory.getLogger(MyProfilePage.class);


    @FindBy(css = ".user-dropdown.dropdown")
    private WebElement userDropdown;

    @FindBy(css = "[data-icon='sign-out-alt']")
    private WebElement logoutButton;

    @FindBy(css = ".active-account")
    private WebElement activeAccount;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public MyProfilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(activeAccount);
    }

    /**
     * Selects the User dropdown and go to Logout
     *
     * @retun new page object
     */
    public EdcAppLoginPage logout() {
        pageUtils.waitForElementAndClick(logoutButton);
        return new EdcAppLoginPage(driver);
    }
}
