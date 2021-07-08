package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.entity.request.assemblyobjects.Assembly;
import com.apriori.entity.request.assemblyobjects.AssemblyComponent;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.entity.response.upload.FileResponse;
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
import io.qameta.allure.Issue;
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
    @Issue("AP-69600")
    @Category(CidAPITest.class)
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

        String webImage = fileUploadResources
                .getImageById(generatePartImagesOutputs.getWebImageIdentity()).toString();
        String desktopImage = fileUploadResources
                .getImageById(generatePartImagesOutputs.getDesktopImageIdentity()).toString();
        String thumbnailImage = fileUploadResources
                .getImageById(generatePartImagesOutputs.getThumbnailImageIdentity()).toString();

        fileUploadResources.imageValidation(webImage);
        fileUploadResources.imageValidation(desktopImage);
        fileUploadResources.imageValidation(thumbnailImage);
    }

    @Test
    @Issue("AP-69600")
    @Category(CidAPITest.class)
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

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup
        );

        getAndValidateImage(costOutputs.getScenarioIterationKey());

        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);

        getAndValidateImage(publishResultOutputs.getScenarioIterationKey());
    }

    @Test
    @Issue("AP-69600")
    @Category(CidAPITest.class)
    @TestRail(testCaseId = {"7710"})
    @Description("Upload a part, load CAD Metadata, and generate assembly images")
    public void testLoadCadMetadataAndGenerateAssemblyImages() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String processGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
        ArrayList<AssemblyComponent> assemblyComponents = new ArrayList<>();

        assemblyComponents.add(
                AssemblyComponent.builder()
                .componentName("3574727.prt")
                .scenarioName(scenarioName)
                .processGroup(processGroup)
                .build()
        );
        assemblyComponents.add(
                AssemblyComponent.builder()
                .componentName("3574875.prt")
                .scenarioName(scenarioName)
                .processGroup(processGroup)
                .build()
        );

        GenerateAssemblyImagesOutputs generateAssemblyImagesOutputs = prepareForGenerateAssemblyImages(
                Assembly.builder()
                .assemblyName("PatternThreadHoles.asm")
                .scenarioName(scenarioName)
                .processGroup(processGroup)
                .components(assemblyComponents)
                .build());

        ArrayList<String> images = generateAssemblyImagesOutputs.getGeneratedWebImages();
        images.add(generateAssemblyImagesOutputs.getDesktopImageIdentity());
        images.add(generateAssemblyImagesOutputs.getThumbnailImageIdentity());

        for (String image : images) {
            fileUploadResources.imageValidation(image);
        }
    }

    @Test
    @Issue("AP-69600")
    @Category(CidAPITest.class)
    @TestRail(testCaseId = {"7710"})
    @Description("Upload a part, cost it and publish it with comment and description fields")
    public void testPublishCommentAndDescriptionFields() {
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        // upload cad file
        FileUploadResources fileUploadResources = new FileUploadResources();
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "bracket_basic.prt",
                processGroup
        );
        FileUploadOutputs fileUploadOutputs = fileUploadResources.uploadPart(fileResponse, testScenarioName);

        // cost cad file
        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup
        );

        // publish cad file with comment and description fields
        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);

        assertThat(publishResultOutputs.getComments(), is(notNullValue()));
        assertThat(publishResultOutputs.getDescription(), is(notNullValue()));
    }

    private GenerateAssemblyImagesOutputs prepareForGenerateAssemblyImages(Assembly assemblyToUse) {
        FileUploadResources fileUploadResources = new FileUploadResources();
        ArrayList<LoadCadMetadataOutputs> componentMetadataOutputs = new ArrayList<>();

        for (AssemblyComponent component : assemblyToUse.getComponents()) {
            FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                    component.getComponentName(),
                    component.getProcessGroup()
            );

            fileUploadResources.uploadPart(
                    fileResponse,
                    component.getScenarioName()
            );

            componentMetadataOutputs.add(fileUploadResources.loadCadMetadata(fileResponse));
        }

        FileResponse assemblyFileResponse = fileUploadResources.initialisePartUpload(
                assemblyToUse.getAssemblyName(),
                assemblyToUse.getProcessGroup()
        );

        fileUploadResources.uploadPart(
                assemblyFileResponse,
                assemblyToUse.getScenarioName()
        );

        LoadCadMetadataOutputs assemblyMetadataOutput = fileUploadResources.loadCadMetadata(assemblyFileResponse);

        return fileUploadResources.generateAssemblyImages(
                assemblyFileResponse,
                componentMetadataOutputs,
                assemblyMetadataOutput
        );
    }

    private void getAndValidateImage(ScenarioIterationKey scenarioIterationKey) {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String webImageResponse = fileUploadResources
                .getImageByScenarioIterationKey(
                scenarioIterationKey.getScenarioKey(), "web").toString();
        String desktopImageResponse = fileUploadResources
                .getImageByScenarioIterationKey(
                scenarioIterationKey.getScenarioKey(), "desktop").toString();

        fileUploadResources.imageValidation(webImageResponse);
        fileUploadResources.imageValidation(desktopImageResponse);
    }
}
