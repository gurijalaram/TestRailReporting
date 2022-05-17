package com.customer.users;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EditUserTests extends TestBase {

    private static final String STAFF_TEST_CUSTOMER = "StaffTestCustomer";
    private static final String USER_NAME = new GenerateStringUtil().generateUserName();

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
        String customerType = Constants.CLOUD_CUSTOMER;

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
                ? cdsTestUtil.addCustomer(STAFF_TEST_CUSTOMER, customerType, now, salesforce, email).getResponseEntity()
                : targetCustomer;
        customerIdentity = targetCustomer.getIdentity();
        customerName = targetCustomer.getName();

        userProfilePage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToUsersPage()
                .goToCustomerStaff()
                .clickNew()
                .formFillNewUserDetails(USER_NAME, USER_NAME + "@" + customerName + ".com", "Test", "User")
                .save(UserProfilePage.class);
    }

    @After
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Category({SmokeTest.class})
    @Description("Test user profile details, edit mode and cancel edit button")
    @TestRail(testCaseId = {"5576", "11952", "4374", "4382", "11962"})
    public void testEditAndCancelUserProfile() {
        userIdentity = userProfilePage.getUserIdentity();
        SoftAssertions soft = new SoftAssertions();
        List<String> userProfileFields = Arrays.asList(
                "username",
                "identity",
                "email",
                "userType",
                "userProfile\\.givenName",
                "userProfile\\.familyName",
                "userProfile\\.prefix",
                "userProfile\\.suffix",
                "userProfile\\.jobTitle",
                "userProfile\\.department",
                "userProfile\\.townCity",
                "userProfile\\.county",
                "userProfile\\.countryCode",
                "userProfile\\.timezone",
                "userProfile\\.officePhoneCountryCode",
                "userProfile\\.officePhoneNumber",
                "authenticationType"
        );

        List<String> editModeFields = Arrays.asList(
                "userProfile.givenName",
                "userProfile.familyName",
                "userProfile.prefix",
                "userProfile.suffix",
                "userProfile.jobTitle",
                "userProfile.department",
                "userProfile.townCity",
                "userProfile.county",
                "country-code",
                "timezone",
                "userProfile.officePhoneCountryCode",
                "userProfile.officePhoneNumber"
        );

        userProfilePage.assertNonEditable(userProfileFields, soft);

        soft.assertThat(userProfilePage.edit())
                .overridingErrorMessage("Expected edit button to be displayed and clickable.")
                .isNotNull();

        userProfilePage.assertNonEditable(Arrays.asList("username", "identity", "email"), soft)
                .assertEditable(editModeFields, soft)
                .assertButtonAvailable(soft, "Cancel")
                .assertButtonAvailable(soft, "Save");

        userProfilePage.cancel()
                .assertButtonAvailable(soft, "Edit")
                .assertNonEditable(userProfileFields, soft);

        soft.assertAll();
    }

    @Test
    @Category({SmokeTest.class})
    @Description("Test that user details can be edited and saved")
    @TestRail(testCaseId = {"11963"})
    public void testEditUserAndSave() {
        userIdentity = userProfilePage.getUserIdentity();
        SoftAssertions soft = new SoftAssertions();

        userProfilePage = userProfilePage.edit();

        soft.assertThat(userProfilePage.canSave())
                .overridingErrorMessage("Expected save button to be disabled.")
                .isFalse();

        String newGivenName = "EditedGivenName";

        userProfilePage.editGivenName(newGivenName)
                .save();

        String savedGivenName = userProfilePage.getReadOnlyLabel("userProfile\\.givenName").getText();
        soft.assertThat(savedGivenName)
                .overridingErrorMessage(String.format("Expected changed description to equal %s. Actual %s.", newGivenName, savedGivenName))
                .isEqualTo(newGivenName);

        soft.assertAll();
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