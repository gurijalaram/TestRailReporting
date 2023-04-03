package com.ootbreports.newreportstests.utils;

import static com.apriori.utils.TestHelper.logger;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;

import com.google.common.base.Stopwatch;
import lombok.Data;
import org.jsoup.nodes.Element;
import utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class JasperApiUtils {
    private ReportRequest reportRequest;
    private String jSessionId;
    private String exportSetName;
    private String reportsJsonFileName;

    public JasperApiUtils(String jSessionId, String exportSetName, String reportsJsonFileName) {
        this.reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);
        this.jSessionId = jSessionId;
        this.exportSetName = exportSetName;
        this.reportsJsonFileName = reportsJsonFileName;
    }

    public void inputControlGenericTest(String inputControlToSet, String valueToSet) {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        InputControl inputControls = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = !valueToSet.isEmpty()
            ? jasperApiUtils.setReportParameterByName(reportRequest, Constants.inputControlNames.get(inputControlToSet), valueToSet) :
            reportRequest;
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        valueToSet = valueToSet.equals("7820000") ? "7,820,000.00" : valueToSet;

        JasperReportSummary js = jasperApiUtils.generateReportSummary(reportRequest);
        List<Element> elements = js.getReportHtmlPart().getElementsContainingText(valueToSet);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        assertThat(tdResultElements.toString().contains(valueToSet), is(equalTo(true)));
    }

    public JasperReportSummary dtcScoreGenericTest(String valueToSet) {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        InputControl inputControls = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);
        reportRequest = !valueToSet.isEmpty()
            ? jasperApiUtils.setReportParameterByName(reportRequest, Constants.inputControlNames.get("DTC Score"), valueToSet) :
            reportRequest;

        return jasperApiUtils.generateReportSummary(reportRequest);
    }

    public JasperReportSummary massMetricGenericTest(String valueToSet) {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        InputControl inputControls = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);
        reportRequest = !valueToSet.isEmpty()
            ? jasperApiUtils.setReportParameterByName(reportRequest, Constants.inputControlNames.get("Mass Metric"), valueToSet) :
            reportRequest;

        return jasperApiUtils.generateReportSummary(reportRequest);
    }

    public JasperReportSummary costMetricGenericTest(String valueToSet) {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        InputControl inputControls = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);
        reportRequest = !valueToSet.isEmpty()
            ? jasperApiUtils.setReportParameterByName(reportRequest, Constants.inputControlNames.get("Cost Metric"), valueToSet) :
            reportRequest;

        return jasperApiUtils.generateReportSummary(reportRequest);
    }

    public JasperReportSummary minAnnualSpendGenericTest() {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
        String minAnnualSpendValue = "7820000";
        String minAnnualSpendAssertValue = "7,820,000.00";

        InputControl inputControls = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        //reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, Constants.inputControlNames.get("Minimum Annual Spend"), minAnnualSpendValue);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        return jasperApiUtils.generateReportSummary(reportRequest);
    }

    public ReportRequest setReportParameterByName(ReportRequest reportRequest, String valueToGet, String valueToSet) {
        reportRequest.getParameters().getReportParameterByName(valueToGet)
            .setValue(Collections.singletonList(valueToSet));
        return reportRequest;
    }

    public ChartDataPoint generateReportAndGetChartDataPoint(ReportRequest reportRequest) {
        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = generateReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s", timer));
        return jasperReportSummary.getFirstChartData().getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");
    }

    public JasperReportSummary generateReportSummary(ReportRequest reportRequest) {
        return JasperReportUtil.init(jSessionId).generateJasperReportSummary(reportRequest);
    }

    public Double getAnnualSpendFromChartDataPoint(ChartDataPoint dataToUse) {
        return dataToUse.getAnnualSpend();
    }

    public String getFullyBurdenedCostFromChartDataPoint(ChartDataPoint dataToUse) {
        return dataToUse.getFullyBurdenedCost();
    }
}
