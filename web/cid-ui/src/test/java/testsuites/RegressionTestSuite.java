package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.ui.tests.bulkcosting.ProjectsPageTests;
import com.apriori.cid.ui.tests.compare.ComparisonTests;
import com.apriori.cid.ui.tests.compare.QuickComparisonTests;
import com.apriori.cid.ui.tests.evaluate.CostAllCadTests;
import com.apriori.cid.ui.tests.evaluate.CostHistoryTests;
import com.apriori.cid.ui.tests.evaluate.CostScenarioTests;
import com.apriori.cid.ui.tests.evaluate.DeleteTests;
import com.apriori.cid.ui.tests.evaluate.MachiningStrategyTests;
import com.apriori.cid.ui.tests.evaluate.NewScenarioNameTests;
import com.apriori.cid.ui.tests.evaluate.OpenUnknownComponentsTests;
import com.apriori.cid.ui.tests.evaluate.ProcessGroupsTests;
import com.apriori.cid.ui.tests.evaluate.ProcessRoutingTests;
import com.apriori.cid.ui.tests.evaluate.PsoEditTests;
import com.apriori.cid.ui.tests.evaluate.PublishExistingCostedTests;
import com.apriori.cid.ui.tests.evaluate.PublishTests;
import com.apriori.cid.ui.tests.evaluate.SecondaryProcessTests;
import com.apriori.cid.ui.tests.evaluate.SustainabilityTests;
import com.apriori.cid.ui.tests.evaluate.TwoModelMachiningTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.AssemblyGroupDeleteTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.EditAssembliesTest;
import com.apriori.cid.ui.tests.evaluate.assemblies.FiltersTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.GroupCostingTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.GroupEditAssemblies;
import com.apriori.cid.ui.tests.evaluate.assemblies.IncludeAndExcludeNestedAssemblyTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.IncludeAndExcludeTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.LargeGroupAssemblyTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.MaturityAssemblyAssociationsTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.MissingAssemblyAssociationsTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.PublishAssembliesTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.UpdateCADFileTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.UploadAssembliesTests;
import com.apriori.cid.ui.tests.evaluate.dtc.DFMRiskTests;
import com.apriori.cid.ui.tests.evaluate.dtc.DTCCastingTests;
import com.apriori.cid.ui.tests.evaluate.dtc.DTCMachiningTests;
import com.apriori.cid.ui.tests.evaluate.dtc.DTCPlasticMouldingTests;
import com.apriori.cid.ui.tests.evaluate.dtc.SheetMetalDTCTests;
import com.apriori.cid.ui.tests.evaluate.dtc.ThreadTests;
import com.apriori.cid.ui.tests.evaluate.dtc.ToleranceTests;
import com.apriori.cid.ui.tests.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.apriori.cid.ui.tests.evaluate.materialutilization.MaterialPMITests;
import com.apriori.cid.ui.tests.evaluate.materialutilization.MaterialStockTests;
import com.apriori.cid.ui.tests.evaluate.materialutilization.PartNestingTests;
import com.apriori.cid.ui.tests.explore.ActionsTests;
import com.apriori.cid.ui.tests.explore.FilterCriteriaTests;
import com.apriori.cid.ui.tests.explore.GroupCostTests;
import com.apriori.cid.ui.tests.explore.GroupDeleteTests;
import com.apriori.cid.ui.tests.explore.GroupEditTests;
import com.apriori.cid.ui.tests.explore.GroupMachineStrategyApplyTests;
import com.apriori.cid.ui.tests.explore.GroupPublishTests;
import com.apriori.cid.ui.tests.explore.PreviewPanelTests;
import com.apriori.cid.ui.tests.explore.SustainabilityFieldsTests;
import com.apriori.cid.ui.tests.explore.TableHeadersTests;
import com.apriori.cid.ui.tests.explore.UploadComponentTests;
import com.apriori.cid.ui.tests.explore.UploadTests;
import com.apriori.cid.ui.tests.help.HelpTests;
import com.apriori.cid.ui.tests.settings.DecimalPlaceTests;
import com.apriori.cid.ui.tests.settings.SettingsTests;

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
    CostHistoryTests.class,
    PublishAssembliesTests.class,
    ProjectsPageTests.class
})
public class RegressionTestSuite {
}
