package com.apriori.cid.api.tests.evaluate;

import com.apriori.cid.api.models.response.CostingTemplates;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.rules.TestRulesAPI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CostingTemplateTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    public void testCostingTemplateId() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent("Machined Box AMERICAS");

        ComponentInfoBuilder costingInfo = ComponentInfoBuilder.builder()
            .costingTemplate(CostingTemplate.builder()
                .annualVolume(4400)
                .batchSize(7)
                .productionLife(2.5)
                .processGroupName(ProcessGroupEnum.CASTING.getProcessGroup())
                .build())
            .user(component.getUser())
            .build();

        ComponentInfoBuilder costingTemplateId = new ScenariosUtil().postCostingTemplate(costingInfo);

        component.setCostingTemplate(costingTemplateId.getCostingTemplate());

        componentsUtil.postComponent(component);

        scenariosUtil.postGroupCostScenarios(component);

        CostingTemplate costingTemplate = scenariosUtil.getCostingTemplateIdentity(component.getUser(), costingTemplateId.getCostingTemplate().getIdentity());

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

        ComponentInfoBuilder costingTemplateId = new ScenariosUtil().postCostingTemplate(costingInfo);

        softAssertions.assertThat(costingTemplateId.getCostingTemplate().getIdentity()).isNotEmpty();
        softAssertions.assertThat(costingTemplateId.getCostingTemplate().getBatchSize()).isEqualTo(3);

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

        ComponentInfoBuilder costingTemplate = new ScenariosUtil().postCostingTemplate(costingInfo);

        CostingTemplate costingDefaultValues = CostingTemplate.builder().build();

        softAssertions.assertThat(costingTemplate.getCostingTemplate().getAnnualVolume()).isEqualTo(costingDefaultValues.getAnnualVolume());
        softAssertions.assertThat(costingTemplate.getCostingTemplate().getBatchSize()).isEqualTo(costingDefaultValues.getBatchSize());
        softAssertions.assertThat(costingTemplate.getCostingTemplate().getProductionLife()).isEqualTo(costingDefaultValues.getProductionLife());
        softAssertions.assertThat(costingTemplate.getCostingTemplate().getProcessGroupName()).isEqualTo(costingDefaultValues.getProcessGroupName());
        softAssertions.assertThat(costingTemplate.getCostingTemplate().getMaterialName()).isEqualTo(costingDefaultValues.getMaterialName());

        softAssertions.assertAll();
    }
}
