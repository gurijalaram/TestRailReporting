package com.apriori.cir.ui.tests.ootbreports.scenariocomparison;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.apriori.cir.ui.tests.inputcontrols.InputControlsTests;
import com.apriori.shared.util.enums.ListNameEnum;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ScenarioComparisonReportTests extends TestBaseUI {
    private InputControlsTests inputControlsTests;

    public ScenarioComparisonReportTests() {
        super();
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7667")
    @TestRail(id = {7667})
    @Description("Verify Created By input control buttons work - Scenario Comparison Report")
    public void testCreatedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName(),
            "",
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Tag(REPORTS)
    @TmsLink("7666")
    @TestRail(id = {7666})
    @Description("Verify Last Modified By input control buttons work - Scenario Comparison Report")
    public void testLastModifiedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.SCENARIO_COMPARISON.getReportName(),
            "",
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }
}
