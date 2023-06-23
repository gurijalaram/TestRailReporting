package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.newreportstests.general.assemblycost.AssemblyCostA4ReportTests;
import com.ootbreports.newreportstests.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.JasperApiTest;

@ProjectRunID("261")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(JasperApiTest.class)
@Suite.SuiteClasses({
    AssemblyCostA4ReportTests.class,
    AssemblyDetailsReportTests.class,
    CastingDtcReportTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcReportTests.class,
    SheetMetalDtcComparisonReportTests.class
})
public class ReportsApiTestSuite {
}
