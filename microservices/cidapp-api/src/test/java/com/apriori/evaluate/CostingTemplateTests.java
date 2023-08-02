package com.apriori.evaluate;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.models.response.CostingTemplates;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class CostingTemplateTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    public void testCostingTemplateId() {
        final ProcessGroupEnum processGroup = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroup, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder costingInfo = ComponentInfoBuilder.builder()
            .costingTemplate(CostingTemplate.builder()
                .annualVolume(4400)
                .batchSize(7)
                .productionLife(2.5)
                .processGroupName(ProcessGroupEnum.CASTING.getProcessGroup())
                .build())
            .user(currentUser)
            .build();

        CostingTemplate costingTemplateId = new ScenariosUtil().postCostingTemplate(costingInfo);

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .costingTemplate(costingTemplateId)
                .build());

        scenariosUtil.postCostScenario(componentResponse);

        CostingTemplate costingTemplate = scenariosUtil.getCostingTemplateIdentity(currentUser, costingTemplateId.getIdentity());

        softAssertions.assertThat(costingTemplate.getAnnualVolume()).isEqualTo(4400);
        softAssertions.assertThat(costingTemplate.getBatchSize()).isEqualTo(7);
        softAssertions.assertThat(costingTemplate.getProductionLife()).isEqualTo(2.5);
        softAssertions.assertThat(costingTemplate.getProductionLife()).isNotEqualTo(9.5);
        softAssertions.assertThat(costingTemplate.getProcessGroupName()).isEqualTo(ProcessGroupEnum.CASTING.getProcessGroup());

        softAssertions.assertAll();
    }

    @Test
    public void testCostingTemplate() {
        final UserCredentials currentUser = UserUtil.getUser();

        ComponentInfoBuilder costingInfo = ComponentInfoBuilder.builder()
            .costingTemplate(CostingTemplate.builder()
                .annualVolume(4400)
                .batchSize(3)
                .productionLife(2.5)
                .processGroupName(ProcessGroupEnum.CASTING.getProcessGroup())
                .build())
            .user(currentUser)
            .build();

        CostingTemplate costingTemplateId = new ScenariosUtil().postCostingTemplate(costingInfo);

        softAssertions.assertThat(costingTemplateId.getIdentity()).isNotEmpty();
        softAssertions.assertThat(costingTemplateId.getBatchSize()).isEqualTo(3);

        softAssertions.assertAll();
    }

    @Test
    public void testCostingTemplates() {
        final UserCredentials currentUser = UserUtil.getUser();

        ResponseWrapper<CostingTemplates> costingTemplates = new ScenariosUtil().getCostingTemplates(currentUser);

        softAssertions.assertThat(costingTemplates.getResponseEntity().getItems().size()).isGreaterThan(0);
        costingTemplates.getResponseEntity().getItems().forEach(o -> softAssertions.assertThat(o.getIdentity()).isNotEmpty());

        softAssertions.assertAll();
    }

    @Test
    public void testCostingTemplateDefaultValues() {
        final UserCredentials currentUser = UserUtil.getUser();

        ComponentInfoBuilder costingInfo = ComponentInfoBuilder.builder()
            .costingTemplate(CostingTemplate.builder().build())
            .user(currentUser)
            .build();

        CostingTemplate costingTemplate = new ScenariosUtil().postCostingTemplate(costingInfo);

        CostingTemplate costingDefaultValues = CostingTemplate.builder().build();

        softAssertions.assertThat(costingTemplate.getAnnualVolume()).isEqualTo(costingDefaultValues.getAnnualVolume());
        softAssertions.assertThat(costingTemplate.getBatchSize()).isEqualTo(costingDefaultValues.getBatchSize());
        softAssertions.assertThat(costingTemplate.getProductionLife()).isEqualTo(costingDefaultValues.getProductionLife());
        softAssertions.assertThat(costingTemplate.getProcessGroupName()).isEqualTo(costingDefaultValues.getProcessGroupName());
        softAssertions.assertThat(costingTemplate.getMaterialName()).isEqualTo(costingDefaultValues.getMaterialName());

        softAssertions.assertAll();
    }
}
