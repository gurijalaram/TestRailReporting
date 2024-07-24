package com.apriori.acs.api.tests.workorders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.acs.api.models.request.workorders.NewPartRequest;
import com.apriori.acs.api.models.request.workorders.assemblyobjects.AssemblyInfo;
import com.apriori.acs.api.models.request.workorders.assemblyobjects.AssemblyInfoComponent;
import com.apriori.acs.api.models.response.workorders.assemblyimages.AssemblyImagesOutputs;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.models.response.workorders.deletescenario.DeleteScenarioOutputs;
import com.apriori.acs.api.models.response.workorders.editscenario.EditScenarioOutputs;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioKey;
import com.apriori.acs.api.models.response.workorders.getimageinfo.ImageInfoResponse;
import com.apriori.acs.api.models.response.workorders.loadcadmetadata.LoadCadMetadataOutputs;
import com.apriori.acs.api.models.response.workorders.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.acs.api.models.response.workorders.upload.Assembly;
import com.apriori.acs.api.models.response.workorders.upload.AssemblyComponent;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.json.JsonManager;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class WorkorderApiUtils {

    private final String assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY.getProcessGroup();
    private final String castingProcessGroup = ProcessGroupEnum.CASTING.getProcessGroup();
    private final String scenarioName = new GenerateStringUtil().generateStringForAutomation("Scenario");
    private FileUploadResources fileUploadResources;
    private AcsResources acsResources;
    private RequestEntityUtil requestEntityUtil;

    public WorkorderApiUtils(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
        this.fileUploadResources = new FileUploadResources(this.requestEntityUtil);
        this.acsResources = new AcsResources(this.requestEntityUtil);
    }

    /**
     * Core code for testing shallow edit of a scenario
     *
     * @param fileName - String
     * @param includeSubComponents - boolean flag (inc sub components or not)
     *                             to enable this method to work for both parts and assemblies
     */
    public void testShallowEditOfScenario(String fileName, boolean includeSubComponents) {
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
    public FileUploadOutputs initializeAndUploadPartFile(String fileName, String processGroup, boolean loadCadMetadata) {
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
    public FileResponse initializeAndUploadAssemblyFile(AssemblyInfo assemblyToUse, boolean doLoadCadMetadata) {
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
    public Assembly createAssembly(List<FileUploadOutputs> componentFileUploadOutputs, FileResponse assemblyFileResponse) {
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
    public AssemblyInfo createAndReturnAssemblyInfo() {
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
    public AssemblyImagesOutputs loadCadMetadataAndGenerateAssemblyImages(FileResponse assemblyFileResponse) {
        LoadCadMetadataOutputs assemblyMetadataOutput = fileUploadResources.loadCadMetadataSuppressError(assemblyFileResponse);
        return fileUploadResources.generateAssemblyImages(
            assemblyFileResponse,
            fileUploadResources.getComponentMetadataOutputs(),
            assemblyMetadataOutput
        );
    }

    /**
     * Gets image info and validates it
     *
     * @param scenarioIterationKey - info to use to get image
     */
    /*public void bomLoaderManualCost(ScenarioIterationKey scenarioIterationKey) {
        BomLoaderResponse bomLoaderResponse = fileUploadResources.getImageInfo(scenarioIterationKey);
    }*/

    /**
     * Gets image info and validates it
     *
     * @param scenarioIterationKey - info to use to get image
     */
    public void getAndValidateImageInfo(ScenarioIterationKey scenarioIterationKey) {
        ImageInfoResponse imageInfoResponse = fileUploadResources.getImageInfo(scenarioIterationKey);

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
    public ScenarioIterationKey setupScenarioIterationKey(DeleteScenarioOutputs deleteScenarioOutputs, String iteration) {
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
    public void performUploadCostPublishAssemblyComponentAssertions(ScenarioIterationKey scenarioIterationKey, Integer notExpectedIteration) {
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
