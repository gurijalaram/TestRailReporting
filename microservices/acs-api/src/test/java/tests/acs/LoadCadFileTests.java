package tests.acs;

import com.apriori.GenerateStringUtil;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class LoadCadFileTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(id = "16515")
    @Description("Test LOADCADFILE API with keepFreeBodies set to False")
    public void testLoadCadFileKeepFreeBodiesFalse() {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs response = fileUploadResources.createFileUploadWorkorderWithParameters(
            fileResponse,
            testScenarioName,
            false,
            false,
            true
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getScenarioIterationKey()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(id = "16516")
    @Description("Test LOADCADFILE API with keepFreeBodies set to True")
    public void testLoadCadFileKeepFreeBodiesTrue() {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs response = fileUploadResources.createFileUploadWorkorderWithParameters(
            fileResponse,
            testScenarioName,
            true,
            false,
            true
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getScenarioIterationKey()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(id = "16517")
    @Description("Test LOADCADFILE API with freeBodiesPreserveCad set to True")
    public void testLoadCadFilefreeBodiesPreserveCadTrue() {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs response = fileUploadResources.createFileUploadWorkorderWithParameters(
            fileResponse,
            testScenarioName,
            false,
            true,
            true
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getScenarioIterationKey()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(id = "16518")
    @Description("Test LOADCADFILE API with freeBodiesPreserveCad set to False")
    public void testLoadCadFilefreeBodiesPreserveCadFalse() {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs response = fileUploadResources.createFileUploadWorkorderWithParameters(
            fileResponse,
            testScenarioName,
            false,
            false,
            true
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getScenarioIterationKey()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(id = "16519")
    @Description("Test LOADCADFILE API with freeBodiesIgnoreMissingComponents set to False")
    public void testLoadCadFilefreeBodiesIgnoreMissingComponentsFalse() {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs response = fileUploadResources.createFileUploadWorkorderWithParameters(
            fileResponse,
            testScenarioName,
            false,
            false,
            false
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getScenarioIterationKey()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(id = "16520")
    @Description("Test LOADCADFILE API with freeBodiesIgnoreMissingComponents set to True")
    public void testLoadCadFilefreeBodiesIgnoreMissingComponentsTrue() {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs response = fileUploadResources.createFileUploadWorkorderWithParameters(
            fileResponse,
            testScenarioName,
            false,
            false,
            true
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getScenarioIterationKey()).isNotNull();
        softAssertions.assertAll();
    }
}
