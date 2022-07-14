package com.apriori.sds.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PublishAssembliesTests extends SDSTestUtil {
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;

    @Test
    @TestRail(testCaseId = "12308")
    @Description("Verify Shallow Publish through SDS api")
    public void testShallowPublishAssembly() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testingUser = UserUtil.getUser();

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

        ResponseWrapper<Scenario> assemblyUploadResponse = publishAssembly(componentAssembly, Scenario.class);

        assertThat(assemblyUploadResponse.getStatusCode(), is(HttpStatus.SC_OK));
    }

    @Test
    @TestRail(testCaseId = "12309")
    @Description("Verify that an error is returned if Shallow Publish is requested when associated sub-component scenarios still exist in private workspace, through SDS api")
    public void testShallowPublishAssemblyWithPrivateSubcomponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        UserCredentials testingUser = UserUtil.getUser();

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

        ResponseWrapper<Scenario> assemblyUploadResponse = publishAssemblyExpectError(componentAssembly);

        assertThat(assemblyUploadResponse.getStatusCode(), is(HttpStatus.SC_CONFLICT));
    }
}
