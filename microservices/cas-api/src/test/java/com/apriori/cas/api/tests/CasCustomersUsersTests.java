package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.UsersData;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.file.InitFileData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.utils.Constants;
import com.apriori.cds.utils.RandomCustomerData;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@ExtendWith(TestRulesAPI.class)
public class CasCustomersUsersTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private Customer newCustomer;
    private String customerIdentity;
    private String userIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private UserCredentials currentUser = UserUtil.getUser();
    private String siteIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String installationIdentity;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
    }

    @AfterEach
    public void cleanUp() {
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApProIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApProIdentity);
        }
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedAcsIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedAcsIdentity);
        }
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                customerIdentity,
                userIdentity
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @TestRail(id = {5661, 5662, 5663})
    @Description("Add a user to a customer, return a list of users for the customer, get the User identified by its identity.")
    public void addCustomerUsers() {
        setCustomerData();
        ResponseWrapper<User> user = casTestUtil.createUser(newCustomer);
        soft.assertThat(user.getResponseEntity().getCustomerIdentity())
            .isEqualTo(customerIdentity);

        ResponseWrapper<Users> customerUsers = casTestUtil.getCommonRequest(CASAPIEnum.USERS, Users.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(customerUsers.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        userIdentity = customerUsers.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<User> singleUser = casTestUtil.getCommonRequest(CASAPIEnum.USER, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(singleUser.getResponseEntity().getIdentity())
            .isEqualTo(userIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5664})
    @Description("Update the User.")
    public void updateUsers() {
        setCustomerData();
        ResponseWrapper<User> userResponse = casTestUtil.createUser(newCustomer);
        User user = userResponse.getResponseEntity();
        userIdentity = user.getIdentity();

        ResponseWrapper<User> updatedUser = CasTestUtil.updateUser(user);

        soft.assertThat(updatedUser.getResponseEntity().getUserProfile().getDepartment())
            .isEqualTo("QA");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5667})
    @Description("Reset the MFA configuration for a user.")
    public void resettingUserMfa() {
        setCustomerData();
        ResponseWrapper<User> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();

        CasTestUtil.resetUserMfa(customerIdentity, userIdentity);
    }

    private List<String[]> getFileContent(InputStream response, String filename) {
        File file = FileResourceUtil.copyIntoTempFile(response, null, filename);
        List<String[]> fileData = null;
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .build();
            fileData = csvReader.readAll();
            fileReader.close();
            csvReader.close();
        } catch (Exception e) {
            log.error(String.format("FILE NOT FOUND ::: %s", e.getMessage()));
            throw new IllegalArgumentException(e);
        }
        return fileData;
    }

    @Test
    @TestRail(id = {16379})
    @Description("Export users template")
    public void exportUsersTemplate() {
        newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();
        List<String> headers = Arrays.asList(
            "loginID", "email", "firstName", "lastName", "fullName", "isAdmin", "isVPEAdmin", "isJasperAdmin", "AppStream", "ReportUser", "defaultPassword", "resetPassword",
            "userLicenseName", "preferredCurrency", "schemaPrivileges", "defaultSchema", "roles", "defaultRole", "roleName", "applicationList", "prefix", "suffix", "jobTitle",
            "department", "city/town", "state/province", "county", "countryCode", "timezone"
        );

        ResponseWrapper<String> template = casTestUtil.getCommonRequest(CASAPIEnum.EXPORT_TEMPLATE, null, HttpStatus.SC_OK, customerIdentity);
        InputStream responseTemplate = new ByteArrayInputStream(template.getBody().getBytes(StandardCharsets.UTF_8));
        List<String[]> templateData = getFileContent(responseTemplate, "template.csv");

        soft.assertThat(Arrays.stream(templateData.get(0)).collect(Collectors.toList())).isEqualTo(headers);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {16378})
    @Description("Export customer users")
    public void exportUsers() {
        setCustomerData();
        ResponseWrapper<User> user = casTestUtil.createUser(newCustomer);
        String cloudRef = newCustomer.getCloudReference();
        String userName = user.getResponseEntity().getUsername();

        ResponseWrapper<String> users = casTestUtil.getCommonRequest(CASAPIEnum.EXPORT_USERS, null, HttpStatus.SC_OK, customerIdentity);
        InputStream usersResponse = new ByteArrayInputStream(users.getBody().getBytes(StandardCharsets.UTF_8));
        ConcurrentLinkedQueue<UsersData> usersData = new InitFileData().initRows(UsersData.class, FileResourceUtil.copyIntoTempFile(usersResponse, null, "users.csv"));

        soft.assertThat(usersData.stream().filter(exportU -> exportU.getLoginID().equals(userName)).collect(Collectors.toList())).isNotEmpty();
        soft.assertThat(usersData.stream().filter(exportU -> exportU.getLoginID().equals(cloudRef + ".service-account.1")).collect(Collectors.toList())).isNotEmpty();
        soft.assertThat(usersData.stream().filter(exportU -> exportU.getLoginID().equals(cloudRef + ".service-account.2")).collect(Collectors.toList())).isNotEmpty();
        soft.assertThat(usersData.stream().filter(exportU -> exportU.getLoginID().equals(cloudRef + ".service-account.3")).collect(Collectors.toList())).isNotEmpty();
        soft.assertThat(usersData.stream().filter(exportU -> exportU.getLoginID().equals(cloudRef + ".service-account.4")).collect(Collectors.toList())).isNotEmpty();
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> acsLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, acsIdentity);
        licensedAcsIdentity = acsLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, cirIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, acsIdentity, siteIdentity);
    }
}
