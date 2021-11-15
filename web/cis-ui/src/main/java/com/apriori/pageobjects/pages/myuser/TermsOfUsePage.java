package com.apriori.pageobjects.pages.myuser;

import com.apriori.utils.web.components.EagerPageComponent;

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
}
