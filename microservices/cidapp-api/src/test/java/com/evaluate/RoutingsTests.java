package com.evaluate;

import com.apriori.cidappapi.entity.response.scenarios.Routings;
import com.apriori.cidappapi.utils.DataCreationUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class RoutingsTests {
        private final ScenariosUtil scenariosUtil = new ScenariosUtil();
        private SoftAssertions softAssertions;

        @Test
        @Description("Test routings")
        public void cadFormatSLDPRT() {

            final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
            final String componentName = "Machined Box AMERICAS";
            final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
            final UserCredentials currentUser = UserUtil.getUser();
            final String scenarioName = new GenerateStringUtil().generateScenarioName();

            ScenarioItem scenarioItem = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

            Routings routings = scenariosUtil.getRoutings(currentUser, scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity()).getResponseEntity();

            softAssertions = new SoftAssertions();

            softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);

            routings.getItems().forEach(routing -> {
                softAssertions.assertThat(routing.getDigitalFactoryName()).isNotEmpty();
                softAssertions.assertThat(routing.getDisplayName()).isNotEmpty();
                softAssertions.assertThat(routing.getName()).isNotEmpty();
                softAssertions.assertThat(routing.getProcessGroupName()).isNotEmpty();
            });

            softAssertions.assertAll();
        }
}
