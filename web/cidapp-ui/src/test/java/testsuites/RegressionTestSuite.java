package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.compare.ComparisonTests;
import com.apriori.compare.QuickComparisonTests;
import com.apriori.evaluate.CostAllCadTests;
import com.apriori.evaluate.CostHistoryTests;
import com.apriori.evaluate.CostScenarioTests;
import com.apriori.evaluate.DeleteTests;
import com.apriori.evaluate.MachiningStrategyTests;
import com.apriori.evaluate.NewScenarioNameTests;
import com.apriori.evaluate.OpenUnknownComponentsTests;
import com.apriori.evaluate.ProcessGroupsTests;
import com.apriori.evaluate.ProcessRoutingTests;
import com.apriori.evaluate.PsoEditTests;
import com.apriori.evaluate.PublishExistingCostedTests;
import com.apriori.evaluate.PublishTests;
import com.apriori.evaluate.SecondaryProcessTests;
import com.apriori.evaluate.SustainabilityTests;
import com.apriori.evaluate.TwoModelMachiningTests;
import com.apriori.evaluate.assemblies.AssemblyGroupDeleteTests;
import com.apriori.evaluate.assemblies.EditAssembliesTest;
import com.apriori.evaluate.assemblies.FiltersTests;
import com.apriori.evaluate.assemblies.GroupCostingTests;
import com.apriori.evaluate.assemblies.GroupEditAssemblies;
import com.apriori.evaluate.assemblies.IncludeAndExcludeNestedAssemblyTests;
import com.apriori.evaluate.assemblies.IncludeAndExcludeTests;
import com.apriori.evaluate.assemblies.LargeGroupAssemblyTests;
import com.apriori.evaluate.assemblies.MaturityAssemblyAssociationsTests;
import com.apriori.evaluate.assemblies.MissingAssemblyAssociationsTests;
import com.apriori.evaluate.assemblies.UpdateCADFileTests;
import com.apriori.evaluate.assemblies.UploadAssembliesTests;
import com.apriori.evaluate.dtc.DFMRiskTests;
import com.apriori.evaluate.dtc.DTCCastingTests;
import com.apriori.evaluate.dtc.DTCMachiningTests;
import com.apriori.evaluate.dtc.DTCPlasticMouldingTests;
import com.apriori.evaluate.dtc.SheetMetalDTCTests;
import com.apriori.evaluate.dtc.ThreadTests;
import com.apriori.evaluate.dtc.ToleranceTests;
import com.apriori.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.apriori.evaluate.materialutilization.MaterialPMITests;
import com.apriori.evaluate.materialutilization.MaterialStockTests;
import com.apriori.evaluate.materialutilization.PartNestingTests;
import com.apriori.explore.ActionsTests;
import com.apriori.explore.FilterCriteriaTests;
import com.apriori.explore.GroupCostTests;
import com.apriori.explore.GroupDeleteTests;
import com.apriori.explore.GroupEditTests;
import com.apriori.explore.GroupMachineStrategyApplyTests;
import com.apriori.explore.GroupPublishTests;
import com.apriori.explore.PreviewPanelTests;
import com.apriori.explore.SustainabilityFieldsTests;
import com.apriori.explore.TableHeadersTests;
import com.apriori.explore.UploadComponentTests;
import com.apriori.explore.UploadTests;
import com.apriori.help.HelpTests;
import com.apriori.settings.DecimalPlaceTests;
import com.apriori.settings.SettingsTests;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@ExcludeTags({SMOKE, SANITY, IGNORE, EXTENDED_REGRESSION})
@SelectClasses({
    EditAssembliesTest.class,
    GroupCostingTests.class,
    CostAllCadTests.class,
    CostScenarioTests.class,
    NewScenarioNameTests.class,
    ProcessGroupsTests.class,
    SecondaryProcessTests.class,
    ChangeMaterialSelectionTests.class,
    MaterialStockTests.class,
    UploadComponentTests.class,
    UploadTests.class,
    HelpTests.class,
    DecimalPlaceTests.class,
    UploadAssembliesTests.class,
    PublishExistingCostedTests.class,
    PreviewPanelTests.class,
    DFMRiskTests.class,
    DeleteTests.class,
    PublishTests.class,
    SheetMetalDTCTests.class,
    DTCPlasticMouldingTests.class,
    ActionsTests.class,
    TableHeadersTests.class,
    DTCMachiningTests.class,
    DTCCastingTests.class,
    TwoModelMachiningTests.class,
    MaterialPMITests.class,
    SettingsTests.class,
    ToleranceTests.class,
    ComparisonTests.class,
    QuickComparisonTests.class,
    ThreadTests.class,
    PartNestingTests.class,
    PsoEditTests.class,
    IncludeAndExcludeTests.class,
    GroupEditAssemblies.class,
    LargeGroupAssemblyTests.class,
    IncludeAndExcludeNestedAssemblyTests.class,
    FiltersTests.class,
    FilterCriteriaTests.class,
    GroupEditTests.class,
    GroupDeleteTests.class,
    GroupCostTests.class,
    OpenUnknownComponentsTests.class,
    ProcessRoutingTests.class,
    GroupPublishTests.class,
    MachiningStrategyTests.class,
    GroupMachineStrategyApplyTests.class,
    UpdateCADFileTests.class,
    AssemblyGroupDeleteTests.class,
    MaturityAssemblyAssociationsTests.class,
    MissingAssemblyAssociationsTests.class,
    SustainabilityTests.class,
    SustainabilityFieldsTests.class,
    CostHistoryTests.class
})
public class RegressionTestSuite {
}
