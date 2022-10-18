package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.partprimaryprocessgroups.PartPrimaryProcessGroupsResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PartPrimaryProcessGroupsTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "10881")
    @Description("Validate Get Part Primary Process Groups Endpoint")
    public void testGetPartPrimaryProcessGroupsEndpoint() {
        AcsResources acsResources = new AcsResources();
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
