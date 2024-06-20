package com.apriori.sds.api.tests;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.sds.api.models.response.Scenario;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@Slf4j
@ExtendWith(TestRulesAPI.class)
public class PublishAssembliesTests extends SDSTestUtil {
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentInfoBuilder componentAssembly;

    @Test
    @TestRail(id = 12308)
    @Description("Verify Shallow Publish through SDS api")
    public void testShallowPublishAssembly() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        log.info("test name - testShallowPublishAssembly, scenario name {} and user {}", scenarioName, testingUser.getEmail());

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            testingUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        publishAssembly(componentAssembly, Scenario.class, HttpStatus.SC_OK);
    }

    @Test
    @TestRail(id = 12309)
    @Description("Verify that an error is returned if Shallow Publish is requested when associated sub-component scenarios still exist in private workspace, through SDS api")
    public void testShallowPublishAssemblyWithPrivateSubcomponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        log.info("test name - testShallowPublishAssemblyWithPrivateSubcomponents, scenario name {} and user {}", scenarioName, testingUser.getEmail());

        final String FLANGE = "flange";
        final String NUT = "nut";
        final String BOLT = "bolt";
        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList(FLANGE, NUT, BOLT);
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentExtension = ".CATPart";

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            processGroupEnum,
            scenarioName,
            testingUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        publishAssemblyExpectError(componentAssembly, HttpStatus.SC_CONFLICT);
    }
}
