package com.apriori.cid.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_TYPE_EQ;
import static com.apriori.css.api.enums.CssSearch.PAGE_SIZE;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_CREATED_AT_LT;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_CN;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_PUBLISHED_EQ;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class DeleteScenariosTests {
    private static final int MAX_DAYS = 7;
    private static final int SIZE_OF_PAGE = 1000;
    private final CssComponent cssComponent = new CssComponent();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();

    @Test
    public void deleteScenarios() {
        UserUtil.getUsers().forEach(user -> {
            List<ScenarioItem> assembliesToDelete = searchComponentType("ASSEMBLY", user);

            ScenariosDeleteResponse deletedAssemblies = scenariosUtil.deleteScenarios(assembliesToDelete, user);

            softAssertions.assertThat(deletedAssemblies.getSuccesses().size()).isEqualTo(assembliesToDelete.size());

            List<ScenarioItem> scenariosToDelete = searchComponentType("PART", user);

            ScenariosDeleteResponse deletedScenarios = scenariosUtil.deleteScenarios(scenariosToDelete, user);

            softAssertions.assertThat(deletedScenarios.getSuccesses().size()).isEqualTo(scenariosToDelete.size());

            softAssertions.assertAll();
        });
    }

    private List<ScenarioItem> searchComponentType(String componentType, UserCredentials currentUser) {
        return cssComponent.getBaseCssComponents(currentUser, SCENARIO_PUBLISHED_EQ.getKey() + false,
            COMPONENT_TYPE_EQ.getKey() + componentType, SCENARIO_NAME_CN.getKey() + "AutoScenario", PAGE_SIZE.getKey() + SIZE_OF_PAGE,
            SCENARIO_CREATED_AT_LT.getKey() + LocalDateTime.now().minusDays(MAX_DAYS).format(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ));
    }
}