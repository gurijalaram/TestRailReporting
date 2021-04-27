package com.apriori;

import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.cost.costworkorderstatus.ScenarioIterationKey;
import com.apriori.entity.response.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.GeneratePartImagesOutputs;
import com.apriori.entity.response.upload.LoadCadMetadataOutputs;
import com.apriori.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.CidAPITest;

public class WorkorderAPITests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    @Category(CidAPITest.class)
    @TestRail(testCaseId = {"6933"})
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void loadCadMetadataAndGeneratePartImages() {
        //String testScenarioName = new GenerateStringUtil().generateScenarioName();
        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "bracket_basic.prt",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
        //fileUploadResources.uploadPart(fileResponse, testScenarioName);

        LoadCadMetadataOutputs loadCadMetadataOutputs = fileUploadResources.loadCadMetadata(fileResponse);

        GeneratePartImagesOutputs generatePartImagesOutputs = fileUploadResources.generatePartImages(
                fileResponse,
                loadCadMetadataOutputs
        );

        String webImage = fileUploadResources.getImageById(
                generatePartImagesOutputs.getWebImageIdentity()).toString();
        String desktopImage = fileUploadResources.getImageById(
                generatePartImagesOutputs.getDesktopImageIdentity()).toString();
        String thumbnailImage = fileUploadResources.getImageById(
                generatePartImagesOutputs.getThumbnailImageIdentity()).toString();

        fileUploadResources.imageValidation(webImage);
        fileUploadResources.imageValidation(desktopImage);
        fileUploadResources.imageValidation(thumbnailImage);
    }

    @Test
    @Category(CidAPITest.class)
    @TestRail(testCaseId = {"7697"})
    @Description("Get image after each iteration - Upload, Cost, Publish")
    public void uploadCostPublishGetImage() {
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        FileUploadResources fileUploadResources = new FileUploadResources();
        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "Casting.prt",
                processGroup
        );
        FileUploadOutputs fileUploadOutputs = fileUploadResources.uploadPart(fileResponse, testScenarioName);

        getAndValidateImage(fileUploadOutputs.getScenarioIterationKey());

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(productionInfoInputs, fileUploadOutputs, processGroup);

        getAndValidateImage(costOutputs.getScenarioIterationKey());

        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);

        getAndValidateImage(publishResultOutputs.getScenarioIterationKey());

        /*new FileUploadResources().uploadCostPublishGetImageByScenarioIterationKey(
                productionInfoInputs,
                "Casting.prt",
                testScenarioName,
                ProcessGroupEnum.CASTING.getProcessGroup()
        );*/
    }

    private void getAndValidateImage(ScenarioIterationKey scenarioIterationKey) {
        FileUploadResources fileUploadResources = new FileUploadResources();
        String webImageResponse = fileUploadResources.getImageByScenarioIterationKey(
                scenarioIterationKey.getScenarioKey(), "web").toString();
        String desktopImageResponse = fileUploadResources.getImageByScenarioIterationKey(
                scenarioIterationKey.getScenarioKey(), "desktop").toString();
        fileUploadResources.imageValidation(webImageResponse);
        fileUploadResources.imageValidation(desktopImageResponse);
    }
}
