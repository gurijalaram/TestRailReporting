package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlPermissionItems;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AccessControlsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7752"})
    @Description("Get a list of Access Control Groups for a specific customer.")
    public void getGroups() {
        getAccessControlGroupsResponse();
    }

    @Test
    @TestRail(testCaseId = {"7753"})
    @Description("Get a list of Access Control Permissions for a specific customer.")
    public void getPermissions() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_PERMISSIONS, AccessControlPermissionItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"7754"})
    @Description("Post synchronize the access controls for this customer. ")
    public void postSynchronize() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.POST_SYNCHRONIZE, null)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).post();
    }
}
