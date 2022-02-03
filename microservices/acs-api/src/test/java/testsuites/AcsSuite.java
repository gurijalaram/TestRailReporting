package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.CreateMissingScenarioTests;
import tests.GetEnabledCurrencyRateVersionsTests;
import tests.GetPartPrimaryProcessGroupsTests;
import tests.GetScenariosInfoTests;
import tests.GetSetDisplayUnitsTests;
import tests.GetSetProductionDefaultsTests;
import tests.GetSetTolerancePolicyDefaultsTests;
import tests.GetSetUserPreferenceByNameTests;
import tests.GetSetUserPreferencesTests;
import tests.GetUnitVariantSettingsTests;
import testsuites.categories.AcsTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(AcsTest.class)
@Suite.SuiteClasses({
    CreateMissingScenarioTests.class,
    GetScenariosInfoTests.class,
    GetSetDisplayUnitsTests.class,
    GetSetProductionDefaultsTests.class,
    GetSetTolerancePolicyDefaultsTests.class,
    GetUnitVariantSettingsTests.class,
    GetEnabledCurrencyRateVersionsTests.class,
    GetPartPrimaryProcessGroupsTests.class,
    GetSetUserPreferencesTests.class,
    GetSetUserPreferenceByNameTests.class
})

public class AcsSuite {
}
