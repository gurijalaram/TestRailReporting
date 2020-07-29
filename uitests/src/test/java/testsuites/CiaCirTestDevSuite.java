package testsuites;

import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class,
        PlasticDtcComparisonReportTests.class,
        PlasticDtcDetailsReportTests.class,
        MachiningDtcDetailsReportTests.class,
        MachiningDtcComparisonReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcDetailsReportTests.class,
        CastingDtcComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
