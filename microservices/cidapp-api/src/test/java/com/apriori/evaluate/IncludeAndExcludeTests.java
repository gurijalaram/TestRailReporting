package com.apriori.evaluate;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.request.ScenarioAssociationGroupItems;
import com.apriori.cidappapi.models.response.scenarios.ScenarioManifestSubcomponents;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.AssociationSuccessesFailures;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class IncludeAndExcludeTests {

    private static UserCredentials currentUser;
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {11925})
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
    @TestRail(id = {11923, 11924})
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
    @TestRail(id = {11926, 11927})
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
    @TestRail(id = {11928})
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
    @TestRail(id = {11931})
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

}
