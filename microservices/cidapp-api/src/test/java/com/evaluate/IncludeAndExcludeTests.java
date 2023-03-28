package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ScenarioAssociationGroupItems;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioManifestSubcomponents;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.AssociationSuccessesFailures;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
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

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation1, scenarioAssociation2), AssociationSuccessesFailures.class);

        softAssertions.assertThat(patchResponse.getResponseEntity().getSuccesses().size()).isEqualTo(2);

        scenariosUtil.postCostScenario(componentAssembly);

        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0001, scenarioName)).isEqualTo(true);
        softAssertions.assertThat(scenariosUtil.isSubcomponentExcluded(componentAssembly, PART_0002, scenarioName)).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @Ignore("Caused by APD-1119 which is marked as Won't Do")
    @TestRail(testCaseId = {"11932", "11933", "11939", "11940", "11941"})
    @Description("Error returned on invalid Scenario Association Identity, invalid Child Scenario Identity, empty Scenario Association Identity, Child Scenario Identity and mismatched Ids")
    public void testIncorrectAndEmptyAssociationScenarioId() {
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

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation1), AssociationSuccessesFailures.class);

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

        ResponseWrapper<AssociationSuccessesFailures> patchResponse2 = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation2), AssociationSuccessesFailures.class);

        softAssertions.assertThat(patchResponse2.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse2.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo("Resource 'Scenario' with identity '" + INCORRECT_CHILD_SCENARIO_ID + "' was not found");

        ScenarioAssociationGroupItems scenarioAssociation3 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity("")
            .childScenarioIdentity(part002Details.getScenarioIdentity())
            .occurrences(part002Details.getOccurrences())
            .excluded(true)
            .build();

        ResponseWrapper<ErrorMessage> patchResponse3 = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation3), ErrorMessage.class);

        softAssertions.assertThat(patchResponse3.getResponseEntity().getMessage()).isEqualTo("'scenarioAssociationIdentity' should not be null.");

        ScenarioAssociationGroupItems scenarioAssociation4 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part002Details.getScenarioAssociationIdentity())
            .childScenarioIdentity("")
            .occurrences(part002Details.getOccurrences())
            .excluded(true)
            .build();

        ResponseWrapper<ErrorMessage> patchResponse4 = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation4), ErrorMessage.class);

        softAssertions.assertThat(patchResponse4.getResponseEntity().getMessage()).isEqualTo("'childScenarioIdentity' should not be null.");

        ScenarioAssociationGroupItems scenarioAssociation5 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part001Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(part002Details.getScenarioIdentity())
            .occurrences(part002Details.getOccurrences())
            .excluded(true)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse5 = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation5), AssociationSuccessesFailures.class);

        softAssertions.assertThat(patchResponse5.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse5.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo("An unexpected error occurred whilst performing a HTTP request. If the issue persists, contact aPriori Support.");

        softAssertions.assertAll();
    }

    @Test
    @Ignore("Caused by APD-1119 which is marked as Won't Do")
    @TestRail(testCaseId = {"11934"})
    @Description("Error returned when more occurrences than exist requested")
    public void testIncorrectOccurrences() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final List<String> subComponentNames = Arrays.asList(PART_0001);
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;
        final int INCORRECT_OCCURRENCES = 5;

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
            .scenarioAssociationIdentity(part001Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(part001Details.getScenarioIdentity())
            .occurrences(INCORRECT_OCCURRENCES)
            .excluded(true)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Arrays.asList(scenarioAssociation1), AssociationSuccessesFailures.class);

        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo("Scenario association with identity '" + INCORRECT_OCCURRENCES + "' is not a member of scenario with identity '" + componentAssembly.getScenarioIdentity() + "'");

        softAssertions.assertAll();
    }

    @Test
    @Ignore("Caused by APD-1119 which is marked as Won't Do")
    @TestRail(testCaseId = {"11937"})
    @Description("Error returned when including an included component")
    public void testIncludeIncludedSubcomponent() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final List<String> subComponentNames = Arrays.asList(PART_0001);
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

        scenariosUtil.patchAssociations(componentAssembly, false, PART_0001 + ", " + scenarioName);

        scenariosUtil.postCostScenario(componentAssembly);

        ScenarioManifestSubcomponents part001Details = scenariosUtil.filterScenarioManifest(componentAssembly, PART_0001, scenarioName).get(0);

        ScenarioAssociationGroupItems scenarioAssociation1 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part001Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(part001Details.getScenarioIdentity())
            .occurrences(part001Details.getOccurrences())
            .excluded(false)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Collections.singletonList(scenarioAssociation1), AssociationSuccessesFailures.class);

        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().get(0).getError()).isEqualTo("Error");

        softAssertions.assertAll();
    }

    @Test
    @Ignore("Caused by APD-1119 which is marked as Won't Do")
    @TestRail(testCaseId = {"11938"})
    @Description("Error returned when excluding an excluded component")
    public void testExcludingExcludedSubcomponent() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String PART_0001 = "Part0001";
        final List<String> subComponentNames = Arrays.asList(PART_0001);
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

        scenariosUtil.patchAssociations(componentAssembly, true, PART_0001 + ", " + scenarioName);

        scenariosUtil.postCostScenario(componentAssembly);

        ScenarioManifestSubcomponents part001Details = scenariosUtil.filterScenarioManifest(componentAssembly, PART_0001, scenarioName).get(0);

        ScenarioAssociationGroupItems scenarioAssociation1 = ScenarioAssociationGroupItems.builder()
            .scenarioAssociationIdentity(part001Details.getScenarioAssociationIdentity())
            .childScenarioIdentity(part001Details.getScenarioIdentity())
            .occurrences(part001Details.getOccurrences())
            .excluded(true)
            .build();

        ResponseWrapper<AssociationSuccessesFailures> patchResponse = scenariosUtil.patchAssociations(componentAssembly, Collections.singletonList(scenarioAssociation1), AssociationSuccessesFailures.class);

        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().size()).isEqualTo(1);
        softAssertions.assertThat(patchResponse.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo("Part is already excluded");

        softAssertions.assertAll();
    }
}
