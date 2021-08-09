package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class SourceModelInvalidPageTests extends TestBase {

    private EvaluatePage evaluatePage;

    public SourceModelInvalidPageTests(){
        super();
    }

    @Test
    @Description("Source Model Invalid Test")
    public void testInvalidCostModelPart(){
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String componentName = "5436411";
        String scenarioName = "GatlingAutoScenario_6dc7ee1e-2223-46fe-a187-3f27f556c74d";
        String presetFilter = "Private";

        UserCredentials currentUser = UserUtil.getUser();
        CidAppLoginPage loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .selectFilter(presetFilter)
            .openScenario(componentName, scenarioName)
            .selectProcessGroup(processGroupEnum)
            .selectSourcePart()
            .clickSearch(componentName).highlightScenario(componentName, scenarioName).submit(EvaluatePage.class);

        assertThat(evaluatePage.getSourceModelInvalid(), is(equalTo("Source Model Invalid")));

        evaluatePage.clickExplore()
            .selectFilter(presetFilter)
            .openScenario(componentName, scenarioName)
            .selectProcessGroup(processGroupEnum)
            .selectSourcePart()
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .submit(EvaluatePage.class);


    }
}
