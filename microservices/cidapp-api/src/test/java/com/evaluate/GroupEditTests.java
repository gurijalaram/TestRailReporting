package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.request.ForkRequest;
import com.apriori.cidappapi.entity.response.ScenarioSuccessesFailures;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupEditTests {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder componentAssembly;
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = {"11094", "11095"})
    @Description("Edit multiple public sub-components with no Private counterparts (Overwrite)")
    public void testGroupEditSubcomponents() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();

        final String STAND = "stand";
        final String DRIVE = "drive";
        final String JOINT = "joint";
        String assemblyName = "oldham";
        final String assemblyExtension = ".asm.1";

        List<String> subComponentNames = Arrays.asList(STAND, DRIVE);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".prt.1";

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

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costAssembly(componentAssembly);

        assemblyUtils.publishSubComponents(componentAssembly);

        ForkRequest forkRequest = ForkRequest.builder()
            .scenarioName(newScenarioName)
            .override(true)
            .build();

        ResponseWrapper<ScenarioSuccessesFailures> groupEditResponse = scenariosUtil.postEditGroupScenarios(componentAssembly, forkRequest,
            STAND + "," + scenarioName, DRIVE + "," + scenarioName);

        SoftAssertions softAssertions = new SoftAssertions();

        groupEditResponse.getResponseEntity().getSuccesses().forEach(o -> softAssertions.assertThat(o.getScenarioName()).isEqualTo(newScenarioName));
    }
}
