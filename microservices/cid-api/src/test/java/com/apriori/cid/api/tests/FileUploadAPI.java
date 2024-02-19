package com.apriori.cid.api.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.rules.TestRulesAPI;

import io.qameta.allure.Description;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.File;

@ExtendWith(TestRulesAPI.class)
public class FileUploadAPI {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/auto_api_upload.csv")
    @Description("Upload, cost and publish a part")
    public void uploadCostPublish(String componentName, String processGroup) {

        String[] component = componentName.split("\\.", 2);
        ProcessGroupEnum pg = ProcessGroupEnum.fromString(processGroup);
        File resourceFile = FileResourceUtil.getCloudFile(pg, component[0] + "." + component[1]);
        String scenarioName = ("AutoAPI" + processGroup + component[0]).replace(" ", "");
        String mode = "manual";
        String materialName = "Use Default";
        UserCredentials currentUser = UserUtil.getUser();

        ComponentInfoBuilder scenarioItem = componentsUtil.postComponent(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        scenariosUtil.postGroupCostScenarios(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .costingTemplate(CostingTemplate.builder().processGroupName(pg.getProcessGroup())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .materialMode(mode)
                    .materialName(materialName)
                    .build())
                .user(currentUser)
                .build());

        ScenarioResponse publishResponse = scenariosUtil.postPublishScenario(scenarioItem);

        assertThat(publishResponse.getLastAction(), is("PUBLISH"));
        assertThat(publishResponse.getPublished(), is(true));
    }
}
