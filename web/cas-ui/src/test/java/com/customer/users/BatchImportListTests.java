package com.customer.users;

import com.apriori.PageUtils;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.components.CheckboxComponent;
import com.apriori.components.PaginatorComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.components.TableRowComponent;
import com.apriori.customer.users.ImportPage;
import com.apriori.customer.users.UsersListPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.Obligation;
import com.apriori.utils.TestRail;
import com.apriori.utils.common.customer.response.Customer;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.hc.core5.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BatchImportListTests extends TestBase {

    private String cloudRef;
    private String customerName;
    private ImportPage importPage;
    private String email;
    private SoftAssertions soft = new SoftAssertions();
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private final String fileName = "testUsersBatch.csv";
    private String invalidDataFile = "invalidUsersData.csv";

    @Before
    public void setup() {
        customerName = new GenerateStringUtil().generateCustomerName();
        cloudRef = new GenerateStringUtil().generateCloudReference();
        email = customerName.toLowerCase();

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        importPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToImport();
    }

    @After
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
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
    @Description("New CSV file with users can be uploaded in CAS")
    @TestRail(testCaseId = {"4344", "4361", "4354", "4357", "4352", "13234"})
    public void testUploadCsvNewUsers() {
        cdsTestUtil.addCASBatchFile(Constants.USERS_BATCH, email, customerIdentity);
        ImportPage uploadUsers = importPage.refreshBatchFilesList()
            .validateImportUsersTableHasCorrectColumns("User Name", "userName", soft)
            .validateImportUsersTableHasCorrectColumns("Status", "cdsStatus", soft)
            .validateImportUsersTableHasCorrectColumns("Identity", "userIdentity", soft)
            .validateImportUsersTableHasCorrectColumns("Email", "email", soft)
            .validateImportUsersTableHasCorrectColumns("Given Name", "givenName", soft)
            .validateImportUsersTableHasCorrectColumns("Family Name", "familyName", soft)
            .validateImportUsersTableHasCorrectColumns("Job Title", "jobTitle", soft)
            .validateImportUsersTableHasCorrectColumns("Department", "department", soft)
            .validateImportUsersTableHasCorrectColumns("Created At", "createdAt", soft)
            .validateImportUsersTableHasCorrectColumns("Created By", "createdByName", soft);

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;
        long selected = 0;

        SourceListComponent users = uploadUsers.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing.");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        Obligation.mandatory(users::getSearch, "The user search functionality is missing.").search("user");
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(usersTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(usersTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        paginator.clickFirstPage().getPageSize().select("50").select("100");
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        long expected = usersTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        soft.assertThat(expected).overridingErrorMessage("The selection is not holding across pages.").isEqualTo(selected);
        soft.assertThat(importPage.canLoad()).isTrue();

        importPage.clickRemoveButton()
            .clickCancelConfirmRemove()
            .clickRemoveButton()
            .clickOkConfirmRemove(fileName);

        soft.assertThat(importPage.isBatchFileDisplayed(fileName)).isFalse();
        soft.assertAll();
    }

    private List<User> collectUsers(String customerIdentity) {
        cdsTestUtil = new CdsTestUtil();
        List<User> sourceUsers = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            User added = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_USERS, Users.class, HttpStatus.SC_OK, customerIdentity).getResponseEntity().getItems().get(i);
            sourceUsers.add(added);
        }
        return sourceUsers;
    }

    @Test
    @Category({SmokeTest.class})
    @Description("Users can be loaded from CSV by Load button")
    @TestRail(testCaseId = {"5598", "5599", "4360", "4353", "4358", "4359"})
    public void testLoadUsersFromFile() {
        cdsTestUtil.addCASBatchFile(Constants.USERS_BATCH, email, customerIdentity);

        ImportPage uploadUsers = importPage.refreshBatchFilesList();

        soft.assertThat(uploadUsers.getFieldName()).containsExactly("Users in Total:", "Successfully Loaded:", "Failed Loaded:", "Created By:", "Created At:");

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;

        SourceListComponent users = uploadUsers.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing.");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        CheckboxComponent checkHeader = Obligation.mandatory(usersTable::getCheckHeader, "The user list check header is missing");
        checkHeader.check(true);

        long expected = usersTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        soft.assertThat(expected).overridingErrorMessage("The selection is not holding across pages.").isEqualTo(pageSize);

        importPage.loadUsers()
            .refreshUsersList();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        importPage.refreshUsersList();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        long loaded = usersTable.getRows().filter((row) -> row.getCell("cdsStatus").hasValue("loaded")).count();
        soft.assertThat(loaded).overridingErrorMessage("The new batch import users were not loaded").isGreaterThan(0L);

        UsersListPage newUsers = importPage.goToCustomerStaff();

        SourceListComponent uploadedUsers = newUsers.getUsersList();
        TableComponent uploadUsersTable = Obligation.mandatory(uploadedUsers::getTable, "The users table is missing.");

        long addedUsers = uploadUsersTable.getRows().count();
        soft.assertThat(addedUsers).isEqualTo(pageSize);

        importPage = newUsers.goToImport()
            .refreshBatchFilesList();

        soft.assertThat(importPage.getCardFieldValue("successUsers")).isEqualTo("10");
        soft.assertAll();

        importPage.clickRemoveButton()
            .clickOkConfirmRemove(fileName);

        sourceUsers = collectUsers(customerIdentity);
        sourceUsers.forEach(user -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
    }

    @Test
    @Description("Upload user csv with invalid users data")
    @TestRail(testCaseId = {"4348"})
    public void testCsvInvalidUsersData() {
        cdsTestUtil.addInvalidBatchFile(customerIdentity, invalidDataFile);
        importPage.refreshBatchFilesList();

        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent users = importPage.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing.");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select("10");
        CheckboxComponent checkHeader = Obligation.mandatory(usersTable::getCheckHeader, "The user list check header is missing");
        checkHeader.check(true);

        importPage.loadUsers()
            .refreshUsersList();

        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        long loaded = usersTable.getRows().filter((row) -> row.getCell("cdsStatus").hasValue("failed")).count();
        soft.assertThat(loaded).overridingErrorMessage("The new batch import users were not loaded").isGreaterThan(1L);
        soft.assertAll();

        importPage.clickRemoveButton()
            .clickOkConfirmRemove(fileName);
    }
}
