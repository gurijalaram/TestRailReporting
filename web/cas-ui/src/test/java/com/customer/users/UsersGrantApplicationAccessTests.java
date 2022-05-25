package com.customer.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.components.CardsViewComponent;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UsersGrantApplicationAccessTests extends TestBase {

    private IdentityHolder installationIdentityHolder;
    private IdentityHolder licensedAppIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private Customer targetCustomer;
    private String customerIdentity;
    private String customerName;
    private String siteName;
    private String deploymentName;
    private ResponseWrapper<User> user;
    private String userIdentity;
    private String userName;
    private UserProfilePage userProfilePage;

    @Before
    public void setup() {
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesforce = generateStringUtil.generateSalesForceId();
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        String email = "\\S+@".concat(customerName);
        String customerType = Constants.ON_PREM_CUSTOMER;

        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesforce, email).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
        siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();
        deploymentName = generateStringUtil.generateDeploymentName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, deploymentName, siteIdentity, "PRODUCTION");
        String deploymentIdentity = deployment.getResponseEntity().getResponse().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> newApplication = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = newApplication.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);

        String installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        userProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .selectUser(customerIdentity, userIdentity, userName);
    }

    @After
    public void teardown() {

        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
            licensedAppIdentityHolder.customerIdentity(),
            licensedAppIdentityHolder.siteIdentity(),
            licensedAppIdentityHolder.licenseIdentity());
        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
            installationIdentityHolder.customerIdentity(),
            installationIdentityHolder.deploymentIdentity(),
            installationIdentityHolder.installationIdentity());
        cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Description("Validate granted applications can be added by Add button")
    @TestRail(testCaseId = {"12515"})
    public void testUserGrantedAccessControls() {
        SoftAssertions soft = new SoftAssertions();
        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent accessControls = userProfilePage.getGrantedAccessControlsContainer();
        userProfilePage.validateContainerIsPageableAndRefreshable(soft, accessControls);

        soft.assertThat(userProfilePage.canRemove())
            .overridingErrorMessage("Expected Remove button to be disabled.")
            .isFalse();

        UserProfilePage addModal = userProfilePage
            .clickAddButton()
            .clickCancelModalButton()
            .clickAddButton()
            .clickCloseModalButton()
            .clickAddButton();

        SourceListComponent applicationCandidates = addModal.getApplicationCandidates();
        utils.waitForCondition(applicationCandidates::isStable, PageUtils.DURATION_LOADING);

        assertThat(userProfilePage.getSiteInDropDown(), is(equalTo(siteName)));
        assertThat(userProfilePage.getDeploymentInDropDown(), is(equalTo(deploymentName)));

        TableComponent appCandidatesTable = Obligation.mandatory(applicationCandidates::getTable, "The candidates table is missing from the add modal.");
        addModal.validateContainerIsPageableAndRefreshable(soft, applicationCandidates);
        appCandidatesTable.getRows().findFirst().ifPresent((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));

        UserProfilePage updated = addModal.clickApplicationAddButton()
            .clickModalConfirmationCancelButton()
            .clickApplicationAddButton()
            .clickModalConfirmationCloseButton()
            .clickApplicationAddButton()
            .clickModalConfirmOkButton()
            .waitForCardIsDisplayed();

        SourceListComponent updatedControls = updated.getGrantedAccessControlsContainer();
        CardsViewComponent controlsCards = Obligation.mandatory(updatedControls::getCardGrid, "The access controls grid is missing");
        long cards = controlsCards.getCards("apriori-card").count();

        assertThat(cards, CoreMatchers.is(CoreMatchers.equalTo(1L)));
        controlsCards.getCards("apriori-card").findFirst().ifPresent((card) -> Obligation.mandatory(card::getCheck, "The check cell is missing").check(true));

        soft.assertThat(updated.canRemove())
            .overridingErrorMessage("Expected Remove button to be enabled.")
            .isTrue();
        soft.assertAll();
    }

    @Test
    @Description("Validate granted applications can be removed by Remove button")
    @TestRail(testCaseId = {"12516"})
    public void testAccessControlCanBeRemoved() {
        SoftAssertions soft = new SoftAssertions();
        UserProfilePage addModal = userProfilePage
            .clickAddButton();

        SourceListComponent applicationCandidates = addModal.getApplicationCandidates();
        TableComponent appCandidatesTable = Obligation.mandatory(applicationCandidates::getTable, "The candidates table is missing from the add modal.");

        appCandidatesTable.getRows().findFirst().ifPresent((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));

        addModal.clickApplicationAddButton()
            .clickModalConfirmOkButton()
            .waitForCardIsDisplayed();

        PageUtils utils = new PageUtils(getDriver());
        SourceListComponent accessControls = addModal.getGrantedAccessControlsContainer();
        CardsViewComponent controlsCards = Obligation.mandatory(accessControls::getCardGrid, "The access controls grid is missing");
        controlsCards.getCards("apriori-card").findFirst().ifPresent((card) -> Obligation.mandatory(card::getCheck, "The check cell is missing").check(true));

        userProfilePage.clickRemoveButton()
            .clickCancelConfirmRemove();
        utils.waitForCondition(accessControls::isStable, PageUtils.DURATION_LOADING);

        soft.assertThat(userProfilePage.isCardDisplayed())
                .overridingErrorMessage("Expected access control card is displayed.")
                .isTrue();

        userProfilePage.clickRemoveButton()
            .clickOkConfirmRemove();
        utils.waitForCondition(accessControls::isStable, PageUtils.DURATION_LOADING);

        soft.assertThat(userProfilePage.isCardDisplayed())
            .overridingErrorMessage("Expected access control card is removed.")
            .isFalse();
        soft.assertAll();
    }
}