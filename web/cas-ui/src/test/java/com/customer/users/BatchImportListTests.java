package com.customer.users;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.users.ImportPage;
import com.apriori.customer.users.UsersListPage;
import com.apriori.customer.users.UsersPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.components.CheckboxComponent;
import com.apriori.utils.web.components.PaginatorComponent;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.components.TableRowComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BatchImportListTests extends TestBase {

    private static final String STAFF_TEST_CUSTOMER = "StaffTestCustomer";

    private ImportPage importPage;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private File resourceFile;
    private File resourceInvalidHeaders;
    private File resourceInvalidData;
    private String fileName = "testUsersBatch.csv";
    private String invalidHeadersFileName = "invalidHeaders.csv";
    private String invalidDataFile = "invalidUsersData.csv";

    @Before
    public void setup() {
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", STAFF_TEST_CUSTOMER);
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String salesforce = StringUtils.leftPad(now, 15, "0");
        String email = "\\S+@".concat(STAFF_TEST_CUSTOMER);
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);
        resourceInvalidHeaders = FileResourceUtil.getResourceAsFile(invalidHeadersFileName);
        resourceInvalidData = FileResourceUtil.getResourceAsFile(invalidDataFile);

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
                ? cdsTestUtil.addCustomer(STAFF_TEST_CUSTOMER, now, salesforce, email).getResponseEntity()
                : targetCustomer;

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
    @TestRail(testCaseId = {"4344", "4361", "4354", "4357"})
    public void testUploadCsvNewUsers() {
        SoftAssertions soft = new SoftAssertions();
        ImportPage uploadUsers = importPage.importFile(resourceFile)
                .selectCard(fileName)
                .validateImportTableArePageableAndRefreshable(soft)
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
        soft.assertAll();

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
        assertThat("The selection is not holding across pages.", expected, is(equalTo(selected)));
        assertThat(importPage.canLoad(), is(true));

        importPage.deleteCsvFile(fileName);

        assertThat(importPage.isCardDisplayed(fileName), is(equalTo(false)));
    }

    private List<User> collectUsers(String customerIdentity) {
        cdsTestUtil = new CdsTestUtil();
        List<User> sourceUsers = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            User added = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_USERS, Users.class, customerIdentity).getResponseEntity().getItems().get(i);
            sourceUsers.add(added);
        }
        return sourceUsers;
    }

    @Test
    @Description("Users can be loaded from CSV by Load button")
    @TestRail(testCaseId = {"5598", "5599", "4360", "4353", "4358", "4359"})
    public void testLoadUsersFromFile() {
        ImportPage uploadUsers = importPage.importFile(resourceFile)
                .selectCard(fileName);

        assertThat(uploadUsers.getFieldName(), containsInRelativeOrder("Users in Total", "Success/Failed", "Created At", "Created By"));

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;

        SourceListComponent users = uploadUsers.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing.");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        CheckboxComponent checkHeader = Obligation.mandatory(usersTable::getCheckHeader, "The user list check header is missing");
        checkHeader.check(true);

        long expected = usersTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        assertThat("The selection is not holding across pages.", expected, is(equalTo(pageSize)));

        importPage.loadUsers()
                .refreshList();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        importPage.refreshList();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        long loaded = usersTable.getRows().filter((row) -> row.getCell("cdsStatus").hasValue("loaded")).count();
        assertThat("The new batch import users were not loaded", loaded, is(greaterThan(0L)));

        UsersListPage newUsers = new UsersPage(driver).goToCustomerStaff();

        SourceListComponent uploadedUsers = newUsers.getUsersList();
        TableComponent uploadUsersTable = Obligation.mandatory(uploadedUsers::getTable, "The users table is missing.");

        long addedUsers = uploadUsersTable.getRows().count();
        assertThat(addedUsers, is(equalTo(pageSize)));

        importPage = new UsersPage(driver).goToImport()
                .selectCard(fileName);
        assertThat(importPage.getCardFieldValue("Success/Failed"), is(equalTo(pageSize + "/0")));

        sourceUsers = collectUsers(customerIdentity);
        sourceUsers.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
    }

    @Test
    @Description("Upload user csv with invalid headers")
    @TestRail(testCaseId = {"4347"})
    public void testCsvInvalidHeaders() {
        importPage.importFile(resourceInvalidHeaders);

        assertThat(importPage.getTextErrorMessage(), is(equalTo("The file could not be read. Please check the file and make sure \n" +
                "  that it is formatted as a CSV and matches the required set of columns in the template.")));
    }

    @Test
    @Description("Upload user csv with invalid headers")
    @TestRail(testCaseId = {"4348"})
    public void testCsvInvalidUsersData() {
        importPage.importFile(resourceInvalidData)
                .selectCard(invalidDataFile);

        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent users = importPage.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing.");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select("10");
        CheckboxComponent checkHeader = Obligation.mandatory(usersTable::getCheckHeader, "The user list check header is missing");
        checkHeader.check(true);

        importPage.loadUsers()
                .refreshList();

        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        long loaded = usersTable.getRows().filter((row) -> row.getCell("cdsStatus").hasValue("failed")).count();
        assertThat("The new batch import users were not loaded", loaded, is(greaterThan(1L)));
    }
}
