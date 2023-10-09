package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;
import com.apriori.util.VDSTestUtil;
import com.apriori.vds.enums.VDSAPIEnum;
import com.apriori.vds.models.response.access.control.AccessControlPermissionItems;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AccessControlsTest extends VDSTestUtil {

    @Test
    @TestRail(id = {7752})
    @Description("Get a list of Access Control Groups for a specific customer.")
    public void getGroups() {
        getAccessControlGroupsResponse();
    }

    @Test
    @TestRail(id = {7753})
    @Description("Get a list of Access Control Permissions for a specific customer.")
    public void getPermissions() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_PERMISSIONS, AccessControlPermissionItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {7754})
    @Description("Post synchronize the access controls for this customer. ")
    public void postSynchronize() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.POST_SYNCHRONIZE, null)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).post();
    }
}
