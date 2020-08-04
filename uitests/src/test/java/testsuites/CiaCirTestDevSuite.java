package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        MachiningDtcReportTests.class,
        PlasticDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
