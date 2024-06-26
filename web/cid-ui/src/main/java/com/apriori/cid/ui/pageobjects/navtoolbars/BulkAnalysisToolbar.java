package com.apriori.cid.ui.pageobjects.navtoolbars;

import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class BulkAnalysisToolbar extends ExploreToolbar {

    private PageUtils pageUtils;
    private WebDriver driver;

    public BulkAnalysisToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }
}
