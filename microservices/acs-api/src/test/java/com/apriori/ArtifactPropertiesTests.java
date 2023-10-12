package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.models.request.workorders.NewPartRequest;
import com.apriori.acs.models.response.acs.artifactproperties.ArtifactListItem;
import com.apriori.acs.models.response.acs.artifactproperties.ArtifactPropertiesResponse;
import com.apriori.acs.models.response.acs.gcdmapping.GcdMappingResponse;
import com.apriori.acs.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.json.JsonManager;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ArtifactPropertiesTests extends TestUtil {

    @Test
    @TestRail(id = 12079)
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

        GcdMappingResponse getGcdMappingResponse = acsResources.getGcdMapping(costOutputs.getScenarioIterationKey());

        ArtifactPropertiesResponse getArtifactPropertiesResponse = acsResources.getArtifactProperties(costOutputs.getScenarioIterationKey(), getGcdMappingResponse);

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
