package com.apriori.pages.workflows.history;

import com.apriori.pages.CICBasePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Navigate to job details page from view history page
 * #### This class will be used in future when there are test cases like to view details of workflow job.####
 */
@Slf4j
public class JobDetails extends CICBasePage {

    @FindBy(xpath = "//div/span[.='Job Details']")
    private WebElement jobDetailsTitleElement;

    public JobDetails(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementAppear(jobDetailsTitleElement);
    }

    public WebElement getStartedAtElement() {
        return driver.findElement(By.cssSelector("#root_pagemashupcontainer-1_navigation-94-popup_label-181 > span"));
    }
}
