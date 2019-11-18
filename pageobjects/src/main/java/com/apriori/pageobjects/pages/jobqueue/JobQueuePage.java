package com.apriori.pageobjects.pages.jobqueue;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.utils.PageUtils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class JobQueuePage extends LoadableComponent<JobQueuePage> {

    private Logger logger = LoggerFactory.getLogger(JobQueuePage.class);

    @FindBy(css = ".table.table-striped")
    private WebElement jobQueueTable;

    @FindBy(css = "a[data-ap-comp='jobQueue']")
    private WebElement jobQueueButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public JobQueuePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(jobQueueTable);
    }

    /**
     * Opens the scenario from the job queue
     *
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @param jobType      - the job type
     * @return new page object
     */
    public EvaluatePage openScenarioLink(String scenarioName, String partName, String jobType) {
        By scenario = By.xpath("//div[.='" + StringUtils.capitalize(jobType) + "']/ancestor::tr//a[contains(@href,'#openFromQueue::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]");
        pageUtils.waitForElementAndClick(scenario);
        return new EvaluatePage(driver);
    }

    /**
     * Checks the job queue that the first job for the specified scenario is complete
     *
     * @param scenarioName - the scenario name
     * @param jobType      - the job type
     * @param icon         - icon can be 'okay' or 'stop'
     * @return true/false
     */
    public boolean isJobQueueActionDisplayed(String scenarioName, String jobType, String icon) {
        By jobStatus = By.xpath("//a[@title='" + scenarioName + "']/ancestor::tr//div[.='" + jobType + "']/ancestor::tr//img[@src='" + icon + "18.png']");
        return pageUtils.waitForElementToAppear(jobStatus, 2).isDisplayed();
    }
}