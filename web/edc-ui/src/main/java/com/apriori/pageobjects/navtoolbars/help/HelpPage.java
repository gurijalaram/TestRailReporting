package com.apriori.pageobjects.navtoolbars.help;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class HelpPage extends LoadableComponent<HelpPage> {

    @FindBy(css = "[data-icon='info-circle']")
    private WebElement aboutButton;

    @FindBy(css = "[data-icon='question-circle']")
    private WebElement helpButton;

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
        pageUtils.waitForElementToAppear(aboutButton);
    }

    /**
     * Click on About from Help drop down
     *
     * @return new page object
     */
    public AboutUsPage selectAbout() {
        pageUtils.waitForElementAndClick(aboutButton);
        return new AboutUsPage(driver);
    }

    /**
     * Click on Help from Help drop down
     *
     * @return new page object
     */
    public ZendeskSignInPage selectHelp() {
        pageUtils.waitForElementAndClick(helpButton);
        return new ZendeskSignInPage(driver);
    }
}
