package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.access.control.AccessControlGroupItems;
import com.apriori.vds.entity.response.access.control.AccessControlPermissionItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AccessControlsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7752"})
    @Description("Get a list of Access Control Groups for a specific customer.")
    public void getGroups() {
        getGroupsResponse();
    }

    @Test
    @TestRail(testCaseId = {"7753"})
    @Description("Get a list of Access Control Permissions for a specific customer.")
    public void getPermissions() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PERMISSIONS, AccessControlPermissionItems.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"7754"})
    @Description("Post synchronize the access controls for this customer. ")
    public void postSynchronize() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.POST_SYNCHRONIZE, null);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT,
            HTTP2Request.build(requestEntity).post().getStatusCode()
        );
    }
}
