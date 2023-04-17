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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
public class JasperApiUtils {
    private ReportRequest reportRequest;
    private String jSessionId;
    private String exportSetName;
    private String reportsJsonFileName;

    /**
     * Default constructor for this class
     *
     * @param jSessionId - String for authentication/session
     * @param exportSetName - String of the export set which should be set
     * @param reportsJsonFileName - String of the right json file to use to be sent to the api
     */
    public JasperApiUtils(String jSessionId, String exportSetName, String reportsJsonFileName) {
        this.reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);
        this.jSessionId = jSessionId;
        this.exportSetName = exportSetName;
        this.reportsJsonFileName = reportsJsonFileName;
    }

    /**
     * A generic test method for input controls
     * TODO: replace with below method
     *
     * @param inputControlToSet String - key of which input control to set
     * @param valueToSet String - value which to set
     */
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

    /**
     * Generic method that sets one particular value in the input controls
     *
     * @param keyToSet String - key of the value to set
     * @param valueToSet String - value which to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTest(String keyToSet, String valueToSet) {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        InputControl inputControls = JasperReportUtil.init(jSessionId).getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = !valueToSet.isEmpty()
            ? setReportParameterByName(reportRequest, Constants.inputControlNames.get(keyToSet), valueToSet) :
            reportRequest;
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        return jasperApiUtils.generateReportSummary(reportRequest);
    }

    /**
     * Sets a particular report parameter within the ReportRequest instance
     *
     * @param reportRequest ReportRequest instance to use
     * @param valueToGet String the key of the value to set
     * @param valueToSet String the value which to set
     * @return ReportRequest instance with specified parameter set
     */
    public ReportRequest setReportParameterByName(ReportRequest reportRequest, String valueToGet, String valueToSet) {
        reportRequest.getParameters().getReportParameterByName(valueToGet)
            .setValue(Collections.singletonList(valueToSet));
        return reportRequest;
    }

    /**
     * Generates report and gets the particular data point named on line 94 (return statement)
     *
     * @param reportRequest ReportRequest instance to submit to the api
     * @return ChartDataPoint instance of the specified part
     */
    public ChartDataPoint generateReportAndGetChartDataPoint(ReportRequest reportRequest, String partToGet) {
        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = generateReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));
        return jasperReportSummary.getChartData().get(0).getChartDataPointByPartName(partToGet);
    }

    /**
     * Generates report summary based on specified report request
     *
     * @param reportRequest ReportRequest details which are to be sent to the api
     * @return JasperReportSummary instance
     */
    public JasperReportSummary generateReportSummary(ReportRequest reportRequest) {
        return JasperReportUtil.init(jSessionId).generateJasperReportSummary(reportRequest);
    }

    /**
     * Gets annual spend from a specified chart data point
     *
     * @param dataToUse ChartDataPoint from which the value should be retrieved
     * @return Double of annual spend value
     */
    public Double getAnnualSpendFromChartDataPoint(ChartDataPoint dataToUse) {
        return dataToUse.getAnnualSpend();
    }

    /**
     * Gets Fully Burdened Cost from a specified chart data point
     *
     * @param dataToUse ChartDataPoint from which the value should be retrieved
     * @return String of fully burdened cost
     */
    public String getFullyBurdenedCostFromChartDataPoint(ChartDataPoint dataToUse) {
        return dataToUse.getFullyBurdenedCost();
    }
}
