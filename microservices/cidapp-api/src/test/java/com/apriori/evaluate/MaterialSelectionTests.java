package com.apriori.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

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
            new Object[] {ProcessGroupEnum.ADDITIVE_MANUFACTURING, MaterialNameEnum.ALUMINIUM_ALSI10MG.getMaterialName(), "ADD-LOW-001", ".SLDPRT"},
            new Object[] {ProcessGroupEnum.BAR_TUBE_FAB, MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName(), "AP-000-006", ".step"},
            new Object[] {ProcessGroupEnum.CASTING, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "CastedPart", ".CATPart"},
            new Object[] {ProcessGroupEnum.CASTING_DIE, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "CurvedWall", ".CATPart"},
            new Object[] {ProcessGroupEnum.CASTING_INVESTMENT, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "AP-000-506", ".prt.1"},
            new Object[] {ProcessGroupEnum.CASTING_SAND, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "casting_q5_thinvalve", ".prt"},
            new Object[] {ProcessGroupEnum.FORGING, MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName(), "big ring", ".SLDPRT"},
            new Object[] {ProcessGroupEnum.PLASTIC_MOLDING, MaterialNameEnum.ABS.getMaterialName(), "bolt", ".CATPart"},
            new Object[] {ProcessGroupEnum.POWDER_METAL, MaterialNameEnum.STEEL_F0005.getMaterialName(), "case_31_test_part_6_small", ".prt.2"},
            new Object[] {ProcessGroupEnum.RAPID_PROTOTYPING, "Default", "Plastic moulded cap DFM", ".CATPart"},
            new Object[] {ProcessGroupEnum.ROTO_BLOW_MOLDING, MaterialNameEnum.POLYETHYLENE_HDPE.getMaterialName(), "Plastic moulded cap DFM", ".CATPart"},
            new Object[] {ProcessGroupEnum.SHEET_METAL, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName(), "3571050_cad", ".prt.1"},
            new Object[] {ProcessGroupEnum.SHEET_METAL_HYDROFORMING, MaterialNameEnum.ALUMINIUM_ANSI_2017.getMaterialName(), "FlangedRound", ".SLDPRT"},
            //new Object[] {ProcessGroupEnum.SHEET_METAL_ROLLFORMING, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName(), "", ""},
            new Object[] {ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING, MaterialNameEnum.ALUMINIUM_ANSI_2024.getMaterialName(), "bracket_basic", ".prt"},
            new Object[] {ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName(), "SheetMetal", ".prt"},
            new Object[] {ProcessGroupEnum.SHEET_PLASTIC, MaterialNameEnum.POLYETHYLENE_HDPE_EXTRUSION_SHEET.getMaterialName(), "r151294", ".prt.1"},
            new Object[] {ProcessGroupEnum.STOCK_MACHINING, MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName(), "case_005_flat end mill contouring", ".SLDPRT"}
        };
    }

    @Test
    @Parameters(method = "testParameters")
    @TestCaseName(value = "{method}-{0}")
    @TestRail(id = 5901)
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
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(pg.getProcessGroup())
                    .build())
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
