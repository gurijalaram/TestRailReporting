package com.apriori.cas.ui.tests.customer;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.components.SourceListComponent;
import com.apriori.cas.ui.components.TableComponent;
import com.apriori.cas.ui.pageobjects.customer.CustomerWorkspacePage;
import com.apriori.cas.ui.pageobjects.customer.users.StaffAccessHistoryPage;
import com.apriori.cas.ui.pageobjects.customer.users.StaffPage;
import com.apriori.cas.ui.pageobjects.customer.users.UsersListPage;
import com.apriori.cas.ui.pageobjects.customer.users.UsersPage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.Obligation;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.web.app.util.PageUtils;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CustomerAccessTests extends TestBaseUI {

    SoftAssertions soft = new SoftAssertions();
    private Customer targetCustomer;
    private String cloudRef;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String customerName;
    private String userName;
    private String userEmail;
    private UsersPage usersPage;

    @BeforeEach
    public void setup() {
        customerName = new GenerateStringUtil().generateCustomerName();
        cloudRef = new GenerateStringUtil().generateCloudReference();
        String email = customerName.toLowerCase();
        UserCredentials currentUser = UserUtil.getUser();
        userName = currentUser.getUsername();
        userEmail = currentUser.getEmail();

        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email, currentUser).getResponseEntity();

        customerIdentity = targetCustomer.getIdentity();

        usersPage = new CasLoginPage(driver)
            .login(currentUser)
            .openCustomer(customerIdentity)
            .goToUsersPage();
    }

    @AfterEach
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Tag(SMOKE)
    @Description("Validate requesting access for a user that is not in white list of customer")
    @TestRail(id = {12082, 12083, 12085, 12088, 12144, 13172})
    public void requestAccessForNotWhiteListUser() {
        UsersListPage serviceAccountUsers = usersPage.goToCustomerStaff();
        SourceListComponent users = serviceAccountUsers.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users list table is missing");
        long rows = usersTable.getRows().count();
        soft.assertThat(rows)
            .overridingErrorMessage("There are no service accounts created for Cloud customer.")
            .isEqualTo(4L);

        UserProfilePage serviceAccountProfile = serviceAccountUsers.openUser(cloudRef + ".service-account.1");

        soft.assertThat(serviceAccountProfile.canEdit())
            .overridingErrorMessage("Expected Edit button to be disabled for service account.")
            .isFalse();

        CustomerWorkspacePage customerAccess = serviceAccountProfile.backToUsersListPage(CustomerWorkspacePage.class);

        soft.assertThat(customerAccess.canRevoke())
            .overridingErrorMessage("Expected revoke access button to be disabled.")
            .isFalse();

        customerAccess.clickRequestAccessButton()
            .selectServiceAccount("service-account.1")
            .clickCancelRequest();

        soft.assertThat(customerAccess.canRevoke())
            .overridingErrorMessage("Expected revoke access button to be disabled.")
            .isFalse();

        soft.assertThat(customerAccess.canRequest())
            .overridingErrorMessage("Expected request access button to be enabled.")
            .isTrue();

        customerAccess.clickRequestAccessButton()
            .selectServiceAccount("service-account.1")
            .clickOkRequestAccess();

        soft.assertThat(customerAccess.getTextErrorMessage())
            .isEqualTo((String.format("HTTP 400: Can't authorize '%s' access to '%s' as they are not on the authorized user list for the customer.", userEmail, customerName)));
        soft.assertAll();
    }

    @Test
    @Description("Validate user from white list can request and revoke customer access")
    @TestRail(id = {12084, 12087, 12934, 12935, 12936, 12923})
    public void requestCustomerAccess() {
        String userName = "qa-automation-10";
        String email = "qa-automation-10@apriori.com";
        String password = "TrumpetSnakeFridgeToasty18!%";
        PageUtils utils = new PageUtils(driver);
        StaffPage aPrioriUsers = usersPage.goToStaffPage()
            .clickAddFromList();

        SourceListComponent userCandidates = aPrioriUsers.getCandidates();
        TableComponent candidatesTable = Obligation.mandatory(userCandidates::getTable, "The candidates table is missing");
        Obligation.mandatory(userCandidates::getSearch, "The user search functionality is missing.").search(userName);
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);
        candidatesTable.getRows().forEach((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));

        CustomerWorkspacePage customerAccess = aPrioriUsers
            .clickCandidatesAddButton()
            .clickCandidatesConfirmOkButton()
            .logout()
            .login(new UserCredentials(email, password).generateToken())
            .openCustomer(customerIdentity)
            .clickRequestAccessButton()
            .selectServiceAccount("service-account.1")
            .clickOkRequestAccess();

        soft.assertThat(customerAccess.getTextSuccessMessage())
            .isEqualTo("Email has been sent");
        customerAccess.closeMessage();

        soft.assertThat(customerAccess.canRequest())
            .overridingErrorMessage("Expected request access button to be disabled.")
            .isFalse();

        customerAccess.clickRevokeAccessButton()
            .clickCancelRevoke()
            .clickRevokeAccessButton()
            .clickOkRevokeAccess();

        soft.assertThat(customerAccess.getTextSuccessMessage())
            .isEqualTo("Access successfully revoked");
        customerAccess.closeMessage();

        StaffAccessHistoryPage goToHistoryTab = customerAccess
            .goToUsersPage()
            .goToAccessHistory()
            .validateStaffHistoryTableArePageableRefreshable(soft)
            .validateHistoryTableHasCorrectColumns("User Name", "user.username", soft)
            .validateHistoryTableHasCorrectColumns("Identity", "user.identity", soft)
            .validateHistoryTableHasCorrectColumns("Email", "user.email", soft)
            .validateHistoryTableHasCorrectColumns("User Type", "user.userType", soft)
            .validateHistoryTableHasCorrectColumns("Family Name", "user.userProfile.familyName", soft)
            .validateHistoryTableHasCorrectColumns("Given Name", "user.userProfile.givenName", soft)
            .validateHistoryTableHasCorrectColumns("Job Title", "user.userProfile.jobTitle", soft)
            .validateHistoryTableHasCorrectColumns("Department", "user.userProfile.department", soft)
            .validateHistoryTableHasCorrectColumns("Service Account", "serviceAccount", soft)
            .validateHistoryTableHasCorrectColumns("Access Granted At", "createdAt", soft)
            .validateHistoryTableHasCorrectColumns("Access Revoked At", "updatedAt", soft);

        SourceListComponent historyList = goToHistoryTab.getUsersList();
        Obligation.mandatory(historyList::getSearch, "Users account search is missing").search("service-account.1");

        TableComponent accountFound = Obligation.mandatory(historyList::getTable, "The account was not found");
        long count = accountFound.getRows().count();
        soft.assertThat(count)
            .isEqualTo(1L);
        soft.assertThat(customerAccess.canRequest())
            .overridingErrorMessage("Expected request access button to be enabled.")
            .isTrue();
        soft.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Validate that aPriori staff user cannot add self to a customer")
    @TestRail(id = {16123})
    public void selfAddingToWhiteList() {
        PageUtils utils = new PageUtils(driver);
        StaffPage aPrioriUsers = usersPage.goToStaffPage()
            .clickAddFromList();

        SourceListComponent userCandidates = aPrioriUsers.getCandidates();
        TableComponent candidatesTable = Obligation.mandatory(userCandidates::getTable, "The candidates table is missing");
        Obligation.mandatory(userCandidates::getSearch, "The user search functionality is missing.").search(userName);
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);
        candidatesTable.getRows().forEach((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));

        CustomerWorkspacePage errorMessage = aPrioriUsers.clickCandidatesAddButton()
            .clickCandidatesConfirmOkButton();

        soft.assertThat(errorMessage.getTextErrorMessage())
            .isEqualTo("HTTP 403: User cannot add self to a customer.");
    }
}