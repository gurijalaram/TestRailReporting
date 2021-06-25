package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.DeleteTests;
import com.evaluate.ListOfDigitalFactoryTests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.PublishExistingCostedTests;
import com.evaluate.PublishTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.TwoModelMachiningTests;
import com.evaluate.UploadAssembliesTests;
import com.evaluate.dtc.DFMRiskTests;
import com.evaluate.dtc.DTCCastingTests;
import com.evaluate.dtc.DTCMachiningTests;
import com.evaluate.dtc.DTCPlasticMouldingTests;
import com.evaluate.dtc.SheetMetalDTCTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.explore.ActionsTests;
import com.explore.PreviewPanelTests;
import com.explore.TableHeadersTests;
import com.explore.UploadComponentTests;
import com.explore.UploadTests;
import com.help.HelpTests;
import com.login.LoginTests;
import com.settings.DecimalPlaceTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("562")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SanityTests.class)
@Suite.SuiteClasses({
    UploadComponentTests.class,
    CostScenarioTests.class,
    PublishTests.class
})
public class SanityTestSuite {
}
