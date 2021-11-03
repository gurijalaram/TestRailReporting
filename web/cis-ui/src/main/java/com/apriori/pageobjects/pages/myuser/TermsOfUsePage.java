package com.apriori.pageobjects.pages.myuser;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class TermsOfUsePage extends LoadableComponent<TermsOfUsePage> {

    @FindBy(xpath = "//p[@class='main-text']")
    private WebElement termsText;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public TermsOfUsePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(termsText);
    }
}
