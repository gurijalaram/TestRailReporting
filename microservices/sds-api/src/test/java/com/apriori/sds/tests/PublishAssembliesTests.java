package com.apriori.sds.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Arrays;

public class PublishAssembliesTests extends SDSTestUtil {
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;

    @Test
    @TestRail(id = 12308")
        @Description("Verify Shallow Publish through SDS api")
        public void testShallowPublishAssembly(){
        String scenarioName=new GenerateStringUtil().generateScenarioName();
        UserCredentials testingUser=UserUtil.getUser();

        final String FLANGE="flange";
        final String NUT="nut";
        final String BOLT="bolt";
        String assemblyName="flange c";
        final String assemblyExtension=".CATProduct";

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
@TestRail(id = 12309")
    @Description("Verify that an error is returned if Shallow Publish is requested when associated sub-component scenarios still exist in private workspace, through SDS api")
    public void testShallowPublishAssemblyWithPrivateSubcomponents(){
    String scenarioName=new GenerateStringUtil().generateScenarioName();
    UserCredentials testingUser=UserUtil.getUser();

    final String FLANGE="flange";
    final String NUT="nut";
    final String BOLT="bolt";
    String assemblyName="flange c";
    final String assemblyExtension=".CATProduct";

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
