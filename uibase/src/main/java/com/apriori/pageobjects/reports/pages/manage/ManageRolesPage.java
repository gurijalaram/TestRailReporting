package com.apriori.pageobjects.reports.pages.manage;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageRolesPage extends ReportsPageHeader {

    private Logger logger = LoggerFactory.getLogger(ManageRolesPage.class);

    @FindBy(xpath = "//div[@id='roles']/div/div[1]/div[contains(@class, 'title')]")
    private WebElement rolesPageTitle;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ManageRolesPage(WebDriver driver) {
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
     * Get page title text
     *
     * @return String - page title text
     */
    public String getRolesTitleText() {
        pageUtils.waitForElementToAppear(rolesPageTitle);
        return rolesPageTitle.getText();
    }
}
