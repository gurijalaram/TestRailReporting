package com.customer.users;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.users.UsersListPage;
import com.apriori.customer.users.profile.NewUserPage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.Obligation;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class NewUserTests extends TestBase {

    private static final String STAFF_TEST_CUSTOMER = "StaffTestCustomer";

    private Customer targetCustomer;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String customerName;
    private String userIdentity;
    private NewUserPage newUserPage;

    @Before
    public void setup() {
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", STAFF_TEST_CUSTOMER);
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String salesforce = StringUtils.leftPad(now, 15, "0");
        String email = "\\S+@".concat(STAFF_TEST_CUSTOMER);

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.GET_CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
                ? cdsTestUtil.addCustomer(STAFF_TEST_CUSTOMER, now, salesforce, email).getResponseEntity()
                : targetCustomer;
        customerIdentity = targetCustomer.getIdentity();
        customerName = targetCustomer.getName();

        newUserPage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToUsersPage()
                .goToCustomerStaff()
                .clickNew();
    }

    @After
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Description("New User profile form has correct fields, user can be added by filling only mandatory fields")
    @TestRail(testCaseId = {"4063", "4062", "4073"})
    public void testUserIsCreatedWithOnlyRequiredFields() {
        SoftAssertions soft = new SoftAssertions();

        newUserPage
                .testNewUserLabelAvailable("User Name:*", soft)
                .testNewUserLabelAvailable("Email:*", soft)
                .testNewUserLabelAvailable("User Type", soft)
                .testNewUserLabelAvailable("Status:", soft)
                .testNewUserLabelAvailable("Given Name:*", soft)
                .testNewUserLabelAvailable("Family Name:*", soft)
                .testNewUserLabelAvailable("Name Prefix:", soft)
                .testNewUserLabelAvailable("Name Suffix:", soft)
                .testNewUserLabelAvailable("Job Title:", soft)
                .testNewUserLabelAvailable("Department:", soft)
                .testNewUserLabelAvailable("Town or City:", soft)
                .testNewUserLabelAvailable("County:", soft)
                .testNewUserLabelAvailable("Country:", soft)
                .testNewUserLabelAvailable("Time Zone:", soft)
                .testNewUserLabelAvailable("Office Phone Country Code:", soft)
                .testNewUserLabelAvailable("Office Phone Number:", soft)
                .testNewUserLabelAvailable("Authentication", soft);
        soft.assertAll();

        newUserPage.inputUserName("NewTestUser")
                .inputEmail("NewUserTest@" + customerName + ".com")
                .save(NewUserPage.class);

        soft.assertThat(newUserPage.getGivenNameFeedback())
                .isEqualTo("Enter a given name.");
        soft.assertThat(newUserPage.getFamilyNameFeedback())
                .isEqualTo("Enter a family name.");

        newUserPage.formFillNewUserDetails("NewUserTest", "NewUserTest@" + customerName + ".com", "Test", "User")
                .save(UserProfilePage.class);

        userIdentity = new UserProfilePage(driver).getUserIdentity();

        UsersListPage usersListPage = newUserPage.backToUsersListPage();
        SourceListComponent users = usersListPage.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users list table is missing");

        long rows = usersTable.getRows().count();
        assertThat("There are no users on a page.", rows, is(greaterThan(0L)));
    }
}