package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcReportTests.class,
        PlasticDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
