package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.navtoolbars.ExploreTabToolbar;
import com.apriori.pageobjects.navtoolbars.MainNavigationBar;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ExplorePage extends ExploreTabToolbar {

    @FindBy(css = ".deployment-connection-info")
    private WebElement deploymentInfo;

    private PageUtils pageUtils;
    private WebDriver driver;
    private MainNavigationBar navigationBar;

    public ExplorePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navigationBar = new MainNavigationBar(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(deploymentInfo);
    }
}
