package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcDetailsTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.newreportstests.general.assemblycost.AssemblyCostA4ReportTests;
import com.ootbreports.newreportstests.general.assemblycost.AssemblyCostLetterReportTests;
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
    AssemblyCostLetterReportTests.class,
    AssemblyDetailsReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsTests.class,
    SheetMetalDtcReportTests.class
})
public class ReportsApiTestSuite {
}
