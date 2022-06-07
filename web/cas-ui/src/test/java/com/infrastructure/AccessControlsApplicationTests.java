package com.infrastructure;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.InfrastructurePage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.UserCreation;
import com.apriori.utils.http.utils.ResponseWrapper;
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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccessControlsApplicationTests extends TestBase {

    private static final String APP_CONTROLS_CUSTOMER = "ApplicationControlsCustomer";
    private static final String STAFF_TEST_USER = "staff-test-user";

    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private InfrastructurePage infrastructurePage;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String customerName;
    private UserCreation userCreation;
    private String siteIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String appIdentity;

    @Before
    public void setup() {
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", APP_CONTROLS_CUSTOMER);
        String cloudRef = generateStringUtil.generateCloudReference();
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String salesforce = StringUtils.leftPad(now, 15, "0");
        String email = "\\S+@".concat(APP_CONTROLS_CUSTOMER);
        String customerType = Constants.CLOUD_CUSTOMER;

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
                ? cdsTestUtil.addCustomer(APP_CONTROLS_CUSTOMER, customerType, cloudRef, salesforce, email).getResponseEntity()
                : targetCustomer;

        customerIdentity = targetCustomer.getIdentity();
        customerName = targetCustomer.getName();
        userCreation = new UserCreation();
        sourceUsers = userCreation.populateStaffTestUsers(21, customerIdentity, customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        String deploymentName = generateStringUtil.generateDeploymentName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, deploymentName, siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> newApplication = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = newApplication.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .siteIdentity(siteIdentity)
                .licenseIdentity(licensedApplicationIdentity)
                .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);

        installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .deploymentIdentity(deploymentIdentity)
                .installationIdentity(installationIdentity)
                .build();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        infrastructurePage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToInfrastructure()
                .selectApplication("aPriori Professional");
    }

    @After
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.APPLICATION_INSTALLATION_BY_ID, customerIdentity, deploymentIdentity, installationIdentity, appIdentity);
        sourceUsers.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity());
        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
                installationIdentityHolder.customerIdentity(),
                installationIdentityHolder.deploymentIdentity(),
                installationIdentityHolder.installationIdentity());
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
    @Description("Validate users can be given access to applications from infrastructure view")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"5568", "10154", "10155", "10177"})
    public void testUserCanBeGivenAccessToApplication() {

        InfrastructurePage openModal = infrastructurePage
                .clickGrantFromList()
                .clickCandidatesCancelButton()
                .clickGrantFromList()
                .clickCandidatesCloseButton()
                .clickGrantFromList();

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;
        long selected = 0;
        SourceListComponent userCandidates = openModal.getCandidates();
        TableComponent userCandidatesTable = Obligation.mandatory(userCandidates::getTable, "The candidates table is missing from the add modal.");

        PaginatorComponent paginator = Obligation.mandatory(userCandidates::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));
        Obligation.mandatory(userCandidates::getSearch, "The user search functionality is missing.").search(STAFF_TEST_USER);
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(userCandidatesTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        selected += checkEveryOtherItem(userCandidatesTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        userCandidatesTable.getRows().findFirst().ifPresent((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));
        ++selected;
        paginator.clickFirstPage().getPageSize().select("50").select("100");
        utils.waitForCondition(userCandidates::isStable, PageUtils.DURATION_LOADING);

        long expected = userCandidatesTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        assertThat("The selection is not holding across pages.", expected, is(equalTo(expected)));

        InfrastructurePage updated = openModal
                .clickCandidatesAddButton()
                .clickCandidatesConfirmCancelButton()
                .clickCandidatesAddButton()
                .clickCandidatesConfirmCloseButton()
                .clickCandidatesAddButton()
                .clickCandidatesConfirmOkButton();

        SourceListComponent updatedControlsList = updated.getApplicationAccessControlsTable();
        Obligation.mandatory(updatedControlsList::getPaginator, "The application controls list paginator is missing.").getPageSize().select("50").select("100");
        utils.waitForCondition(updatedControlsList::isStable, PageUtils.DURATION_LOADING);
        TableComponent appControlsTable = Obligation.mandatory(updatedControlsList::getTable, "The application controls list table is missing.");
        long count = appControlsTable.getRows().count();
        assertThat(count, is(equalTo(selected)));
    }

    @Test
    @Description("Validate deny all, deny selected and grant all buttons behave correctly")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"10157", "5569", "10382", "5567"})
    public void testUsersCanBDeniedAccessToApplication() {
        SoftAssertions soft = new SoftAssertions();
        InfrastructurePage grantAll = infrastructurePage.clickGrantAllButton()
                .clickAllCancelButton()
                .clickGrantAllButton()
                .clickAllOkButton()
                .validateAccessControlsTableHasCorrectColumns("User Name", "user.username", soft)
                .validateAccessControlsTableHasCorrectColumns("Identity", "user.identity", soft)
                .validateAccessControlsTableHasCorrectColumns("Email", "user.email", soft)
                .validateAccessControlsTableHasCorrectColumns("Family Name", "user.userProfile.familyName", soft)
                .validateAccessControlsTableHasCorrectColumns("Given Name", "user.userProfile.givenName", soft)
                .validateAccessControlsTableHasCorrectColumns("Role", "roleName", soft)
                .validateAccessControlsTableHasCorrectColumns("Job Title", "user.userProfile.jobTitle", soft)
                .validateAccessControlsTableHasCorrectColumns("Department", "user.userProfile.department", soft)
                .validateAccessControlsTableHasCorrectColumns("Granted By", "createdByName", soft)
                .validateAccessControlsTableHasCorrectColumns("Granted", "createdAt", soft);
        soft.assertAll();

        PageUtils utils = new PageUtils(getDriver());
        long pageSize = 10;
        long selected = 0;
        SourceListComponent grantedUsers = grantAll.getApplicationAccessControlsTable();
        TableComponent usersTable = Obligation.mandatory(grantedUsers::getTable, "The users table is missing");

        selected += checkEveryOtherItem(usersTable.getRows().collect(Collectors.toList()), pageSize);
        usersTable.getRows().findFirst().ifPresent((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));

        long expected = usersTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        assertThat("The selection is not holding across pages.", expected, is(equalTo(expected)));

        InfrastructurePage updated = grantAll
                .clickDenySelectedButton()
                .clickAllOkButton()
                .clickGrantFromList();

        SourceListComponent candidatesList = updated.getCandidates();
        utils.waitForCondition(candidatesList::isStable, PageUtils.DURATION_LOADING);
        TableComponent candidatesTable = Obligation.mandatory(candidatesList::getTable, "The candidates list table is missing.");
        long count = candidatesTable.getRows().count();
        assertThat(count, is(equalTo(selected)));

        InfrastructurePage denied = updated.clickCandidatesCloseButton()
                .clickDenyAllButton()
                .clickAllOkButton();

        SourceListComponent accessControlsList = denied.getApplicationAccessControlsTable();
        TableComponent accessTable = Obligation.mandatory(accessControlsList::getTable, "The access controls list table is missing");

        long granted = accessTable.getRows().count();
        assertThat("Deny all doesn't work", granted, is(equalTo(0L)));
    }
}