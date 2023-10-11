package com.apriori.customer.users;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.PageUtils;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.components.CheckboxComponent;
import com.apriori.components.PaginatorComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.components.TableRowComponent;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.Obligation;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Customers;
import com.apriori.models.response.User;
import com.apriori.models.response.Users;
import com.apriori.pageobjects.customer.users.StaffPage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersStaffAssociationTests extends TestBaseUI {
    private static final String STAFF_TEST_CUSTOMER = "Staff Association Test Customer";
    private static final String STAFF_TEST_USER = "staff-test-user";
    private Customer targetCustomer;
    private Customer aprioriInternal;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private StaffPage staffPage;
    private SoftAssertions soft = new SoftAssertions();
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setup() {
        Map<String, Object> existingUsers = Collections.singletonMap("username[CN]", STAFF_TEST_USER);
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", STAFF_TEST_CUSTOMER);
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String email = STAFF_TEST_CUSTOMER.toLowerCase();

        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        sourceUsers = new ArrayList<>(cdsTestUtil.findAll(
            CDSAPIEnum.CUSTOMER_USERS,
            Users.class,
            existingUsers,
            Collections.emptyMap(),
            aprioriInternal.getIdentity()
        ));

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
            ? cdsTestUtil.addCASCustomer(STAFF_TEST_CUSTOMER, cloudRef, email, currentUser).getResponseEntity()
            : targetCustomer;

        staffPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(targetCustomer.getIdentity())
            .goToUsersPage()
            .goToStaffPage();
    }

    @AfterEach
    public void teardown() {
        sourceUsers.forEach(user -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, aprioriInternal.getIdentity(), user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
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
    @Tag(SMOKE)
    @TestRail(id = {5587, 5588, 5589, 5590, 5591})
    public void testVerifyUsersCanBeAddedToTheList() {

        populateStaffTestUsers(21);

        StaffPage addModal = staffPage
            .clickAddFromList()
            .clickCandidatesCancelButton()
            .clickAddFromList()
            .clickCandidatesCloseButton()
            .clickAddFromList();

        PageUtils utils = new PageUtils(driver);
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
        soft.assertThat(expected).overridingErrorMessage("The selection is not holding across pages.").isEqualTo(expected);

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
        soft.assertThat(count).isEqualTo(selected);
        soft.assertAll();
    }

    @Test
    @Description("Verify user can be removed from aPriori Staff table.")
    @TestRail(id = {12081})
    public void testVerifyAssociatedStaffCanBeRemoved() {

        populateStaffTestUsers(2);

        PageUtils utils = new PageUtils(driver);

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
        soft.assertThat(count).overridingErrorMessage("The candidates were not added.").isGreaterThan(0L);
        checkHeader.check(true);
        updated.clickRemoveButton()
            .clickConfirmRemoveCancelButton();
        utils.waitForCondition(staffTable::isStable, PageUtils.DURATION_LOADING);
        long usersNotDeleted = staffTable.getRows().count();
        soft.assertThat(usersNotDeleted).overridingErrorMessage("The associated users were removed.").isEqualTo(count);

        updated.clickRemoveButton()
            .clickConfirmRemoveOkButton();
        utils.waitForCondition(staffTable::isStable, PageUtils.DURATION_LOADING);
        long usersAdded = staffTable.getRows().count();
        soft.assertThat(usersAdded).overridingErrorMessage("The associated users were not removed.").isEqualTo(0L);

        staffPage.clickAddFromList();
        SourceListComponent candidatesUpd = staffPage.getCandidates();
        TableComponent candidatesUpdTable = Obligation.mandatory(candidatesUpd::getTable, "The candidate table is missing.");
        Obligation.mandatory(candidates::getSearch, "The candidate search feature is missing.").search(STAFF_TEST_USER);
        long removedUser = candidatesUpdTable.getRows().count();
        soft.assertThat(removedUser).overridingErrorMessage("Users do not appear in the list of aPriori staff candidates").isEqualTo(count);
        soft.assertAll();
    }
}
