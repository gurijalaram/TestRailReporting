package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestUtil;
import com.apriori.acs.entity.response.acs.artifacttableinfo.ArtifactTableInfoResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

public class ArtifactTableInfoTests extends TestUtil {

    @Test
    @TestRail(id = 12062)
    @Description("Test Get Artifact Table Info")
    public void testGetArtifactTableInfo() {
        AcsResources acsResources = new AcsResources();
        ArtifactTableInfoResponse getArtifactTableInfoResponse = acsResources.getArtifactTableInfo();

        String expectedName = "SimpleHole";
        assertThat(getArtifactTableInfoResponse.getTypeKey().getSecond(), is(equalTo(expectedName)));
        assertThat(getArtifactTableInfoResponse.getTypeKey().getName(), is(equalTo(expectedName)));
        assertThat(getArtifactTableInfoResponse.getName(), is(equalTo(expectedName)));
    }

    @Test
    @TestRail(id = 12070)
    @Description("Test Get Artifact Table Info - Negative - Invalid Process Group")
    public void testGetArtifactTableInfoNegativeInvalidProcessGroup() {
        AcsResources acsResources = new AcsResources();
        GenericErrorResponse genericErrorResponse = acsResources.getArtifactTableInfoInvalidProcessGroup();

        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(404)));
        assertThat(genericErrorResponse.getErrorMessage(), is(equalTo("Unknown process group: Sheet Metals")));
    }
}
