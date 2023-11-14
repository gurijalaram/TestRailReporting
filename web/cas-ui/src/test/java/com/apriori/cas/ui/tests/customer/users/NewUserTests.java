package com.apriori.cas.ui.tests.customer.users;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.components.SourceListComponent;
import com.apriori.cas.ui.components.TableComponent;
import com.apriori.cas.ui.pageobjects.customer.users.UsersListPage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.NewUserPage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.Obligation;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NewUserTests extends TestBaseUI {
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private final CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private String customerIdentity;
    private String userIdentity;
    private NewUserPage newUserPage;
    private String email;
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setup() {
        setCustomerData();
        newUserPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .clickNew();
    }

    @AfterEach
    public void teardown() {

        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Tag(SMOKE)
    @Description("New User profile form has correct fields, user can be added by filling only mandatory fields")
    @TestRail(id = {4063, 4062, 4073})
    public void testUserIsCreatedWithOnlyRequiredFields() {
        SoftAssertions soft = new SoftAssertions();
        String userName = new GenerateStringUtil().generateUserName();
        List<String> labelsToCheck = Arrays.asList(
            "User Name:",
            "Identity:",
            "Email:",
            "User Type:",
            "Given Name:",
            "Family Name:",
            "Name Prefix:",
            "Name Suffix:",
            "Job Title:",
            "Department:",
            "Town or City:",
            "County:",
            "Country:",
            "Time Zone:",
            "Office Phone Country Code:",
            "Office Phone Number:",
            "Authentication:"
        );

        newUserPage.testNewUserLabelAvailable(labelsToCheck, soft);

        newUserPage.formFillNewUserDetails(userName, userName + "@" + email + ".com", "", "")
            .inputNamePrefix("");

        soft.assertThat(newUserPage.canSave())
            .overridingErrorMessage("Expected save button to be disabled.")
            .isFalse();
        soft.assertThat(newUserPage.getFieldFeedback("given-name"))
            .isEqualTo("Enter a given name.");
        soft.assertThat(newUserPage.getFieldFeedback("family-name"))
            .isEqualTo("Enter a family name.");

        newUserPage.inputGivenName("Test")
            .inputFamilyName("User")
            .save(UserProfilePage.class);

        userIdentity = new UserProfilePage(driver).getUserIdentity();

        UsersListPage usersListPage = newUserPage.backToUsersListPage();
        SourceListComponent users = usersListPage.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users list table is missing");

        long rows = usersTable.getRows().count();
        soft.assertThat(rows).overridingErrorMessage("There are no users on a page.").isGreaterThan(0L);
        soft.assertAll();
    }

    @Test
    @Description("Clicking the cancel button returns the user to the user list.")
    @TestRail(id = {4072})
    public void testCancelReturnsToTheUserListPage() {
        SoftAssertions soft = new SoftAssertions();
        UsersListPage actual = newUserPage
            .formFillNewUserDetails("newUser", "email@com", "givenName", "familyName")
            .cancel(UsersListPage.class);
        soft.assertThat(actual).isNotNull();
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        String customerName = new GenerateStringUtil().generateCustomerName();
        email = customerName.toLowerCase();
        Customer targetCustomer = cdsTestUtil.addCASCustomer(customerName, rcd.getCloudRef(), email, currentUser).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
    }
}