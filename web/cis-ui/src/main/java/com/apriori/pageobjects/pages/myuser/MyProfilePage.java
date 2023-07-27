package com.apriori.pageobjects.pages.myuser;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MyProfilePage extends EagerPageComponent<MyProfilePage> {

    @FindBy(css = "div[class='apriori-card'] .card-header")
    private WebElement myProfile;

    public MyProfilePage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(myProfile);
    }
}
