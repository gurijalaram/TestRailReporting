package tests.acs;

import com.apriori.acs.entity.response.acs.getgcdmapping.GetGcdMappingResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileResponse;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;
import io.qameta.allure.Description;
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

        FileResponse fileResponse = fileUploadResources.initialisePartUpload("bracket_basic.prt", processGroup);
        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, generateStringUtil.generateScenarioName());

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup
        );

        // get gcd image mapping by scenario iteration key
        GetGcdMappingResponse getGcdMappingResponse = acsResources.getGcdMapping(costOutputs.getScenarioIterationKey());

        // get artifact properties
        acsResources.getArtifactTableInfo();
    }
}
