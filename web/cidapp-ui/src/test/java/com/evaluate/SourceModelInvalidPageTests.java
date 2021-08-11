package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.SourceModelInvalidPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class SourceModelInvalidPageTests extends TestBase {

    private SourceModelInvalidPage sourceModelInvalidPage;

    public SourceModelInvalidPageTests(){
        super();
    }

    @Test
    @Description("Source Model Invalid Test")
    public void testInvalidCostModelPart(){
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.TWO_MODEL_MACHINING;

        String componentName = "TRANSFER DIE DEMO 1";
        String scenarioName = "Transfer1";
        String sourceModel = "TRANSFER DIE DEMO 2";
        String scenarioModel = "Transfer2";
        String presetFilter = "Private";

        UserCredentials currentUser = UserUtil.getUser();
        CidAppLoginPage loginPage = new CidAppLoginPage(driver);
        sourceModelInvalidPage = loginPage.login(currentUser)
            .filter()
            .add()
            .inputName("Not Costed")
            .addCriteriaWithOption("State", "In", "Not Costed").submit(ExplorePage.class)
            .openScenario(componentName, scenarioName)
            .selectProcessGroup(processGroupEnum)
            .selectSourcePart()
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName)
            .submit(SourceModelInvalidPage.class);

        assertThat(sourceModelInvalidPage.getSourceModelInvalid(), is(equalTo("Source Model Invalid")));

        sourceModelInvalidPage.clickIgnore()
            .selectSourcePart()
            .clickSearch(sourceModel)
            .highlightScenario(sourceModel, scenarioModel)
            .submit(SourceModelInvalidPage.class)
            .clickFixSource();
    }
}
