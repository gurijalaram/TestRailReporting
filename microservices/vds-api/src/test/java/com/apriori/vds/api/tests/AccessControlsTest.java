package com.apriori.vds.api.tests;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.access.control.AccessControlPermissionItems;
import com.apriori.vds.api.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AccessControlsTest {
    private RequestEntityUtil requestEntityUtil;
    private VDSTestUtil vdsTestUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        vdsTestUtil = new VDSTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {7752})
    @Description("Get a list of Access Control Groups for a specific customer.")
    public void getGroups() {
        vdsTestUtil.getAccessControlGroups();
    }

    @Test
    @TestRail(id = {7753})
    @Description("Get a list of Access Control Permissions for a specific customer.")
    public void getPermissions() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.PERMISSIONS, AccessControlPermissionItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {7754})
    @Description("Post synchronize the access controls for this customer. ")
    public void postSynchronize() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.SYNCHRONIZE, null)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).post();
    }
}
