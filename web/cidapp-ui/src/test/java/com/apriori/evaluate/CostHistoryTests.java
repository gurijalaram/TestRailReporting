package com.apriori.evaluate;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.models.response.SecondaryDigitalFactories;
import com.apriori.models.response.SecondaryProcesses;
import com.apriori.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.pageobjects.evaluate.CostHistoryPage;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

public class CostHistoryTests extends TestBaseUI {
    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private CostHistoryPage costHistoryPage;

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private SoftAssertions softAssertions = new SoftAssertions();
    private UserCredentials currentUser;

    public CostHistoryTests() {
        super();
    }

    @Test
    @TestRail(id = {28442, 28443, 28444, 28447})
    @Description("Verify Cost History available")
    public void testCostHistory() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder castingPart = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .resourceFile(resourceFile)
                .scenarioName(scenarioName)
                .processGroup(processGroupEnum)
                .user(currentUser)
                .build());

        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
                .annualVolume(4096)
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .materialName(MaterialNameEnum.COPPER_UNS_C11000.getMaterialName())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .secondaryProcesses(SecondaryProcesses.builder()
                    .heatTreatment(Arrays.asList("Certification"))
                    .build())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .vpeName(DigitalFactoryEnum.APRIORI_IRELAND.getDigitalFactory())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .vpeName(DigitalFactoryEnum.APRIORI_WESTERN_EUROPE.getDigitalFactory())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .annualVolume(289)
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .batchSize(275)
                .build());
        scenariosUtil.postCostScenario(castingPart);
        SecondaryDigitalFactories secondaryDF = new SecondaryDigitalFactories();
        secondaryDF.setHeatTreatment(DigitalFactoryEnum.APRIORI_FINLAND.getDigitalFactory());
        secondaryDF.setMachining(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());
        secondaryDF.setSurfaceTreatment(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory());
        secondaryDF.setOtherSecondaryProcesses(DigitalFactoryEnum.APRIORI_CANADA.getDigitalFactory());
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories(Boolean.FALSE)
                .secondaryDigitalFactories(secondaryDF)
                .build());
        scenariosUtil.postCostScenario(castingPart);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName());

        softAssertions.assertThat(evaluatePage.isProgressButtonEnabled()).as("Verify Progress button disabled before initial cost").isTrue();

        costHistoryPage = evaluatePage.clickProgress();
        ChangeSummaryPage changeSummary = costHistoryPage.openChangeSummary(2);

        softAssertions.assertThat(changeSummary.changedFromHeader()).as("Verify Left Column is Iteration 1").isEqualTo("Iteration 1");
        softAssertions.assertThat(changeSummary.changedToHeader()).as("Verify Right Column is Iteration 1").isEqualTo("Iteration 2");

        softAssertions.assertThat(changeSummary.getChangedFrom("Annual Volume")).as("Verify changed from in Annual Volume").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Annual Volume")).as("Verify changed to in Annual Volume").isEqualTo("500");
        softAssertions.assertThat(changeSummary.getChangedFrom("Batch Size")).as("Verify changed from in Batch Size").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Batch Size")).as("Verify changed to in Batch Size").isEqualTo("458");
        softAssertions.assertThat(changeSummary.getChangedFrom("Machining Mode")).as("Verify changed from in Machining Mode").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Machining Mode")).as("Verify changed to in Machining Mode").isEqualTo("MAY_BE_MACHINED");
        softAssertions.assertThat(changeSummary.getChangedFrom("Material")).as("Verify changed from in Material").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Material"))
            .as("Verify changed to in Material").isEqualTo(MaterialNameEnum.STEEL_HOT_WORKED_AISI1010.getMaterialName());
        softAssertions.assertThat(changeSummary.getChangedFrom("Process Group")).as("Verify changed from in Process Group").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Process Group"))
            .as("Verify changed to in Process Group").isEqualTo(ProcessGroupEnum.CASTING_DIE.getProcessGroup());
        softAssertions.assertThat(changeSummary.getChangedFrom("Years")).as("Verify changed from in Years").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Years")).as("Verify changed to in Years").isEqualTo("5");
        softAssertions.assertThat(changeSummary.getChangedFrom("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed from in Primary Digital Factory = Secondary Digital Factory").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed to in Primary Digital Factory = Secondary Digital Factory").isEqualTo("TRUE");
        softAssertions.assertThat(changeSummary.getChangedFrom("Digital Factory")).as("Verify changed from in Digital Factory").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Digital Factory")).as("Verify changed to in Digital Factory").isEqualTo("aPriori USA");

        softAssertions.assertThat(costHistoryPage.iterationCount()).as("count").isEqualTo(11);

        changeSummary = changeSummary.close()
            .openChangeSummary(6);

        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Processes-Heat Treatment")).as("Verify changed from in Heat Treatment").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Processes-Heat Treatment")).as("Verify changed to in Heat Treatment").isEqualTo("Certification");

        changeSummary = changeSummary.close()
            .openChangeSummary(7);

        softAssertions.assertThat(changeSummary.getChangedFrom("Digital Factory"))
            .as("Verify changed from in Digital Factory").isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedTo("Digital Factory"))
            .as("Verify changed to in Digital Factory").isEqualTo(DigitalFactoryEnum.APRIORI_IRELAND.getDigitalFactory());

        changeSummary = changeSummary.close()
            .openChangeSummary(11);

        softAssertions.assertThat(changeSummary.getChangedFrom("Batch Size")).as("Verify changed from in Batch Size").isEqualTo("275");
        softAssertions.assertThat(changeSummary.getChangedTo("Batch Size")).as("Verify changed to in Batch Size").isEqualTo("458");
        softAssertions.assertThat(changeSummary.getChangedFrom("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed from in Primary Digital Factory = Secondary Digital Factory").isEqualTo("TRUE");
        softAssertions.assertThat(changeSummary.getChangedTo("Primary Digital Factory = Secondary Digital Factory"))
            .as("Verify changed to in Primary Digital Factory = Secondary Digital Factory").isEqualTo("FALSE");
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Heat Treatment"))
            .as("Verify changed from in Secondary Digital Factories-Heat Treatment").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Heat Treatment"))
            .as("Verify changed to in Secondary Digital Factories-Heat Treatment").isEqualTo(DigitalFactoryEnum.APRIORI_FINLAND.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Machining"))
            .as("Verify changed from in Secondary Digital Factories-Machining").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Machining"))
            .as("Verify changed to in Secondary Digital Factories-Machining").isEqualTo(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Other Secondary Processes"))
            .as("Verify changed from in Secondary Digital Factories-Other Secondary Processes").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Other Secondary Processes"))
            .as("Verify changed to in Secondary Digital Factories-Other Secondary Processes").isEqualTo(DigitalFactoryEnum.APRIORI_CANADA.getDigitalFactory());
        softAssertions.assertThat(changeSummary.getChangedFrom("Secondary Digital Factories-Surface Treatment"))
            .as("Verify changed from in Secondary Digital Factories-Surface Treatment").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Secondary Digital Factories-Surface Treatment"))
            .as("Verify changed to in Secondary Digital Factories-Surface Treatment").isEqualTo(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory());

        softAssertions.assertAll();
    }
}
