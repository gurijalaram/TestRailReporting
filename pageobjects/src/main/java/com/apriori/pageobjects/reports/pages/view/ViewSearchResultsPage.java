package com.apriori.pageobjects.reports.pages.view;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewSearchResultsPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(ViewSearchResultsPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "body")
    private WebElement searchResultsPageTitle;

    @FindBy(id = "accessTypeFilter_item1")
    private WebElement allAvailableFilterButton;

    public ViewSearchResultsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Get page title text
     * @return String - page title text
     */
    public String getSearchResultsTitleText() {
        pageUtils.waitForElementToAppear(searchResultsPageTitle);
        return searchResultsPageTitle.getAttribute("id");
    }

    /**
     * Get name of a report
     * @return String - text of report name
     */
    public String getReportName(String reportName) {
        return pageUtils.getReportNameText(reportName);
    }

    public GenericReportPage waitForPageLoad() {
        pageUtils.waitForElementToAppear(allAvailableFilterButton);
        return new GenericReportPage(driver);
    }
}
