package com.apriori.pageobjects.navtoolbars;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AboutUsPage extends LoadableComponent<AboutUsPage> {

    private static final Logger logger = LoggerFactory.getLogger(AboutUsPage.class);

    @FindBy(id = "about_us_section")
    private WebElement aboutUs;

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
        pageUtils.waitForElementToAppear(aboutUs);

    }

}
