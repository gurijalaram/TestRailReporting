package com.apriori.qa.ach.ui.pageobjects.applications;

import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.time.Duration;

@Slf4j
public class AppStreamPage extends LoadableComponent<AppStreamPage> {

    private PageUtils pageUtils;
    private WebDriver driver;

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
        final String resourceNotAvailableWarning = "No streaming resources ara available.";
//        if (pageUtils.waitForElementToAppear(By.xpath("//h1[contains(text(), '" + resourceNotAvailableWarning + "')]")).isDisplayed()) {
//            log.warn("Was: '{}' Message. Skipped aP Pro (AppStream) validation. ", resourceNotAvailableWarning);
//            pageUtils.waitFor();
//            return;
//        }

        pageUtils.waitForElementToAppear(
            By.xpath("//div[@id='toolbar-content' and @ng-show='toolbarOptions.isVisible']"),
            Duration.ofMinutes(3));
    }
}
