package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.UsersData;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.InitFileData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@ExtendWith(TestRulesAPI.class)
public class CasCustomersUsersTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private SoftAssertions soft = new SoftAssertions();
    private Customer newCustomer;
    private String customerIdentity;
    private String userIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private UserCredentials currentUser = UserUtil.getUser("admin");

    @BeforeEach
    public void getToken() {
        RequestEntityUtil_Old.useTokenForRequests(currentUser.getToken());
    }

    @AfterEach
    public void cleanUp() {
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                customerIdentity,
                userIdentity
            );
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
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
            "userLicenseName", "preferredCurrency", "schemaPrivileges", "defaultSchema", "rolesAccessControlsMapping", "defaultRole", "roleName", "applicationList", "prefix", "suffix", "jobTitle",
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

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
    }
}
