package com.apriori;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.bcs.models.response.ProcessGroup;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;
import com.apriori.util.ProcessGroupUtil;
import com.apriori.vds.enums.VDSAPIEnum;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesApi.class)
public class ProcessGroupsTest extends ProcessGroupUtil {
    private static final List<String> cidSupportedPgNames = Arrays.asList("2-Model Machining", "Additive Manufacturing", "Casting - Investment", "Bar & Tube Fab", "Casting", "Casting - Die", "Casting - Sand");
    private static final List<String> cidNotSupportedPgNames = Arrays.asList("Assembly Molding", "Assembly Plastic Molding", "Assembly", "Composites");

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {8271})
    @Description("Get a list of process groups for a specific customer.")
    public void getProcessGroups() {
        List<ProcessGroup> processGroups = ProcessGroupUtil.getProcessGroupsResponse();

        final String failedProcessGroups = this.validateProcessGroups(processGroups);

        assertTrue(failedProcessGroups.isEmpty(), "Process groups are not appropriate to supported types");
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
        assertNotEquals(0, processGroups.size(), "To get Process Group, response should contain it.");

        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_PROCESS_GROUP_BY_IDENTITY, ProcessGroup.class)
            .inlineVariables(processGroups.get(0).getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
