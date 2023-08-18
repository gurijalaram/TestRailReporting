package com.apriori.customer.users;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.Obligation;
import com.apriori.models.response.Customer;
import com.apriori.pageobjects.customer.users.UsersListPage;
import com.apriori.pageobjects.customer.users.profile.NewUserPage;
import com.apriori.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NewUserTests extends TestBaseUI {

    private Customer targetCustomer;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String userIdentity;
    private NewUserPage newUserPage;
    private String email;

    @BeforeEach
    public void setup() {
        String customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        email = customerName.toLowerCase();

        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();

        customerIdentity = targetCustomer.getIdentity();

        newUserPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .clickNew();
    }

    @AfterEach
    public void teardown() {
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
}