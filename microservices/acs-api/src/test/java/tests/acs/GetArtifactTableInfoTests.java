package tests.acs;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.getartifacttableinfo.GetArtifactTableInfoResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetArtifactTableInfoTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "12062")
    @Description("Test Get Artifact Table Info")
    public void testGetArtifactTableInfo() {
        AcsResources acsResources = new AcsResources();
        GetArtifactTableInfoResponse getArtifactTableInfoResponse = acsResources.getArtifactTableInfo();

        assertThat(getArtifactTableInfoResponse, is(notNullValue()));
    }
}
