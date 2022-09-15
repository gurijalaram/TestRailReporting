package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
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
import testsuites.categories.AcsTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(AcsTest.class)
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
