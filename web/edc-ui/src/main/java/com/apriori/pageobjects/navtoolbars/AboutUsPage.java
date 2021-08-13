package com.apriori.pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AboutUsPage extends LoadableComponent<AboutUsPage> {

    private static final Logger logger = LoggerFactory.getLogger(AboutUsPage.class);

    @FindBy(id = "about_us_section")
    private WebElement aboutUs;

    @FindBy(xpath = "//div[@id='gdpr']//button[.='Yes I Agree']")
    private WebElement termsButton;

//    @FindBy (xpath = "//button[contains(@class,'drift-widget-message-close-button')]//svg")
    @FindBy(xpath = "//*[@id='root']/main/div[1]/div/button[1]/svg/path")
    private WebElement closeChatBox;

    @FindBy(xpath = "//div[contains(@class,'drift-widget-message-preview-wrapper')]")
    private WebElement moveMouseElement;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public AboutUsPage(WebDriver driver) {
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
//        pageUtils.waitForElementToAppear(aboutUs);

    }

    public AboutUsPage agreeTermsAndCondition() {
        pageUtils.waitForElementAndClick(termsButton);
        return this;
    }

    public String getAboutUsPageTitle() {
        return pageUtils.windowHandler(1).getTitle();
    }

    public AboutUsPage closeOnlineHelpChat() {
//        pageUtils.waitFor(30000);
        String script = "return document.querySelectorAll('button')[2].click();";
        JavascriptExecutor js =  (JavascriptExecutor) driver;
        js.executeScript(script);
        return this;
    }

    public AboutUsPage switchTab() {
        pageUtils.windowHandler(1);
        return new AboutUsPage(driver);
    }
}
