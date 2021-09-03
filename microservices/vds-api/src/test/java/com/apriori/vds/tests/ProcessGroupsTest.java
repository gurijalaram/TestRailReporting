package com.apriori.vds.tests;

import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.tests.util.ProcessGroupUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ProcessGroupsTest extends ProcessGroupUtil {
    private static final List<String> cidSupportedPgNames = Arrays.asList("2-Model Machining", "Additive Manufacturing", "Assembly", "Bar & Tube Fab", "Casting", "Casting - Die", "Casting - Sand");
    private static final List<String> cidNotSupportedPgNames = Arrays.asList("Assembly Molding", "Assembly Plastic Molding", "Casting - Investment");

    @Test
    @TestRail(testCaseId = {"8271"})
    @Description("Get a list of process groups for a specific customer.")
    public void getProcessGroups() {
        List<ProcessGroup> processGroups = getProcessGroupsResponse();

        final String failedProcessGroups = this.validateProcessGroups(processGroups);

        Assert.assertTrue("Process groups are not appropriate to supported types : " + failedProcessGroups,
            failedProcessGroups.isEmpty()
        );
    }

    private String validateProcessGroups(List<ProcessGroup> processGroups) {
        StringBuilder failedPGs = new StringBuilder();

        processGroups.forEach(processGroup -> {
            if (processGroup.getCidSupported() && !cidSupportedPgNames.contains(processGroup.getName())) {
                failedPGs.append(String.format("Should be supported: %s | ", processGroup.getName()));
            }

            if (!processGroup.getCidSupported() && !cidNotSupportedPgNames.contains(processGroup.getName())) {
                failedPGs.append(String.format("Should NOT be supported: %s | ", processGroup.getName()));
            }
        });

        return failedPGs.toString();
    }


    @Test
    @TestRail(testCaseId = {"8272"})
    @Description("Get a ProcessGroup for a customer identified by its identity.")
    public void getProcessGroupsByIdentity() {
        List<ProcessGroup> processGroups = getProcessGroupsResponse();
        Assert.assertNotEquals("To get Process Group, response should contain it.", 0, processGroups.size());

        RequestEntity requestEntity = RequestEntityUtil.initWithApUserContext(VDSAPIEnum.GET_PROCESS_GROUP_BY_IDENTITY, ProcessGroup.class)
            .inlineVariables(processGroups.get(0).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }
}
