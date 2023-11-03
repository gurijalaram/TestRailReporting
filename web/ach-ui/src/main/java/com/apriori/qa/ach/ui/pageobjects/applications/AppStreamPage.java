package com.apriori.qa.ach.ui.pageobjects.applications;

import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.time.Duration;

@Slf4j
public class AppStreamPage extends LoadableComponent<AppStreamPage> {

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(xpath = "//div[@id='toolbar']")
    private WebElement toolbar;

    public AppStreamPage(WebDriver driver) {
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
        pageUtils.waitForCondition(() -> toolbar.isDisplayed(), Duration.ofMinutes(5));
        pageUtils.waitForElementAppear(toolbar);
    }
}
