package com.apriori.acs.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.api.models.response.acs.partprimaryprocessgroups.PartPrimaryProcessGroupsResponse;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class PartPrimaryProcessGroupsTests extends TestUtil {

    @Test
    @Tag(API_SANITY)
    @TestRail(id = 10881)
    @Description("Validate Get Part Primary Process Groups Endpoint")
    public void testGetPartPrimaryProcessGroupsEndpoint() {
        UserCredentials userCredentials = UserUtil.getUser("common");
        AcsResources acsResources = new AcsResources(userCredentials);
        PartPrimaryProcessGroupsResponse getPartPrimaryProcessGroupsResponse = acsResources.getPartPrimaryProcessGroups();

        List<String> processGroupValues = Arrays.stream(ProcessGroupEnum.getNames()).collect(Collectors.toList());
        processGroupValues.remove(ProcessGroupEnum.ASSEMBLY.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.RESOURCES.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.ROLL_UP.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.WITHOUT_PG.getProcessGroup());
        processGroupValues.remove(ProcessGroupEnum.INVALID_PG.getProcessGroup());

        assertThat(getPartPrimaryProcessGroupsResponse.size(), is(equalTo(22)));
        assertThat(getPartPrimaryProcessGroupsResponse.containsAll(processGroupValues), is(true));
    }
}
