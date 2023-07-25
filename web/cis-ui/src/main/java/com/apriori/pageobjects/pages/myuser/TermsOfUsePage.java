package com.apriori.pageobjects.pages.myuser;

import com.apriori.EagerPageComponent;
import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class TermsOfUsePage extends EagerPageComponent<TermsOfUsePage> {

    @FindBy(xpath = "//p[@class='main-text']")
    private WebElement termsText;

    private  WebDriver driver;
    private  PageUtils pageUtils;

    public TermsOfUsePage(WebDriver driver) {
        super(driver, log);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets page Url
     *
     * @return String
     */
    public String getTermsOfUseUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets Terms Of Use text
     *
     * @return String
     */
    public String getTermsOfUseText() {
        return getPageUtils().waitForElementToAppear(termsText).getText();
    }
}
