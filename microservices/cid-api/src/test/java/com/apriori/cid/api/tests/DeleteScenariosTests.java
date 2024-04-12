package com.apriori.cid.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_TYPE_EQ;
import static com.apriori.css.api.enums.CssSearch.PAGE_SIZE;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_CREATED_AT_LT;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_CN;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_PUBLISHED_EQ;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.DELETE;

import com.apriori.cid.api.models.response.scenarios.ScenariosDeleteResponse;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class DeleteScenariosTests {

    private final CssComponent cssComponent = new CssComponent();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Tag(DELETE)
    public void markPrivateScenariosForDelete() {
        UserUtil.getUsers().forEach(user -> quickDeleteScenarios(false, user));
    }

    @Test
    @Tag(DELETE)
    public void markPublicScenariosForDelete() {
        quickDeleteScenarios(true, UserUtil.getUser());
    }

    @Test
    public void deletePrivateScenarios() {
        UserUtil.getUsers().forEach(user -> deleteScenarios(false, user));
    }

    @Test
    public void deletePublicScenarios() {
        deleteScenarios(true, UserCredentials.init("cfrith@apriori.com","TestEvent2025!"));
    }

    private void deleteScenarios(Boolean scenarioPublished, UserCredentials user) {
        List<ScenarioItem> assembliesToDelete = searchComponentType("ASSEMBLY", scenarioPublished, user);

        ScenariosDeleteResponse deletedAssemblies = scenariosUtil.deleteScenariosCompleted(assembliesToDelete, user);

        log.info("Number of 'ASSEMBLY(S)' deleted '{}'", deletedAssemblies.getSuccesses().size());

        softAssertions.assertThat(deletedAssemblies.getSuccesses().size()).isEqualTo(assembliesToDelete.size());

        List<ScenarioItem> scenariosToDelete = searchComponentType("PART", scenarioPublished, user);

        ScenariosDeleteResponse deletedScenarios = scenariosUtil.deleteScenariosCompleted(scenariosToDelete, user);

        log.info("Number of 'PART(S)' deleted '{}'", deletedScenarios.getSuccesses().size());

        softAssertions.assertThat(deletedScenarios.getSuccesses().size()).isEqualTo(scenariosToDelete.size());

        softAssertions.assertAll();
    }

    private void quickDeleteScenarios(Boolean scenarioPublished, UserCredentials user) {
        List<ScenarioItem> assembliesToDelete = searchComponentType("ASSEMBLY", scenarioPublished, user);

        scenariosUtil.deleteScenarios(assembliesToDelete, user);

        assembliesToDelete.forEach(assembly -> log.info("Scenario Name of ASSEMBLY marked for deletion '{}'", assembly.getScenarioName()));

        List<ScenarioItem> scenariosToDelete = searchComponentType("PART", scenarioPublished, user);

        scenariosToDelete.forEach(scenario -> log.info("Scenario Name of SCENARIO marked for deletion '{}'", scenario.getScenarioName()));

        scenariosUtil.deleteScenarios(scenariosToDelete, user);
    }

    private List<ScenarioItem> searchComponentType(String componentType, Boolean scenarioPublished, UserCredentials currentUser) {
        final int maxDays = Integer.parseInt(PropertiesContext.get("global.max_days"));
        final int pageSize = Integer.parseInt(PropertiesContext.get("global.page_size"));
        final String scenarioPartName = PropertiesContext.get("global.scenario_name_prefix");

        List<ScenarioItem> scenarioItems = cssComponent.getBaseCssComponents(currentUser, SCENARIO_PUBLISHED_EQ.getKey() + scenarioPublished,
            COMPONENT_TYPE_EQ.getKey() + componentType, SCENARIO_NAME_CN.getKey() + scenarioPartName, PAGE_SIZE.getKey() + pageSize,
            SCENARIO_CREATED_AT_LT.getKey() + LocalDateTime.now().minusDays(maxDays).format(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));

        log.info("Number of '{}(S)' found for deletion '{}'", componentType, scenarioItems.size());

        if (scenarioItems.isEmpty()) {
            throw new RuntimeException("No scenarios found for deletion");
        }

        return scenarioItems;
    }
}