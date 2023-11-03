package testsuites;

import com.apriori.acs.api.tests.ActiveAxesByScenarioIterationKeyTests;
import com.apriori.acs.api.tests.ActiveDimensionsByScenarioIterationKeyTests;
import com.apriori.acs.api.tests.AllMaterialStocksInfoTests;
import com.apriori.acs.api.tests.ArtifactPropertiesTests;
import com.apriori.acs.api.tests.ArtifactTableInfoTests;
import com.apriori.acs.api.tests.AvailableRoutingsTests;
import com.apriori.acs.api.tests.CostResultsTests;
import com.apriori.acs.api.tests.CreateMissingScenarioTests;
import com.apriori.acs.api.tests.DesignGuidanceTests;
import com.apriori.acs.api.tests.DisplayUnitsTests;
import com.apriori.acs.api.tests.EnabledCurrencyRateVersionsTests;
import com.apriori.acs.api.tests.GcdPropertiesTests;
import com.apriori.acs.api.tests.GcdTypesTests;
import com.apriori.acs.api.tests.LoadCadFileTests;
import com.apriori.acs.api.tests.MaterialsInfoTests;
import com.apriori.acs.api.tests.PartPrimaryProcessGroupsTests;
import com.apriori.acs.api.tests.ProductionDefaultsTests;
import com.apriori.acs.api.tests.ProductionInfoTests;
import com.apriori.acs.api.tests.RoutingSelectionTests;
import com.apriori.acs.api.tests.ScenariosInfoTests;
import com.apriori.acs.api.tests.TolerancePolicyDefaultsTests;
import com.apriori.acs.api.tests.TwoDImageByScenarioIterationKeyTests;
import com.apriori.acs.api.tests.UnitVariantSettingsTests;
import com.apriori.acs.api.tests.UserPreferenceByNameTests;
import com.apriori.acs.api.tests.UserPreferencesTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AvailableRoutingsTests.class,
    ActiveAxesByScenarioIterationKeyTests.class,
    ActiveDimensionsByScenarioIterationKeyTests.class,
    AllMaterialStocksInfoTests.class,
    ArtifactTableInfoTests.class,
    ArtifactPropertiesTests.class,
    CostResultsTests.class,
    CreateMissingScenarioTests.class,
    DisplayUnitsTests.class,
    DesignGuidanceTests.class,
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
