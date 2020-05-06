package testsuites;

import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.apriori.utils.runner.CategorySuiteRunner;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
