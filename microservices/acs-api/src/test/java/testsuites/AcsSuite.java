package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.acs.ActiveAxesByScenarioIterationKeyTests;
import tests.acs.ActiveDimensionsByScenarioIterationKeyTests;
import tests.acs.AllMaterialStocksInfoTests;
import tests.acs.ArtifactPropertiesTests;
import tests.acs.ArtifactTableInfoTests;
import tests.acs.AvailableRoutingsTests;
import tests.acs.CostResultsTests;
import tests.acs.CreateMissingScenarioTests;
import tests.acs.DisplayUnitsTests;
import tests.acs.EnabledCurrencyRateVersionsTests;
import tests.acs.GcdPropertiesTests;
import tests.acs.GcdTypesTests;
import tests.acs.LoadCadFileTests;
import tests.acs.MaterialsInfoTests;
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
    ActiveAxesByScenarioIterationKeyTests.class,
    ActiveDimensionsByScenarioIterationKeyTests.class,
    AllMaterialStocksInfoTests.class,
    ArtifactTableInfoTests.class,
    ArtifactPropertiesTests.class,
    CostResultsTests.class,
    CreateMissingScenarioTests.class,
    DisplayUnitsTests.class,
    EnabledCurrencyRateVersionsTests.class,
    GcdTypesTests.class,
    GcdPropertiesTests.class,
    LoadCadFileTests.class,
    MaterialsInfoTests.class,
    PartPrimaryProcessGroupsTests.class,
    ProductionDefaultsTests.class,
    ProductionInfoTests.class,
    RoutingSelectionTests.class,
    ScenariosInfoTests.class,
    TolerancePolicyDefaultsTests.class,
    TwoDImageByScenarioIterationKeyTests.class,
    UnitVariantSettingsTests.class,
    UserPreferenceByNameTests.class,
    UserPreferencesTests.class
})

public class AcsSuite {
}
