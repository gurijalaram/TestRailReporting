package com.apriori.cas.api.tests;

import static com.apriori.shared.util.enums.CustomerEnum.AP_INT;
import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Applications;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.Customers;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

@ExtendWith(TestRulesAPI.class)
@EnabledIfSystemProperty(named = "customer", matches = AP_INT)
public class CasCustomersTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private String userToken = UserUtil.getUser(APRIORI_DEVELOPER).getToken();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil_Old.useTokenForRequests(userToken);
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            // TODO z: fix it threads
            // cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, currentUser.getToken(), customerIdentity);
            HTTPRequest.build(new RequestEntity().endpoint(CDSAPIEnum.CUSTOMER_BY_ID).inlineVariables(customerIdentity).token(userToken).expectedResponseCode(HttpStatus.SC_NO_CONTENT)).delete();
        }
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {5810})
    @Description("Get a list of CAS customers sorted by name")
    // TODO z: fix it threads
    public void getCustomersSortedByName() {
        //ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        RequestEntity request = new RequestEntity()
            .endpoint(CASAPIEnum.CUSTOMERS)
            .returnType(Customers.class)
            .token(userToken)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Customers> response = HTTPRequest.build(request).get();

        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5645})
    @Description("Get the Customer identified by its identity")
    public void getCustomersByIdentity() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customer> responseIdentity = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customer.class, HttpStatus.SC_OK, customer.getIdentity());

        soft.assertThat(responseIdentity.getResponseEntity().getName())
            .isEqualTo(customer.getName());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5643})
    @Description("Get the Customer identified by its name")
    public void getCustomerByName() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        Customer customer = response.getResponseEntity().getItems().get(0);

        ResponseWrapper<Customers> responseName = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customers.class, HttpStatus.SC_OK, "?name[CN]=" + customer.getName());

        soft.assertThat(responseName.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5644})
    @Description("Get the Customer by not existing identity")
    public void getCustomerNotExistingIdentity() {
        casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, CasErrorMessage.class, HttpStatus.SC_NOT_FOUND, "76EA87KCHIKD");
    }

    @Test
    @TestRail(id = {5643})
    @Description("Get the Customer by not existing name")
    public void getCustomerNotExistingName() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customers.class, HttpStatus.SC_OK, "?name[CN]=" + generateStringUtil.generateCustomerName());

        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isEqualTo(0);
    }

    @Test
    @TestRail(id = {5642, 5644})
    @Description("Add a new customer, get it by name, update the customer and get it by identity")
    public void createUpdateCustomer() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String emailPattern = "\\S+@".concat(customerName);

        ResponseWrapper<Customer> response = CasTestUtil.addCustomer(customerName, cloudRef, description, emailPattern);

        soft.assertThat(response.getResponseEntity().getName())
            .isEqualTo(customerName);

        ResponseWrapper<Customers> responseName = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customers.class, HttpStatus.SC_OK, "?name[CN]=" + customerName);

        soft.assertThat(responseName.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        customerIdentity = responseName.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Customer> patchResponse = CasTestUtil.updateCustomer(customerIdentity, email);

        soft.assertThat(patchResponse.getResponseEntity().getEmailDomains())
            .isEqualTo(Arrays.asList(email + ".com", email + ".co.uk"));

        ResponseWrapper<Customer> responseIdentity = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, Customer.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(responseIdentity.getResponseEntity().getName())
            .isEqualTo(customerName);
        soft.assertThat(responseIdentity.getResponseEntity().getEmailDomains())
            .isEqualTo(Arrays.asList(email + ".com", email + ".co.uk"));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5826})
    @Description("Resetting the MFA enrollment status of every user for the customer")
    public void resettingMFA() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> response = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        soft.assertThat(response.getResponseEntity().getName())
            .isEqualTo(customerName);
        soft.assertAll();

        customerIdentity = response.getResponseEntity().getIdentity();
        CasTestUtil.resetMfa(customerIdentity);
    }

    @Test
    @TestRail(id = {16546})
    @Description("Return a paged list of applications licensed for a specific customer.")
    public void getCustomerLicensedApplications() {
        String customerIdentity = casTestUtil.getAprioriInternal().getIdentity();

        ResponseWrapper<Applications> responseApplications = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_LICENSED_APP,
            Applications.class,
            HttpStatus.SC_OK,
            customerIdentity);

        soft.assertThat(responseApplications.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}
