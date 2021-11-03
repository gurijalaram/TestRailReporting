package com.apriori.pageobjects.pages.help;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class HelpPage extends LoadableComponent<HelpPage> {

    @FindBy(xpath = "//button[.='Support']")
    private WebElement supportElement;

    @FindBy(xpath = "//button[.='About aPriori']")
    private WebElement aboutApriori;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HelpPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(aboutApriori);
    }

    /**
     * Clicks the Support button
     *
     * @return new page object
     */
    public ZendeskSignInPage clickSupport() {
        pageUtils.waitForElementAndClick(supportElement);
        return new ZendeskSignInPage(driver);
    }

    /**
     * Clicks on the About aPriori button
     *
     * @return new page object
     */
    public AboutAprioriPage clickAboutApriori() {
        pageUtils.waitForElementAndClick(aboutApriori);
        return new AboutAprioriPage(driver);
    }
}