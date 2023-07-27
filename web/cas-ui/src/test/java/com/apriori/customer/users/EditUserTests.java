package com.apriori.customer.users;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.testsuites.categories.SmokeTest;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

public class EditUserTests extends TestBaseUI {

    private static final String USER_NAME = new GenerateStringUtil().generateUserName();

    private Customer targetCustomer;
    private CustomerWorkspacePage customerViewPage;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String userIdentity;
    private UserProfilePage userProfilePage;

    @Before
    public void setup() {
        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String email = customerName.toLowerCase();

        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();

        customerIdentity = targetCustomer.getIdentity();

        userProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .clickNew()
            .formFillNewUserDetails(USER_NAME, USER_NAME + "@" + email + ".com", "Test", "User")
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
    @TestRail(id = {5576, 11952, 4374, 4382, 11962})
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

        userProfilePage.assertNonEditable(Arrays.asList("username", "identity", "email", "userType"), soft)
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
    @TestRail(id = {11963})
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
    @Ignore("Status field is disabled")
    @Description("Status field is greyed out (non editable) if Customer is set to inactive")
    @TestRail(id = {10644, 10645})
    public void testUserStatusFieldNotEditableIfCustomerIsDisabled() {
        SoftAssertions soft = new SoftAssertions();
        UserProfilePage checkUserStatus = userProfilePage.edit();

        soft.assertThat(checkUserStatus.isStatusCheckboxEditable()).isTrue();

        userProfilePage.cancel();

        userIdentity = userProfilePage.getUserIdentity();

        CustomerProfilePage deactivateCustomer = userProfilePage.backToUsersListPage(CustomerWorkspacePage.class)
            .goToProfile()
            .clickEditButton()
            .changeCustomerStatus()
            .clickSaveButton();

        soft.assertThat(deactivateCustomer.getStatus()).isEqualTo("Inactive");

        customerViewPage = new CustomerWorkspacePage(driver);
        UserProfilePage checkIfReadOnly = customerViewPage.goToUsersPage()
            .goToCustomerStaff()
            .selectUser(customerIdentity, userIdentity, USER_NAME)
            .edit();

        soft.assertThat(checkIfReadOnly.isStatusCheckboxEditable()).isFalse();

        checkIfReadOnly.cancel();

        CustomerProfilePage activateCustomer = userProfilePage.backToUsersListPage(CustomerWorkspacePage.class)
            .goToProfile()
            .clickEditButton()
            .changeCustomerStatus()
            .clickSaveButton();

        soft.assertThat(activateCustomer.getStatus()).isEqualTo("Active");
        soft.assertAll();
    }
}