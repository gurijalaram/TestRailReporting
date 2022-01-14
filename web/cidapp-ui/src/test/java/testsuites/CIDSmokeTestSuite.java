package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.compare.ComparisonTests;
import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.DeleteTests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.PsoEditTests;
import com.evaluate.PublishExistingCostedTests;
import com.evaluate.PublishTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.TwoModelMachiningTests;
import com.evaluate.UploadAssembliesTests;
import com.evaluate.dtc.DFMRiskTests;
import com.evaluate.dtc.DTCCastingTests;
import com.evaluate.dtc.DTCMachiningTests;
import com.evaluate.dtc.DTCPlasticMouldingTests;
import com.evaluate.dtc.SheetMetalDTCTests;
import com.evaluate.dtc.ThreadTests;
import com.evaluate.dtc.ToleranceTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.evaluate.materialutilization.PartNestingTests;
import com.explore.ActionsTests;
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
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("767")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SmokeTests.class)
@Categories.ExcludeCategory(IgnoreTests.class)
@Suite.SuiteClasses({
    CostAllCadTests.class,
    CostScenarioTests.class,
    ListProcessGroupTests.class,
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
    SheetMetalDTCTests.class,
    ActionsTests.class,
    TableHeadersTests.class,
    DTCMachiningTests.class,
    DTCCastingTests.class,
    TwoModelMachiningTests.class,
    SettingsTests.class,
    ToleranceTests.class,
    ComparisonTests.class,
    ThreadTests.class,
    PartNestingTests.class,
    PsoEditTests.class
})
public class CIDSmokeTestSuite {
}
