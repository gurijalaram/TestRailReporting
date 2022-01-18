package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.builder.ScenarioRepresentationBuilder;
import com.apriori.cidappapi.entity.response.scenarios.CopyScenarioResponse;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;

public class ScenariosTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    public void testCopyScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String filename  = "oldham.asm.1";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "OLDHAM";
        final UserCredentials currentUser = UserUtil.getUser();
//        File resourceFile = FileResourceUtil.getCloudCadFile(processGroupEnum, filename);
        File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);

        Item postComponentResponse = cidAppTestUtil.postCssComponent(componentName, scenarioName, resourceFile, currentUser);
        String componentIdentity = postComponentResponse.getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getScenarioIdentity();

        ResponseWrapper<ScenarioResponse> preCostState = cidAppTestUtil.getScenarioRepresentation("processing", componentIdentity, scenarioIdentity, currentUser);
        assertThat(preCostState.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

//        ResponseWrapper<CopyScenarioResponse> copyScenarioResponse = cidAppTestUtil.postCopyScenario(componentIdentity, scenarioIdentity, currentUser);
//        assertThat(copyScenarioResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        cidAppTestUtil.postCopyScenario(
            ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .componentId(postComponentResponse.getComponentIdentity())
            .scenarioId(postComponentResponse.getScenarioIdentity())
            .processGroup(processGroupEnum)
            .user(currentUser)
            .build());

        ResponseWrapper<ScenarioResponse> scenarioRepresentation = cidAppTestUtil.getScenarioRepresentation(
            ScenarioRepresentationBuilder.builder()
                .item(postComponentResponse)
                .user(currentUser)
                .build());

        assertThat(scenarioRepresentation.getResponseEntity().getScenarioState(), Matchers.is(Matchers.equalTo(NewCostingLabelEnum.NOT_COSTED)));

    }
}
