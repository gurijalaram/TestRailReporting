package com.apriori.cas.ui.tests.customer.users;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.pageobjects.customer.CustomerWorkspacePage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cas.ui.pageobjects.newcustomer.CustomerProfilePage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EditUserTests extends TestBaseUI {
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static final String USER_NAME = new GenerateStringUtil().generateUserName();
    private final CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();

    private Customer targetCustomer;
    private CustomerWorkspacePage customerViewPage;
    private String customerIdentity;
    private String email;
    private String userIdentity;
    private UserProfilePage userProfilePage;
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setup() {
        setCustomerData();
        userProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .clickNew()
            .formFillNewUserDetails(USER_NAME, USER_NAME + "@" + email + ".com", "Test", "User")
            .save(UserProfilePage.class);
    }

    @AfterEach
    public void teardown() {
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Tag(SMOKE)
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
    @Tag(SMOKE)
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
    @Disabled("Status field is disabled")
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

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        String customerName = new GenerateStringUtil().generateCustomerName();
        email = customerName.toLowerCase();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, rcd.getCloudRef(), email, currentUser).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
    }
}