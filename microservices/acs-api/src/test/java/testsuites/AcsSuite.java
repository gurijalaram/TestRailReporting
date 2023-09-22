package testsuites;

import com.apriori.ActiveAxesByScenarioIterationKeyTests;
import com.apriori.ActiveDimensionsByScenarioIterationKeyTests;
import com.apriori.AllMaterialStocksInfoTests;
import com.apriori.ArtifactPropertiesTests;
import com.apriori.ArtifactTableInfoTests;
import com.apriori.AvailableRoutingsTests;
import com.apriori.CostResultsTests;
import com.apriori.CreateMissingScenarioTests;
import com.apriori.DesignGuidanceTests;
import com.apriori.DisplayUnitsTests;
import com.apriori.EnabledCurrencyRateVersionsTests;
import com.apriori.GcdPropertiesTests;
import com.apriori.GcdTypesTests;
import com.apriori.LoadCadFileTests;
import com.apriori.MaterialsInfoTests;
import com.apriori.PartPrimaryProcessGroupsTests;
import com.apriori.ProductionDefaultsTests;
import com.apriori.ProductionInfoTests;
import com.apriori.RoutingSelectionTests;
import com.apriori.ScenariosInfoTests;
import com.apriori.TolerancePolicyDefaultsTests;
import com.apriori.TwoDImageByScenarioIterationKeyTests;
import com.apriori.UnitVariantSettingsTests;
import com.apriori.UserPreferenceByNameTests;
import com.apriori.UserPreferencesTests;

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
