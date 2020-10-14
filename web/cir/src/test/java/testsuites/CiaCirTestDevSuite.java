package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        PlasticDtcReportTests.class,
        MachiningDtcReportTests.class,
        MachiningDtcDetailsReportTests.class,
        MachiningDtcComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
