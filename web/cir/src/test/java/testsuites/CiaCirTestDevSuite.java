package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        PlasticDtcComparisonReportTests.class,
        PlasticDtcDetailsReportTests.class
})

public class CiaCirTestDevSuite {
}
