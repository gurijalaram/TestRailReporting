package com.apriori.vds.tests;

import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ProcessGroupsTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"8271"})
    @Description("Get a list of process groups for a specific customer.")
    public void getProcessGroups() {
        getProcessGroupsResponse();
    }

    @Test
    @TestRail(testCaseId = {"8272"})
    @Description("Get a ProcessGroup for a customer identified by its identity.")
    public void getProcessGroupsByIdentity() {
        List<ProcessGroup> processGroups = getProcessGroupsResponse();
        Assert.assertNotEquals("To get Process Group, response should contain it.", 0, processGroups.size());

        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_PROCESS_GROUP_BY_IDENTITY, ProcessGroup.class)
            .inlineVariables(processGroups.get(0).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }
}
