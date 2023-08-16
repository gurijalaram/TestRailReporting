package com.apriori.customer.users;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.PageUtils;
import com.apriori.TestBaseUI;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.models.response.Customer;
import com.apriori.cds.models.response.Deployment;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.models.response.LicensedApplications;
import com.apriori.cds.models.response.Site;
import com.apriori.cds.models.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.components.CardsViewComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.Obligation;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UsersGrantApplicationAccessTests extends TestBaseUI {

    private IdentityHolder installationIdentityHolder;
    private IdentityHolder licensedAppIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private Customer targetCustomer;
    private String customerIdentity;
    private String customerName;
    private String siteName;
    private String siteIdentity;
    private String deploymentName;
    private String deploymentIdentity;
    private ResponseWrapper<User> user;
    private String userIdentity;
    private String userName;
    private UserProfilePage userProfilePage;
    private String installationIdentity;
    private String appIdentity;

    @BeforeEach
    public void setup() {
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesforce = generateStringUtil.generateSalesForceId();
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        String email = "\\S+@".concat(customerName);
        String customerType = Constants.ON_PREM_CUSTOMER;

        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCustomer(customerName, customerType, null, salesforce, email).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
        siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        deploymentName = generateStringUtil.generateDeploymentName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, deploymentName, siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> newApplication = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
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

        userProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .selectUser(customerIdentity, userIdentity, userName);
    }

    @AfterEach
    public void teardown() {
        cdsTestUtil.delete(CDSAPIEnum.APPLICATION_INSTALLATION_BY_ID, customerIdentity, deploymentIdentity, installationIdentity, appIdentity);
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
    @Tag(SMOKE)
    @TestRail(id = {12515})
    public void testUserGrantedAccessControls() {
        SoftAssertions soft = new SoftAssertions();
        PageUtils utils = new PageUtils(driver);

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

        soft.assertThat(userProfilePage.getSiteInDropDown()).isEqualTo(siteName);
        soft.assertThat(userProfilePage.getDeploymentInDropDown()).isEqualTo(deploymentName);

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

        soft.assertThat(cards).isEqualTo(1L);
        controlsCards.getCards("apriori-card").findFirst().ifPresent((card) -> Obligation.mandatory(card::getCheck, "The check cell is missing").check(true));

        soft.assertThat(updated.canRemove())
            .overridingErrorMessage("Expected Remove button to be enabled.")
            .isTrue();
        soft.assertAll();
    }

    @Test
    @Description("Validate granted applications can be removed by Remove button")
    @TestRail(id = {12516})
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

        PageUtils utils = new PageUtils(driver);
        SourceListComponent accessControls = addModal.getGrantedAccessControlsContainer();
        CardsViewComponent controlsCards = Obligation.mandatory(accessControls::getCardGrid, "The access controls grid is missing");
        controlsCards.getCards("apriori-card").findFirst().ifPresent(card -> Obligation.mandatory(card::getCheck, "The check cell is missing").check(true));

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