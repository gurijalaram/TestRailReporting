package com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * To validate write fields tab in publish results part during workflow creation
 * * #### This class will be used in future when there are test cases like write fields back to PLM during workflow creation.####
 */
public class WriteFieldsTab extends PublishResultsPart {

    private static final Logger logger = LoggerFactory.getLogger(WriteFieldsTab.class);

    public WriteFieldsTab(WebDriver driver) {
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
