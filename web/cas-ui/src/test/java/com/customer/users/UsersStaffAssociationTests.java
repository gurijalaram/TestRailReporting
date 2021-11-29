package com.customer.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;

import com.apriori.customer.users.StaffAddModal;
import com.apriori.customer.users.StaffPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.components.PaginatorComponent;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.components.TableRowComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UsersStaffAssociationTests extends TestBase {
    private static final String STAFF_TEST_USER = "staff-test-user";
    private List<User> sourceUsers;
    private Customer targetCustomer;
    private Customer aprioriInternal;
    private CdsTestUtil cdsTestUtil;
    private StaffPage staffPage;

    @Before
    public void setup() {
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String customerName = String.format("Test Customer %s", now);
        String email = "test.com";

        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        targetCustomer = cdsTestUtil.addCustomer(customerName, now, StringUtils.leftPad(now, 15, "0"), email).getResponseEntity();
        sourceUsers = new ArrayList<>();
        staffPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(targetCustomer.getIdentity())
            .goToUsersPage()
            .goToStaffPage();
    }

    @After
    public void teardown() {
        sourceUsers.forEach((user) -> cdsTestUtil.delete(
            CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS,
            aprioriInternal.getIdentity(),
            user.getIdentity()));

        cdsTestUtil.delete(
            CDSAPIEnum.DELETE_CUSTOMER_BY_ID,
            targetCustomer.getIdentity());
    }

    private void populateStaffTestUsers(int count) {
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        for (int i = 0; i < count; ++i) {
            String identity = aprioriInternal.getIdentity();
            String username = String.format("%s-%s-%d", STAFF_TEST_USER, now, i);
            User added = cdsTestUtil.addUser(identity, username, "apriori").getResponseEntity();
            sourceUsers.add(added);
        }
    }

    private long checkEveryOtherItem(List<TableRowComponent> rows, long pageSize) {
        long count = 0;
        for (int i = 0; i < pageSize; i += 2, ++count) {
            rows.get(i).check(true);
        }
        return count;
    }

    @Test
    @Description("Verify staff users can be added to the list.")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"5587", "5588", "5591"})
    public void testVerifyUsersCanBeAddedToTheList() {

        populateStaffTestUsers(21);

        StaffAddModal addModal = staffPage
            .clickAddFromList()
            .clickSecondary()
            .clickAddFromList()
            .clickClose()
            .clickAddFromList();

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;
        long selected = 0;
        SourceListComponent list = addModal.getUsers();
        TableComponent userTable = Obligation.mandatory(list::getTable, "The users table is missing from the add modal.");

        PaginatorComponent paginator = Obligation.mandatory(list::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        Obligation.mandatory(list::getSearch, "The user search functionality is missing.").search(STAFF_TEST_USER);
        utils.waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(userTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(userTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(list::isStable, PageUtils.DURATION_LOADING);

        userTable.getRows().findFirst().ifPresent((r) -> r.check(true));
        ++selected;
        paginator.clickFirstPage();

        StaffPage updated = addModal.clickPrimary()
            .clickSecondary()
            .clickPrimary()
            .clickClose()
            .clickPrimary()
            .clickPrimary();

        SourceListComponent staffList = updated.getStaffAssociationList();
        Obligation.mandatory(staffList::getSearch, "The staff list search functionality is missing.").search(STAFF_TEST_USER);
        utils.waitForCondition(staffList::isStable, PageUtils.DURATION_LOADING);
        Obligation.mandatory(staffList::getPaginator, "The staff list paginator is missing.").getPageSize().select("100");
        utils.waitForCondition(staffList::isStable, PageUtils.DURATION_LOADING);
        TableComponent staffTable = Obligation.mandatory(staffList::getTable, "The staff list table is missing.");
        long count = staffTable.getRows().count();
        assertThat(count, is(equalTo(selected)));
    }

    @Test
    @Description("Verify enable and disable.")
    @TestRail(testCaseId = {"5592, 5593"})
    public void testVerifyAssociatedStaffCanBeDisabledAndEnabled() {

        populateStaffTestUsers(2);
    }
}
