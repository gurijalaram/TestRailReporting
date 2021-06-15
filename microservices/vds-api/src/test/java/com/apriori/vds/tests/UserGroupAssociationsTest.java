package com.apriori.vds.tests;

import static org.junit.Assert.assertNotEquals;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.request.user.group.associations.UserGroupAssociationRequest;
import com.apriori.vds.entity.response.user.group.associations.UserGroupAssociation;
import com.apriori.vds.entity.response.user.group.associations.UserGroupAssociationsItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserGroupAssociationsTest extends VDSTestUtil {
    private static final List<String> userGroupAssociationsToDelete = new ArrayList<>();

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
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_SPECIFIC_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, UserGroupAssociation.class)
                .inlineVariables(Arrays.asList(
                    getGroupIdentity(),
                    this.getFirstUserGroupAssociation().getIdentity()
                ));
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTP2Request.build(requestEntity).get().getStatusCode());
    }

    private UserGroupAssociation getFirstUserGroupAssociation() {
        List<UserGroupAssociation> userGroupAssociations = this.getUserGroupAssociationsResponse();
        assertNotEquals("To get user group association, response should contain it.", 0, userGroupAssociations.size());

        return userGroupAssociations.get(0);
    }

    @Test
    @TestRail(testCaseId = {"8322"})
    @Description("POST a new UserGroupAssociation.")
    public void postUserGroupAssociationTest() {
        userGroupAssociationsToDelete.add(this.postUserGroupAssociation().getIdentity());
    }


    @Test
    @TestRail(testCaseId = {"8324"})
    @Description("DELETE a UserGroupAssociation.")
    public void deleteSiteVariablesByIdentity() {
        deleteUserGroupAssociationById(this.postUserGroupAssociation().getIdentity());
//        deleteUserGroupAssociationById("K0NCDA6413J2");
    }

    @Test
    @TestRail(testCaseId = {"8325"})
    @Description("PATCH a UserGroupAssociation.")
    public void patchUserGroupAssociationByIdentity() {
        UserGroupAssociation userGroupAssociationBeforeUpdate = this.postUserGroupAssociation();
        userGroupAssociationsToDelete.add(userGroupAssociationBeforeUpdate.getIdentity());

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PATCH_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, UserGroupAssociation.class)
                .inlineVariables(Arrays.asList(getGroupIdentity(), userGroupAssociationBeforeUpdate.getIdentity()))
                .body(UserGroupAssociationRequest.builder()
                    .userIdentity("testingUserId")
                    .build()
                );

        final ResponseWrapper<UserGroupAssociation> updatedSiteVariableResponse = HTTP2Request.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, updatedSiteVariableResponse.getStatusCode());
    }


    private List<UserGroupAssociation> getUserGroupAssociationsResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_UG_ASSOCIATIONS_BY_GROUP_ID, UserGroupAssociationsItems.class)
            .inlineVariables(Collections.singletonList(getGroupIdentity()));
//            .inlineVariables(Collections.singletonList("MDK300J15BIE"));

        ResponseWrapper<UserGroupAssociationsItems> userGroupAssociationsResponse = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, userGroupAssociationsResponse.getStatusCode());

        return userGroupAssociationsResponse.getResponseEntity().getItems();
    }

    private UserGroupAssociation postUserGroupAssociation() {
//        UserGroupAssociation exampleOfUserGroupAssociation = getFirstUserGroupAssociation();
//        deleteUserGroupAssociationById(exampleOfUserGroupAssociation.getIdentity());

        List<UserGroupAssociation> userGroupAssociations = this.getUserGroupAssociationsResponse();
        UserGroupAssociation exampleOfUserGroupAssociation = getFirstUserGroupAssociation();

        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.POST_UG_ASSOCIATIONS_BY_GROUP_ID, UserGroupAssociation.class)
                .inlineVariables(Collections.singletonList(getGroupIdentity()))
//                .inlineVariables(Collections.singletonList("MDK300J15BIE"))
                .body(UserGroupAssociationRequest.builder()
                    .customerIdentity(exampleOfUserGroupAssociation.getCustomerIdentity())
//                    .customerIdentity("H337GKD0LA0M")
//                    .customerIdentity("DF038C3C63CB")
//                    .userIdentity("JLF7MA33C7JG")
//                    .userIdentity("87A01AD4D90B")
                    .userIdentity(exampleOfUserGroupAssociation.getUserIdentity())
//                    .createdBy("SYSTEM00000")
//                    .createdBy("#ETL00000000")
                    .createdBy(exampleOfUserGroupAssociation.getCreatedBy())
                    .build()
                );

        ResponseWrapper<UserGroupAssociation> userGroupAssociationResponse = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, userGroupAssociationResponse.getStatusCode());

        return userGroupAssociationResponse.getResponseEntity();
    }

    private static void deleteUserGroupAssociationById(final String ugaIdentity) {
        RequestEntity requestEntity =
            VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.DELETE_UG_ASSOCIATIONS_BY_GROUP_UGA_IDs, null)
//                .inlineVariables(Arrays.asList(getGroupIdentity(), ugaIdentity));
                .inlineVariables(Arrays.asList("MDK300J15BIE", ugaIdentity));
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }
}
