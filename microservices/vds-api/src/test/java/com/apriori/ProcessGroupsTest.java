package com.apriori;

import com.apriori.bcs.entity.response.ProcessGroup;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;
import com.apriori.util.ProcessGroupUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ProcessGroupsTest extends ProcessGroupUtil {
    private static final List<String> cidSupportedPgNames = Arrays.asList("2-Model Machining", "Additive Manufacturing", "Casting - Investment", "Bar & Tube Fab", "Casting", "Casting - Die", "Casting - Sand");
    private static final List<String> cidNotSupportedPgNames = Arrays.asList("Assembly Molding", "Assembly Plastic Molding", "Assembly", "Composites");

    @Test
    @TestRail(id = {8271})
    @Description("Get a list of process groups for a specific customer.")
    public void getProcessGroups() {
        List<ProcessGroup> processGroups = ProcessGroupUtil.getProcessGroupsResponse();

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
    @TestRail(id = {8272})
    @Description("Get a ProcessGroup for a customer identified by its identity.")
    public void getProcessGroupsByIdentity() {
        List<ProcessGroup> processGroups = ProcessGroupUtil.getProcessGroupsResponse();
        Assert.assertNotEquals("To get Process Group, response should contain it.", 0, processGroups.size());

        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_BY_IDENTITY, ProcessGroup.class)
            .inlineVariables(processGroups.get(0).getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
