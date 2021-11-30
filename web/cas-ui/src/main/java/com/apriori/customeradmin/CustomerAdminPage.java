package com.apriori.customeradmin;

import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SearchFieldComponent;
import com.apriori.utils.web.components.SourceListComponent;

import lombok.extern.slf4j.Slf4j;
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
        SourceListComponent list = getSourceList();
        SearchFieldComponent search = Obligation.mandatory(list::getSearch, "The customer search is missing.");
        search.search(CUSTOMER_APRIORI_INTERNAL);
        getPageUtils().waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        list.selectTableLayout();
        Obligation.mandatory(list::getTable, "The table layout is not active")
            .getRows()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("aPriori Internal is missing."))
            .getCell("name")
            .click();

        return new CustomerWorkspacePage(getDriver());
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
            .orElseThrow(() -> new NoSuchElementException("customer is missing."))
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
}
