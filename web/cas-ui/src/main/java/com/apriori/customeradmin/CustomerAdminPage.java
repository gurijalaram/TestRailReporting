package com.apriori.customeradmin;

import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerAdminPage extends LoadableComponent<CustomerAdminPage> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAdminPage.class);

    @FindBy(className = "customer-list-view-new-button")
    private WebElement newCustomerButton;

    @FindBy(id = "qa-customer-counter")
    private WebElement customerCount;

    @FindBy(css = "svg[data-icon='list']")
    private WebElement listViewButton;

    @FindBy(css = "svg[data-icon='th']")
    private WebElement tileViewButton;

    @FindBy(css = "div[class='input-group select-input customer-type']")
    private WebElement custTypeDropdown;

    @FindBy(css = ".customer-list-view input[name='search']")
    private WebElement customerSearchInput;

    @FindBy(css = ".customer-list-view .loader")
    private WebElement customerListLoader;

    @FindBy(id = "qa-page-size-dropdown")
    private WebElement pageSizeDropdown;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public CustomerAdminPage(WebDriver driver) {
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
        pageUtils.waitForElementAppear(newCustomerButton);
    }

    /**
     * Checks the customers button is present
     *
     * @return true/false
     */
    public boolean isNewCustomerButtonPresent() {
        return pageUtils.waitForElementToAppear(newCustomerButton).isDisplayed();
    }

    /**
     * Create a new customer
     *
     * @return new page object
     */
    public CustomerWorkspacePage clickNewCustomerButton() {
        pageUtils.waitForElementAndClick(newCustomerButton);
        return new CustomerWorkspacePage(driver);
    }

    /**
     * Same as searchForCustomer("aPriori Internal").selectCustomer("aPriori Internal")
     *
     * @return The profile page for aPriori Internal
     */
    public CustomerWorkspacePage openAprioriInternal() {
        String name = "aPriori Internal";
        return searchForCustomer(name).selectCustomer(name);
    }

    /**
     * Select a customer
     *
     * @param customerName - customer name
     * @return new page object
     */
    public CustomerWorkspacePage selectCustomer(String customerName) {
        By customer = By.xpath(String.format("//a[.='%s']", customerName));
        pageUtils.waitForElementAndClick(customer);
        return new CustomerWorkspacePage(driver);
    }

    /**
     * Select customer type
     *
     * @param customerType - customer type
     * @return current page object
     */
    public CustomerAdminPage selectCustomerType(String customerType) {
        pageUtils.selectDropdownOption(custTypeDropdown, customerType);
        return this;
    }

    /**
     * Search for customer
     *
     * @param customer - customer details
     * @return current page object
     */
    public CustomerAdminPage searchForCustomer(String customer) {
        pageUtils.setValueOfElement(customerSearchInput, customer, Keys.ENTER);
        return this;
    }

    /**
     * Get customer count
     *
     * @return string
     */
    public String getCustomerCount() {
        return pageUtils.waitForElementToAppear(customerCount).getText();
    }
}
