package com.apriori.customeradmin;

import com.apriori.common.UsersTableController;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.SearchFieldComponent;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.components.TableHeaderComponent;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the root customer list.
 */
@Slf4j
public class CustomerAdminPage extends NavToolbar {

    private static final String CUSTOMER_APRIORI_INTERNAL = "aPriori Internal";

    @FindBy(className = "customer-list-view-new-button")
    private WebElement newCustomerButton;

    @FindBy(className = "customer-list-view-source-list")
    private WebElement sourceListRoot;
    private final SourceListComponent customerSourceList;

    @FindBy(css = ".apriori-source-list-layout-table")
    private WebElement customersTableRoot;
    private final SourceListComponent customersTable;

    @FindBy(className = "apriori-source-list-layout-card-button")
    private WebElement cardViewButton;

    private UsersTableController usersTableController;

    public CustomerAdminPage(WebDriver driver) {
        super(driver);
        this.customerSourceList = new SourceListComponent(driver, sourceListRoot);
        this.customersTable = new SourceListComponent(driver, customersTableRoot);
        usersTableController = new UsersTableController(driver);
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
     * Gets the customers table.
     *
     * @return The customers list.
     */
    public SourceListComponent getCustomersTable() {
        getPageUtils().waitForCondition(customersTable::isStable, getPageUtils().DURATION_LOADING);
        return customersTable;
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
     * Checks is icon has an expected color
     *
     * @param color color of icon
     * @return true or false
     */
    public boolean isStatusIconColour(String color) {
        return getPageUtils().waitForElementToAppear(By.cssSelector(".apriori-card.card.customer-card.light.medium-card svg[role='img']"))
            .getAttribute("color").equals(color);
    }

    /**
     * Validates that table is pageable and refreshable
     *
     * @param soft soft assertions
     * @return This object
     */
    public CustomerAdminPage validateCustomersTableArePageableRefreshable(SoftAssertions soft) {
        return usersTableController.validateUsersTableArePageableAndRefreshable(soft, CustomerAdminPage.class);
    }

    /**
     * Validates that table has a correct columns
     *
     * @param expectedName name of column
     * @param id           id of column
     * @param soft         soft assertions
     * @return This object
     */
    public CustomerAdminPage validateCustomersTableHasCorrectColumns(String expectedName, String id, SoftAssertions soft) {
        SourceListComponent list = getCustomersTable();
        TableComponent table = Obligation.mandatory(list::getTable, "The customers table is missing");

        TableHeaderComponent header = table.getHeader(id);
        soft.assertThat(header)
            .overridingErrorMessage(String.format("The '%s' column is missing.", expectedName))
            .isNotNull();

        if (header != null) {
            String name = header.getName();
            soft.assertThat(name)
                .overridingErrorMessage(String.format("The '%s' column is incorrectly named '%s'", expectedName, name))
                .isEqualTo(expectedName);
        }
        return this;
    }

    /**
     * Validates that table columns are sortable
     *
     * @param id   - id of column
     * @param soft - soft assertions
     * @return This object
     */
    public CustomerAdminPage validateCustomersTableIsSortable(String id, SoftAssertions soft) {
        SourceListComponent list = getCustomersTable();
        TableComponent table = Obligation.mandatory(list::getTable, "The customers table is missing");
        TableHeaderComponent header = table.getHeader(id);
        if (header != null) {
            String name = header.getName();
            soft.assertThat(header.canSort())
                .overridingErrorMessage(String.format("The '%s' column is not sortable.", name))
                .isTrue();
        }
        return this;
    }

    /**
     * Gets field names of customer card
     *
     * @return list of names
     */
    public List<String> getFieldName() {
        List<WebElement> fieldsName = getDriver().findElements(By.cssSelector(".display-property-item-name.form-label.m-0"));
        return fieldsName.stream().map(x -> x.getAttribute("textContent")).collect(Collectors.toList());
    }
}
