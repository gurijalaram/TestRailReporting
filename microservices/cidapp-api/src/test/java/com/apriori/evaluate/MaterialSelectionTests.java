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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

public class MaterialSelectionTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private IterationsUtil iterationUtil = new IterationsUtil();

    UserCredentials currentUser;
    private File resourceFile;

    static Stream<Arguments> materialData() {
        return Stream.of(
            Arguments.of(ProcessGroupEnum.ADDITIVE_MANUFACTURING, MaterialNameEnum.ALUMINIUM_ALSI10MG.getMaterialName(), "ADD-LOW-001", ".SLDPRT"),
            Arguments.of(ProcessGroupEnum.BAR_TUBE_FAB, MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName(), "AP-000-006", ".step"),
            Arguments.of(ProcessGroupEnum.CASTING, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "CastedPart", ".CATPart"),
            Arguments.of(ProcessGroupEnum.CASTING_DIE, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "CurvedWall", ".CATPart"),
            Arguments.of(ProcessGroupEnum.CASTING_INVESTMENT, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "AP-000-506", ".prt.1"),
            Arguments.of(ProcessGroupEnum.CASTING_SAND, MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName(), "casting_q5_thinvalve", ".prt"),
            Arguments.of(ProcessGroupEnum.FORGING, MaterialNameEnum.STEEL_COLD_WORKED_AISI1010.getMaterialName(), "big ring", ".SLDPRT"),
            Arguments.of(ProcessGroupEnum.PLASTIC_MOLDING, MaterialNameEnum.ABS.getMaterialName(), "bolt", ".CATPart"),
            Arguments.of(ProcessGroupEnum.POWDER_METAL, MaterialNameEnum.STEEL_F0005.getMaterialName(), "case_31_test_part_6_small", ".prt.2"),
            Arguments.of(ProcessGroupEnum.RAPID_PROTOTYPING, "Default", "Plastic moulded cap DFM", ".CATPart"),
            Arguments.of(ProcessGroupEnum.ROTO_BLOW_MOLDING, MaterialNameEnum.POLYETHYLENE_HDPE.getMaterialName(), "Plastic moulded cap DFM", ".CATPart"),
            Arguments.of(ProcessGroupEnum.SHEET_METAL, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName(), "3571050_cad", ".prt.1"),
            Arguments.of(ProcessGroupEnum.SHEET_METAL_HYDROFORMING, MaterialNameEnum.ALUMINIUM_ANSI_2017.getMaterialName(), "FlangedRound", ".SLDPRT"),
            //Arguments.of(ProcessGroupEnum.SHEET_METAL_ROLLFORMING, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName(), "", ""},
            Arguments.of(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING, MaterialNameEnum.ALUMINIUM_ANSI_2024.getMaterialName(), "bracket_basic", ".prt"),
            Arguments.of(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE, MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName(), "SheetMetal", ".prt"),
            Arguments.of(ProcessGroupEnum.SHEET_PLASTIC, MaterialNameEnum.POLYETHYLENE_HDPE_EXTRUSION_SHEET.getMaterialName(), "r151294", ".prt.1"),
            Arguments.of(ProcessGroupEnum.STOCK_MACHINING, MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName(), "case_005_flat end mill contouring", ".SLDPRT"));
    }

    @ParameterizedTest
    @MethodSource("materialData")
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
