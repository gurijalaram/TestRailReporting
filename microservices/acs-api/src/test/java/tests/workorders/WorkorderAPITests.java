package tests.workorders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.request.workorders.assemblyobjects.AssemblyInfo;
import com.apriori.acs.entity.request.workorders.assemblyobjects.AssemblyInfoComponent;
import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.deletescenario.DeleteScenarioOutputs;
import com.apriori.acs.entity.response.workorders.editscenario.EditScenarioOutputs;
import com.apriori.acs.entity.response.workorders.generateallimages.GenerateAllImagesOutputs;
import com.apriori.acs.entity.response.workorders.generateassemblyimages.GenerateAssemblyImagesOutputs;
import com.apriori.acs.entity.response.workorders.generatepartimages.GeneratePartImagesOutputs;
import com.apriori.acs.entity.response.workorders.generatesimpleimagedata.GenerateSimpleImageDataOutputs;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioKey;
import com.apriori.acs.entity.response.workorders.getadmininfo.GetAdminInfoResponse;
import com.apriori.acs.entity.response.workorders.getimageinfo.GetImageInfoResponse;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.GetCadMetadataResponse;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.LoadCadMetadataOutputs;
import com.apriori.acs.entity.response.workorders.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.acs.entity.response.workorders.upload.Assembly;
import com.apriori.acs.entity.response.workorders.upload.AssemblyComponent;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.codec.binary.Base64;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.WorkorderTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class WorkorderAPITests extends TestUtil {

    private final FileUploadResources fileUploadResources = new FileUploadResources();
    private final AcsResources acsResources = new AcsResources();
    private final String assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
    private final String sheetMetalProcessGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
    private final String castingProcessGroup = ProcessGroupEnum.CASTING.getProcessGroup();
    private final String scenarioName = new GenerateStringUtil().generateScenarioName();

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"6933"})
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void testLoadCadMetadataAndGeneratePartImages() {
        FileResponse fileResponse = fileUploadResources.initializePartUpload(
                "bracket_basic.prt",
                sheetMetalProcessGroup
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
        NewPartRequest productionInfoInputs = setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(castingProcessGroup);

        FileUploadOutputs fileUploadOutputs = initializeAndUploadPartFile(
            "Casting.prt",
            castingProcessGroup,
            false
        );

        getAndValidateImageInfo(fileUploadOutputs.getScenarioIterationKey());

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
                productionInfoInputs,
                fileUploadOutputs,
                castingProcessGroup,
                false
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
        NewPartRequest productionInfoInputs = setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(assemblyProcessGroup);

        initializeAndUploadAssemblyFile(
            createAndReturnAssemblyInfo(),
            false
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadResources.getCurrentFileUploadOutputs(),
            assemblyProcessGroup,
            true
        );

        List<AssemblyComponent> assemblyComponents = fileUploadResources.getCurrentAssembly().getSubComponents();
        PublishResultOutputs publishResultOutputs = fileUploadResources.publishAssembly(
            costOutputs,
            Arrays.asList(
                assemblyComponents.get(0).getScenarioIterationKey(),
                assemblyComponents.get(1).getScenarioIterationKey()
            )
        );

        assertThat(publishResultOutputs.getScenarioIterationKey().getScenarioKey().getMasterName(), is(startsWith("PATTERNTHREADHOLES")));
        assertThat(publishResultOutputs.getScenarioIterationKey().getScenarioKey().getTypeName(), is(equalTo("assemblyState")));
        assertThat(publishResultOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId(), is(equalTo(0)));
        assertThat(publishResultOutputs.getScenarioIterationKey().getIteration(), is(not(costOutputs.getScenarioIterationKey().getIteration())));

        List<AssemblyComponent> publishedComponentsList = Arrays.asList(
            publishResultOutputs.getSubComponents().get(0),
            publishResultOutputs.getSubComponents().get(1)
        );

        int i = 0;
        for (AssemblyComponent component : publishedComponentsList) {
            performUploadCostPublishAssemblyComponentAssertions(
                component.getScenarioIterationKey(),
                fileUploadResources.getCurrentAssembly().getSubComponents().get(i).getScenarioIterationKey().getIteration()
            );
            i++;
        }
    }

    @Test
    @Issue("AP-69600")
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = {"7710"})
    @Description("Upload a part, load CAD Metadata, and generate assembly images")
    public void testLoadCadMetadataAndGenerateAssemblyImages() {
        fileUploadResources.checkValidProcessGroup(assemblyProcessGroup);

        FileResponse assemblyFileResponse = initializeAndUploadAssemblyFile(
            createAndReturnAssemblyInfo(),
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
        Object productionInfoInputs = setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileUploadOutputs fileUploadOutputs = initializeAndUploadPartFile(
            "bracket_basic.prt",
            sheetMetalProcessGroup,
            false
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            sheetMetalProcessGroup,
            false
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
        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
                "bracket_basic.prt",
                sheetMetalProcessGroup
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
        fileUploadResources.checkValidProcessGroup(assemblyProcessGroup);

        FileResponse assemblyFileResponse = initializeAndUploadAssemblyFile(
            createAndReturnAssemblyInfo(),
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
        NewPartRequest productionInfoInputs = setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileUploadOutputs fileUploadOutputs = initializeAndUploadPartFile(
            "bracket_basic.prt",
            sheetMetalProcessGroup,
            false
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            sheetMetalProcessGroup,
            false
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
        NewPartRequest productionInfoInputs = setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
                "bracket_basic.prt",
                sheetMetalProcessGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderExposeError(
                fileResponse,
                scenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
                productionInfoInputs,
                fileUploadOutputs,
                sheetMetalProcessGroup,
                false
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
        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileUploadOutputs fileUploadOutputs = initializeAndUploadPartFile(
            "bracket_basic.prt",
            sheetMetalProcessGroup,
            false
        );

        DeleteScenarioOutputs deleteScenarioOutputs = fileUploadResources.createDeleteScenarioWorkorderSuppressError(fileUploadOutputs);

        ScenarioKey scenarioKeyToAssertOn = fileUploadOutputs.getScenarioIterationKey().getScenarioKey();
        assertThat(deleteScenarioOutputs.getScenarioKey().getStateName(), is(equalTo(scenarioKeyToAssertOn.getStateName())));
        assertThat(deleteScenarioOutputs.getScenarioKey().getMasterName(), is(equalTo(scenarioKeyToAssertOn.getMasterName())));
        assertThat(deleteScenarioOutputs.getScenarioKey().getTypeName(), is(equalTo(scenarioKeyToAssertOn.getTypeName())));
        assertThat(deleteScenarioOutputs.getScenarioKey().getWorkspaceId(), is(equalTo(scenarioKeyToAssertOn.getWorkspaceId())));

        String iteration = ((LinkedHashMap<?, ?>) fileUploadResources.getDeleteScenarioWorkorderDetails()).get("iteration").toString();
        ScenarioIterationKey scenarioIterationKey = setupScenarioIterationKey(deleteScenarioOutputs, iteration);

        GenericErrorResponse genericErrorResponse = acsResources.getScenarioInfoByScenarioIterationKeyNegative(scenarioIterationKey);

        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(404)));
        assertThat(genericErrorResponse.getErrorMessage(), is(containsString("No scenario found for key: ")));
    }

    @Test
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = "11990")
    @Description("Edit Scenario - Part - Shallow - Change Scenario Name")
    public void testShallowEditPartScenario() {
        testShallowEditOfScenario(
            "Casting.prt",
            false
        );
    }

    @Test
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = "11991")
    @Description("Edit Scenario - Assembly - Shallow - Change Scenario Name")
    public void testShallowEditAssemblyScenario() {
        testShallowEditOfScenario(
            "PatternThreadHoles.asm",
            true
        );
    }

    @Test
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = "12044")
    @Description("Generate All Images - Part File")
    public void testGenerateAllPartImages() {
        FileUploadOutputs fileUploadOutputs = initializeAndUploadPartFile(
            "3574727.prt",
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(),
            false
        );

        GenerateAllImagesOutputs generateAllImagesOutputs = fileUploadResources.createGenerateAllImagesWorkorderSuppressError(fileUploadOutputs);

        assertThat(Base64.isBase64(
            acsResources.getImageByScenarioIterationKey(generateAllImagesOutputs.getScenarioIterationKey(), true)),
            is(equalTo(true))
        );
        assertThat(Base64.isBase64(
            acsResources.getImageByScenarioIterationKey(generateAllImagesOutputs.getScenarioIterationKey(), false)),
            is(equalTo(true))
        );
    }

    @Test
    @Category(WorkorderTest.class)
    @TestRail(testCaseId = "12047")
    @Description("Generate Simple Image - Part File")
    public void testGenerateSimpleImageData() {
        FileUploadOutputs fileUploadOutputs = initializeAndUploadPartFile(
            "3574727.prt",
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(),
            false
        );

        GenerateSimpleImageDataOutputs generateSimpleImageDataOutputs = fileUploadResources
            .createGenerateSimpleImageDataWorkorderSuppressError(fileUploadOutputs);

        assertThat(Base64.isBase64(
            acsResources.getImageByScenarioIterationKey(generateSimpleImageDataOutputs.getScenarioIterationKey(), false)),
            is(equalTo(true))
        );
    }

    /**
     * Core code for testing shallow edit of a scenario
     *
     * @param fileName - String
     * @param includeSubComponents - boolean flag (inc sub components or not)
     *                             to enable this method to work for both parts and assemblies
     */
    private void testShallowEditOfScenario(String fileName, boolean includeSubComponents) {
        NewPartRequest productionInfoInputs = setupProductionInfoInputs();

        String processGroup = fileName.contains("asm") ? assemblyProcessGroup : castingProcessGroup;
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileUploadOutputs fileUploadOutputs;
        if (includeSubComponents) {
            initializeAndUploadAssemblyFile(
                createAndReturnAssemblyInfo(),
                false
            );
            fileUploadOutputs = fileUploadResources.getCurrentFileUploadOutputs();
        } else {
            fileUploadOutputs = initializeAndUploadPartFile(fileName, processGroup, false);
        }

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            includeSubComponents
        );

        PublishResultOutputs publishResultOutputs;
        if (includeSubComponents) {
            List<AssemblyComponent> assemblyComponents = fileUploadResources.getCurrentAssembly().getSubComponents();
            publishResultOutputs = fileUploadResources.publishAssembly(
                costOutputs,
                Arrays.asList(
                    assemblyComponents.get(0).getScenarioIterationKey(),
                    assemblyComponents.get(1).getScenarioIterationKey()
                )
            );
        } else {
            publishResultOutputs = fileUploadResources.publishPart(costOutputs);
        }

        EditScenarioOutputs editScenarioOutputs = fileUploadResources.createEditScenarioWorkorderSuppressError(publishResultOutputs);

        ScenarioKey scenarioKeyToAssertOn = costOutputs.getScenarioIterationKey().getScenarioKey();
        ScenarioKey postEditScenarioKey = editScenarioOutputs.getScenarioIterationKey().getScenarioKey();

        assertThat(postEditScenarioKey.getStateName(), is(not(equalTo(scenarioKeyToAssertOn.getStateName()))));
        assertThat(postEditScenarioKey.getMasterName(), is(equalTo(scenarioKeyToAssertOn.getMasterName())));
        assertThat(postEditScenarioKey.getTypeName(), is(equalTo(scenarioKeyToAssertOn.getTypeName())));
        assertThat(postEditScenarioKey.getWorkspaceId(), is(not(equalTo(0))));

        assertThat(acsResources
                .getScenarioInfoByScenarioIterationKey(
                    editScenarioOutputs
                        .getScenarioIterationKey()
                ).getScenarioName(),
            is(equalTo("Test"))
        );
    }

    /**
     * Initialize and upload part file
     *
     * @param fileName - String
     * @param processGroup - String
     * @param loadCadMetadata - boolean
     * @return FileUploadOutputs instance
     */
    private FileUploadOutputs initializeAndUploadPartFile(String fileName, String processGroup, boolean loadCadMetadata) {
        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            fileName,
            processGroup
        );

        if (loadCadMetadata) {
            fileUploadResources.loadAssemblyComponentCadMetadataAddToArrayList(fileResponse);
        }

        return fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            scenarioName
        );
    }

    /**
     * Initialize and upload an assembly file
     *
     * @param assemblyToUse - AssemblyInfo instance
     * @param doLoadCadMetadata - boolean flag to determine if load cad metadata call is required or not
     * @return FileResponse instance
     */
    private FileResponse initializeAndUploadAssemblyFile(AssemblyInfo assemblyToUse, boolean doLoadCadMetadata) {
        List<FileUploadOutputs> componentFileUploadOutputs = new ArrayList<>();

        for (AssemblyInfoComponent component : assemblyToUse.getComponents()) {
            componentFileUploadOutputs.add(
                initializeAndUploadPartFile(component.getComponentName(), component.getProcessGroup(), doLoadCadMetadata)
            );
        }

        FileResponse assemblyFileResponse = fileUploadResources.initializePartUpload(
            assemblyToUse.getAssemblyName(),
            assemblyToUse.getProcessGroup()
        );

        Assembly assemblyProper = createAssembly(componentFileUploadOutputs, assemblyFileResponse);
        fileUploadResources.setCurrentAssembly(assemblyProper);

        fileUploadResources.setCurrentFileUploadOutputs(
            fileUploadResources.createFileUploadWorkorderAssemblySuppressError(
                assemblyProper
            )
        );

        return assemblyFileResponse;
    }

    /**
     * Creates assembly based on passed in parameters
     *
     * @param componentFileUploadOutputs - List of FileUploadOutputs instances
     * @param assemblyFileResponse - FileResponse instance
     * @return Assembly object instance
     */
    private Assembly createAssembly(List<FileUploadOutputs> componentFileUploadOutputs, FileResponse assemblyFileResponse) {
        List<AssemblyComponent> assemblyComponents = new ArrayList<>();
        for (FileUploadOutputs componentFileUploadOutput : componentFileUploadOutputs) {
            assemblyComponents.add(AssemblyComponent.builder()
                .ignored(false)
                .scenarioIterationKey(componentFileUploadOutput.getScenarioIterationKey())
                .build()
            );
        }

        return Assembly.builder()
            .scenarioName(scenarioName)
            .fileKey(assemblyFileResponse.getIdentity())
            .fileName(assemblyFileResponse.getFilename())
            .keepFreeBodies(false)
            .freeBodiesPreserveCad(false)
            .freeBodiesIgnoreMissingComponent(true)
            .subComponents(assemblyComponents)
            .build();
    }

    /**
     * Creates and returns assembly info instance
     * Assembly info is info for initial file upload, whilst Assembly object is used for file upload workorder creation
     *
     * @return AssemblyInfo instance
     */
    private AssemblyInfo createAndReturnAssemblyInfo() {
        ArrayList<AssemblyInfoComponent> assemblyComponents = new ArrayList<>();

        assemblyComponents.add(
            AssemblyInfoComponent.builder()
                .componentName("3574727.prt")
                .scenarioName(scenarioName)
                .processGroup(assemblyProcessGroup)
                .build()
        );

        assemblyComponents.add(
            AssemblyInfoComponent.builder()
                .componentName("3574875.prt")
                .scenarioName(scenarioName)
                .processGroup(assemblyProcessGroup)
                .build()
        );

        return AssemblyInfo.builder()
            .assemblyName("PatternThreadHoles.asm")
            .scenarioName(scenarioName)
            .processGroup(assemblyProcessGroup)
            .components(assemblyComponents)
            .build();
    }

    /**
     * Loads cad metadata and generate assembly images
     *
     * @param assemblyFileResponse - FileResponse instance
     * @return GenerateAssemblyImagesOutputs instance
     */
    private GenerateAssemblyImagesOutputs loadCadMetadataAndGenerateAssemblyImages(FileResponse assemblyFileResponse) {
        LoadCadMetadataOutputs assemblyMetadataOutput = fileUploadResources.loadCadMetadataSuppressError(assemblyFileResponse);
        return fileUploadResources.generateAssemblyImages(
            assemblyFileResponse,
            fileUploadResources.getComponentMetadataOutputs(),
            assemblyMetadataOutput
        );
    }

    private void getAndValidateImageInfo(ScenarioIterationKey scenarioIterationKey) {
        GetImageInfoResponse imageInfoResponse = fileUploadResources.getImageInfo(scenarioIterationKey);

        assertThat(imageInfoResponse.getDesktopImageAvailable(), is(equalTo("true")));
        assertThat(imageInfoResponse.getThumbnailAvailable(), is(equalTo("true")));
        assertThat(imageInfoResponse.getPartNestingDiagramAvailable(), is(equalTo("false")));
        assertThat(imageInfoResponse.getWebImageAvailable(), is(equalTo("true")));
        assertThat(imageInfoResponse.getWebImageRequiresRegen(), is(equalTo("false")));
    }

    /**
     * Sets up scenario iteration key
     *
     * @param deleteScenarioOutputs - DeleteScenarioOutputs instance
     * @param iteration - String
     * @return ScenarioIterationKey instance
     */
    private ScenarioIterationKey setupScenarioIterationKey(DeleteScenarioOutputs deleteScenarioOutputs, String iteration) {
        ScenarioIterationKey scenarioIterationKey = new ScenarioIterationKey();
        ScenarioKey scenarioKey = new ScenarioKey();

        scenarioKey.setWorkspaceId(deleteScenarioOutputs.getScenarioKey().getWorkspaceId());
        scenarioKey.setTypeName(deleteScenarioOutputs.getScenarioKey().getTypeName());
        scenarioKey.setMasterName(deleteScenarioOutputs.getScenarioKey().getMasterName());
        scenarioKey.setStateName(deleteScenarioOutputs.getScenarioKey().getStateName());

        scenarioIterationKey.setScenarioKey(scenarioKey);
        scenarioIterationKey.setIteration(Integer.parseInt(iteration));

        return scenarioIterationKey;
    }

    /**
     * Performs upload cost and publish assembly component assertions
     *
     * @param scenarioIterationKey - ScenarioIterationKey instance
     * @param notExpectedIteration - Integer
     */
    private void performUploadCostPublishAssemblyComponentAssertions(ScenarioIterationKey scenarioIterationKey, Integer notExpectedIteration) {
        assertThat(scenarioIterationKey.getScenarioKey().getMasterName(), is(startsWith("3574")));
        assertThat(scenarioIterationKey.getScenarioKey().getTypeName(), is(equalTo("partState")));
        assertThat(scenarioIterationKey.getScenarioKey().getWorkspaceId(), is(equalTo(0)));
        assertThat(scenarioIterationKey.getIteration(), is(not(notExpectedIteration)));
    }

    /**
     * Sets up production info inputs to allow for costing of a part or assembly
     *
     * @return NewPartRequest instance
     */
    public NewPartRequest setupProductionInfoInputs() {
        return JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "CreatePartData.json"
            ).getPath(), NewPartRequest.class
        );
    }
}
