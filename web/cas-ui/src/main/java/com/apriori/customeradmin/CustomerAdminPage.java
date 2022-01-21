package com.apriori.customeradmin;

import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SearchFieldComponent;
import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the root customer list.
 */
@Slf4j
public class CustomerAdminPage extends EagerPageComponent<CustomerAdminPage> {

    private static final String CUSTOMER_APRIORI_INTERNAL = "aPriori Internal";

    @FindBy(className = "customer-list-view-new-button")
    private WebElement newCustomerButton;

    @FindBy(className = "customer-list-view-source-list")
    private WebElement sourceListRoot;
    private final SourceListComponent customerSourceList;

    @FindBy(className = "apriori-source-list-layout-card-button")
    private WebElement cardViewButton;

    public CustomerAdminPage(WebDriver driver) {
        super(driver, log);
        this.customerSourceList = new SourceListComponent(getDriver(), sourceListRoot);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(sourceListRoot);
        getPageUtils().waitForElementAppear(newCustomerButton);
    }

    /**
     * Create a new customer
     *
     * @return new page object
     */
    public CustomerWorkspacePage clickNewCustomerButton() {
        getPageUtils().waitForElementAndClick(newCustomerButton);
        return new CustomerWorkspacePage(getDriver());
    }

    /**
     * Same as searchForCustomer("aPriori Internal").selectCustomer("aPriori Internal")
     *
     * @return The profile page for aPriori Internal
     */
    public CustomerWorkspacePage openAprioriInternal() {
        return openCustomer(CUSTOMER_APRIORI_INTERNAL);
    }

    /**
     * Opens a customer profile from source table
     *
     * @param name - name of customer
     * @return The profile page for customer
     */
    public CustomerWorkspacePage openCustomer(String name) {
        SourceListComponent list = getSourceList();
        SearchFieldComponent search = Obligation.mandatory(list::getSearch, "The customer search is missing.");
        search.search(name);
        getPageUtils().waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        list.selectTableLayout();
        Obligation.mandatory(list::getTable, "The table layout is not active")
            .getRows()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Customer %s is missing.", name)))
            .getCell("name")
            .click();

        return new CustomerWorkspacePage(getDriver());
    }

    /**
     * Gets the underlying source list for the admin page.
     *
     * @return The source list for the admin page.
     */
    public SourceListComponent getSourceList() {
        return customerSourceList;
    }

    /**
     * Clicks the card view button
     *
     * @return This object
     */
    public CustomerAdminPage clickCardViewButton() {
        getPageUtils().waitForElementAndClick(cardViewButton);
        return this;
    }

    /**
     * Gets locator of status icon
     *
     * @param customerIdentity the customer identity
     * @return web element
     */
    private WebElement findStatusIcon(String customerIdentity) {
        return getDriver().findElement(By.xpath(String.format("//a[@href='/customers/%s']//span[@class='float-right']",
                customerIdentity.toUpperCase().trim())));
    }

    /**
     * Checks is icon has a expected color
     *
     * @param customerIdentity the customer identity
     * @param color color of icon
     * @return true or false
     */
    public boolean isStatusIconColour(String customerIdentity, String color) {
        return getPageUtils().scrollWithJavaScript(findStatusIcon(customerIdentity)
                .findElement(By.cssSelector("svg")), true).getAttribute("color").equals(color);
    }
}
