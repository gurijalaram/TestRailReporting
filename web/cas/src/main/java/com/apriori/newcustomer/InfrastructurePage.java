package com.apriori.newcustomer;

import com.apriori.customeradmin.NavToolbar;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfrastructurePage extends LoadableComponent<InfrastructurePage> {

    private final Logger logger = LoggerFactory.getLogger(InfrastructurePage.class);

    @FindBy(xpath = "//div[contains(text(),'Please select infrastructure within the Tree View')]")
    private WebElement noContentMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public InfrastructurePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(noContentMessage);
    }

    /**
     * Select the infrastructure dropdown
     *
     * @param infrastructures - the infrastructure
     * @return current page object
     */
    public InfrastructurePage selectInfrastructureDropdown(String infrastructures) {
        String[] listInfrastructure = infrastructures.split(",");

        for (String infrastructure : listInfrastructure) {
            By dropdown = By.xpath(String.format("//li[.='%s']//div[@class='rstm-toggle-icon-symbol']", infrastructure));
            pageUtils.waitForElementAndClick(dropdown);
        }
        return this;
    }

    /**
     * Select the application
     * @param application - the name
     * @return current page object
     */
    public InfrastructurePage selectApplication(String application) {
        By data = By.xpath(String.format("//li[.='%s']", application));
        pageUtils.waitForElementAndClick(data);
        return this;
    }

    /**
     * Get the data for each field
     * @param field - the field
     * @return string
     */
    public String getApplicationDetails(String field) {
        By fieldName = By.xpath(String.format("//div[contains(text(),'%s')]/ancestor::div[@class='py-2 ']", field));
        return pageUtils.waitForElementToAppear(fieldName).getText();
    }
}
