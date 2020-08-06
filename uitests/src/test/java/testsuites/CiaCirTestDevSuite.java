package testsuites;

import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        MachiningDtcReportTests.class,
        MachiningDtcDetailsReportTests.class,
        MachiningDtcComparisonReportTests.class,
        PlasticDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
