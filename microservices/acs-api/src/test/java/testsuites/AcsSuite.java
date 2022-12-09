package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.acs.ActiveAxesByScenarioIterationKeyTests;
import tests.acs.ActiveDimensionsByScenarioIterationKeyTests;
import tests.acs.ArtifactPropertiesTests;
import tests.acs.ArtifactTableInfoTests;
import tests.acs.AvailableRoutingsTests;
import tests.acs.CreateMissingScenarioTests;
import tests.acs.DisplayUnitsTests;
import tests.acs.EnabledCurrencyRateVersionsTests;
import tests.acs.LoadCadFileTests;
import tests.acs.PartPrimaryProcessGroupsTests;
import tests.acs.ProductionDefaultsTests;
import tests.acs.ProductionInfoTests;
import tests.acs.RoutingSelectionTests;
import tests.acs.ScenariosInfoTests;
import tests.acs.TolerancePolicyDefaultsTests;
import tests.acs.TwoDImageByScenarioIterationKeyTests;
import tests.acs.UnitVariantSettingsTests;
import tests.acs.UserPreferenceByNameTests;
import tests.acs.UserPreferencesTests;

@ProjectRunID("1347")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AvailableRoutingsTests.class,
    CreateMissingScenarioTests.class,
    ActiveAxesByScenarioIterationKeyTests.class,
    ActiveDimensionsByScenarioIterationKeyTests.class,
    ArtifactTableInfoTests.class,
    ArtifactPropertiesTests.class,
    EnabledCurrencyRateVersionsTests.class,
    PartPrimaryProcessGroupsTests.class,
    ScenariosInfoTests.class,
    DisplayUnitsTests.class,
    ProductionDefaultsTests.class,
    ProductionInfoTests.class,
    TolerancePolicyDefaultsTests.class,
    UserPreferenceByNameTests.class,
    UserPreferencesTests.class,
    UnitVariantSettingsTests.class,
    TwoDImageByScenarioIterationKeyTests.class,
    RoutingSelectionTests.class,
    LoadCadFileTests.class
})

public class AcsSuite {
}
