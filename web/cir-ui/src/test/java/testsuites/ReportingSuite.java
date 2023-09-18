package testsuites;

import com.ootbreports.assemblycost.AssemblyCostReportTests;
import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.costoutlieridentification.CostOutlierIdentificationReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    AssemblyDetailsReportTests.class,
    AssemblyCostReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    ComponentCostReportTests.class,
    CostOutlierIdentificationDetailsReportTests.class,
    CostOutlierIdentificationReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    CycleTimeValueTrackingReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsReportTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsReportTests.class,
    SheetMetalDtcReportTests.class
})
public class ReportingSuite {
}