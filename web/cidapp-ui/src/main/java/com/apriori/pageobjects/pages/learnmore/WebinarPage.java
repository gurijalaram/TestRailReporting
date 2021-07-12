package com.apriori.pageobjects.pages.learnmore;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebinarPage extends LoadableComponent<WebinarPage> {

    private static final Logger logger = LoggerFactory.getLogger(WebinarPage.class);
    private WebDriver driver;
    private PageUtils pageUtils;


    public WebinarPage(WebDriver driver) {
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

    public String getWebinarTitle() {
        return driver.getTitle();
    }
}
