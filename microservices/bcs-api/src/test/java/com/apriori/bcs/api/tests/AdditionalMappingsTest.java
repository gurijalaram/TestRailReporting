package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.enums.MachiningMode;
import com.apriori.bcs.api.enums.PartFieldsEnum;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.bcs.api.models.response.Results;
import com.apriori.bcs.api.utils.BcsTestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestRulesAPI.class)
public class AdditionalMappingsTest {
    private SoftAssertions softAssertions;
    private BcsTestUtil bcsTestUtil;
    private QueryParams queryParams;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bcsTestUtil = new BcsTestUtil();
    }

    @Test
    @TestRail(id = {28071, 28072})
    @Description("Target Cost and Target Mass set for Part and Assembly" +
        "Setting Min and Max values for Target Cost and Target Mass for Part and Assembly")
    public void createBatchWithTargetCostAndMass() {
        final String partName = "79d-083-111000";
        final String assemblyName = "79d-083-110000";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, partName + ".SLDPRT");
        final File assemblyFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.TARGET_COST.getPartFieldName(), "1.00")
            .use(PartFieldsEnum.TARGET_MASS.getPartFieldName(), "1.50");

        QueryParams assemblyQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), assemblyName)
            .use(PartFieldsEnum.TARGET_COST.getPartFieldName(), "2.00")
            .use(PartFieldsEnum.TARGET_MASS.getPartFieldName(), "2.50");

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .addAssemblyToBatch(assemblyQueryParams, assemblyFile)
            .startCosting();

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getTargetCost()).isEqualTo(1.0);
        softAssertions.assertThat(partResults.getTargetMass()).isEqualTo(1.5);
        Results assemblyResults = bcsTestUtil.getAssemblyResult();
        softAssertions.assertThat(assemblyResults.getTargetCost()).isEqualTo(2.0);
        softAssertions.assertThat(assemblyResults.getTargetMass()).isEqualTo(2.5);
    }

    @Test
    @TestRail(id = {28073, 28082})
    @Description("invalid numerical value for Target Cost and Target Mass input validation" +
        "aPW Scenario Link present in response")
    public void createBatchWithInvalidTargetCostAndMassNumber() {
        final String partName = "79d-083-111000";
        final String assemblyName = "79d-083-110000";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, partName + ".SLDPRT");
        final File assemblyFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.TARGET_COST.getPartFieldName(), "-1.00")
            .use(PartFieldsEnum.TARGET_MASS.getPartFieldName(), "-1.50");

        QueryParams assemblyQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), assemblyName)
            .use(PartFieldsEnum.TARGET_COST.getPartFieldName(), "-2.00")
            .use(PartFieldsEnum.TARGET_MASS.getPartFieldName(), "-2.50");

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .addAssemblyToBatch(assemblyQueryParams, assemblyFile)
            .startCosting();

        Part partInfo = bcsTestUtil.getPartInfo();
        softAssertions.assertThat(partInfo.getErrors()).contains("Ignoring costing input 'Target Cost' due to invalid value '-1.0'");
        softAssertions.assertThat(partInfo.getErrors()).contains("Ignoring costing input 'Target Mass' due to invalid value '-1.5'");
        softAssertions.assertThat(partInfo.getApwScenarioLink()).isNotEmpty();

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getTargetCost()).isNull();
        softAssertions.assertThat(partResults.getTargetMass()).isNull();
        Results assemblyResults = bcsTestUtil.getAssemblyResult();
        softAssertions.assertThat(assemblyResults.getTargetCost()).isNull();
        softAssertions.assertThat(assemblyResults.getTargetMass()).isNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28074})
    @Description("invalid value for Target Cost and Target Mass")
    public void createBatchWithTargetCostAndMassString() {
        final String partName = "79d-083-111000";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, partName + ".SLDPRT");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.TARGET_COST.getPartFieldName(), "invalid")
            .use(PartFieldsEnum.TARGET_MASS.getPartFieldName(), "invalid");

        BatchPartResources.addPartToBatch(partQueryParams, partFile, bcsTestUtil.createBatch().getBatch().getIdentity(), ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @TestRail(id = {28075})
    @Description("MachiningMode 'May Be Machined' set for all supported Process Groups")
    public void createBatchWithSupportedMachiningMode() {
        final String partName = "hinge_1";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ADDITIVE_MANUFACTURING, partName + ".prt.1");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.MACHINING_MODE.getPartFieldName(), MachiningMode.MAY_BE_MACHINED.getMachiningMode());

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .startCosting();

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getMachiningMode()).isEqualTo(MachiningMode.MAY_BE_MACHINED.getMachiningMode());
    }

    @Test
    @TestRail(id = {28076})
    @Description("MachiningMode 'Not Machined' set for all supported Process Groups")
    public void createBatchWithSupportedNotMachined() {
        final String partName = "hinge_1";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ADDITIVE_MANUFACTURING, partName + ".prt.1");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.MACHINING_MODE.getPartFieldName(), MachiningMode.NOT_MACHINED.getMachiningMode());

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .startCosting();

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getMachiningMode()).isEqualTo(MachiningMode.NOT_MACHINED.getMachiningMode());
    }

    @Test
    @TestRail(id = {28077})
    @Description("MachiningMode set for part with Process Group that does not support machiningMode input")
    public void createBatchWithNotSupportedMachiningMode() {
        final String partName = "79d-083-111000";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, partName + ".SLDPRT");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.MACHINING_MODE.getPartFieldName(), MachiningMode.MAY_BE_MACHINED.getMachiningMode());

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .startCosting();

        Part partInfo = bcsTestUtil.getPartInfo();
        softAssertions.assertThat(partInfo.getErrors()).isEqualTo("Ignoring costing input 'Machining Mode'. The process group 'Plastic Molding' does not support machining");

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getMachiningMode()).isNull();
    }

    @Test
    @TestRail(id = {28078})
    @Description("default value used for machiningMode when invalid string set in request")
    public void createBatchWithInvalidMachiningMode() {
        final String partName = "hinge_1";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ADDITIVE_MANUFACTURING, partName + ".prt.1");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.MACHINING_MODE.getPartFieldName(), "Invalid");

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .startCosting();

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getMachiningMode()).isEqualTo(MachiningMode.MAY_BE_MACHINED.getMachiningMode());
    }

    @Test
    @TestRail(id = {28079})
    @Description("default value used for machiningMode when set to null in request body")
    public void createBatchWithNullMachiningMode() {
        final String partName = "hinge_1";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ADDITIVE_MANUFACTURING, partName + ".prt.1");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName)
            .use(PartFieldsEnum.MACHINING_MODE.getPartFieldName(), null);

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .startCosting();

        Part partInfo = bcsTestUtil.getPartInfo();
        softAssertions.assertThat(partInfo.getErrors()).contains("Ignoring costing input 'Machining Mode' due to invalid value");

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getMachiningMode()).isEqualTo(MachiningMode.MAY_BE_MACHINED.getMachiningMode());
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
