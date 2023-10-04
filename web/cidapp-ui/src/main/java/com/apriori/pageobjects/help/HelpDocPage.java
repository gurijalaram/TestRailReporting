package com.apriori.pageobjects.help;

import com.apriori.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpDocPage extends LoadableComponent<HelpDocPage> {

    private static final Logger logger = LoggerFactory.getLogger(HelpDocPage.class);

    @FindBy(css = ".logo")
    private WebElement brandLogo;

    @FindBy(xpath = "//div[@id='gdpr']//button[.='Yes I Agree']")
    private WebElement agreeButton;

    @FindBy(css = "h1")
    private WebElement userGuideTitle;

    @FindBy(css = "h2")
    private WebElement designGuidanceUserGuideTitle;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HelpDocPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(brandLogo);
    }

    /**
     * Selects button to agree to gdpr
     * @return current page object
     */
    public HelpDocPage clickAgreeButton() {
        pageUtils.waitForElementAndClick(agreeButton);
        return this;
    }

    /**
     * Gets the page title
     * @return string
     */
    public String getChildPageTitle() {
        pageUtils.switchToWindow(1);
        return pageUtils.waitForElementToAppear(userGuideTitle).getText();

    }

    /**
     * Gets the page title
     * @return string
     */
    public String getDesignGuidanceChildPageTitle() {
        pageUtils.switchToWindow(1);
        return pageUtils.waitForElementToAppear(designGuidanceUserGuideTitle).getText();
    }
}
