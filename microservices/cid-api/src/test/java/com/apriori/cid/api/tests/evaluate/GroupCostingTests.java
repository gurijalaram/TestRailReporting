package com.apriori.cid.api.tests.evaluate;

import com.apriori.cid.api.models.response.GroupCostResponse;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class GroupCostingTests {

    private final List<String> subComponentNames = Arrays.asList(
        "50mmArc", "50mmCube", "50mmEllipse", "50mmOctagon", "75mmCube", "75mmHexagon",
        "100mmCube", "100mmSlot", "150mmCuboid", "200mmCylinder", "500mmBlob");
    private ComponentInfoBuilder componentAssembly;
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions;
    private UserCredentials currentUser;
    private String subComponentExtension = ".SLDPRT";
    private String assemblyName = "RandomShapeAsm";
    private String assemblyExtension = ".SLDASM";

    @Test
    @TestRail(id = {10620, 11845})
    @Description("Group Cost 10 components")
    public void testGroupCostTenParts() {
        final int noOfSubcomponentsToCost = 10;

        ComponentInfoBuilder componentAssembly = new AssemblyRequestUtil().getAssembly(assemblyName);
        componentAssembly.getSubComponents().forEach(subcomponent -> subcomponent.setProcessGroup(ProcessGroupEnum.SHEET_METAL));

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        GroupCostResponse groupCostResponse = scenariosUtil.postGroupCostScenarios(componentAssembly.getSubComponents().subList(0, noOfSubcomponentsToCost));

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupCostResponse.getSuccesses().size()).as("Group Cost Successes").isEqualTo(noOfSubcomponentsToCost);
        softAssertions.assertThat(groupCostResponse.getFailures().size()).as("Group Cost Failures").isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10620, 11846})
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

        componentAssembly.setCostingTemplate(scenariosUtil.postCostingTemplate(componentAssembly));
        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getStatusCode()).as("Group Cost Bad Request Code").isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(
            groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'groupItems' should be less than or equal to 10.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11849, 11848, 11847})
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

        componentAssembly.getSubComponents().forEach(component -> softAssertions.assertThat(scenariosUtil.getScenarioCompleted(component).getScenarioState())
            .isEqualTo(ScenarioStateEnum.NOT_COSTED.getState()));

        softAssertions.assertThat(error.getResponseEntity().getMessage()).contains("validation failures were found");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11848)
    @Description("Verify Error Response for Empty Scenario Identity")
    public void testEmptyScenarioIdentity() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames.subList(0, 3),
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setScenarioIdentity(""));
        componentAssembly.setCostingTemplate(scenariosUtil.postCostingTemplate(componentAssembly));

        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'scenarioIdentity' should not be null.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11847)
    @Description("Verify Error Response for Empty Scenario Identity")
    public void testEmptyComponentIdentity() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames.subList(0, 3),
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.getSubComponents().forEach(subComponent -> subComponent.setComponentIdentity(""));
        componentAssembly.setCostingTemplate(scenariosUtil.postCostingTemplate(componentAssembly));

        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'componentIdentity' should not be null.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11849)
    @Description("Verify Error Response for Empty Costing Template Identity")
    public void testEmptyCostingTemplateIdentity() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames.subList(0, 3),
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        componentAssembly.setCostingTemplate(CostingTemplate.builder().identity("").build());

        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'costingTemplateIdentity' should not be null.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 11850)
    @Description("Verify Error Response for Empty Group Item List")
    public void testEmptyGroupItemList() {

        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.SHEET_METAL;
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly = ComponentInfoBuilder.builder()
            .componentName(assemblyName)
            .extension(assemblyExtension)
            .scenarioName(scenarioName)
            .processGroup(asmProcessGroupEnum)
            .user(currentUser)
            .build();

        assemblyUtils.uploadAssembly(componentAssembly);

        componentAssembly.setCostingTemplate(scenariosUtil.postCostingTemplate(componentAssembly));
        ResponseWrapper<ErrorMessage> groupErrorResponse = scenariosUtil.postIncorrectGroupCostScenarios(componentAssembly);

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(groupErrorResponse.getResponseEntity().getMessage()).as("Group Cost Error Message").isEqualTo("'groupItems' should not be empty.");

        softAssertions.assertAll();
    }
}
