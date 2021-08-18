package com.apriori.pageobjects.navtoolbars.myuser;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class MyProfilePage extends LoadableComponent<MyProfilePage> {

    @FindBy(css = "div[class='panel'] .panel-title")
    private WebElement myProfile;

    @FindBy(id = "name")
    private WebElement username;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public MyProfilePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(myProfile);
    }

    /**
     * Get Current url
     *
     * @return String
     */
    public String getUserProfileUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Check text field is read only
     *
     * @return boolean
     */
    public boolean isTextFieldEnabled() {
        return pageUtils.waitForElementToAppear(username).isEnabled();
    }
}
