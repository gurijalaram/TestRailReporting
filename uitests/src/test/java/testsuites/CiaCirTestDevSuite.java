package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
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
        PlasticDtcReportTests.class,
        PlasticDtcDetailsReportTests.class,
        PlasticDtcComparisonReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcDetailsReportTests.class,
        CastingDtcComparisonReportTests.class,
        MachiningDtcReportTests.class,
        MachiningDtcDetailsReportTests.class,
        MachiningDtcComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
