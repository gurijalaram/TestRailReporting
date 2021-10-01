package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.user.group.associations.UserGroupAssociationRequest;
import com.apriori.vds.entity.response.user.group.associations.UserGroupAssociation;
import com.apriori.vds.entity.response.user.group.associations.UserGroupAssociationsItems;
import com.apriori.vds.tests.util.ProcessGroupUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserGroupAssociationsTest extends ProcessGroupUtil {
    private static final Set<String> userGroupAssociationsToDelete = new HashSet<>();

    @AfterClass
    public static void deleteTestingData() {
        userGroupAssociationsToDelete.forEach(UserGroupAssociationsTest::deleteUserGroupAssociationById);
    }

    @Test
    @TestRail(testCaseId = {"8321"})
    @Description("GET a list of UserGroupAssociations assigned to a specific group for a customer.")
    public void getUserGroupAssociations() {
        this.getUserGroupAssociationsResponse();
    }

    @Test
    @TestRail(testCaseId = {"8323"})
    @Description("GET a specific UserGroupAssociation.")
    public void getUserGroupAssociationByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_SPECIFIC_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, UserGroupAssociation.class)
                .inlineVariables(
                    getGroupIdentity(),
                    this.postUserGroupAssociation().getIdentity()
                );
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8322"})
    @Description("POST a new UserGroupAssociation.")
    public void postUserGroupAssociationTest() {
        this.postUserGroupAssociation();
    }


    @Test
    @TestRail(testCaseId = {"8324"})
    @Description("DELETE a UserGroupAssociation.")
    public void deleteSiteVariablesByIdentity() {
        final String idToDelete = this.postUserGroupAssociation().getIdentity();

        deleteUserGroupAssociationById(idToDelete);
        userGroupAssociationsToDelete.remove(idToDelete);
    }

    @Test
    @TestRail(testCaseId = {"8325"})
    @Description("PATCH a UserGroupAssociation.")
    public void patchUserGroupAssociationByIdentity() {
        UserGroupAssociation userGroupAssociationBeforeUpdate = this.postUserGroupAssociation();

        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.PATCH_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, UserGroupAssociation.class)
                .inlineVariables(getGroupIdentity(), userGroupAssociationBeforeUpdate.getIdentity())
                .body(UserGroupAssociationRequest.builder()
                    .customerIdentity(customerId)
                    .userIdentity(userId)
                    .updatedBy(userId)
                    .build()
                );

        final ResponseWrapper<UserGroupAssociation> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, updatedSiteVariableResponse.getStatusCode());
    }

    private List<UserGroupAssociation> getUserGroupAssociationsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_UG_ASSOCIATIONS_BY_GROUP_ID, UserGroupAssociationsItems.class)
            .inlineVariables(getGroupIdentity());

        ResponseWrapper<UserGroupAssociationsItems> userGroupAssociationsResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, userGroupAssociationsResponse.getStatusCode());

        return userGroupAssociationsResponse.getResponseEntity().getItems();
    }

    private UserGroupAssociation postUserGroupAssociation() {

        UserGroupAssociationRequest requestBody;

        List<UserGroupAssociation> userGroupAssociations = this.getUserGroupAssociationsResponse();


        if (!userGroupAssociations.isEmpty()) {
            UserGroupAssociation userGroupAssociation = userGroupAssociations.get(0);

            deleteUserGroupAssociationById(userGroupAssociation.getIdentity());
            userGroupAssociationsToDelete.remove(userGroupAssociation.getIdentity());

            requestBody = UserGroupAssociationRequest.builder()
                .customerIdentity(userGroupAssociation.getCustomerIdentity())
                .userIdentity(userGroupAssociation.getUserIdentity())
                .createdBy(userGroupAssociation.getUserIdentity())
                .build();

        } else {
            requestBody = UserGroupAssociationRequest.builder()
                .customerIdentity(customerId)
                .userIdentity(userId)
                .createdBy(userId)
                .build();
        }


        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.POST_UG_ASSOCIATIONS_BY_GROUP_ID, UserGroupAssociation.class)
                .inlineVariables(getGroupIdentity())
                .body(requestBody);

        ResponseWrapper<UserGroupAssociation> userGroupAssociationResponse = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, userGroupAssociationResponse.getStatusCode());

        UserGroupAssociation createdUserGroupAssociation = userGroupAssociationResponse.getResponseEntity();

        userGroupAssociationsToDelete.add(createdUserGroupAssociation.getIdentity());

        this.getUserGroupAssociations();
        return userGroupAssociationResponse.getResponseEntity();
    }

    private static void deleteUserGroupAssociationById(final String ugaIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(VDSAPIEnum.DELETE_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, null)
                .inlineVariables(getGroupIdentity(), ugaIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());
    }
}
