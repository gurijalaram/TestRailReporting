package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.CreateMissingScenarioTests;
import tests.GetEnabledCurrencyRateVersionsTests;
import tests.GetSetDisplayUnitsTests;
import tests.GetUnitVariantSettingsTests;
import testsuites.categories.AcsTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(AcsTest.class)
@Suite.SuiteClasses({
        CreateMissingScenarioTests.class,
        GetEnabledCurrencyRateVersionsTests.class,
        GetSetDisplayUnitsTests.class,
        GetUnitVariantSettingsTests.class
})

public class AcsSuite {
}
