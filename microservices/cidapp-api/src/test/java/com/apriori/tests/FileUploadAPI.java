package com.apriori.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

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

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @FileParameters(value = "classpath:auto_api_upload.csv", mapper = CustomMapper.class, encoding = "ISO-8859-1")
    @Description("Upload, cost and publish a part")
    public void uploadCostPublish(String componentName, String scenarioName, String processGroup) {

        String[] component = componentName.split("\\.", 2);
        ProcessGroupEnum pg = ProcessGroupEnum.fromString(processGroup);
        File resourceFile = FileResourceUtil.getCloudFile(pg, component[0] + "." + component[1]);
        String mode = "manual";
        String materialName = "Use Default";
        UserCredentials currentUser = UserUtil.getUser();

        Item componentResponse = cidAppTestUtil.postCssComponents(componentName, scenarioName, resourceFile, currentUser);

        cidAppTestUtil.postCostScenario(componentName, scenarioName, componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), pg, DigitalFactoryEnum.APRIORI_USA, mode, materialName, currentUser);

        ResponseWrapper<ScenarioResponse> publishResponse = cidAppTestUtil.postPublishScenario(componentResponse, componentResponse.getComponentIdentity(), componentResponse.getScenarioIdentity(), currentUser);

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
                String scenarioName = index[1].replace("\"", "");
                String processGroup = index[2].replace("\"", "");
                result.add(new Object[] {fileName, scenarioName, processGroup});
            }
            return result.toArray();
        }
    }
}
