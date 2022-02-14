package com.apriori.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.IdentityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class FileUploadAPI {

    final private ComponentsUtil componentsUtil = new ComponentsUtil();
    final private ScenariosUtil scenariosUtil = new ScenariosUtil();

    @Test
    @FileParameters(value = "classpath:auto_api_upload.csv", mapper = CustomMapper.class, encoding = "ISO-8859-1")
    @Description("Upload, cost and publish a part")
    public void uploadCostPublish(String componentName, String processGroup) {

        String[] component = componentName.split("\\.", 2);
        ProcessGroupEnum pg = ProcessGroupEnum.fromString(processGroup);
        File resourceFile = FileResourceUtil.getCloudFile(pg, component[0] + "." + component[1]);
        String scenarioName = ("AutoAPI" + processGroup + component[0]).replace(" ", "");
        String mode = "manual";
        String materialName = "Use Default";
        UserCredentials currentUser = UserUtil.getUser();

        ScenarioItem scenarioItem = componentsUtil.postComponentQueryCSS(ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .user(currentUser)
                .build(),
            resourceFile);

        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(scenarioItem.getComponentIdentity())
                .scenarioId(scenarioItem.getScenarioIdentity())
                .processGroup(pg)
                .digitalFactory(DigitalFactoryEnum.APRIORI_USA)
                .mode(mode)
                .material(materialName)
                .user(currentUser)
                .build());

        ResponseWrapper<ScenarioResponse> publishResponse = scenariosUtil.postPublishScenario(scenarioItem, currentUser);

        assertThat(publishResponse.getResponseEntity().getLastAction(), is("PUBLISH"));
        assertThat(publishResponse.getResponseEntity().getPublished(), is(true));
    }

    public static class CustomMapper extends IdentityMapper {
        @Override
        public Object[] map(Reader reader) {
            Object[] map = super.map(reader);
            List<Object> result = new ArrayList<>();
            for (Object lineObj : map) {
                String line = lineObj.toString();
                String[] index = line.split(",(?=([^\\\"]|\\\"[^\\\"]*\\\")*$)");
                String fileName = index[0].replace("\"", "");
                String processGroup = index[1].replace("\"", "");
                result.add(new Object[] {fileName, processGroup});
            }
            return result.toArray();
        }
    }
}
