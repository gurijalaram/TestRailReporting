package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AssemblyCostA4ReportTests.class,
    AssemblyCostLetterReportTests.class,
    AssemblyDetailsReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcComparisonReportTests.class,
    MachiningDtcReportTests.class,
    MachiningDtcDetailsTests.class,
    MachiningDtcComparisonReportTests.class,
    PlasticDtcReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcComparisonReportTests.class,
    SheetMetalDtcReportTests.class,
    SheetMetalDtcDetailsTests.class,
    SheetMetalDtcComparisonReportTests.class
})
public class ReportsApiTestSuite {
}
