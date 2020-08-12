package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcReportTests.class,
        CastingDtcDetailsReportTests.class,
        CastingDtcComparisonReportTests.class,
        PlasticDtcReportTests.class,
        PlasticDtcDetailsReportTests.class,
        PlasticDtcComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
