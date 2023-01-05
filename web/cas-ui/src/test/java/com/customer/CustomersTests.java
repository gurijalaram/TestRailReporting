package com.customer;

import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.components.CardsViewComponent;
import com.apriori.utils.web.components.PaginatorComponent;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class CustomersTests extends TestBase {
    private CustomerAdminPage customerAdminPage;
    private Customer aprioriInternal;
    private CdsTestUtil cdsTestUtil;
    String customerIdentity;
    String customerSalesforceID;

    @Before
    public void setup() {
        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        customerIdentity = aprioriInternal.getIdentity();
        customerSalesforceID = aprioriInternal.getSalesforceId();
        customerAdminPage = new CasLoginPage(driver)
            .login(UserUtil.getUser());
    }

    @Test
    @Description("Validate customers table has correct details")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"5594", "5596", "9943", "5555", "6267", "6268", "6269"})
    public void testCustomersTableViewHasCorrectDetails() {
        SoftAssertions soft = new SoftAssertions();
        customerAdminPage
            .validateCustomersTableArePageableRefreshable(soft)
            .validateCustomersTableHasCorrectColumns("Customer name", "name", soft)
            .validateCustomersTableHasCorrectColumns("Customer Type", "customerType", soft)
            .validateCustomersTableHasCorrectColumns("Identity", "identity", soft)
            .validateCustomersTableHasCorrectColumns("Salesforce ID", "salesforceId", soft)
            .validateCustomersTableHasCorrectColumns("Cloud Reference", "cloudReference", soft)
            .validateCustomersTableHasCorrectColumns("Email Domains", "emailDomains", soft)
            .validateCustomersTableHasCorrectColumns("Authentication", "authenticationType", soft)
            .validateCustomersTableHasCorrectColumns("Created At", "createdAt", soft)
            .validateCustomersTableHasCorrectColumns("Created By", "createdByName", soft)
            .validateCustomersTableHasCorrectColumns("Last Updated", "updatedAt", soft)
            .validateCustomersTableHasCorrectColumns("Updated By", "updatedByName", soft)
            .validateCustomersTableIsSortable("name", soft)
            .validateCustomersTableIsSortable("identity", soft)
            .validateCustomersTableIsSortable("salesforceId", soft)
            .validateCustomersTableIsSortable("cloudReference", soft)
            .validateCustomersTableIsSortable("createdAt", soft)
            .validateCustomersTableIsSortable("updatedAt", soft);

        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent customers = customerAdminPage.getSourceList();
        PaginatorComponent paginator = Obligation.mandatory(customers::getPaginator, "The customers table is missing pagination.");
        paginator.getPageSize().select("10");
        paginator.clickNextPage();
        TableComponent customersTable = Obligation.mandatory(customers::getTable, "The customers list table is missing");

        long rows = customersTable.getRows().count();
        soft.assertThat(rows).overridingErrorMessage("There are no customers on next page.").isGreaterThan(0L);

        paginator.clickFirstPage().getPageSize().select("20");
        utils.waitForCondition(customers::isStable, PageUtils.DURATION_LOADING);
        paginator.getPageSize().select("50");
        utils.waitForCondition(customers::isStable, PageUtils.DURATION_LOADING);

        Obligation.mandatory(customers::getSearch, "Customers list search is missing").search("aPriori Internal");

        SourceListComponent searchResult = customerAdminPage.getSourceList();
        TableComponent customerFound = Obligation.mandatory(searchResult::getTable, "The customer was not found");
        long count = customerFound.getRows().count();
        soft.assertThat(count).isEqualTo(1L);

        customers.getSearch().search(customerIdentity);
        long identityFound = customerFound.getRows().count();
        soft.assertThat(identityFound).isEqualTo(1L);

        customers.getSearch().search(customerSalesforceID);
        long salesforceFound = customerFound.getRows().count();
        soft.assertThat(salesforceFound).isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @Description("Validate Card button switches to card view of customers")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"13249", "13250", "13251"})
    public void testCustomerStaffCardView() {
        SoftAssertions soft = new SoftAssertions();
        CustomerAdminPage goToCardView = customerAdminPage
            .clickCardViewButton();

        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent customers = goToCardView.getSourceList();

        PaginatorComponent paginator = Obligation.mandatory(customers::getPaginator, "The customers page is missing pagination.");
        paginator.getPageSize().select("10");

        CardsViewComponent customersGrid = Obligation.mandatory(customers::getCardGrid, "The customers grid is missing");

        long cards = customersGrid.getCards("apriori-card").count();
        soft.assertThat(cards).isEqualTo(10L);
        utils.waitForCondition(customersGrid::isStable, PageUtils.DURATION_LOADING);

        Obligation.mandatory(customers::getSearch, "Customers search is missing").search(customerIdentity);
        utils.waitForCondition(customers::isStable, PageUtils.DURATION_LOADING);

        soft.assertThat(customerAdminPage.getFieldName())
                .containsExactly("Salesforce ID:", "Cloud Reference:", "Customer Type:", "Created:");
        soft.assertThat(customerAdminPage.isStatusIconColour("green")).isTrue();
        soft.assertAll();
    }
}