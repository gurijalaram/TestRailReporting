package com.apriori.cas.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.CustomerAssociation;
import com.apriori.cas.api.models.response.CustomerAssociationUser;
import com.apriori.cas.api.models.response.CustomerAssociationUsers;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CasCustomerUserAssociationTests {
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private Customer aprioriInternal;
    private Customer targetCustomer;
    private CustomerAssociation customerAssociationToAprioriInternal;
    private List<CustomerAssociationUser> associatedUsers;
    private List<User> usersToAssociate;

    @BeforeAll
    public static void globalSetup() {
        RequestEntityUtil_Old.useTokenForRequests(UserUtil.getUser(APRIORI_DEVELOPER).getToken());
    }

    @BeforeEach
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

    @AfterEach
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
    @TestRail(id = {5683})
    public void createUserAssociation() {
        User user = usersToAssociate.get(0);
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
    @TestRail(id = {5684})
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
    @TestRail(id = {10066})
    public void getUserAssociationCandidates() {
        associatedUsers.add(casTestUtil.createCustomerAssociationUser(usersToAssociate.get(0), customerAssociationToAprioriInternal).getResponseEntity());
        List<User> allUsers = casTestUtil.findUsers(aprioriInternal);
        ResponseWrapper<Users> response = casTestUtil.findCustomerAssociationCandidates(aprioriInternal, customerAssociationToAprioriInternal);

        soft.assertThat(response.getResponseEntity().getItems().size())
            .isEqualTo(allUsers.size() - associatedUsers.size());
        soft.assertAll();
    }

    @Test
    @Description("Delete a user association from a customer.")
    @TestRail(id = {5685})
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
