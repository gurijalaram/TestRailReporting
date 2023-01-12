package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.compare.ComparisonTests;
import com.evaluate.ProcessRoutingTests;
import com.evaluate.PsoEditTests;
import com.evaluate.TwoModelMachiningTests;
import com.evaluate.assemblies.AssemblyAssociations;
import com.evaluate.assemblies.EditAssembliesTest;
import com.evaluate.assemblies.GroupEditAssemblies;
import com.evaluate.assemblies.IncludeAndExcludeNestedAssemblyTests;
import com.evaluate.assemblies.IncludeAndExcludeTests;
import com.evaluate.assemblies.LargeGroupAssemblyTests;
import com.evaluate.assemblies.UploadAssembliesTests;
import com.evaluate.dtc.DFMRiskTests;
import com.evaluate.dtc.DTCCastingTests;
import com.evaluate.dtc.ThreadTests;
import com.evaluate.dtc.ToleranceTests;
import com.evaluate.materialutilization.MaterialPMITests;
import com.evaluate.materialutilization.PartNestingTests;
import com.explore.GroupPublishTests;
import com.settings.SettingsTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.IgnoreTests;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(ExtendedRegressionSuite.class)
@Categories.ExcludeCategory(IgnoreTests.class)
@Suite.SuiteClasses({
    ComparisonTests.class,
    ProcessRoutingTests.class,
    PsoEditTests.class,
    TwoModelMachiningTests.class,
    AssemblyAssociations.class,
    EditAssembliesTest.class,
    GroupEditAssemblies.class,
    IncludeAndExcludeNestedAssemblyTests.class,
    IncludeAndExcludeTests.class,
    LargeGroupAssemblyTests.class,
    UploadAssembliesTests.class,
    DFMRiskTests.class,
    DTCCastingTests.class,
    ThreadTests.class,
    ToleranceTests.class,
    MaterialPMITests.class,
    PartNestingTests.class,
    GroupPublishTests.class,
    SettingsTests.class
})
public class ExtendedRegressionSuite {
}
