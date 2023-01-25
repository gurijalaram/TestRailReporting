package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(JUnitParamsRunner.class)
public class MaterialSelectionTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private IterationsUtil iterationUtil = new IterationsUtil();

    UserCredentials currentUser;
    private File resourceFile;

    private Object[] testParameters() {
        return new Object[] {
            new Object[] {ProcessGroupEnum.ADDITIVE_MANUFACTURING, "Aluminum AlSi10Mg", "ADD-LOW-001", ".SLDPRT"},
            new Object[] {ProcessGroupEnum.BAR_TUBE_FAB, "Steel, Hot Worked, AISI 1010", "AP-000-006", ".step"},
            new Object[] {ProcessGroupEnum.CASTING, "Aluminum, Cast, ANSI AL380.0", "CastedPart", ".CATPart"},
            new Object[] {ProcessGroupEnum.CASTING_DIE, "Aluminum, Cast, ANSI AL380.0", "CurvedWall", ".CATPart"},
            new Object[] {ProcessGroupEnum.CASTING_INVESTMENT, "Aluminum, Cast, ANSI AL380.0", "AP-000-506", ".prt.1"},
            new Object[] {ProcessGroupEnum.CASTING_SAND, "Aluminum, Cast, ANSI AL380.0", "casting_q5_thinvalve", ".prt"},
            new Object[] {ProcessGroupEnum.FORGING, "Steel, Cold Worked, AISI 1010", "big ring", ".SLDPRT"},
            new Object[] {ProcessGroupEnum.PLASTIC_MOLDING, "ABS", "bolt", ".CATPart"},
            new Object[] {ProcessGroupEnum.POWDER_METAL, "F-0005", "case_31_test_part_6_small", ".prt.2"},
            new Object[] {ProcessGroupEnum.RAPID_PROTOTYPING, "Default", "Plastic moulded cap DFM", ".CATPart"},
            new Object[] {ProcessGroupEnum.ROTO_BLOW_MOLDING, "Polyethylene, High Density (HDPE)", "Plastic moulded cap DFM", ".CATPart"},
            new Object[] {ProcessGroupEnum.SHEET_METAL, "Steel, Cold Worked, AISI 1020", "3571050_cad", ".prt.1"},
            new Object[] {ProcessGroupEnum.SHEET_METAL_HYDROFORMING, "Aluminum, Stock, ANSI 2017", "FlangedRound", ".SLDPRT"},
            //new Object[] {ProcessGroupEnum.SHEET_METAL_ROLLFORMING, "Steel, Cold Worked, AISI 1020", "", ""},
            new Object[] {ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING, "Aluminum, Stock, ANSI 2024", "bracket_basic", ".prt"},
            new Object[] {ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE, "Steel, Cold Worked, AISI 1020", "SheetMetal", ".prt"},
            new Object[] {ProcessGroupEnum.SHEET_PLASTIC, "Polyethylene, HDPE Extrusion Sheet", "r151294", ".prt.1"},
            new Object[] {ProcessGroupEnum.STOCK_MACHINING, "Steel, Hot Worked, AISI 1010", "case_005_flat end mill contouring", ".SLDPRT"}
        };
    }

    @Test
    @Parameters(method = "testParameters")
    @TestCaseName(value = "{method}-{0}")
    @TestRail(testCaseId = "5901")
    @Description("Verify default material for each Process Group")
    public void defaultMaterialTest(ProcessGroupEnum pg, String defaultMaterial, String componentName, String componentExt) {

        resourceFile = FileResourceUtil.getCloudFile(pg, componentName + componentExt);
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build()
        );

        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .processGroup(pg)
                .user(currentUser)
                .build());

        ResponseWrapper<ComponentIteration> getScenarioInfo = componentsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .processGroup(pg)
                .user(currentUser)
                .build());

        assertThat(getScenarioInfo.getResponseEntity().getAnalysisOfScenario().getDigitalFactoryDefaultMaterialName(), is(equalTo(defaultMaterial)));
    }
}
