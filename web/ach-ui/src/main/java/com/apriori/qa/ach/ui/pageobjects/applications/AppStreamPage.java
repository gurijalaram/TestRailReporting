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
        final String resourceNotAvailableWarning = "No streaming resources";

        if (isDisplayedResourceNotAvailableWarning(resourceNotAvailableWarning)) {
            pageUtils.waitFor(Duration.ofMinutes(3).toMillis()); // give a time for the streaming resource to appear
            pageUtils.waitForElementAndClick(By.xpath("//button[@id='modal-alert-retry']"));

            if(isDisplayedResourceNotAvailableWarning(resourceNotAvailableWarning)) {
                log.warn("'{}' Message. Skipped aP Pro (AppStream) validation.", resourceNotAvailableWarning);
                return;
            }
        }

        pageUtils.waitForElementToAppear(
            By.xpath("//div[@id='toolbar-content' and @ng-show='toolbarOptions.isVisible']"),
            Duration.ofMinutes(3));
    }

    public Boolean isDisplayedResourceNotAvailableWarning(final String messageToCheck) {
        return pageUtils.waitForElementToAppear(By.xpath("//h1[contains(text(), '" + messageToCheck + "')]")).isDisplayed();
    }
}
