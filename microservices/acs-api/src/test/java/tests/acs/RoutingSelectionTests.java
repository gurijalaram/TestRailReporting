package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;
import tests.workorders.WorkorderAPITests;

public class RoutingSelectionTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "14843")
    @Description("Save Routing Selection after Cost")
    public void testSaveRoutingSelection() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
                productionInfoInputs,
                fileUploadOutputs,
                processGroup,
                false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Sheet Metal",
            "aPriori USA",
            "Sheet Metal"
        );

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(testCaseId = "14854")
    @Description("Save Routing Selection after Cost for Additive Manufacturing")
    public void testSaveRoutingSelectionAdditiveManufacturing() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "BasicScenario_Additive.prt.1",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Additive Manufacturing",
            "aPriori India",
            "Additive Manufacturing"
        );

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(testCaseId = "14854")
    @Description("Save Routing Selection after Cost for Bar & Tube")
    public void testSaveRoutingSelectionBarandTube() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "B&T-LOW-001.SLDPRT",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Bar & Tube Fab",
            "aPriori India",
            "Bar & Tube Fab"
        );

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }

    @Test
    @TestRail(testCaseId = "14854")
    @Description("Save Routing Selection after Cost for Casting - Die")
    public void testSaveRoutingSelectionCastingDie() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "Casting-Die.stp",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        GenericResourceCreatedIdResponse response = acsResources.saveRoutingSelection(
            costOutputs.getScenarioIterationKey(),
            "Casting - Die",
            "aPriori India",
            "Casting - Die"
        );

        assertThat(response.getId(), is(notNullValue()));
        assertThat(response.getResourceCreated(), is(equalTo("true")));
    }
}
