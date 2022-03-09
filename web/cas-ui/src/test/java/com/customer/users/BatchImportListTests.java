package com.customer.users;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.users.ImportPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.*;
import com.apriori.utils.reader.file.user.UserUtil;
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
import org.junit.experimental.categories.Category;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BatchImportListTests extends TestBase {

    private static final String STAFF_TEST_CUSTOMER = "StaffTestCustomer";

    private ImportPage importPage;
    private FileImport fileImport;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private File resourceFile;
    private String fileName = "testUsersBatch.csv";

    @Before
    public void setup() {
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", STAFF_TEST_CUSTOMER);
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String salesforce = StringUtils.leftPad(now, 15, "0");
        String email = "\\S+@".concat(STAFF_TEST_CUSTOMER);
        resourceFile = FileResourceUtil.getLocalResourceFile(fileName);

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.GET_CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
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
        cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, targetCustomer.getIdentity());
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
    @Description("djfdjf")
    @TestRail(testCaseId = {"4344"})
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

        PageUtils utils = new PageUtils(driver);
        long pageSize = 10;
        long selected = 0;

        SourceListComponent users = uploadUsers.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing.");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        selected += checkEveryOtherItem(usersTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(usersTable.getRows().collect(Collectors.toList()), pageSize);


    }

}
