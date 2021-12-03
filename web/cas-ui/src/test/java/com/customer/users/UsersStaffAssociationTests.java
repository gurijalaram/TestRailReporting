package com.customer.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.utils.CdsTestUtil;

import com.apriori.customer.users.StaffAddModal;
import com.apriori.customer.users.StaffPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.components.CheckboxComponent;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersStaffAssociationTests extends TestBase {
    private static final String STAFF_TEST_CUSTOMER = "Staff Association Test Customer";
    private static final String STAFF_TEST_USER = "staff-test-user";
    private Customer targetCustomer;
    private Customer aprioriInternal;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private StaffPage staffPage;

    @Before
    public void setup() {
        Map<String, Object> existingUsers = Collections.singletonMap("username[CN]", STAFF_TEST_USER);
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", STAFF_TEST_CUSTOMER);
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String salesforce = StringUtils.leftPad(now, 15, "0");
        String email = "test.com";

        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        sourceUsers = new ArrayList<>(cdsTestUtil.findAll(
            CDSAPIEnum.GET_USERS_BY_CUSTOMER_ID,
            Users.class,
            existingUsers,
            Collections.emptyMap(),
            aprioriInternal.getIdentity()
        ));

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.GET_CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
            ? cdsTestUtil.addCustomer(STAFF_TEST_CUSTOMER, now, salesforce, email).getResponseEntity()
            : targetCustomer;

        staffPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(targetCustomer.getIdentity())
            .goToUsersPage()
            .goToStaffPage();
    }

    @After
    public void teardown() {
        sourceUsers.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, aprioriInternal.getIdentity(), user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    private void populateStaffTestUsers(int count) {
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        for (int i = sourceUsers.size(); i < count; ++i) {
            String identity = aprioriInternal.getIdentity();
            String username = String.format("%s-%s-%s", STAFF_TEST_USER, now, i);
            User added = cdsTestUtil.addUser(identity, username, "apriori").getResponseEntity();
            sourceUsers.add(added);
        }
    }

    private long checkEveryOtherItem(List<TableRowComponent> rows, long pageSize) {
        long count = 0;
        for (int i = 0; i < pageSize; i += 2, ++count) {
            final TableRowComponent row = rows.get(i);
            Obligation.mandatory(row::getCheck, "The check cell is missing").check(true);
        }
        return count;
    }

    @Test
    @Description("Verify staff users can be added to the list.")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"5587", "5588", "5589", "5590", "5591"})
    public void testVerifyUsersCanBeAddedToTheList() {

        populateStaffTestUsers(21);

        StaffPage addModal = staffPage
            .clickAddFromList()
            .clickCandidatesCancelButton()
            .clickAddFromList()
            .clickCandidatesCloseButton()
            .clickAddFromList();

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;
        long selected = 0;
        SourceListComponent userCandidates = addModal.getCandidates();
        TableComponent userCandidatesTable = Obligation.mandatory(userCandidates::getTable, "The candidates table is missing from the add modal.");

        PaginatorComponent paginator = Obligation.mandatory(userCandidates::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        Obligation.mandatory(userCandidates::getSearch, "The user search functionality is missing.").search(STAFF_TEST_USER);
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(userCandidatesTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(userCandidatesTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        userCandidatesTable.getRows().findFirst().ifPresent((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));
        ++selected;
        paginator.clickFirstPage().getPageSize().select("50").select("100");
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        long expected = userCandidatesTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        assertThat("The selection is not holding across pages.", expected, is(equalTo(expected)));

        StaffPage updated = addModal
            .clickCandidatesAddButton()
            .clickCandidatesConfirmCancelButton()
            .clickCandidatesAddButton()
            .clickCandidatesConfirmCloseButton()
            .clickCandidatesAddButton()
            .clickCandidatesConfirmOkButton();

        SourceListComponent staffList = updated.getStaffAssociationList();
        Obligation.mandatory(staffList::getSearch, "The staff list search functionality is missing.").search(STAFF_TEST_USER);
        Obligation.mandatory(staffList::getPaginator, "The staff list paginator is missing.").getPageSize().select("50").select("100");
        utils.waitForCondition(staffList::isStable, PageUtils.DURATION_LOADING);
        TableComponent staffTable = Obligation.mandatory(staffList::getTable, "The staff list table is missing.");
        long count = staffTable.getRows().count();
        assertThat(count, is(equalTo(selected)));
    }

    @Test
    @Description("Verify enable and disable.")
    @TestRail(testCaseId = {"5592", "5593"})
    public void testVerifyAssociatedStaffCanBeDisabledAndEnabled() {

        populateStaffTestUsers(2);

        PageUtils utils = new PageUtils(getDriver());

        StaffPage addModal = staffPage.clickAddFromList();
        SourceListComponent candidates = addModal.getCandidates();
        TableComponent candidatesTable = Obligation.mandatory(candidates::getTable, "The candidate table is missing.");
        Obligation.mandatory(candidates::getSearch, "The candidate search feature is missing.").search(STAFF_TEST_USER);
        utils.waitForCondition(candidates::isStable, PageUtils.DURATION_LOADING);
        Obligation.mandatory(candidatesTable::getCheckHeader, "The candidates checkbox header is missing.").check(true);
        StaffPage updated = addModal.clickCandidatesAddButton().clickCandidatesConfirmOkButton();

        SourceListComponent staffList = updated.getStaffAssociationList();
        TableComponent staffTable = Obligation.mandatory(staffList::getTable, "The staff list table is missing");
        CheckboxComponent checkHeader = Obligation.mandatory(staffTable::getCheckHeader, "The staff list check header is missing.");
        Obligation.mandatory(staffList::getSearch, "Staff list search is missing").search(STAFF_TEST_USER);

        long count = staffTable.getRows().count();
        assertThat("The candidates were not added.", count, is(greaterThan(0L)));

        checkHeader.check(true);
        updated.clickDisableButton();
        utils.waitForCondition(candidates::isStable, PageUtils.DURATION_LOADING);
        long disabled = staffTable.getRows().filter((row) -> row.getCell("deletedAt").hasValue("No")).count();
        assertThat("The associated users were not disabled.", disabled, is(equalTo(count)));

        checkHeader.check(true);
        updated.clickEnableButton();
        utils.waitForCondition(candidates::isStable, PageUtils.DURATION_LOADING);
        long enabled = staffTable.getRows().filter((row) -> row.getCell("deletedAt").hasValue("Yes")).count();
        assertThat("The associated users were not enabled.", enabled, is(equalTo(count)));
    }
}
