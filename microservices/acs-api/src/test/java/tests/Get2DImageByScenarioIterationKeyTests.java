package tests;

import java.util.ArrayList;
import java.util.List;

import com.apriori.acs.utils.AcsResources;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.upload.FileResponse;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class Get2DImageByScenarioIterationKeyTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10902")
    @Description("Validate Get 2D Image by Scenario Iteration Key Endpoint")
    public void testGet2DImageByScenarioIterationKey() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
            "Casting.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, testScenarioName);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup
        );

        List<String> infoToGetImage = new ArrayList<>();
        infoToGetImage.add(costOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId().toString());
        infoToGetImage.add(costOutputs.getScenarioIterationKey().getScenarioKey().getTypeName());
        infoToGetImage.add(costOutputs.getScenarioIterationKey().getScenarioKey().getMasterName());
        infoToGetImage.add(costOutputs.getScenarioIterationKey().getScenarioKey().getStateName());
        infoToGetImage.add(costOutputs.getScenarioIterationKey().getIteration().toString());

        acsResources.get2DImageByScenarioIterationKey(infoToGetImage);
    }
}
