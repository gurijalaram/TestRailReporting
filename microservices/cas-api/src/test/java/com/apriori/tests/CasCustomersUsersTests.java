package com.apriori.tests;

import com.apriori.entity.response.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUsers;
import com.apriori.entity.response.UpdateUser;
import com.apriori.entity.response.UsersData;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.InitFileData;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class CasCustomersUsersTests {
    private static final Logger logger = LoggerFactory.getLogger(CasCustomersUsersTests.class);
    private SoftAssertions soft = new SoftAssertions();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private Customer newCustomer;
    private String customerIdentity;
    private String userIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();
    }

    @After
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
    @TestRail(testCaseId = {"5661", "5662", "5663"})
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
    @TestRail(testCaseId = {"5664"})
    @Description("Update the User.")
    public void updateCustomerUsers() {
        String customerName = newCustomer.getName();

        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();
        String profileIdentity = user.getResponseEntity().getUserProfile().getIdentity();
        String userName = user.getResponseEntity().getUsername();

        ResponseWrapper<UpdateUser> updatedUser = CasTestUtil.updateUser(userName, customerName, userIdentity, customerIdentity, profileIdentity);

        soft.assertThat(updatedUser.getResponseEntity().getUserProfile().getDepartment())
            .isEqualTo("QA");
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5667"})
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
            logger.error(String.format("FILE NOT FOUND ::: %s", e.getMessage()));
            throw new IllegalArgumentException(e);
        }
        return fileData;
    }

    @Test
    @TestRail(testCaseId = {"16379"})
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
    @TestRail(testCaseId = {"16378"})
    @Description("Export customer users")
    public void exportCustomerUsers() {
        ResponseWrapper<CustomerUser> user = casTestUtil.createUser(newCustomer);
        String cloudRef = newCustomer.getCloudReference();
        String userName = user.getResponseEntity().getUsername();

        ResponseWrapper<String> users = casTestUtil.getCommonRequest(CASAPIEnum.EXPORT_USERS, null, HttpStatus.SC_OK, customerIdentity);
        InputStream usersResponse = new ByteArrayInputStream(users.getBody().getBytes(StandardCharsets.UTF_8));
        ConcurrentLinkedQueue<UsersData> usersData = new InitFileData().initRows(UsersData.class, FileResourceUtil.copyIntoTempFile(usersResponse, null, "users.csv"));

        soft.assertThat(usersData.poll().getLoginID().equals(cloudRef + ".service-account.1"));
        soft.assertThat(usersData.poll().getLoginID().equals(cloudRef + ".service-account.2"));
        soft.assertThat(usersData.poll().getLoginID().equals(cloudRef + ".service-account.3"));
        soft.assertThat(usersData.poll().getLoginID().equals(cloudRef + ".service-account.4"));
        soft.assertThat(usersData.poll().getLoginID().equals(userName));
        soft.assertAll();
    }
}
