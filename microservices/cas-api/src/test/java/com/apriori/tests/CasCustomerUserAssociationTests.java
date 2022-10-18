package com.apriori.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.entity.response.CustomerAssociation;
import com.apriori.entity.response.CustomerAssociationUser;
import com.apriori.entity.response.CustomerAssociationUsers;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.CustomerUsers;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CasCustomerUserAssociationTests {
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private final CasTestUtil casTestUtil = new CasTestUtil();

    private Customer aprioriInternal;
    private Customer targetCustomer;
    private CustomerAssociation customerAssociationToAprioriInternal;
    private List<CustomerAssociationUser> associatedUsers;
    private List<CustomerUser> usersToAssociate;

    @BeforeClass
    public static void globalSetup() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Before
    public void setup() {
        aprioriInternal = casTestUtil.getAprioriInternal();

        associatedUsers = new ArrayList<>();

        usersToAssociate = new ArrayList<>();
        usersToAssociate.add(casTestUtil.createUser(aprioriInternal).getResponseEntity());
        usersToAssociate.add(casTestUtil.createUser(aprioriInternal).getResponseEntity());
        usersToAssociate.add(casTestUtil.createUser(aprioriInternal).getResponseEntity());

        targetCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerAssociationToAprioriInternal = casTestUtil.findCustomerAssociation(aprioriInternal, targetCustomer);
    }

    @After
    public void teardown() {
        associatedUsers.forEach((user) -> casTestUtil.delete(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USER,
            aprioriInternal.getIdentity(),
            customerAssociationToAprioriInternal.getIdentity(),
            user.getIdentity())
        );

        // Note:  The CAS api does not support customer or user deletes, so we have to use CDS to do this.
        usersToAssociate.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, aprioriInternal.getIdentity(), user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Description("Create a user association from a source customer user to a target customer.")
    @TestRail(testCaseId = {"5683"})
    public void createUserAssociation() {
        CustomerUser user = usersToAssociate.get(0);
        ResponseWrapper<CustomerAssociationUser> response = casTestUtil.createCustomerAssociationUser(user, customerAssociationToAprioriInternal);
        CustomerAssociationUser associatedUser = response.getResponseEntity();
        associatedUsers.add(associatedUser);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(response.getResponseEntity().getUserIdentity(), is(equalTo(user.getIdentity())));

        ResponseWrapper<CasErrorMessage> error = casTestUtil.create(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USERS,
            CasErrorMessage.class,
            associatedUser,
            aprioriInternal.getIdentity(),
            customerAssociationToAprioriInternal.getIdentity()
        );
        assertThat(error.getStatusCode(), is(equalTo(HttpStatus.SC_CONFLICT)));
    }

    @Test
    @Description("Gets a list of customer associations.")
    @TestRail(testCaseId = {"5684"})
    public void getUserAssociations() {
        usersToAssociate.stream()
            .map((user) -> casTestUtil.createCustomerAssociationUser(user, customerAssociationToAprioriInternal))
            .map(ResponseWrapper::getResponseEntity)
            .forEach((associatedUser) -> associatedUsers.add(associatedUser));

        ResponseWrapper<CustomerAssociationUsers> response = casTestUtil.findCustomerAssociationUsers(aprioriInternal, customerAssociationToAprioriInternal);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getItems().size(), is(equalTo(associatedUsers.size())));
    }

    @Test
    @Description("Gets a list of candidates that can be associated to a customer.")
    @TestRail(testCaseId = {"10066"})
    public void getUserAssociationCandidates() {
        associatedUsers.add(casTestUtil.createCustomerAssociationUser(usersToAssociate.get(0), customerAssociationToAprioriInternal).getResponseEntity());
        List<CustomerUser> allUsers = casTestUtil.findUsers(aprioriInternal);
        ResponseWrapper<CustomerUsers> response = casTestUtil.findCustomerAssociationCandidates(aprioriInternal, customerAssociationToAprioriInternal);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getItems().size(), is(equalTo(allUsers.size() - associatedUsers.size())));
    }

    @Test
    @Description("Delete a user association from a customer.")
    @TestRail(testCaseId = {"5685"})
    public void deleteUserAssociation() {
        final CustomerAssociationUser user = casTestUtil.createCustomerAssociationUser(usersToAssociate.get(0), customerAssociationToAprioriInternal).getResponseEntity();

        ResponseWrapper<String> response = casTestUtil.delete(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USER,
            aprioriInternal.getIdentity(),
            customerAssociationToAprioriInternal.getIdentity(),
            user.getIdentity()
        );

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));

        //cannot return deleted association

        /*CustomerAssociationUser check = casTestUtil.findCustomerAssociationUser(aprioriInternal, customerAssociationToAprioriInternal, user.getIdentity());
        assertThat(check.getDeletedAt(), is(notNullValue()));*/
    }
}
