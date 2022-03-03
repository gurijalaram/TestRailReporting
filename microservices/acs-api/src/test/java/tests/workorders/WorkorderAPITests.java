package tests.workorders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.request.workorders.assemblyobjects.Assembly;
import com.apriori.acs.entity.request.workorders.assemblyobjects.AssemblyComponent;
import com.apriori.acs.entity.response.workorders.getadmininfo.GetAdminInfoResponse;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.GetCadMetadataResponse;
import com.apriori.acs.entity.response.workorders.getimageinfo.GetImageInfoResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.acs.entity.response.workorders.deletescenario.DeleteScenarioOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileResponse;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.entity.response.workorders.generateassemblyimages.GenerateAssemblyImagesOutputs;
import com.apriori.acs.entity.response.workorders.generatepartimages.GeneratePartImagesOutputs;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.LoadCadMetadataOutputs;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioKey;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.WorkorderTest;

import java.util.ArrayList;

public class WorkorderAPITests {

    private final FileUploadResources fileUploadResources = new FileUploadResources();
    private final String scenarioName = new GenerateStringUtil().generateScenarioName();

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"6933"})
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void testLoadCadMetadataAndGeneratePartImages() {
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "bracket_basic.prt",
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );

        LoadCadMetadataOutputs loadCadMetadataOutputs = fileUploadResources.loadCadMetadataSuppressError(fileResponse);

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
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"7697"})
    @Description("Get image after each iteration - Upload, Cost, Publish")
    public void testUploadCostPublishGetImage() {
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileUploadOutputs fileUploadOutputs = initialiseAndUploadFile("Casting.prt", processGroup);

        getAndValidateImageInfo(fileUploadOutputs.getScenarioIterationKey());

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup
        );

        getAndValidateImageInfo(costOutputs.getScenarioIterationKey());

        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);

        getAndValidateImageInfo(publishResultOutputs.getScenarioIterationKey());
    }

    @Test
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = "11974")
    @Description("Upload, Cost, and Publish an Assembly")
    public void testUploadCostAndPublishAssembly() {
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        Assembly assemblyToUse = createAndReturnAssembly(scenarioName, processGroup);

        initialiseAndUploadAssembly(assemblyToUse, false);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
            productionInfoInputs,
            fileUploadResources.getCurrentFileUploadOutputs(),
            processGroup
        );

        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);

        assertThat(publishResultOutputs.getScenarioIterationKey().getScenarioKey().getMasterName(), is(equalTo("PATTERNTHREADHOLES")));
        assertThat(publishResultOutputs.getScenarioIterationKey().getScenarioKey().getTypeName(), is(equalTo("assemblyState")));
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"7710"})
    @Description("Upload a part, load CAD Metadata, and generate assembly images")
    public void testLoadCadMetadataAndGenerateAssemblyImages() {
        String processGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse assemblyFileResponse = initialiseAndUploadAssembly(
            createAndReturnAssembly(scenarioName, processGroup),
            true
        );

        GenerateAssemblyImagesOutputs generateAssemblyImagesOutputs = loadCadMetadataAndGenerateAssemblyImages(assemblyFileResponse);

        ArrayList<String> images = generateAssemblyImagesOutputs.getGeneratedWebImages();
        images.add(generateAssemblyImagesOutputs.getDesktopImageIdentity());
        images.add(generateAssemblyImagesOutputs.getThumbnailImageIdentity());

        for (String image : images) {
            fileUploadResources.imageValidation(image);
        }
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"8681"})
    @Description("Upload a part, cost it and publish it with comment and description fields")
    public void testPublishCommentAndDescriptionFields() {
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileUploadOutputs fileUploadOutputs = initialiseAndUploadFile("bracket_basic.prt", processGroup);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup
        );

        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);
        GetAdminInfoResponse getAdminInfoResponse = fileUploadResources
                .getAdminInfo(publishResultOutputs.getScenarioIterationKey().getScenarioKey());

        assertThat(getAdminInfoResponse.getLastSavedTime(), is(notNullValue()));
        assertThat(getAdminInfoResponse.getComments(), is(equalTo("Comments go here...")));
        assertThat(getAdminInfoResponse.getDescription(), is(equalTo("Description goes here...")));
        assertThat(getAdminInfoResponse.getLocked(), is(equalTo("false")));
        assertThat(getAdminInfoResponse.getActive(), is(equalTo("true")));
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"8682"})
    @Description("Upload a part, load cad metadata with part name and extension and get cad metadata to verify")
    public void testFileNameAndExtensionInputAndOutput() {
        JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "bracket_basic.prt",
                processGroup
        );

        LoadCadMetadataOutputs loadCadMetadataOutputs = fileUploadResources.loadCadMetadataExposeError(fileResponse);

        GetCadMetadataResponse getCadMetadataResponse = fileUploadResources
                .getCadMetadata(loadCadMetadataOutputs.getCadMetadataIdentity());

        assertThat(getCadMetadataResponse.getFileMetadataIdentity(), is(equalTo(fileResponse.getIdentity())));
        assertThat(getCadMetadataResponse.getCadType(), is(equalTo("PART")));
        assertThat(getCadMetadataResponse.getKeepFreeBodies(), is(equalTo("false")));
        assertThat(getCadMetadataResponse.getFreeBodiesPreserveCad(), is(equalTo("false")));
        assertThat(getCadMetadataResponse.getFreeBodiesIgnoreMissingComponents(), is(equalTo("true")));
        assertThat(getCadMetadataResponse.getLengthUnit(), is(equalTo("MM")));
        assertThat(getCadMetadataResponse.getVendor(), is(equalTo("PROE")));
        assertThat(getCadMetadataResponse.getPmi().size(), is(equalTo(3)));
        assertThat(getCadMetadataResponse.getCreatedAt(), is(notNullValue()));
        assertThat(getCadMetadataResponse.getCreatedBy(), is(notNullValue()));
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"8689"})
    @Description("Upload a part, load cad metadata, then get cad metadata to verify that all components are returned")
    public void testLoadCadMetadataReturnsAllComponents() {
        String processGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse assemblyFileResponse = initialiseAndUploadAssembly(
            createAndReturnAssembly(scenarioName, processGroup),
            true
        );

        GenerateAssemblyImagesOutputs generateAssemblyImagesOutputs = loadCadMetadataAndGenerateAssemblyImages(assemblyFileResponse);

        GetCadMetadataResponse getCadMetadataResponse = fileUploadResources.getCadMetadata(
                generateAssemblyImagesOutputs.getCadMetadataIdentity());

        assertThat(getCadMetadataResponse.getPmi().size(), is(equalTo(39)));
        assertThat(getCadMetadataResponse.getManifest(), is(notNullValue()));
        assertThat(getCadMetadataResponse.getManifest().size(), is(equalTo(2)));

        for (int i = 0; i < getCadMetadataResponse.getManifest().size(); i++) {
            assertThat(getCadMetadataResponse.getManifest().get(i).getOccurrences(), is(equalTo("1")));
        }
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"8693"})
    @Description("Upload a part, cost it, then get image info to ensure fields are correctly returned")
    public void testGetImageInfoSuppress500Version() {
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileUploadOutputs fileUploadOutputs = initialiseAndUploadFile("bracket_basic.prt", processGroup);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup
        );

        GetImageInfoResponse getImageInfoResponse = fileUploadResources
                .getImageInfo(costOutputs.getScenarioIterationKey());

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(getImageInfoResponse.getDesktopImageAvailable()).isEqualTo("true");
        softAssert.assertThat(getImageInfoResponse.getThumbnailAvailable()).isEqualTo("true");
        softAssert.assertThat(getImageInfoResponse.getPartNestingDiagramAvailable()).isEqualTo("false");
        softAssert.assertThat(getImageInfoResponse.getWebImageAvailable()).isEqualTo("true");
        softAssert.assertThat(getImageInfoResponse.getWebImageRequiresRegen()).isEqualTo("false");

        softAssert.assertAll();
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"8693"})
    @Description("Upload a part, cost it, then get image info to ensure fields are correctly returned")
    public void testGetImageInfoExpose500ErrorVersion() {
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                "bracket_basic.prt",
                processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderExposeError(
                fileResponse,
                scenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup
        );

        GetImageInfoResponse getImageInfoResponse = fileUploadResources
                .getImageInfo(costOutputs.getScenarioIterationKey());

        SoftAssertions softAssert = new SoftAssertions();
        softAssert.assertThat(getImageInfoResponse.getDesktopImageAvailable()).isEqualTo("true");
        softAssert.assertThat(getImageInfoResponse.getThumbnailAvailable()).isEqualTo("true");
        softAssert.assertThat(getImageInfoResponse.getPartNestingDiagramAvailable()).isEqualTo("false");
        softAssert.assertThat(getImageInfoResponse.getWebImageAvailable()).isEqualTo("true");
        softAssert.assertThat(getImageInfoResponse.getWebImageRequiresRegen()).isEqualTo("false");

        softAssert.assertAll();
    }

    @Test
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = "11981")
    @Description("Delete Scenario")
    public void testDeleteScenario() {
        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileUploadOutputs fileUploadOutputs = initialiseAndUploadFile("bracket_basic.prt", processGroup);

        DeleteScenarioOutputs deleteScenarioOutputs = fileUploadResources.createDeleteScenarioWorkorderSuppressError(fileUploadOutputs);

        ScenarioKey scenarioKeyToAssertOn = fileUploadOutputs.getScenarioIterationKey().getScenarioKey();
        assertThat(deleteScenarioOutputs.getScenarioKey().getStateName(), is(equalTo(scenarioKeyToAssertOn.getStateName())));
        assertThat(deleteScenarioOutputs.getScenarioKey().getMasterName(), is(equalTo(scenarioKeyToAssertOn.getMasterName())));
        assertThat(deleteScenarioOutputs.getScenarioKey().getTypeName(), is(equalTo(scenarioKeyToAssertOn.getTypeName())));
        assertThat(deleteScenarioOutputs.getScenarioKey().getWorkspaceId(), is(equalTo(scenarioKeyToAssertOn.getWorkspaceId())));
    }

    private FileUploadOutputs initialiseAndUploadFile(String fileName, String processGroup) {
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
            fileName,
            processGroup
        );

        return fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            scenarioName
        );
    }

    private FileResponse initialiseAndUploadAssembly(Assembly assemblyToUse, boolean doLoadCadMetadata) {
        for (AssemblyComponent component : assemblyToUse.getComponents()) {
            FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                    component.getComponentName(),
                    component.getProcessGroup()
            );

            fileUploadResources.createFileUploadWorkorderSuppressError(
                    fileResponse,
                    component.getScenarioName()
            );

            if (doLoadCadMetadata) {
                fileUploadResources.loadAssemblyComponentCadMetadataAddToArrayList(fileResponse);
            }
        }

        FileResponse assemblyFileResponse = fileUploadResources.initialisePartUpload(
                assemblyToUse.getAssemblyName(),
                assemblyToUse.getProcessGroup()
        );

        fileUploadResources.setCurrentFileUploadOutputs(
            fileUploadResources.createFileUploadWorkorderSuppressError(
                assemblyFileResponse,
                assemblyToUse.getScenarioName())
        );

        return assemblyFileResponse;
    }

    private GenerateAssemblyImagesOutputs loadCadMetadataAndGenerateAssemblyImages(FileResponse assemblyFileResponse) {
        LoadCadMetadataOutputs assemblyMetadataOutput = fileUploadResources.loadCadMetadataSuppressError(assemblyFileResponse);
        return fileUploadResources.generateAssemblyImages(
            assemblyFileResponse,
            fileUploadResources.getComponentMetadataOutputs(),
            assemblyMetadataOutput
        );
    }

    private Assembly createAndReturnAssembly(String testScenarioName, String processGroup) {
        ArrayList<AssemblyComponent> assemblyComponents = new ArrayList<>();

        assemblyComponents.add(
            AssemblyComponent.builder()
                .componentName("3574727.prt")
                .scenarioName(testScenarioName)
                .processGroup(processGroup)
                .build()
        );

        assemblyComponents.add(
            AssemblyComponent.builder()
                .componentName("3574875.prt")
                .scenarioName(testScenarioName)
                .processGroup(processGroup)
                .build()
        );

        return Assembly.builder()
            .assemblyName("PatternThreadHoles.asm")
            .scenarioName(testScenarioName)
            .processGroup(processGroup)
            .components(assemblyComponents)
            .build();
    }

    private void getAndValidateImageInfo(ScenarioIterationKey scenarioIterationKey) {
        GetImageInfoResponse imageInfoResponse = fileUploadResources.getImageInfo(scenarioIterationKey);

        assertThat(imageInfoResponse.getDesktopImageAvailable(), is(equalTo("true")));
        assertThat(imageInfoResponse.getThumbnailAvailable(), is(equalTo("true")));
        assertThat(imageInfoResponse.getPartNestingDiagramAvailable(), is(equalTo("false")));
        assertThat(imageInfoResponse.getWebImageAvailable(), is(equalTo("true")));
        assertThat(imageInfoResponse.getWebImageRequiresRegen(), is(equalTo("false")));
    }
}
