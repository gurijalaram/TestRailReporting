package com.apriori.pageobjects.pages.create;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.ReportsPageHeader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateReportPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(CreateReportPage.class);

    @FindBy(id = "reportGeneratorProperties")
    private WebElement createReportDialog;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public CreateReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Gets dialog isDisplayed value
     *
     * @return boolean - isDisplayed
     */
    public boolean isDialogDisplayed() {
        pageUtils.waitForElementToAppear(createReportDialog);
        return createReportDialog.isDisplayed();
    }

    /**
     * Gets dialog isEnabled value
     *
     * @return boolean - is Enabled
     */
    public boolean isDialogEnabled() {
        pageUtils.waitForElementToAppear(createReportDialog);
        return createReportDialog.isEnabled();
    }
}
