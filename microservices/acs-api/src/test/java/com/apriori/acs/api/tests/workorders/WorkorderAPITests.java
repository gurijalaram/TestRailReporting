package com.apriori.acs.api.tests.workorders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.acs.api.models.request.workorders.NewPartRequest;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.workorders.allimages.AllImagesOutputs;
import com.apriori.acs.api.models.response.workorders.assemblyimages.AssemblyImagesOutputs;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.models.response.workorders.deletescenario.DeleteScenarioOutputs;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioKey;
import com.apriori.acs.api.models.response.workorders.getadmininfo.AdminInfoResponse;
import com.apriori.acs.api.models.response.workorders.getimageinfo.ImageInfoResponse;
import com.apriori.acs.api.models.response.workorders.loadcadmetadata.CadMetadataResponse;
import com.apriori.acs.api.models.response.workorders.loadcadmetadata.LoadCadMetadataOutputs;
import com.apriori.acs.api.models.response.workorders.partimages.PartImagesOutputs;
import com.apriori.acs.api.models.response.workorders.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.acs.api.models.response.workorders.simpleimagedata.SimpleImageDataOutputs;
import com.apriori.acs.api.models.response.workorders.upload.AssemblyComponent;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class WorkorderAPITests extends TestUtil {
    private FileUploadResources fileUploadResources;
    private WorkorderApiUtils workorderApiUtils;
    private AcsResources acsResources;
    private final String assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
    private final String sheetMetalProcessGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
    private final String castingProcessGroup = ProcessGroupEnum.CASTING.getProcessGroup();

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        workorderApiUtils = new WorkorderApiUtils(requestEntityUtil);
        fileUploadResources = new FileUploadResources(requestEntityUtil);
    }

    @Test
    @Issue("AP-69600")
    @TestRail(id = {6933})
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void testLoadCadMetadataAndGeneratePartImages() {
        FileResponse fileResponse = fileUploadResources.initializePartUpload(
                "bracket_basic.prt",
                sheetMetalProcessGroup
        );

        LoadCadMetadataOutputs loadCadMetadataOutputs = fileUploadResources.loadCadMetadataSuppressError(fileResponse);

        PartImagesOutputs generatePartImagesOutputs = fileUploadResources.generatePartImages(
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
    @TestRail(id = {7697})
    @Description("Get image after each iteration - Upload, Cost, Publish")
    public void testUploadCostPublishGetImage() {
        NewPartRequest productionInfoInputs = workorderApiUtils.setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(castingProcessGroup);

        FileUploadOutputs fileUploadOutputs = workorderApiUtils.initializeAndUploadPartFile(
            "Casting.prt",
            castingProcessGroup,
            false
        );

        workorderApiUtils.getAndValidateImageInfo(fileUploadOutputs.getScenarioIterationKey());

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
                productionInfoInputs,
                fileUploadOutputs,
                castingProcessGroup,
                false
        );

        workorderApiUtils.getAndValidateImageInfo(costOutputs.getScenarioIterationKey());

        PublishResultOutputs publishResultOutputs = fileUploadResources.publishPart(costOutputs);

        workorderApiUtils.getAndValidateImageInfo(publishResultOutputs.getScenarioIterationKey());
    }

    @Test
    @TestRail(id = 11974)
    @Description("Upload, Cost, and Publish an Assembly")
    public void testUploadCostAndPublishAssembly() {
        NewPartRequest productionInfoInputs = workorderApiUtils.setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(assemblyProcessGroup);

        workorderApiUtils.initializeAndUploadAssemblyFile(
            workorderApiUtils.createAndReturnAssemblyInfo(),
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
            workorderApiUtils.performUploadCostPublishAssemblyComponentAssertions(
                component.getScenarioIterationKey(),
                fileUploadResources.getCurrentAssembly().getSubComponents().get(i).getScenarioIterationKey().getIteration()
            );
            i++;
        }
    }

    @Test
    @Issue("AP-69600")
    @TestRail(id = {7710})
    @Description("Upload a part, load CAD Metadata, and generate assembly images")
    public void testLoadCadMetadataAndGenerateAssemblyImages() {
        fileUploadResources.checkValidProcessGroup(assemblyProcessGroup);

        FileResponse assemblyFileResponse = workorderApiUtils.initializeAndUploadAssemblyFile(
            workorderApiUtils.createAndReturnAssemblyInfo(),
            true
        );

        AssemblyImagesOutputs generateAssemblyImagesOutputs = workorderApiUtils.loadCadMetadataAndGenerateAssemblyImages(assemblyFileResponse);

        ArrayList<String> images = generateAssemblyImagesOutputs.getGeneratedWebImages();
        images.add(generateAssemblyImagesOutputs.getDesktopImageIdentity());
        images.add(generateAssemblyImagesOutputs.getThumbnailImageIdentity());

        for (String image : images) {
            fileUploadResources.imageValidation(image);
        }
    }

    @Test
    @Issue("AP-69600")
    @TestRail(id = {8681})
    @Description("Upload a part, cost it and publish it with comment and description fields")
    public void testPublishCommentAndDescriptionFields() {
        Object productionInfoInputs = workorderApiUtils.setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileUploadOutputs fileUploadOutputs = workorderApiUtils.initializeAndUploadPartFile(
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
        AdminInfoResponse getAdminInfoResponse = fileUploadResources
                .getAdminInfo(publishResultOutputs.getScenarioIterationKey().getScenarioKey());

        assertThat(getAdminInfoResponse.getLastSavedTime(), is(notNullValue()));
        assertThat(getAdminInfoResponse.getComments(), is(equalTo("Comments go here...")));
        assertThat(getAdminInfoResponse.getDescription(), is(equalTo("Description goes here...")));
        assertThat(getAdminInfoResponse.getLocked(), is(equalTo("false")));
        assertThat(getAdminInfoResponse.getActive(), is(equalTo("true")));
    }

    @Test
    @Issue("AP-69600")
    @TestRail(id = {8682})
    @Description("Upload a part, load cad metadata with part name and extension and get cad metadata to verify")
    public void testFileNameAndExtensionInputAndOutput() {
        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
                "bracket_basic.prt",
                sheetMetalProcessGroup
        );

        LoadCadMetadataOutputs loadCadMetadataOutputs = fileUploadResources.loadCadMetadataExposeError(fileResponse);

        CadMetadataResponse getCadMetadataResponse = fileUploadResources
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
    @TestRail(id = {8689})
    @Description("Upload a part, load cad metadata, then get cad metadata to verify that all components are returned")
    public void testLoadCadMetadataReturnsAllComponents() {
        fileUploadResources.checkValidProcessGroup(assemblyProcessGroup);

        FileResponse assemblyFileResponse = workorderApiUtils.initializeAndUploadAssemblyFile(
            workorderApiUtils.createAndReturnAssemblyInfo(),
            true
        );

        AssemblyImagesOutputs generateAssemblyImagesOutputs = workorderApiUtils.loadCadMetadataAndGenerateAssemblyImages(assemblyFileResponse);

        CadMetadataResponse getCadMetadataResponse = fileUploadResources.getCadMetadata(
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
    @TestRail(id = {8693})
    @Description("Upload a part, cost it, then get image info to ensure fields are correctly returned")
    public void testGetImageInfoSuppress500Version() {
        NewPartRequest productionInfoInputs = workorderApiUtils.setupProductionInfoInputs();

        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileUploadOutputs fileUploadOutputs = workorderApiUtils.initializeAndUploadPartFile(
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

        ImageInfoResponse getImageInfoResponse = fileUploadResources
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
    @TestRail(id = {8693})
    @Description("Upload a part, cost it, then get image info to ensure fields are correctly returned")
    public void testGetImageInfoExpose500ErrorVersion() {
        String scenarioName = new GenerateStringUtil().generateStringForAutomation("Scenario");
        NewPartRequest productionInfoInputs = workorderApiUtils.setupProductionInfoInputs();

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

        ImageInfoResponse getImageInfoResponse = fileUploadResources
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
    @TestRail(id = 11981)
    @Description("Delete Scenario")
    public void testDeleteScenario() {
        fileUploadResources.checkValidProcessGroup(sheetMetalProcessGroup);

        FileUploadOutputs fileUploadOutputs = workorderApiUtils.initializeAndUploadPartFile(
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
        ScenarioIterationKey scenarioIterationKey = workorderApiUtils.setupScenarioIterationKey(deleteScenarioOutputs, iteration);

        GenericErrorResponse genericErrorResponse = acsResources.getScenarioInfoByScenarioIterationKeyNegative(scenarioIterationKey);

        assertThat(genericErrorResponse.getErrorCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
        assertThat(genericErrorResponse.getErrorMessage(), is(containsString("No scenario found for key: ")));
    }

    @Test
    @TestRail(id = 11990)
    @Description("Edit Scenario - Part - Shallow - Change Scenario Name")
    public void testShallowEditPartScenario() {
        workorderApiUtils.testShallowEditOfScenario(
            "Casting.prt",
            false
        );
    }

    @Test
    @TestRail(id = 11991)
    @Description("Edit Scenario - Assembly - Shallow - Change Scenario Name")
    public void testShallowEditAssemblyScenario() {
        workorderApiUtils.testShallowEditOfScenario(
            "PatternThreadHoles.asm",
            true
        );
    }

    @Test
    @TestRail(id = 12044)
    @Description("Generate All Images - Part File")
    public void testGenerateAllPartImages() {
        FileUploadOutputs fileUploadOutputs = workorderApiUtils.initializeAndUploadPartFile(
            "3574727.prt",
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(),
            false
        );

        AllImagesOutputs generateAllImagesOutputs = fileUploadResources.createGenerateAllImagesWorkorderSuppressError(fileUploadOutputs);

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
    @TestRail(id = 12047)
    @Description("Generate Simple Image - Part File")
    public void testGenerateSimpleImageData() {
        FileUploadOutputs fileUploadOutputs = workorderApiUtils.initializeAndUploadPartFile(
            "3574727.prt",
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(),
            false
        );

        SimpleImageDataOutputs generateSimpleImageDataOutputs = fileUploadResources
            .createGenerateSimpleImageDataWorkorderSuppressError(fileUploadOutputs);

        assertThat(Base64.isBase64(
            acsResources.getImageByScenarioIterationKey(generateSimpleImageDataOutputs.getScenarioIterationKey(), false)),
            is(equalTo(true))
        );
    }
}
