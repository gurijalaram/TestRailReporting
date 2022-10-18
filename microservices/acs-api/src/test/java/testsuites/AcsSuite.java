package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.acs.*;

@ProjectRunID("1347")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AvailableRoutingsTests.class,
    CreateMissingScenarioTests.class,
    GetActiveAxesByScenarioIterationKeyTests.class,
    GetActiveDimensionsByScenarioIterationKeyTests.class,
    GetArtifactTableInfoTests.class,
    GetSetArtifactPropertiesTests.class,
    GetEnabledCurrencyRateVersionsTests.class,
    GetPartPrimaryProcessGroupsTests.class,
    GetScenariosInfoTests.class,
    GetSetDisplayUnitsTests.class,
    GetSetProductionDefaultsTests.class,
    GetSetProductionInfoTests.class,
    GetSetTolerancePolicyDefaultsTests.class,
    GetSetUserPreferenceByNameTests.class,
    GetSetUserPreferencesTests.class,
    GetUnitVariantSettingsTests.class,
    Get2DImageByScenarioIterationKeyTests.class,
    SaveRoutingSelectionTests.class
})

public class AcsSuite {
}
