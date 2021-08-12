package com.apriori.pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HelpPage extends LoadableComponent<HelpPage> {
    private static final Logger logger = LoggerFactory.getLogger(HelpPage.class);

    @FindBy(css = "[data-icon='info-circle']")
    private WebElement aboutButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public HelpPage(WebDriver driver) {
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

    }

    public AboutUsPage selectAbout() {
        pageUtils.waitForElementAndClick(aboutButton);
        return new AboutUsPage(driver);
    }
}
