package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        MachiningDtcReportTests.class,
        MachiningDtcDetailsReportTests.class,
        MachiningDtcComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
