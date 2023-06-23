package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.JasperApiTest;

@ProjectRunID("261")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(JasperApiTest.class)
@Suite.SuiteClasses({
    CastingDtcReportTests.class,
    MachiningDtcReportTests.class
})
public class ReportsApiTestSuite {
}
