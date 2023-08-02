package com.apriori.pageobjects.workflows.history;

import com.apriori.pageobjects.CICBasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Navigate to job details page from view history page
 * #### This class will be used in future when there are test cases like to view details of workflow job.####
 */
public class JobDetails extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(JobDetails.class);

    public JobDetails(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {

    }
}
