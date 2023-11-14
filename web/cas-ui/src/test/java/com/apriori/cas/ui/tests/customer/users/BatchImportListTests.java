package com.apriori.cas.ui.tests.customer.users;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.components.CheckboxComponent;
import com.apriori.cas.ui.components.PaginatorComponent;
import com.apriori.cas.ui.components.SourceListComponent;
import com.apriori.cas.ui.components.TableComponent;
import com.apriori.cas.ui.components.TableRowComponent;
import com.apriori.cas.ui.pageobjects.customer.users.ImportPage;
import com.apriori.cas.ui.pageobjects.customer.users.UsersListPage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.Obligation;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.web.app.util.PageUtils;

import io.qameta.allure.Description;
import org.apache.hc.core5.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class   BatchImportListTests extends TestBaseUI {
    private final CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private final String fileName = "testUsersBatch.csv";
    private ImportPage importPage;
    private String email;
    private SoftAssertions soft = new SoftAssertions();
    private Customer targetCustomer;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String invalidDataFile = "invalidUsersData.csv";
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setup() {
        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        email = customerName.toLowerCase();

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email, currentUser).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        importPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToImport();
    }

    @AfterEach
    public void teardown() {
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
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
    @TestRail(id = {4344, 4361, 4354, 4357, 4352, 13234})
    public void testUploadCsvNewUsers() {
        cdsTestUtil.addCASBatchFile(Constants.USERS_BATCH, email, customerIdentity, currentUser);
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

        PageUtils utils = new PageUtils(driver);
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
    @Tag(SMOKE)
    @Description("Users can be loaded from CSV by Load button")
    @TestRail(id = {5598, 5599, 4360, 4353, 4358, 4359})
    public void testLoadUsersFromFile() {
        setCustomerData();
        cdsTestUtil.addCASBatchFile(Constants.USERS_BATCH, email, customerIdentity, currentUser);

        ImportPage uploadUsers = importPage.refreshBatchFilesList();

        soft.assertThat(uploadUsers.getFieldName()).containsExactly("Users in Total:", "Successfully Loaded:", "Failed Loaded:", "Created By:", "Created At:");

        PageUtils utils = new PageUtils(driver);
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

        List<User> sourceUsers = collectUsers(customerIdentity);
        sourceUsers.forEach(user -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
    }

    @Test
    @Description("Upload user csv with invalid users data")
    @TestRail(id = {4348})
    public void testCsvInvalidUsersData() {
        setCustomerData();
        cdsTestUtil.addInvalidBatchFile(customerIdentity, invalidDataFile, currentUser);
        importPage.refreshBatchFilesList();

        PageUtils utils = new PageUtils(driver);

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

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
    }
}
