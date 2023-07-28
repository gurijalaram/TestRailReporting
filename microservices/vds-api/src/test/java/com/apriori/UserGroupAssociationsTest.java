package com.apriori;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;
import com.apriori.util.ProcessGroupUtil;
import com.apriori.util.VDSTestUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.user.group.associations.UserGroupAssociationRequest;
import com.apriori.vds.entity.response.user.group.associations.UserGroupAssociation;
import com.apriori.vds.entity.response.user.group.associations.UserGroupAssociationsItems;

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
    @TestRail(id = {8321})
    @Description("GET a list of UserGroupAssociations assigned to a specific group for a customer.")
    public void getUserGroupAssociations() {
        this.getUserGroupAssociationsResponse();
    }

    @Test
    @TestRail(id = {8323})
    @Description("GET a specific UserGroupAssociation.")
    public void getUserGroupAssociationByIdentity() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.GET_SPECIFIC_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, UserGroupAssociation.class)
                .inlineVariables(
                    ProcessGroupUtil.getGroupIdentity(),
                    this.postUserGroupAssociation().getIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {8322})
    @Description("POST a new UserGroupAssociation.")
    public void postUserGroupAssociationTest() {
        this.postUserGroupAssociation();
    }

    @Test
    @TestRail(id = {8324})
    @Description("DELETE a UserGroupAssociation.")
    public void deleteSiteVariablesByIdentity() {
        final String idToDelete = this.postUserGroupAssociation().getIdentity();

        deleteUserGroupAssociationById(idToDelete);
        userGroupAssociationsToDelete.remove(idToDelete);
    }

    @Test
    @TestRail(id = {8325})
    @Description("PATCH a UserGroupAssociation.")
    public void patchUserGroupAssociationByIdentity() {
        UserGroupAssociation userGroupAssociationBeforeUpdate = this.postUserGroupAssociation();

        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.PATCH_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, UserGroupAssociation.class)
                .inlineVariables(ProcessGroupUtil.getGroupIdentity(), userGroupAssociationBeforeUpdate.getIdentity())
                .body(UserGroupAssociationRequest.builder()
                    .customerIdentity(VDSTestUtil.customerId)
                    .userIdentity(VDSTestUtil.userId)
                    .updatedBy(VDSTestUtil.userId)
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).patch();
    }

    private List<UserGroupAssociation> getUserGroupAssociationsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_UG_ASSOCIATIONS_BY_GROUP_ID, UserGroupAssociationsItems.class)
            .inlineVariables(ProcessGroupUtil.getGroupIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<UserGroupAssociationsItems> userGroupAssociationsResponse = HTTPRequest.build(requestEntity).get();

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
                .customerIdentity(VDSTestUtil.customerId)
                .userIdentity(VDSTestUtil.userId)
                .createdBy(VDSTestUtil.userId)
                .build();
        }

        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.POST_UG_ASSOCIATIONS_BY_GROUP_ID, UserGroupAssociation.class)
                .inlineVariables(ProcessGroupUtil.getGroupIdentity())
                .body(requestBody)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<UserGroupAssociation> userGroupAssociationResponse = HTTPRequest.build(requestEntity).post();

        UserGroupAssociation createdUserGroupAssociation = userGroupAssociationResponse.getResponseEntity();

        userGroupAssociationsToDelete.add(createdUserGroupAssociation.getIdentity());

        this.getUserGroupAssociations();
        return userGroupAssociationResponse.getResponseEntity();
    }

    private static void deleteUserGroupAssociationById(final String ugaIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(VDSAPIEnum.DELETE_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, null)
                .inlineVariables(ProcessGroupUtil.getGroupIdentity(), ugaIdentity)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }
}
