package com.apriori;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.File;

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

        ComponentInfoBuilder scenarioItem = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        scenariosUtil.postCostScenario(
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
