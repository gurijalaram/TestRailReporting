package com.apriori;

import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.GenerateAssemblyImagesOutputs;
import com.apriori.entity.response.upload.GeneratePartImagesOutputs;
import com.apriori.entity.response.upload.LoadCadMetadataOutputs;
import com.apriori.entity.response.upload.ScenarioIterationKey;
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

import java.util.ArrayList;

public class WorkorderAPITests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    //@Category(CidAPITest.class)
    @TestRail(testCaseId = {"6933"})
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void testLoadCadMetadataAndGeneratePartImages() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "bracket_basic.prt",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

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
    //@Category(CidAPITest.class)
    @TestRail(testCaseId = {"7697"})
    @Description("Get image after each iteration - Upload, Cost, Publish")
    public void testUploadCostPublishGetImage() {
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
    }

    @Test
    @Category(CidAPITest.class)
    @TestRail(testCaseId = {"7710"})
    @Description("Upload a part, load CAD Metadata, and generate assembly images")
    public void testLoadCadMetadataAndGenerateAssemblyImages() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        // upload component one - 3574727
        FileResponse fileResponseComponentOne = fileUploadResources.initialisePartUpload(
                "3574727.prt",
                ProcessGroupEnum.ASSEMBLY.getProcessGroup()
        );
        FileUploadOutputs fileUploadOutputsComponentOne = fileUploadResources.uploadPart(
                fileResponseComponentOne,
                scenarioName
        );

        // upload component two - 3574875
        FileResponse fileResponseComponentTwo = fileUploadResources.initialisePartUpload(
                "3574875.prt",
                ProcessGroupEnum.ASSEMBLY.getProcessGroup()
        );
        FileUploadOutputs fileUploadOutputsComponentTwo = fileUploadResources.uploadPart(
                fileResponseComponentTwo,
                scenarioName
        );

        // upload assembly
        FileResponse fileResponseAssembly = fileUploadResources.initialisePartUpload(
                "PatternThreadHoles.asm",
                ProcessGroupEnum.ASSEMBLY.getProcessGroup()
        );
        FileUploadOutputs fileUploadOutputsAssembly = fileUploadResources.uploadPart(
                fileResponseAssembly,
                scenarioName
        );

        // load cad metadata for component one - pin
        LoadCadMetadataOutputs loadCadMetadataOutputsComponentOne = fileUploadResources.loadCadMetadata(
                fileResponseComponentOne);

        // load cad metadata for component two - small ring
        LoadCadMetadataOutputs loadCadMetadataOutputsComponentTwo = fileUploadResources.loadCadMetadata(
                fileResponseComponentTwo);

        // load cad metadata for assembly
        LoadCadMetadataOutputs loadCadMetadataOutputsAssembly = fileUploadResources.loadCadMetadata(
                fileResponseAssembly);

        // generate assembly images call
        GenerateAssemblyImagesOutputs generateAssemblyImagesOutputs = fileUploadResources.generateAssemblyImages(
                fileResponseAssembly,
                loadCadMetadataOutputsAssembly,
                loadCadMetadataOutputsComponentOne,
                loadCadMetadataOutputsComponentTwo
        );

        ArrayList<String> images = generateAssemblyImagesOutputs.getGeneratedWebImages();

        // assertions
        for (String image : images) {
            fileUploadResources.imageValidation(image);
        }
        fileUploadResources.imageValidation(generateAssemblyImagesOutputs.getDesktopImageIdentity());
        fileUploadResources.imageValidation(generateAssemblyImagesOutputs.getThumbnailImageIdentity());
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
