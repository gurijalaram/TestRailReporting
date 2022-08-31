package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ScenarioAssociationGroupItems;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifestSubcomponents;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.AssociationSuccessesFailures;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IncludeAndExcludeTests {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    private static UserCredentials currentUser;

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = {"11925"})
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeAllSubcomponents() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final String PART_0002 = "Part0002";
        final String PART_0003 = "Part0003";
        final String PART_0004 = "Part0004";
        final List<String> subComponentNames = Arrays.asList(PART_0001, PART_0002, PART_0003, PART_0004);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;

        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, true, PART_0001 + ", " + scenarioName +
            "", PART_0002 + ", " + scenarioName, PART_0003 + ", " + scenarioName, PART_0004 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse.getResponseEntity().getSuccesses().size()).isEqualTo(4);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0001, scenarioName)).isEqualTo(true);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0002, scenarioName)).isEqualTo(true);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0003, scenarioName)).isEqualTo(true);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0004, scenarioName)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11923", "11924"})
    @Description("Exclude 1 or more sub-components from top-level assembly")
    public void testExcludeOneSeveralSubcomponents() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final String PART_0002 = "Part0002";
        final String PART_0003 = "Part0003";
        final String PART_0004 = "Part0004";
        final List<String> subComponentNames = Arrays.asList(PART_0001, PART_0002, PART_0003, PART_0004);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;

        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);


        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, true, PART_0001 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse.getResponseEntity().getSuccesses().size()).isEqualTo(1);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0001, scenarioName)).isEqualTo(true);

        ResponseWrapper<AssociationSuccessesFailures> patchResponse2 = scenariosUtil.patchAssociations(componentAssembly, true, PART_0002 + ", " + scenarioName, PART_0003 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse2.getResponseEntity().getSuccesses().size()).isEqualTo(2);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0002, scenarioName)).isEqualTo(true);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0003, scenarioName)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11926", "11927"})
    @Description("Include 1 or more sub-components from top-level assembly")
    public void testIncludeOneSeveralSubcomponents() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final String PART_0002 = "Part0002";
        final String PART_0003 = "Part0003";
        final String PART_0004 = "Part0004";
        final List<String> subComponentNames = Arrays.asList(PART_0001, PART_0002, PART_0003, PART_0004);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;

        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, true, PART_0001 + ", " + scenarioName +
            "", PART_0002 + ", " + scenarioName, PART_0003 + ", " + scenarioName, PART_0004 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse.getResponseEntity().getSuccesses().size()).isEqualTo(4);

        scenariosUtil.postCostScenario(componentAssembly);

        ResponseWrapper<AssociationSuccessesFailures> patchResponse2 = scenariosUtil.patchAssociations(componentAssembly, false, PART_0001 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse2.getResponseEntity().getSuccesses().size()).isEqualTo(1);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0001, scenarioName)).isEqualTo(false);

        ResponseWrapper<AssociationSuccessesFailures> patchResponse3 = scenariosUtil.patchAssociations(componentAssembly, false, PART_0002 + ", " + scenarioName, PART_0003 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse3.getResponseEntity().getSuccesses().size()).isEqualTo(2);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0002, scenarioName)).isEqualTo(false);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0003, scenarioName)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11928"})
    @Description("Include all sub-components from top-level assembly")
    public void testIncludeAllSubcomponents() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final String PART_0002 = "Part0002";
        final String PART_0003 = "Part0003";
        final String PART_0004 = "Part0004";
        final List<String> subComponentNames = Arrays.asList(PART_0001, PART_0002, PART_0003, PART_0004);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;

        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, false, PART_0001 + ", " + scenarioName +
            "", PART_0002 + ", " + scenarioName, PART_0003 + ", " + scenarioName, PART_0004 + ", " + scenarioName);

        softAssertions.assertThat(patchResponse.getResponseEntity().getSuccesses().size()).isEqualTo(4);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0001, scenarioName)).isEqualTo(false);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0002, scenarioName)).isEqualTo(false);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0003, scenarioName)).isEqualTo(false);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0004, scenarioName)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11931"})
    @Description("Include and Exclude at the same time")
    public void testIncludeExcludeSimultaneous() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final String PART_0002 = "Part0002";
        final List<String> subComponentNames = Arrays.asList(PART_0001, PART_0002);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;

        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ScenarioManifestSubcomponents part001Details = scenariosUtil.filterScenarioManifest(componentAssembly, PART_0001, scenarioName).get(0);
        ScenarioManifestSubcomponents part002Details = scenariosUtil.filterScenarioManifest(componentAssembly, PART_0002, scenarioName).get(0);

        ScenarioAssociationGroupItems scenarioAssociation1 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part001Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(part001Details.getScenarioIdentity())
            .occurrences(part001Details.getOccurrences())
            .excluded(true)
            .build();

        ScenarioAssociationGroupItems scenarioAssociation2 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part002Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(part002Details.getScenarioIdentity())
            .occurrences(part002Details.getOccurrences())
            .excluded(false)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation1, scenarioAssociation2));

        softAssertions.assertThat(patchResponse.getResponseEntity().getSuccesses().size()).isEqualTo(2);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0001, scenarioName)).isEqualTo(true);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0002, scenarioName)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11932", "11933"})
    @Description("Error returned on invalid Scenario Association Identity")
    public void testIncorrectAssociationId() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final String PART_0002 = "Part0002";
        final List<String> subComponentNames = Arrays.asList(PART_0001, PART_0002);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;
        final String INCORRECT_SCENARIO_ASSOCIATION_ID = "INC27C000000";
        final String INCORRECT_CHILD_SCENARIO_ID = "ABCDEF123456";

        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ScenarioManifestSubcomponents part001Details = scenariosUtil.filterScenarioManifest(componentAssembly, PART_0001, scenarioName).get(0);

        ScenarioAssociationGroupItems scenarioAssociation1 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(INCORRECT_SCENARIO_ASSOCIATION_ID)
            .childScenarioIdentity(part001Details.getScenarioIdentity())
            .occurrences(part001Details.getOccurrences())
            .excluded(true)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation1));

        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo("Scenario association with identity '" + INCORRECT_SCENARIO_ASSOCIATION_ID + "' is not a member of scenario with identity '" + componentAssembly.getScenarioIdentity() + "'");

        ScenarioManifestSubcomponents part002Details = scenariosUtil.filterScenarioManifest(componentAssembly, PART_0002, scenarioName).get(0);

        ScenarioAssociationGroupItems scenarioAssociation2 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part002Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(INCORRECT_CHILD_SCENARIO_ID)
            .occurrences(part002Details.getOccurrences())
            .excluded(true)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse2 = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation2));

        softAssertions.assertThat(patchResponse2.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse2.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo("Resource 'Scenario' with identity '" + INCORRECT_CHILD_SCENARIO_ID + "' was not found");

        softAssertions.assertAll();
    }
}
