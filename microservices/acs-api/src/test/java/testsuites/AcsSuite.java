package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.acs.CreateMissingScenarioTests;
import tests.acs.Get2DImageByScenarioIterationKeyTests;
import tests.acs.GetActiveAxesByScenarioIterationKeyTests;
import tests.acs.GetActiveDimensionsByScenarioIterationKeyTests;
import tests.acs.GetArtifactTableInfoTests;
import tests.acs.GetEnabledCurrencyRateVersionsTests;
import tests.acs.GetPartPrimaryProcessGroupsTests;
import tests.acs.GetScenariosInfoTests;
import tests.acs.GetSetArtifactPropertiesTests;
import tests.acs.GetSetDisplayUnitsTests;
import tests.acs.GetSetProductionDefaultsTests;
import tests.acs.GetSetProductionInfoTests;
import tests.acs.GetSetTolerancePolicyDefaultsTests;
import tests.acs.GetSetUserPreferenceByNameTests;
import tests.acs.GetSetUserPreferencesTests;
import tests.acs.GetUnitVariantSettingsTests;
import tests.acs.SaveRoutingSelectionTests;

@ProjectRunID("1347")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
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
