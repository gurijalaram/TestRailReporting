package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioAssociations;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class IncludeAndExcludeTests {

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

    @Test
    @TestRail(testCaseId = "11925")
    @Description("Exclude all sub-components from top-level assembly")
    public void testExcludeAllSubcomponents() {
        final String assemblyName = "Assembly01";
        final String assemblyExtension = ".iam";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("Part0001", "Part0002", "Part0003", "Part0004");
        final String subComponentExtension = ".ipt";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.SHEET_METAL;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

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

        ResponseWrapper<ScenarioAssociations> patchResponse = scenariosUtil.patchAssociations(componentAssembly, "Part0001",  scenarioName, true);

        assertThat(patchResponse.getResponseEntity().getSuccesses(), is(equalTo(1)));
    }
}
