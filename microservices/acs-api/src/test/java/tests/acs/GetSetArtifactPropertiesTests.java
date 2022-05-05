package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.getartifactproperties.ArtifactListItem;
import com.apriori.acs.entity.response.acs.getartifactproperties.GetArtifactPropertiesResponse;
import com.apriori.acs.entity.response.acs.getgcdmapping.GetGcdMappingResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetSetArtifactPropertiesTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "12079")
    @Description("Verify Get Artifact Properties Endpoint")
    public void testGetArtifactPropertiesEndpoint() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        GenerateStringUtil generateStringUtil = new GenerateStringUtil();

        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), NewPartRequest.class
        );

        FileResponse fileResponse = fileUploadResources.initializePartUpload("bracket_basic.prt", processGroup);
        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, generateStringUtil.generateScenarioName());

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GetGcdMappingResponse getGcdMappingResponse = acsResources.getGcdMapping(costOutputs.getScenarioIterationKey());

        GetArtifactPropertiesResponse getArtifactPropertiesResponse = acsResources.getArtifactProperties(costOutputs.getScenarioIterationKey(), getGcdMappingResponse);

        performAssertions(getArtifactPropertiesResponse.getArtifactList().get(0), "Edge:57");
        performAssertions(getArtifactPropertiesResponse.getArtifactList().get(1), "Edge:61");
    }

    private void performAssertions(ArtifactListItem listItemToUse, String valueToAssertOn) {
        assertThat(listItemToUse.getArtifactKey().getDisplayName().equals(valueToAssertOn), is(equalTo(true)));
        assertThat(listItemToUse.getName().equals(valueToAssertOn), is(equalTo(true)));
        assertThat(
            listItemToUse.getArtifactData().getPropertyValueMap().getArtifactKey().getDisplayName().equals(valueToAssertOn),
            is(equalTo(true))
        );
    }
}
