package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
