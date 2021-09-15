package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcReportTests.class,
        ComponentCostReportTests.class
})

public class CiaCirTestDevSuite {
}
