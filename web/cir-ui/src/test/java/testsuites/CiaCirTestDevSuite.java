package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class,
        MachiningDtcReportTests.class,
        ScenarioComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
