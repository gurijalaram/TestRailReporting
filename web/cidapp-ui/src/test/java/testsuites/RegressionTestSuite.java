package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.compare.ComparisonTests;
import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.DeleteTests;
import com.evaluate.MachiningStrategyTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.OpenUnknownComponentsTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.ProcessRoutingTests;
import com.evaluate.PsoEditTests;
import com.evaluate.PublishExistingCostedTests;
import com.evaluate.PublishTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.TwoModelMachiningTests;
import com.evaluate.assemblies.AssemblyAssociations;
import com.evaluate.assemblies.EditAssembliesTest;
import com.evaluate.assemblies.FiltersTests;
import com.evaluate.assemblies.GroupCostingTests;
import com.evaluate.assemblies.GroupEditAssemblies;
import com.evaluate.assemblies.IncludeAndExcludeNestedAssemblyTests;
import com.evaluate.assemblies.IncludeAndExcludeTests;
import com.evaluate.assemblies.LargeGroupAssemblyTests;
import com.evaluate.assemblies.UpdateCADFileTests;
import com.evaluate.assemblies.UploadAssembliesTests;
import com.evaluate.dtc.DFMRiskTests;
import com.evaluate.dtc.DTCCastingTests;
import com.evaluate.dtc.DTCMachiningTests;
import com.evaluate.dtc.DTCPlasticMouldingTests;
import com.evaluate.dtc.SheetMetalDTCTests;
import com.evaluate.dtc.ThreadTests;
import com.evaluate.dtc.ToleranceTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.MaterialPMITests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.evaluate.materialutilization.PartNestingTests;
import com.explore.ActionsTests;
import com.explore.FilterCriteriaTests;
import com.explore.GroupCostTests;
import com.explore.GroupDeleteTests;
import com.explore.GroupEditTests;
import com.explore.GroupMachineStrategyApplyTests;
import com.explore.GroupPublishTests;
import com.explore.PreviewPanelTests;
import com.explore.TableHeadersTests;
import com.explore.UploadComponentTests;
import com.explore.UploadTests;
import com.help.HelpTests;
import com.settings.DecimalPlaceTests;
import com.settings.SettingsTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("768")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory({SmokeTests.class, SanityTests.class, IgnoreTests.class})
@Suite.SuiteClasses({
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
    AssemblyAssociations.class,
    UpdateCADFileTests.class
})
public class RegressionTestSuite {
}
