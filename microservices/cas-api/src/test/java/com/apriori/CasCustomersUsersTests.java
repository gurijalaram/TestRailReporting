package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.CustomerUser;
import com.apriori.cas.models.response.CustomerUsers;
import com.apriori.cas.models.response.UsersData;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.InitFileData;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

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
@ExtendWith(TestRulesApi.class)
public class CasCustomersUsersTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private Customer newCustomer;
    private String customerIdentity;
    private String userIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
        newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();
    }

    @AfterEach
    public void cleanUp() {
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

        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        soft.assertThat(user.getResponseEntity().getCustomerIdentity())
            .isEqualTo(customerIdentity);

        ResponseWrapper<CustomerUsers> customerUsers = casTestUtil.getCommonRequest(CASAPIEnum.USERS, CustomerUsers.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(customerUsers.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        userIdentity = customerUsers.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<CustomerUser> singleUser = casTestUtil.getCommonRequest(CASAPIEnum.USER, CustomerUser.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(singleUser.getResponseEntity().getIdentity())
            .isEqualTo(userIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5664})
    @Description("Update the User.")
    public void updateCustomerUsers() {
        ResponseWrapper<CustomerUser> userResponse = casTestUtil.createUser(newCustomer);
        CustomerUser user = userResponse.getResponseEntity();
        userIdentity = user.getIdentity();

        ResponseWrapper<CustomerUser> updatedUser = CasTestUtil.updateUser(user);

        soft.assertThat(updatedUser.getResponseEntity().getUserProfile().getDepartment())
            .isEqualTo("QA");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5667})
    @Description("Reset the MFA configuration for a user.")
    public void resettingUserMfa() {
        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
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
    public void exportCustomerUsers() {
        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        String cloudRef = newCustomer.getCloudReference();
        String userName = user.getResponseEntity().getUsername();

        ResponseWrapper<String> users = casTestUtil.getCommonRequest(CASAPIEnum.EXPORT_USERS, null, HttpStatus.SC_OK, customerIdentity);
        InputStream usersResponse = new ByteArrayInputStream(users.getBody().getBytes(StandardCharsets.UTF_8));
        ConcurrentLinkedQueue<UsersData> usersData = new InitFileData().initRows(UsersData.class, FileResourceUtil.copyIntoTempFile(usersResponse, null, "users.csv"));

        soft.assertThat(usersData.poll().getLoginID()).isEqualTo(cloudRef + ".service-account.1");
        soft.assertThat(usersData.poll().getLoginID()).isEqualTo(cloudRef + ".service-account.2");
        soft.assertThat(usersData.poll().getLoginID()).isEqualTo(cloudRef + ".service-account.3");
        soft.assertThat(usersData.poll().getLoginID()).isEqualTo(cloudRef + ".service-account.4");
        soft.assertThat(usersData.poll().getLoginID()).isEqualTo(userName);
        soft.assertAll();
    }
}
