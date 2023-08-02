package com.apriori.pageobjects.customer.users;

import com.apriori.components.RoutingComponent;
import com.apriori.pageobjects.customer.CustomerWorkspacePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the root of the users page that contains the tabs.
 */
@Slf4j
public class UsersPage extends CustomerWorkspacePage {
    private final RoutingComponent customerStaffTab;
    private final RoutingComponent importTab;
    private final RoutingComponent aPrioriStaffTab;
    private final RoutingComponent aPrioriAccessHistoryTab;
    @FindBy(xpath = "//a[.='Customer Staff']")
    private WebElement customerStaffTabRoot;
    @FindBy(xpath = "//a[.='Import']")
    private WebElement importTabRoot;
    @FindBy(xpath = "//a[.='aPriori Staff']")
    private WebElement aPrioriStaffTabRoot;
    @FindBy(xpath = "//a[.='aPriori Staff Access History']")
    private WebElement aPrioriAccessHistoryTabRoot;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public UsersPage(WebDriver driver) {
        super(driver);

        // Required Tabs
        customerStaffTab = new RoutingComponent(driver, customerStaffTabRoot);
        importTab = new RoutingComponent(driver, importTabRoot);

        // Optional Tabs
        aPrioriStaffTab = getPageUtils().isElementDisplayed(aPrioriStaffTabRoot) ? new RoutingComponent(driver, aPrioriStaffTabRoot) : null;
        aPrioriAccessHistoryTab = getPageUtils().isElementDisplayed(aPrioriAccessHistoryTabRoot) ? new RoutingComponent(driver, aPrioriAccessHistoryTabRoot) : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(customerStaffTabRoot);
        getPageUtils().waitForElementToAppear(importTabRoot);
    }

    /**
     * Navigates to the Customer Staff tab.
     *
     * @return The user list page.
     */
    public UsersListPage goToCustomerStaff() {
        customerStaffTab.navigate();
        return new UsersListPage(getDriver());
    }

    /**
     * Navigates to the Import tab.
     *
     * @return The Import page.
     */
    public ImportPage goToImport() {
        importTab.navigate();
        return new ImportPage(getDriver());
    }

    /**
     * Navigates to the aPriori Staff tab.
     *
     * @return The aPriori Staff tab.
     */
    public StaffPage goToStaffPage() {
        aPrioriStaffTab.navigate();
        return new StaffPage(getDriver());
    }

    /**
     * Navigates to the aPriori Staff History tab.
     *
     * @return The aPriori Staff History tab
     */
    public StaffAccessHistoryPage goToAccessHistory() {
        aPrioriAccessHistoryTab.navigate();
        return new StaffAccessHistoryPage(getDriver());
    }
}
