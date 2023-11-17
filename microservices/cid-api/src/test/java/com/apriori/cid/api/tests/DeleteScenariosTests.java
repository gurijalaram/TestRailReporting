package com.apriori.cid.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_TYPE_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_OWNED_BY_EQ;

import com.apriori.cid.api.utils.PeopleUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteScenariosTests {
    private final CssComponent cssComponent = new CssComponent();
    private static final int MAX_DAYS = 7;

    @Test
    public void deleteScenarios() {
        UserUtil.getUsers().forEach(user -> {
            List<ScenarioItem> scenarios = cssComponent.getBaseCssComponents(user, SCENARIO_OWNED_BY_EQ.getKey() + new PeopleUtil().getCurrentUser(user).getIdentity(),
                COMPONENT_TYPE_EQ.getKey() + " PART", "pageSize, 1000");

            List<ScenarioItem> scenariosToDelete = scenarios.stream().filter(o -> o.getScenarioCreatedAt().until(LocalDateTime.now(), ChronoUnit.DAYS) > MAX_DAYS).collect(Collectors.toList());

            scenariosToDelete.forEach(scenario -> new ScenariosUtil().deleteScenario(scenario.getComponentIdentity(), scenario.getScenarioIdentity(), user));
        });
    }
}
