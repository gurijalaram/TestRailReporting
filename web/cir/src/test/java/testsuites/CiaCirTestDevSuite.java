package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcReportTests.class,
        CastingDtcComparisonReportTests.class,
        MachiningDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
