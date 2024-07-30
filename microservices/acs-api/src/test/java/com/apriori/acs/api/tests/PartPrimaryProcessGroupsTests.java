package com.apriori.acs.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.acs.api.models.response.acs.partprimaryprocessgroups.PartPrimaryProcessGroupsResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class PartPrimaryProcessGroupsTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = 10881)
    @Description("Validate Get Part Primary Process Groups Endpoint")
    public void testGetPartPrimaryProcessGroupsEndpoint() {
        PartPrimaryProcessGroupsResponse getPartPrimaryProcessGroupsResponse = acsResources.getPartPrimaryProcessGroups();

        List<String> processGroupValues = Arrays.stream(ProcessGroupEnum.getNames()).collect(Collectors.toList());
        processGroupValues.remove(ProcessGroupEnum.ASSEMBLY.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.RESOURCES.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.ROLL_UP.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.WITHOUT_PG.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.INVALID_PG.getProcessGroup());

        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.size()).isEqualTo(22);
        softAssertions.assertThat(getPartPrimaryProcessGroupsResponse.containsAll(processGroupValues)).isEqualTo(true);
        softAssertions.assertAll();
    }
}
