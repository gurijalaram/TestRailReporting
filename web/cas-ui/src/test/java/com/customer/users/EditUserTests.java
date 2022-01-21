package com.customer.users;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class EditUserTests extends TestBase {

    private static final String STAFF_TEST_CUSTOMER = "StaffTestCustomer";
    private static final String USER_NAME = "NewTestUser";

    private Customer targetCustomer;
    private CustomerWorkspacePage customerViewPage;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String customerName;
    private String userIdentity;
    private UserProfilePage userProfilePage;

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

        userProfilePage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToUsersPage()
                .goToCustomerStaff()
                .clickNew()
                .formFillNewUserDetails(USER_NAME, "NewUserTest@" + customerName + ".com", "Test", "User")
                .save(UserProfilePage.class);
    }

    @After
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Description("Status field is greyed out (non editable) if Customer is set to inactive")
    @TestRail(testCaseId = {"10644", "10645"})
    public void testUserStatusFieldNotEditableIfCustomerIsDisabled() {
        UserProfilePage checkUserStatus = userProfilePage.edit();

        assertThat(checkUserStatus.isStatusCheckboxEditable(), is(equalTo(true)));

        userProfilePage.cancel();

        userIdentity = userProfilePage.getUserIdentity();

        CustomerProfilePage deactivateCustomer = userProfilePage.backToUsersListPage(CustomerWorkspacePage.class)
                .goToProfile()
                .clickEditButton()
                .changeCustomerStatus()
                .clickSaveButton();

        assertThat(deactivateCustomer.getStatus(), is(equalTo("Inactive")));

        customerViewPage = new CustomerWorkspacePage(driver);
        UserProfilePage checkIfReadOnly = customerViewPage.goToUsersPage()
                .goToCustomerStaff()
                .selectUser(customerIdentity, userIdentity, USER_NAME)
                .edit();

        assertThat(checkIfReadOnly.isStatusCheckboxEditable(), is(equalTo(false)));

        checkIfReadOnly.cancel();

        CustomerProfilePage activateCustomer = userProfilePage.backToUsersListPage(CustomerWorkspacePage.class)
                .goToProfile()
                .clickEditButton()
                .changeCustomerStatus()
                .clickSaveButton();

        assertThat(activateCustomer.getStatus(), is(equalTo("Active")));
    }
}