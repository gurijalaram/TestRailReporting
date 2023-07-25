package com.apriori.pageobjects.navtoolbars;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MyProfilePage extends EagerPageComponent<MyProfilePage> {

    @FindBy(css = "div[class='panel'] .panel-title")
    private WebElement myProfile;

    @FindBy(id = "name")
    private WebElement username;

    public MyProfilePage(WebDriver driver) {
        super(driver, log);

    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(myProfile);
    }

    /**
     * Get Current url
     *
     * @return String
     */
    public String getUserProfileUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Check text field is read only
     *
     * @return boolean
     */
    public boolean isTextFieldEnabled() {
        return getPageUtils().waitForElementToAppear(username).isEnabled();
    }
}
