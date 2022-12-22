package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.GroupCostResponse;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupCostingTests {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;
    private SoftAssertions softAssertions;
    private UserCredentials currentUser;

    private final List<String> subComponentNames = Arrays.asList(
        "50mmArc", "50mmCube", "50mmEllipse", "50mmOctagon", "75mmCube", "75mmHexagon",
        "100mmCube", "100mmSlot", "150mmCuboid", "200mmCylinder", "500mmBlob");
    private String subComponentExtension = ".SLDPRT";
    private String assemblyName = "RandomShapeAsm";
    private String assemblyExtension = ".SLDASM";
    private CssComponent cssComponent = new CssComponent();

    @Test
    @TestRail(testCaseId = {"10620", "11845"})
    @Description("Group Cost 10 components")
    public void testGroupCostTenParts() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames,
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        String[] subComponentsToCost = subComponentNames.subList(0, 10).toArray(new String[10]);
        for (int i = 0; i < subComponentsToCost.length; i++) {
            subComponentsToCost[i] += "," + scenarioName;
        }

        ResponseWrapper<GroupCostResponse> groupCostResponse = scenariosUtil.postGroupCostScenarios(componentAssembly, subComponentsToCost);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupCostResponse.getResponseEntity().getSuccesses().size()).as("Group Cost Successes").isEqualTo(subComponentsToCost.length);
        softAssertions.assertThat(groupCostResponse.getResponseEntity().getFailures().size()).as("Group Cost Failures").isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10620", "11846"})
    @Description("Attempt to Group Cost 11 components")
    public void testGroupCostElevenParts() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            processGroupEnum,
            subComponentNames,
            subComponentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.setComponentIdentity("");
        componentAssembly.setScenarioIdentity("");

        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getStatusCode()).as("Group Cost Bad Request Code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(
            groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'groupItems' should be less than or equal to 10.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11850", "11849", "11848", "11847"})
    @Description("Verify 400 Error")
    public void testVerify400Error() {
        final String FLANGE = "flange";
        final String NUT = "nut";
        final String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        final List<String> subComponentNames = Arrays.asList(FLANGE, NUT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        ResponseWrapper<ErrorMessage> error = scenariosUtil.postGroupCostNullScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        componentAssembly.getSubComponents().forEach(component -> softAssertions.assertThat(scenariosUtil.getScenarioRepresentationCompleted(component).getScenarioState())
            .isEqualTo(ScenarioStateEnum.NOT_COSTED.getState()));

        softAssertions.assertThat(error.getResponseEntity().getMessage()).contains("validation failures were found");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11848", "11847"})
    @Description("Verify Error Response for Empty Scenario Identity")
    public void testEmptyScenarioIdentity() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames,
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'componentIdentity' should not be null.");
        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'scenarioIdentity' should not be null.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11849")
    @Description("Verify Error Response for Empty Costing Template Identity")
    public void testEmptyCostingTemplateIdentity() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames,
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.setCostingTemplate(CostingTemplate.builder().costingTemplateIdentity("").build());

        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'costingTemplateIdentity' should not be null.");

        softAssertions.assertAll();
    }
}
