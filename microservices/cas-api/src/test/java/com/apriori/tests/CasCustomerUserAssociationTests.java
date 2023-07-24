package com.apriori.tests;

import com.apriori.entity.response.Customer;
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
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CasCustomerUserAssociationTests {
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(response.getResponseEntity().getUserIdentity())
            .isEqualTo(user.getIdentity());

        casTestUtil.create(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USERS,
            CasErrorMessage.class,
            associatedUser,
            HttpStatus.SC_CONFLICT,
            aprioriInternal.getIdentity(),
            customerAssociationToAprioriInternal.getIdentity()
        );
        soft.assertAll();
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

        soft.assertThat(response.getResponseEntity().getItems().size())
            .isEqualTo(associatedUsers.size());
        soft.assertAll();
    }

    @Test
    @Description("Gets a list of candidates that can be associated to a customer.")
    @TestRail(testCaseId = {"10066"})
    public void getUserAssociationCandidates() {
        associatedUsers.add(casTestUtil.createCustomerAssociationUser(usersToAssociate.get(0), customerAssociationToAprioriInternal).getResponseEntity());
        List<CustomerUser> allUsers = casTestUtil.findUsers(aprioriInternal);
        ResponseWrapper<CustomerUsers> response = casTestUtil.findCustomerAssociationCandidates(aprioriInternal, customerAssociationToAprioriInternal);

        soft.assertThat(response.getResponseEntity().getItems().size())
            .isEqualTo(allUsers.size() - associatedUsers.size());
        soft.assertAll();
    }

    @Test
    @Description("Delete a user association from a customer.")
    @TestRail(testCaseId = {"5685"})
    public void deleteUserAssociation() {
        final CustomerAssociationUser user = casTestUtil.createCustomerAssociationUser(usersToAssociate.get(0), customerAssociationToAprioriInternal).getResponseEntity();

        casTestUtil.delete(
            CASAPIEnum.CUSTOMER_ASSOCIATIONS_USER,
            aprioriInternal.getIdentity(),
            customerAssociationToAprioriInternal.getIdentity(),
            user.getIdentity()
        );
    }
}
