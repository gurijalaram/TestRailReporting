package com.apriori.compare;

import static com.apriori.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PublicPrivateComparisonTests extends TestBaseUI {
    private static UserCredentials currentUser;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static String assemblyName1;
    private static String assemblyScenarioName1;
    private static String assemblyName2;
    private static String assemblyScenarioName2;
    private static String assemblyName3;
    private static String assemblyScenarioName3;
    private CidAppLoginPage loginPage;
    private ComparePage comparePage;

    @BeforeAll
    public static void testSetup() {
        currentUser = UserUtil.getUser();

        assemblyName1 = "Hinge assembly";
        final String assemblyExtension1 = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup1 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames1 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension1 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup1 = ProcessGroupEnum.FORGING;

        assemblyScenarioName1 = new GenerateStringUtil().generateScenarioName();

        // TODO: 09/03/2023 refactor to dto
        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName1,
            assemblyExtension1,
            assemblyProcessGroup1,
            subComponentNames1,
            subComponentExtension1,
            subComponentProcessGroup1,
            assemblyScenarioName1,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1).costAssembly(componentAssembly1);

        assemblyName2 = "flange c";
        final String assemblyExtension2 = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup2 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames2 = Arrays.asList("flange", "nut", "bolt");
        final String subComponentExtension2 = ".CATPart";
        final ProcessGroupEnum subComponentProcessGroup2 = ProcessGroupEnum.PLASTIC_MOLDING;

        assemblyScenarioName2 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly2 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName2,
            assemblyExtension2,
            assemblyProcessGroup2,
            subComponentNames2,
            subComponentExtension2,
            subComponentProcessGroup2,
            assemblyScenarioName2,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly2).uploadAssembly(componentAssembly2);
        assemblyUtils.costSubComponents(componentAssembly2).costAssembly(componentAssembly2);

        assemblyName3 = "Hinge assembly";
        final String assemblyExtension3 = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup3 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames3 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension3 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup3 = ProcessGroupEnum.FORGING;

        assemblyScenarioName3 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly3 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName3,
            assemblyExtension3,
            assemblyProcessGroup3,
            subComponentNames3,
            subComponentExtension3,
            subComponentProcessGroup3,
            assemblyScenarioName3,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly3).uploadAssembly(componentAssembly3);
        assemblyUtils.costSubComponents(componentAssembly3).costAssembly(componentAssembly3);
        assemblyUtils.publishSubComponents(componentAssembly3).publishAssembly(componentAssembly3);
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = 6533)
    @Description("User can add assemblies to a blank new comparison")
    public void addPublicAndPrivateAssemblyToComparison() {
        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .selectFilter("Recent")
            .multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "", "" + assemblyName2 + ", " + assemblyScenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .modify()
            .selectFilter("Public")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickScenarioCheckbox(assemblyName3, assemblyScenarioName3)
            .submit(ComparePage.class);

        assertThat(comparePage.getAllScenariosInComparison(), containsInRelativeOrder(assemblyName3.toUpperCase() + "  / " + assemblyScenarioName3));
    }
}
