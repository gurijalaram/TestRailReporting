package com.apriori.cid.ui.testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ASSEMBLY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.ui.tests.bulkcosting.BulkCostingPageTests;
import com.apriori.cid.ui.tests.compare.ComparisonTests;
import com.apriori.cid.ui.tests.compare.PublicPrivateComparisonTests;
import com.apriori.cid.ui.tests.compare.QuickComparisonTests;
import com.apriori.cid.ui.tests.evaluate.CostAllCadTests;
import com.apriori.cid.ui.tests.evaluate.CostHistoryTests;
import com.apriori.cid.ui.tests.evaluate.CostScenarioTests;
import com.apriori.cid.ui.tests.evaluate.DeleteTests;
import com.apriori.cid.ui.tests.evaluate.MachiningStrategyTests;
import com.apriori.cid.ui.tests.evaluate.ManualCostingTests;
import com.apriori.cid.ui.tests.evaluate.NewScenarioNameTests;
import com.apriori.cid.ui.tests.evaluate.OpenUnknownComponentsTests;
import com.apriori.cid.ui.tests.evaluate.ProcessRoutingTests;
import com.apriori.cid.ui.tests.evaluate.PsoEditTests;
import com.apriori.cid.ui.tests.evaluate.PublishExistingCostedTests;
import com.apriori.cid.ui.tests.evaluate.PublishTests;
import com.apriori.cid.ui.tests.evaluate.SecondaryProcessTests;
import com.apriori.cid.ui.tests.evaluate.SustainabilityTests;
import com.apriori.cid.ui.tests.evaluate.TwoModelMachiningTests;
import com.apriori.cid.ui.tests.evaluate.WatchpointReportTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.AssemblyGroupDeleteTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.ColumnDataTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.EditAssembliesTest;
import com.apriori.cid.ui.tests.evaluate.assemblies.FiltersTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.GroupCostingTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.GroupEditAssemblies;
import com.apriori.cid.ui.tests.evaluate.assemblies.IncludeAndExcludeNestedAssemblyTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.IncludeAndExcludeTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.LargeGroupAssemblyTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.LargeGroupEditAssemblies;
import com.apriori.cid.ui.tests.evaluate.assemblies.LargeGroupEditAssemblies2;
import com.apriori.cid.ui.tests.evaluate.assemblies.MaturityAssemblyAssociationsTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.MissingAssemblyAssociationsTests;
import com.apriori.cid.ui.tests.evaluate.assemblies.PrivatePublicAssemblyAssociationsTests;
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
import com.apriori.cid.ui.tests.explore.ActionComparisonsTests;
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
import com.apriori.cid.ui.tests.help.ReportAnIssueTests;
import com.apriori.cid.ui.tests.settings.DecimalPlaceTests;
import com.apriori.cid.ui.tests.settings.SettingsTests;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@ExcludeTags({SMOKE, SANITY, IGNORE, ASSEMBLY})
@SelectClasses({
    ActionComparisonsTests.class,
    ActionsTests.class,
    AssemblyGroupDeleteTests.class,
    BulkCostingPageTests.class,
    ChangeMaterialSelectionTests.class,
    ColumnDataTests.class,
    ComparisonTests.class,
    CostAllCadTests.class,
    CostHistoryTests.class,
    CostScenarioTests.class,
    DecimalPlaceTests.class,
    DeleteTests.class,
    DFMRiskTests.class,
    DTCCastingTests.class,
    DTCMachiningTests.class,
    DTCPlasticMouldingTests.class,
    EditAssembliesTest.class,
    FilterCriteriaTests.class,
    FiltersTests.class,
    GroupCostingTests.class,
    GroupCostTests.class,
    GroupDeleteTests.class,
    GroupEditAssemblies.class,
    GroupEditTests.class,
    GroupMachineStrategyApplyTests.class,
    GroupPublishTests.class,
    HelpTests.class,
    IncludeAndExcludeNestedAssemblyTests.class,
    IncludeAndExcludeTests.class,
    LargeGroupAssemblyTests.class,
    LargeGroupEditAssemblies.class,
    LargeGroupEditAssemblies2.class,
    MachiningStrategyTests.class,
    ManualCostingTests.class,
    MaterialPMITests.class,
    MaterialStockTests.class,
    MaturityAssemblyAssociationsTests.class,
    MissingAssemblyAssociationsTests.class,
    NewScenarioNameTests.class,
    OpenUnknownComponentsTests.class,
    PartNestingTests.class,
    PreviewPanelTests.class,
    PrivatePublicAssemblyAssociationsTests.class,
    ProcessRoutingTests.class,
    PsoEditTests.class,
    PublicPrivateComparisonTests.class,
    PublishAssembliesTests.class,
    PublishExistingCostedTests.class,
    PublishTests.class,
    QuickComparisonTests.class,
    ReportAnIssueTests.class,
    SecondaryProcessTests.class,
    SettingsTests.class,
    SheetMetalDTCTests.class,
    SustainabilityFieldsTests.class,
    SustainabilityTests.class,
    TableHeadersTests.class,
    ThreadTests.class,
    ToleranceTests.class,
    TwoModelMachiningTests.class,
    UpdateCADFileTests.class,
    UploadAssembliesTests.class,
    UploadComponentTests.class,
    UploadTests.class,
    WatchpointReportTests.class

})
public class CidGuiRegressionSuite {
}
