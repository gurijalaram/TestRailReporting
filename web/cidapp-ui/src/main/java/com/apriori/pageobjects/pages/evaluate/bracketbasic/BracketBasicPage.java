package com.apriori.pageobjects.pages.evaluate.bracketbasic;

import com.apriori.pageobjects.pages.learnmore.WebinarPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.users.UserUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BracketBasicPage extends LoadableComponent<WebinarPage> {

    private static final Logger logger = LoggerFactory.getLogger(WebinarPage.class);
    private WebDriver driver;
    private PageUtils pageUtils;


    public BracketBasicPage(WebDriver driver) {
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
//        pageUtils.waitForElementToAppear(aboutaPrioriButton);
//        pageUtils.waitForElementToAppear(userGuideButton);
    }

    public void loginApp(){


    }

}
