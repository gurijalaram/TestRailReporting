package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
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

        String expectedName = "SimpleHole";
        assertThat(getArtifactTableInfoResponse.getTypeKey().getSecond(), is(equalTo(expectedName)));
        assertThat(getArtifactTableInfoResponse.getTypeKey().getName(), is(equalTo(expectedName)));
        assertThat(getArtifactTableInfoResponse.getName(), is(equalTo(expectedName)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "12070")
    @Description("Test Get Artifact Table Info - Negative - Invalid Process Group")
    public void testGetArtifactTableInfoNegativeInvalidProcessGroup() {
        AcsResources acsResources = new AcsResources();
        GenericErrorResponse genericErrorResponse = acsResources.getArtifactTableInfoInvalidProcessGroup();

        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(404)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("Uknown process group: Sheet Metals")));
    }
}
