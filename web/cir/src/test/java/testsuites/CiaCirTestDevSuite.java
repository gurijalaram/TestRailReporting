package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        TargetAndQuotedCostValueTrackingTests.class
})

public class CiaCirTestDevSuite {
}
