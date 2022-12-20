package com.apriori.utils.login;

import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CloudHomePage extends LoadableComponent<CloudHomePage> {

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "img[alt='Application Logo']")
    private WebElement logo;

    public CloudHomePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(logo);
    }

    /**
     * Opens profile user page
     *
     * @return UserProfilePage object
     */
    public UserProfilePage goToProfilePage() {
        isLoaded();
        driver.get(PropertiesContext.get("${env}.cloud.ui_url").concat("user-profile"));
        return new UserProfilePage(driver);
    }
}
