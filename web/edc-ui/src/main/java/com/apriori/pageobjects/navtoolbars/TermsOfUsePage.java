package com.apriori.pageobjects.navtoolbars;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class TermsOfUsePage extends EagerPageComponent<TermsOfUsePage> {

    @FindBy(xpath = "//p[@class='main-text']")
    private WebElement termsText;

    public TermsOfUsePage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(termsText);
    }

    /**
     * Gets page Url
     *
     * @return String
     */
    public String getTermsOfUseUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Gets Terms Of Use text
     *
     * @return String
     */
    public String getTermsOfUseText() {
        return getPageUtils().waitForElementToAppear(termsText).getAttribute("textContent");
    }
}
