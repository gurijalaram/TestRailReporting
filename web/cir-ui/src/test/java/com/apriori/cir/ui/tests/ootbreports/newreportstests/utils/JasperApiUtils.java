package com.apriori.cir.ui.tests.ootbreports.newreportstests.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.request.ReportRequest;
import com.apriori.cir.api.models.response.ChartData;
import com.apriori.cir.api.models.response.ChartDataPoint;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.utils.JasperReportUtil;
import com.apriori.cir.ui.enums.DtcScoreEnum;
import com.apriori.cir.ui.enums.JasperCirApiPartsEnum;
import com.apriori.cir.ui.enums.RollupEnum;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;

import com.google.common.base.Stopwatch;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@Slf4j
public class JasperApiUtils {
    private JasperApiInputControlsPathEnum reportValueForInputControls;
    private SoftAssertions softAssertions = new SoftAssertions();
    @Getter
    private ReportRequest reportRequest;
    private String reportsJsonFileName;
    private String exportSetName;
    private String processGroupName;
    private String jasperSessionID;

    /**
     * Default constructor for this class
     *
     * @param jasperSessionID            - String for authentication/session
     * @param exportSetName              - String of the export set which should be set
     * @param reportsJsonFileName        - String of the right json file to use to be sent to the api
     * @param reportNameForInputControls - String of the report name to use to retrieve input controls
     */
    public JasperApiUtils(String jasperSessionID, String exportSetName, String reportsJsonFileName, JasperApiInputControlsPathEnum reportNameForInputControls) {
        this.reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);
        this.reportValueForInputControls = reportNameForInputControls;
        this.jasperSessionID = jasperSessionID;
        this.exportSetName = exportSetName;
        this.reportsJsonFileName = reportsJsonFileName;
    }

    /**
     * Second constructor for this class (not default), without reportValueForInputControls
     *
     * @param jasperSessionID - String for authentication/session
     * @param exportSetName - String of the export set which should be set
     * @param processGroup - String of process group which should be set
     * @param reportsJsonFileName - String of the right json file to use to be sent to the api
     */
    public JasperApiUtils(String jasperSessionID, String exportSetName, ProcessGroupEnum processGroup, String reportsJsonFileName) {
        this.reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);
        this.jasperSessionID = jasperSessionID;
        this.exportSetName = exportSetName;
        this.processGroupName = processGroup.getProcessGroup();
        this.reportsJsonFileName = reportsJsonFileName;
    }

    /**
     * Generic method that sets one particular value in the input controls
     *
     * @param keyToSet   String - key of the value to set
     * @param valueToSet String - value which to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCore(String keyToSet, String valueToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        //setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(), inputControls.getRollup().getOption(RollupEnum.UC_CASTING_DTC_ALL.getRollupName()).getValue());

        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        if (!valueToSet.isEmpty()) {
            setReportParameterByName(keyToSet, valueToSet);
        }

        if (processGroupName != null) {
            String processGroupId = inputControls.getProcessGroup().getOption(processGroupName).getValue();
            setReportParameterByName(InputControlsEnum.PROCESS_GROUP.getInputControlId(), processGroupId);
        }

        setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);

        if (reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId()) != null) {
            setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), currentDateTime);
        }

        if (reportRequest.getParameters().toString().contains(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId())) {
            setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);
        }

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic test for upgrade part comparison report
     *
     * @param isUpcReport - boolean to determine if part number need set or not
     * @param firstExportSetName - String of name of first export set to select
     * @param secondExportSetName - String of name of second export set to select
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreSetTwoExportSetsAndAllPgRollup(boolean isUpcReport, String firstExportSetName, String secondExportSetName) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String firstExportSetValue = inputControls.getExportSetName().getOption(firstExportSetName).getValue();
        String secondExportSetValue = inputControls.getExportSetName().getOption(secondExportSetName).getValue();
        String rollupValue = inputControls.getRollup().getOption(RollupEnum.ALL_PG.getRollupName()).getValue();
        if (isUpcReport) {
            this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.PART_NUMBER.getInputControlId())
                .setValue(Collections.singletonList(inputControls.getPartNumber().getOption("2X1 CAVITY MOLD").getValue()));
            this.reportRequest.getParameters().getReportParameterByName("partNumberNew").setValue(Collections.singletonList("223"));
        }
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId())
            .setValue(Arrays.asList(firstExportSetValue, secondExportSetValue));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId())
            .setValue(Collections.singletonList(rollupValue));

        this.reportRequest.getParameters().getReportParameterByName("rollupNew").setValue(Collections.singletonList("298"));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList("2023-04-13T06:57:36"));
        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList(currentDateTime));

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic upgrade part comparison report generation with change level specified
     *
     * @param changeLevelsToSet - ArrayList of Strings of change levels to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreSetChangeLevel(boolean setPartNumber, ArrayList<String> changeLevelsToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String firstExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();
        String secondExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();
        String rollupValue = inputControls.getRollup().getOption(RollupEnum.ALL_PG.getRollupName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        if (setPartNumber) {
            this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.PART_NUMBER.getInputControlId())
                .setValue(Collections.singletonList(inputControls.getPartNumber().getOption("2X1 CAVITY MOLD").getValue()));
            this.reportRequest.getParameters().getReportParameterByName("partNumberNew").setValue(Collections.singletonList("223"));
        }

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId())
            .setValue(Arrays.asList(firstExportSetValue, secondExportSetValue));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId())
            .setValue(Collections.singletonList(rollupValue));

        this.reportRequest.getParameters().getReportParameterByName("rollupNew").setValue(Collections.singletonList("298"));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList("2023-04-13T06:57:36"));
        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList(currentDateTime));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.CHANGE_LEVEL.getInputControlId())
            .setValue(changeLevelsToSet);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic generation of upgrade part comparison with metric thresholds specified
     *
     * @param setCostMetricThresholds - boolean to determine if cost or time metrics
     * @param lowThreshold - String of low threshold to set
     * @param highThreshold - String of high threshold to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreSetCostMetricOrTimeMetricsThresholdLevels(boolean setPartNumber, boolean setCostMetricThresholds, String lowThreshold, String highThreshold) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String firstExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();
        String secondExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();
        String rollupValue = inputControls.getRollup().getOption(RollupEnum.ALL_PG.getRollupName()).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        if (setPartNumber) {
            this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.PART_NUMBER.getInputControlId())
                .setValue(Collections.singletonList(inputControls.getPartNumber().getOption("2X1 CAVITY MOLD").getValue()));
            this.reportRequest.getParameters().getReportParameterByName("partNumberNew").setValue(Collections.singletonList("223"));
        }

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId())
            .setValue(Arrays.asList(firstExportSetValue, secondExportSetValue));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId())
            .setValue(Collections.singletonList(rollupValue));

        this.reportRequest.getParameters().getReportParameterByName("rollupNew").setValue(Collections.singletonList("298"));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList("2023-04-13T06:57:36"));
        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList(currentDateTime));

        String lowThresholdToSet = setCostMetricThresholds
            ? InputControlsEnum.COST_METRIC_LOW_THRESHOLD.getInputControlId()
            : InputControlsEnum.TIME_METRICS_LOW_THRESHOLD.getInputControlId();
        String highThresholdToSet = setCostMetricThresholds
            ? InputControlsEnum.COST_METRIC_HIGH_THRESHOLD.getInputControlId()
            : InputControlsEnum.TIME_METRICS_HIGH_THRESHOLD.getInputControlId();
        this.reportRequest.getParameters().getReportParameterByName(lowThresholdToSet)
            .setValue(Collections.singletonList(lowThreshold));
        this.reportRequest.getParameters().getReportParameterByName(highThresholdToSet)
            .setValue(Collections.singletonList(highThreshold));

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic generation of upgrade part comparison with part number specified
     *
     * @param partNumberToSet - String of part number to set
     * @return - JasperReportSummary instance
     */
    public JasperReportSummary genericPartNumberUpgradePartComparisonTest(String partNumberToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String firstExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();
        String secondExportSetValue = inputControls.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();
        String rollupValue = inputControls.getRollup().getOption(RollupEnum.ALL_PG.getRollupName()).getValue();
        String partNumber = inputControls.getPartNumber().getOption(partNumberToSet).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId())
            .setValue(Arrays.asList(firstExportSetValue, secondExportSetValue));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId())
            .setValue(Collections.singletonList(rollupValue));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.PART_NUMBER.getInputControlId())
            .setValue(Collections.singletonList(partNumber));

        this.reportRequest.getParameters().getReportParameterByName("rollupNew").setValue(Collections.singletonList("298"));
        this.reportRequest.getParameters().getReportParameterByName("partNumberNew").setValue(Collections.singletonList("224"));

        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EARLIEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList("2023-04-13T06:57:36"));
        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId())
            .setValue(Collections.singletonList(currentDateTime));

        return jasperReportUtil.generateJasperReportSummary(this.reportRequest);
    }

    /**
     * Generic method that sets one particular value in the input controls
     *
     * @param keyToSet   String - key of the value to set
     * @param valueToSet String - value which to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummaryIncRawDataAsString genericTestCoreRawAsString(String keyToSet, String valueToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();

        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        if (!valueToSet.isEmpty()) {
            setReportParameterByName(keyToSet, valueToSet);
        }

        if (processGroupName != null) {
            String processGroupId = inputControls.getProcessGroup().getOption(processGroupName).getValue();
            setReportParameterByName(InputControlsEnum.PROCESS_GROUP.getInputControlId(), processGroupId);
        }

        setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), currentExportSet);
        setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummaryIncRawDataAsString jasperReportSummaryIncRawDataAsString = jasperReportUtil
            .generateJasperReportSummaryIncRawDataAsString(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummaryIncRawDataAsString;
    }

    /**
     * Generic method for testing currency where export set is not relevant (Cycle Time Report only)
     *
     * @param currencyToSet - currency that is to be set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreCurrencyAndDateOnlyCycleTimeReport(String currencyToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);

        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), inputControls.getProjectRollup().getOption(RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName()).getValue());

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic method for testing currency for Digital Factory Perf and Details Report tests
     *
     * @param currencyToSet - currency that is to be set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(String currencyToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);

        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        String dateTimeNowValue = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());
        setReportParameterByName(InputControlsEnum.EXPORT_DATE.getInputControlId(), dateTimeNowValue);
        setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(), dateTimeNowValue);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), inputControls.getExportSetName().getOption(exportSetName).getValue());
        setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(), RollupEnum.QA_TEST_ONE.getRollupName());

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic method for testing currency and latest cost date only
     *
     * @param currencyToSet - currency that is to be set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreCurrencyLatestCostDateOnly(String currencyToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        setReportParameterByName(InputControlsEnum.LATEST_COST_DATE.getInputControlId(),
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now())
        );

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic method for testing currency only, for UC and UPC tests
     *
     * @param currencyToSet - currency that is to be set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreCurrencyOnlyUpgradeComparisonTests(String currencyToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);

        InputControl inputControlState = jasperReportUtil.getInputControls(reportValueForInputControls);
        String valueOneToSet = inputControlState.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();
        String valueTwoToSet = inputControlState.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();

        String rollupValue = inputControlState.getRollup().getOption(RollupEnum.ALL_PG.getRollupName()).getValue();

        setTwoExportSetsParametersByName(valueOneToSet, valueTwoToSet);
        setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(), rollupValue);
        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now())
        );

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic method for testing currency only, for UC and UPC tests
     *
     * @param processGroupToSet - currency that is to be set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreProcessGroupOnlyUpgradeComparisonTests(String processGroupToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);

        InputControl inputControlState = jasperReportUtil.getInputControls(reportValueForInputControls);
        String valueOneToSet = inputControlState.getExportSetName().getOption(ExportSetEnum.ALL_PG_NEW.getExportSetName()).getValue();
        String valueTwoToSet = inputControlState.getExportSetName().getOption(ExportSetEnum.ALL_PG_CURRENT.getExportSetName()).getValue();

        String rollupValue = inputControlState.getRollup().getOption(RollupEnum.ALL_PG.getRollupName()).getValue();

        setTwoExportSetsParametersByName(valueOneToSet, valueTwoToSet);
        setReportParameterByName(InputControlsEnum.ROLLUP.getInputControlId(), rollupValue);
        setReportParameterByName(InputControlsEnum.PROCESS_GROUP.getInputControlId(),
            inputControlState.getProcessGroup().getOption(processGroupToSet).getValue());
        setReportParameterByName(InputControlsEnum.LATEST_EXPORT_DATE.getInputControlId(),
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now())
        );

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic currency test for Upgrade Comparison and Upgrade Part Comparison Reports
     *
     * @param currencyToUse - String
     * @param currencyIndexToGet - int
     * @return ArrayList of values to assert on
     */
    public ArrayList<String> generateReportAndGetAssertValues(String currencyToUse, int currencyIndexToGet) {
        JasperReportSummary jasperReportSummary = genericTestCoreCurrencyOnlyUpgradeComparisonTests(currencyToUse);

        String colspanToUse = currencyIndexToGet == 2 ? "19" : "12";
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", colspanToUse).get(currencyIndexToGet).child(0).text());
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "4").get(7).child(0).text());
        return assertValues;
    }

    /**
     * Generic test for reports that require project rollup and currency only to be specified
     *
     * @param projectRollupName - String of project rollup to use
     * @param currencyString - String of currency to use
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreProjectRollupAndCurrencyOnly(String projectRollupName, String currencyString) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jasperSessionID);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);

        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyString);
        String projectRollupValue = inputControls.getProjectRollup().getOption(projectRollupName).getValue();
        setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        log.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic test for currency input control within all four Spend Analysis Reports
     *
     * @param indexValues - indexes of assert values to get
     */
    public void spendAnalysisReportGenericCurrencyTest(List<Integer> indexValues) {
        ArrayList<String> gbpAssertValues = spendAnalysisGenerateReportReturnValues(CurrencyEnum.GBP.getCurrency(), indexValues);
        ArrayList<String> usdAssertValues = spendAnalysisGenerateReportReturnValues(CurrencyEnum.USD.getCurrency(), indexValues);

        assertThat(gbpAssertValues.get(0), is(not(equalTo(usdAssertValues.get(0)))));
        assertThat(gbpAssertValues.get(1), is(not(equalTo(usdAssertValues.get(1)))));
    }

    /**
     * Generic test of currency code for use on a dtc report
     *
     * @param partName          - String of partName which is to be used
     * @param areBubblesPresent - boolean which states if bubbles are present or not
     * @param isDetailsOrComparisonReport - boolean which states if current test is sheet metal dtc details report or not
     */
    public void genericDtcCurrencyTest(String partName, boolean areBubblesPresent, boolean isDetailsOrComparisonReport) {
        String currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = genericTestCore(
            InputControlsEnum.CURRENCY.getInputControlId(),
            currencyAssertValue
        );

        String currentCurrencyAboveChart = isDetailsOrComparisonReport
            ? getCurrentCurrencyFromAboveChartDtcDetailsOrComparisonReport(jasperReportSummaryUsd)
            : getCurrentCurrencyFromAboveChart(jasperReportSummaryUsd, areBubblesPresent);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = genericTestCore(
            InputControlsEnum.CURRENCY.getInputControlId(),
            currencyAssertValue
        );

        currentCurrencyAboveChart = isDetailsOrComparisonReport
            ? getCurrentCurrencyFromAboveChartDtcDetailsOrComparisonReport(jasperReportSummaryGbp)
            : getCurrentCurrencyFromAboveChart(jasperReportSummaryGbp, areBubblesPresent);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        if (areBubblesPresent) {
            String usdCurrencyValue = getCurrencyValueFromChart(jasperReportSummaryUsd, partName);
            String gbpCurrencyValue = getCurrencyValueFromChart(jasperReportSummaryGbp, partName);
            softAssertions.assertThat(usdCurrencyValue).isNotEqualTo(gbpCurrencyValue);
        }

        softAssertions.assertAll();
    }

    /**
     * Generic top level method for Cycle Time Value Tracking currency test
     */
    public void cycleTimeValueTrackingCurrencyTest() {
        ArrayList<String> gbpScenarioCycleTimeValueList = getScenarioCycleTimeValues(CurrencyEnum.GBP.getCurrency());
        ArrayList<String> usdScenarioCycleTimeValueList = getScenarioCycleTimeValues(CurrencyEnum.USD.getCurrency());

        for (int i = 0; i < 4; i++) {
            softAssertions.assertThat(
                gbpScenarioCycleTimeValueList.get(i)
            ).isEqualTo(
                usdScenarioCycleTimeValueList.get(i)
            );
        }

        softAssertions.assertAll();
    }

    /**
     * Generic test for Currency within Component Cost Report
     */
    public void genericComponentCostCurrencyTest() {
        String currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = genericTestCore(
            InputControlsEnum.COMPONENT_COST_CURRENCY.getInputControlId(),
            currencyAssertValue
        );

        String currentCurrencyAboveChart = getCurrencySettingValueFromChartComponentCost(jasperReportSummaryUsd).substring(10);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = genericTestCore(
            InputControlsEnum.COMPONENT_COST_CURRENCY.getInputControlId(),
            currencyAssertValue
        );

        currentCurrencyAboveChart = getCurrencySettingValueFromChartComponentCost(jasperReportSummaryGbp).substring(10);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        String usdCurrencyValue = getCurrencyValueFromChartComponentCost(jasperReportSummaryUsd);
        String gbpCurrencyValue = getCurrencyValueFromChartComponentCost(jasperReportSummaryGbp);

        softAssertions.assertThat(usdCurrencyValue).isNotEqualTo(gbpCurrencyValue);

        softAssertions.assertAll();
    }

    /**
     * Generic test to be used on any dtc report
     *
     * @param partNames - List of Strings of part names for use in the test
     * @param miscData  - String array of data to be used in the test
     */
    public void genericDtcTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        for (String partName : partNames) {
            softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().toString().contains(partName)).isEqualTo(true);
        }

        String keyToGetValuesBy = fixKeyToGetValuesBy(miscDataList.get(0));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(keyToGetValuesBy);
        softAssertions.assertThat(elements.get(5).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for Cost Metric within Cost Outlier Report
     *
     * @param miscData - List of Strings of relevant data to use in the test
     */
    public void genericCostMetricCostOutlierTest(List<String> miscData) {
        JasperReportSummary jasperReportSummary = genericTestCore(miscData.get(0), miscData.get(1));

        String keyToGetValuesBy = fixKeyToGetValuesBy(miscData.get(0));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(keyToGetValuesBy);
        softAssertions.assertThat(elements.get(5).toString().contains(miscData.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for Cost Metric within Cost Outlier Details Report
     *
     * @param miscData - List of Strings of relevant data to use in the test
     */
    public void genericCostMetricCostOutlierDetailsTest(List<String> partList, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partElementsFromPage = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "_jrHyperLink ReportExecution");
        for (String partName : partList) {
            softAssertions.assertThat(partElementsFromPage.toString().contains(partName)).isEqualTo(true);
        }
        jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "_jrHyperLink ReportExecution").get(0).children();

        String keyToGetValuesBy = fixKeyToGetValuesBy(miscDataList.get(0));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(keyToGetValuesBy);
        softAssertions.assertThat(elements.get(5).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for any dtc details report
     *
     * @param partNames - list of Strings containing the parts to use
     * @param miscData  - String array of data to be used in the test
     */
    public void genericDtcDetailsTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");

        int i = 6;
        if (!partNames.get(0).isEmpty()) {
            for (String partName : partNames) {
                String assertPartName = partName.contains("Initial") ? partName.replace(" (Initial)", "") : partName.replace(" (Bulkload)", "");
                softAssertions.assertThat(partNumberElements.toString().contains(assertPartName)).isEqualTo(true);
                i = !partName.equals(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName()) ? i + 1 : i;
            }
        }

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for dtc score
     *
     * @param areBubblesPresent - boolean which specifies what report type is being used
     * @param partNames         - List of Strings of part names
     * @param miscData          - String array of data to be used in the test
     */
    public void genericDtcScoreTest(boolean areBubblesPresent, List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        String assertValue = miscDataList.get(1).isEmpty() ? DtcScoreEnum.ALL_CORRECT_ORDER.getDtcScoreName() : miscDataList.get(1);

        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        if (!partNames.get(0).isEmpty()) {
            if (areBubblesPresent) {
                for (String partName : partNames) {
                    softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().toString().contains(partName)).isEqualTo(true);
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 3; j++) {
                        softAssertions.assertThat(jasperReportSummary.getChartData().get(i).getChartDataPoints().get(j).getPartName()).isEqualTo(partNames.get(j));
                    }
                }
            }
        }

        if (assertValue.equals(DtcScoreEnum.ALL_CORRECT_ORDER.getDtcScoreName())) {
            String valueFromReports = jasperReportSummary.getReportHtmlPart().getElementsContainingText(fixKeyToGetValuesBy(miscDataList.get(0)))
                .get(5).text();
            softAssertions.assertThat(valueFromReports.contains("High")).isEqualTo(true);
            softAssertions.assertThat(valueFromReports.contains("Medium")).isEqualTo(true);
            softAssertions.assertThat(valueFromReports.contains("Low")).isEqualTo(true);
        } else {
            softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText(fixKeyToGetValuesBy(miscDataList.get(0)))
                .get(5).text()).contains(assertValue);
        }

        softAssertions.assertAll();
    }

    /**
     * Generic test of process group input control on any dtc report
     *
     * @param partNames - List of Strings of part names for the test
     * @param miscData  - String array of data to be used in the test
     */
    public void genericProcessGroupDtcTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        String processGroup = miscDataList.size() > 1 ? miscDataList.get(1) : "";
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), processGroup);

        if (!partNames.get(0).isEmpty()) {
            for (String partName : partNames) {
                partName = miscDataList.get(1).contains("(Initial") ? partName.replace(" (Initial)", "") : partName;
                softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().toString().contains(partName)).isEqualTo(true);
            }
        } else {
            softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("No data available")).isEqualTo(true);
        }

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for process group on a dtc details report
     *
     * @param partNames - List of Strings of part names for the test
     * @param miscData  - String array of data to be used in the test
     */

    public void genericProcessGroupDtcDetailsTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        List<String> listOfPartNumbers = partNumberElements.stream().flatMap(e -> Stream.of(e.text())).collect(Collectors.toList());

        for (String partName : partNames) {
            if (partNumberElements.isEmpty()) {
                softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString().contains("No data available")).isEqualTo(true);
            } else {
                String partNameWithoutScenarioName = partName.contains(" (Initial)") ? partName.replace(" (Initial)", "") : partName.replace(" (sand casting)", "");
                partNameWithoutScenarioName = partName.contains(" (Bulkload)") ? partName.replace(" (Bulkload)", "") : partNameWithoutScenarioName;
                softAssertions.assertThat(listOfPartNumbers.contains(partNameWithoutScenarioName)).isEqualTo(true);
            }
        }

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Process Group").get(6)
            .siblingElements().get(10).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for minimum annual spend input control on a dtc report
     *
     * @param expectedBubbleNumber - int to specify the number of chart data points to assert against
     */
    public void genericMinAnnualSpendDtcTest(int expectedBubbleNumber) {
        String minimumAnnualSpendValue = "7820000";
        JasperReportSummary jasperReportSummary = genericTestCore(
            InputControlsEnum.MINIMUM_ANNUAL_SPEND.getInputControlId(),
            minimumAnnualSpendValue
        );

        if (expectedBubbleNumber == 0) {
            softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString()).contains("No data available");
        } else {
            softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().size()).isEqualTo(expectedBubbleNumber);
            softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().get(0).getAnnualSpend()).isNotEqualTo(minimumAnnualSpendValue);
        }

        softAssertions.assertAll();
    }

    /**
     * Generic test for minimum annual spend input control on a dtc details report
     * @param isNoDataAvailableExpected - boolean flag for if we are expecting no data available or not
     */
    public void genericMinAnnualSpendDtcDetailsTest(boolean isNoDataAvailableExpected) {
        String minimumAnnualSpendValue = "7820000";
        JasperReportSummary jasperReportSummary = genericTestCore(
            InputControlsEnum.MINIMUM_ANNUAL_SPEND.getInputControlId(),
            minimumAnnualSpendValue
        );

        if (!isNoDataAvailableExpected) {
            String minAnnualSpendValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "4").get(9).text();
            //softAssertions.assertThat(minAnnualSpendValue).isEqualTo("34,661,340.98");
            softAssertions.assertThat(minAnnualSpendValue).isNotEqualTo(minimumAnnualSpendValue);
        } else {
            softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().toString()).contains("No data available");
        }

        softAssertions.assertAll();
    }

    /**
     * Generic test for sort order input control on a dtc comparison report
     *
     * @param miscData      - List of Strings of data for the test
     * @param partNames     - List of Strings of part names for the test
     * @param assertFigures - List of Doubles for the assertion of Annual Spend values
     */
    public void genericSortOrderAnnualSpendDtcTest(List<String> partNames, List<Double> assertFigures, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        ChartData chartData = jasperReportSummary.getChartData().get(0);
        softAssertions.assertThat(chartData.getChartDataPoints().get(0).getPartName()).isEqualTo(partNames.get(0));
        softAssertions.assertThat(chartData.getChartDataPoints().get(1).getPartName()).isEqualTo(partNames.get(1));

        softAssertions.assertThat(chartData.getChartDataPointByPartName(partNames.get(0)).getPropertyByName("annualSpend").getValue())
            .isEqualTo(assertFigures.get(0));
        softAssertions.assertThat(chartData.getChartDataPointByPartName(partNames.get(1)).getPropertyByName("annualSpend").getValue())
            .isEqualTo(assertFigures.get(1));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for sort order (annual spend) input control on a dtc report
     *
     * @param miscData      - List of Strings of data for the test
     * @param partNames     - List of Strings of part names for the test
     */
    public void genericSortOrderDtcComparisonTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<ChartDataPoint> chartDataPointsList = jasperReportSummary.getChartData().get(0).getChartDataPoints();

        softAssertions.assertThat(chartDataPointsList.get(0).getPartName()).isEqualTo(partNames.get(0));
        softAssertions.assertThat(chartDataPointsList.get(1).getPartName()).isEqualTo(partNames.get(1));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for sort order input control on a dtc report
     *
     * @param partNames    - List of Strings of part names for the test
     * @param assertValues - List of Strings for the assertion of Annual Spend values
     * @param miscData     - String array of data to be used in the test
     */
    public void genericSortOrderDtcDetailsTest(List<String> partNames, List<String> assertValues, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        int i = 6;
        for (String partName : partNames) {
            String partNameWithoutScenarioName = partName.contains(" (Initial)") ? partName.replace(" (Initial)", "") : partName.replace(" (sand casting)", "");
            partNameWithoutScenarioName = partName.contains(" (Bulkload)") ? partName.replace(" (Bulkload)", "") : partNameWithoutScenarioName;
            softAssertions.assertThat(partElements.get(i).text()).isEqualTo(partNameWithoutScenarioName);
            i++;
        }

        if (!assertValues.get(0).contains("0.0") && !assertValues.get(1).contains("0.0")) {
            i = assertValues.get(1).startsWith("5") ? 15 : 9;
            String colspanToUse = assertValues.get(1).startsWith("5") ? "3" : "4";
            for (String assertValue : assertValues) {
                softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", colspanToUse).get(i).text()).isEqualTo(assertValue);
                i = assertValues.get(1).startsWith("5") ? 20 : 11;
            }
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Sets a particular report parameter within the ReportRequest instance
     *
     * @param valueToGet String the key of the value to set
     * @param valueToSet String the value which to set
     */
    public void setReportParameterByName(String valueToGet, String valueToSet) {
        this.reportRequest.getParameters().getReportParameterByName(valueToGet)
            .setValue(Collections.singletonList(valueToSet));
    }

    /**
     * Sets export set name to more than one
     *
     * @param valueOneToSet String of the first value which to set
     * @param valueTwoToSet String of the second value which to set
     */
    public void setTwoExportSetsParametersByName(String valueOneToSet, String valueTwoToSet) {
        this.reportRequest.getParameters().getReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId())
            .setValue(Arrays.asList(valueOneToSet, valueTwoToSet));
    }

    /**
     * Generic currency test for Target Quoted Cost Trend and Potential Savings Reports
     *
     * @param keyOne - currency setting key
     * @param keyTwo - first monetary value key
     * @param keyThree - second monetary value key
     */
    public void targetQuotedCostTrendAndPotentialSavingsGenericCurrencyTest(int keyOne, int keyTwo, int keyThree) {
        String tableId = "JR_PAGE_ANCHOR_0_1";
        String attributeNameId = "id";
        String tagName = "span";

        List<Element> gbpSpanElements = genericTestCoreProjectRollupAndCurrencyOnly(
            RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName(),
            CurrencyEnum.GBP.getCurrency()
        ).getReportHtmlPart()
            .getElementsByAttributeValue(attributeNameId, tableId).get(0)
            .getElementsByTag(tagName);

        List<Element> usdSpanElements = genericTestCoreProjectRollupAndCurrencyOnly(
            RollupEnum.AC_CYCLE_TIME_VT_1.getRollupName(),
            CurrencyEnum.USD.getCurrency()
        ).getReportHtmlPart()
            .getElementsByAttributeValue(attributeNameId, tableId).get(0)
            .getElementsByTag(tagName);

        ArrayList<String> gbpCostValues = new ArrayList<>();

        gbpCostValues.add(gbpSpanElements.get(keyOne).text());
        gbpCostValues.add(gbpSpanElements.get(keyTwo).text());
        gbpCostValues.add(gbpSpanElements.get(keyThree).text());

        ArrayList<String> usdCostValues = new ArrayList<>();
        usdCostValues.add(usdSpanElements.get(keyOne).text());
        usdCostValues.add(usdSpanElements.get(keyTwo).text());
        usdCostValues.add(usdSpanElements.get(keyThree).text());

        softAssertions.assertThat(gbpCostValues.get(0)).isNotEqualTo(usdCostValues.get(0));
        softAssertions.assertThat(gbpCostValues.get(0)).isEqualTo("GBP");
        softAssertions.assertThat(usdCostValues.get(0)).isEqualTo("USD");
        softAssertions.assertThat(gbpCostValues.get(1)).isNotEqualTo(usdCostValues.get(1));
        softAssertions.assertThat(gbpCostValues.get(2)).isNotEqualTo(usdCostValues.get(2));

        softAssertions.assertAll();
    }

    /**
     * Get chart data point property value by name
     *
     * @param jasperReportSummary - Jasper Report Summary instance
     * @param partName - String
     * @param propertyName - String
     * @return String of property value
     */
    public String getPartPropertyValueByName(JasperReportSummary jasperReportSummary, String partName, String propertyName) {
        ChartDataPoint chartDataPoint = jasperReportSummary.getFirstChartData().getChartDataPointByPartName(partName);

        if (chartDataPoint == null) {
            throw new IllegalArgumentException(String.format("Part with name %s does not exist", partName));
        }

        Object propertyValue = chartDataPoint.getPropertyByName(propertyName).getValue();

        if (propertyValue == null) {
            throw new IllegalArgumentException(String.format("Property with name %s for part name %s does not exist", propertyName, partName));
        }

        return propertyValue.toString();
    }

    /**
     * Generates report for Scenario Activity including Raw Data
     *
     * @return ArrayList of Jasper Report Summary instances
     */
    public ArrayList<JasperReportSummaryIncRawData> scenarioActivityReportGenerationTwoTrendingPeriodsIncRawData() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(this.jasperSessionID);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        LocalDateTime currentDateTime1 = LocalDateTime.now();
        setReportParameterByName(InputControlsEnum.START_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1.minusYears(10)));
        setReportParameterByName(InputControlsEnum.END_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1));
        setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), jasperReportUtil.getInputControls(reportValueForInputControls).getExportSetName().getOption(ExportSetEnum.ROLL_UP_A.getExportSetName()).getValue());

        JasperReportSummaryIncRawData jasperReportSummaryDaily = jasperReportUtil.generateJasperReportSummaryIncRawData(getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        setReportParameterByName(InputControlsEnum.TRENDING_PERIOD.getInputControlId(), "Yearly");

        timer.reset();
        timer.start();
        setReportParameterByName(InputControlsEnum.START_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1.minusYears(10)));
        setReportParameterByName(InputControlsEnum.END_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1));
        JasperReportSummaryIncRawData jasperReportSummaryYearly = jasperReportUtil.generateJasperReportSummaryIncRawData(getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return new ArrayList<>(Arrays.asList(jasperReportSummaryDaily, jasperReportSummaryYearly));
    }

    /**
     * Generic DTC Details Issue Count Report Generation
     *
     * @param exportSetName - String - export set to use
     * @param partToFind - String - part to find in list
     * @return - List of Elements
     */
    public List<Element> dtcDetailsIssueCountGenericTestReportGeneration(String exportSetName, String partToFind) {
        if (!exportSetName.isEmpty()) {
            setExportSetName(exportSetName);
        }
        JasperReportSummary jasperReportSummary = genericTestCore("", "");

        return jasperReportSummary.getReportHtmlPart().getElementsContainingText(partToFind).get(5).children();
    }

    /**
     * Generates report for Scenario Activity excluding Raw Data
     *
     * @return ArrayList of Jasper Report Summary instances
     */
    public ArrayList<JasperReportSummary> scenarioActivityReportGenerationTwoTrendingPeriods() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(this.jasperSessionID);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();

        LocalDateTime currentDateTime1 = LocalDateTime.now();
        setReportParameterByName(InputControlsEnum.START_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1.minusYears(10)));
        setReportParameterByName(InputControlsEnum.END_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1));
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        setReportParameterByName(InputControlsEnum.EXPORT_SET_NAME.getInputControlId(), inputControls.getExportSetName().getOption(exportSetName).getValue());

        JasperReportSummary jasperReportSummaryDaily = jasperReportUtil.generateJasperReportSummary(getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        setReportParameterByName(InputControlsEnum.TRENDING_PERIOD.getInputControlId(), "Yearly");

        timer.reset();
        timer.start();

        setReportParameterByName(InputControlsEnum.START_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1.minusYears(10)));
        setReportParameterByName(InputControlsEnum.END_DATE.getInputControlId(), DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(currentDateTime1));

        JasperReportSummary jasperReportSummaryYearly = jasperReportUtil.generateJasperReportSummary(getReportRequest());
        timer.stop();
        log.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return new ArrayList<>(Arrays.asList(jasperReportSummaryDaily, jasperReportSummaryYearly));
    }

    /**
     * Gets current month value as a String (capital at the start) of three characters
     *
     * @return String of current month
     */
    public String getCurrentMonthValue() {
        String currentMonth = LocalDateTime.now().getMonth().toString();
        return currentMonth.substring(0, 1).concat(currentMonth.substring(1, 3).toLowerCase());
    }

    /**
     * Get elements for scenario activity report tests
     *
     * @param jasperReportSummary JasperReportSummary to use
     * @return ArrayList of Jsoup Elements
     */
    public ArrayList<Element> getElementsForScenarioActivityReportTests(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().select("[class='jrxtcolfloating jrxtcolheader']");
    }

    /**
     * Sets export set name (used when different from usual setting on a certain report)
     * @param exportSetName - String export set name to set
     */
    public void setExportSetName(String exportSetName) {
        this.exportSetName = exportSetName;
    }

    /**
     * Ensures two values are almost near (within 0.03)
     *
     * @param valueOne BigDecimal
     * @param valueTwo BigDecimal
     * @return boolean
     */
    public boolean areValuesAlmostEqual(BigDecimal valueOne, BigDecimal valueTwo) {
        BigDecimal largerValue = valueOne.max(valueTwo);
        BigDecimal smallerValue = valueOne.min(valueTwo);
        BigDecimal difference = largerValue.subtract(smallerValue);
        return difference.compareTo(new BigDecimal("0.00")) >= 0 &&
            difference.compareTo(new BigDecimal("0.03")) <= 0;
    }

    /**
     * Gets sub total and total values from Assembly Details report
     *
     * @param jasperReportSummary - JasperReportSummary to use to retrieve values
     * @param elementType - String type of element
     * @param indexesToGet - List of Integers
     * @return ArrayList of BigDecimals
     */
    public ArrayList<BigDecimal> getSubTotalAndTotalValuesAssemblyDetails(JasperReportSummary jasperReportSummary, String elementType, List<Integer> indexesToGet) {
        ArrayList<BigDecimal> returnList = new ArrayList<>();
        returnList.add(convertStringToBigDecimalValue(jasperReportSummary.getReportHtmlPart().getElementsContainingText(elementType).get(12).siblingElements().get(indexesToGet.get(0)).text()));
        returnList.add(convertStringToBigDecimalValue(jasperReportSummary.getReportHtmlPart().getElementsContainingText(elementType).get(12).siblingElements().get(indexesToGet.get(1)).text()));
        returnList.add(convertStringToBigDecimalValue(jasperReportSummary.getReportHtmlPart().getElementsContainingText(elementType).get(12).siblingElements().get(indexesToGet.get(2)).text()));
        returnList.add(convertStringToBigDecimalValue(jasperReportSummary.getReportHtmlPart().getElementsContainingText(elementType).get(12).siblingElements().get(indexesToGet.get(3)).text()));
        return returnList;
    }

    /**
     * Takes in a string and returns it as a BigDecimal value
     *
     * @param valueToConvert - String to convert
     * @return BigDecimal of String value
     */
    public BigDecimal convertStringToBigDecimalValue(String valueToConvert) {
        return new BigDecimal(valueToConvert.replaceAll(",", ""));
    }

    /**
     * Gets Assembly Details values at level one
     *
     * @param jasperReportSummary - JasperReportSummary to retrieve values from
     * @return List of BigDecimal values
     */
    public List<BigDecimal> getAssemblyDetailsValuesLevelOne(JasperReportSummary jasperReportSummary) {
        List<BigDecimal> returnValues = new ArrayList<>();
        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("style", "height:15px");
        for (int i = 3; i < 9; i++) {
            returnValues.add(convertStringToBigDecimalValue(elements.get(i).children().get(23).children().get(0).text()));
        }
        return returnValues;
    }

    /**
     * Get Assembly Details total values
     *
     * @param jasperReportSummary - JasperReportSummary to retrieve values from
     * @return List of BigDecimal values
     */
    public List<BigDecimal> getAssemblyDetailsValuesTotals(JasperReportSummary jasperReportSummary) {
        List<BigDecimal> totalValues = new ArrayList<>();
        List<String> heightList = Arrays.asList("13", "15", "15");
        List<Integer> firstIndexList = Arrays.asList(0, 12, 13);
        List<Integer> secondIndexList = Arrays.asList(14, 24, 24);

        IntStream.range(0, firstIndexList.size()).forEach(
            idx -> totalValues.add(
                convertStringToBigDecimalValue(
                    jasperReportSummary.getReportHtmlPart()
                        .getElementsByAttributeValue("style", String.format("height:%spx", heightList.get(idx)))
                        .get(firstIndexList.get(idx)).children()
                        .get(secondIndexList.get(idx)).children()
                        .get(0).text()
                )
            )
        );
        return totalValues;
    }

    /**
     * Gets chart count on a report
     *
     * @param chartDataRaw - String of report data
     * @return int - chart count
     */
    public int getChartUuidCount(String chartDataRaw) {
        int count = 0;
        String[] textArray = chartDataRaw.split(" ");
        for (String s : textArray) {
            if (s.contains("chartUuid")) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Works out and returns expected percentage difference value (Upgrade Comparison and Upgrade Part Comparison Report)
     *
     * @param oldValue - Double of first value for calculations
     * @param newValue - Double of second value for calculations
     * @return - String of expected percentage difference value
     */
    public String getExpectedPercentDiffValue(Double oldValue, Double newValue) {
        DecimalFormat df = new DecimalFormat("#");

        Double newMinusOldOverhead = newValue - oldValue;
        double subSumDivideByOldOverhead = newMinusOldOverhead / oldValue;
        return df.format(Math.round(subSumDivideByOldOverhead * (Double.parseDouble("100"))));
    }

    /**
     * Generic method to get a list of elements based on their column span attribute (a common method to get values for assertion in jasper api)
     *
     * @param jasperReportSummary - JasperReportSummary to use to get elements
     * @param columnSpanValue - String of column span value to use to retrieve values
     * @return ArrayList of Elements by their column span
     */
    public ArrayList<Element> getElementsByColumnSpan(JasperReportSummary jasperReportSummary, String columnSpanValue) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", columnSpanValue);
    }

    private ArrayList<String> getScenarioCycleTimeValues(String currencyToGet) {
        return genericTestCoreCurrencyAndDateOnlyCycleTimeReport(currencyToGet)
            .getFirstChartData().getChartDataPoints()
            .stream().map(e -> e.getPropertyByName("Scenario Cycle Time (s)").getValue().toString())
            .collect(Collectors.toCollection(ArrayList::new)
            );
    }

    private ArrayList<String> spendAnalysisGenerateReportReturnValues(String currencyToUse, List<Integer> indexValues) {
        String attributeValueToUse = "colspan";
        JasperReportSummary jasperReportSummary = genericTestCoreProjectRollupAndCurrencyOnly(
            "AC CYCLE TIME VT 1",
            currencyToUse
        );

        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue(attributeValueToUse, indexValues.get(0).toString()).get(indexValues.get(1)).text());
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue(attributeValueToUse, indexValues.get(2).toString()).get(indexValues.get(3)).text());
        return assertValues;
    }

    private String getCurrentCurrencyFromAboveChartDtcDetailsOrComparisonReport(JasperReportSummary jasperReportSummary) {
        String valsToUse = jasperReportSummary.getReportHtmlPart().toString().contains("DTC Details") ? "3,6" : "2,17";
        String[] valuesToUse = valsToUse.split(",");
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", valuesToUse[0]).get(Integer.parseInt(valuesToUse[1])).text();
    }

    private String getCurrentCurrencyFromAboveChart(JasperReportSummary jasperReportSummary, boolean areBubblesPresent) {
        return areBubblesPresent
            ? getCurrentCurrency(jasperReportSummary, "2", 3)
            : getCurrentCurrency(jasperReportSummary, "3", 6);
    }

    private String getCurrentCurrency(JasperReportSummary jasperReportSummary, String indexOfItemToReturn, int indexOfReturnedItemsToUse) {
        return indexOfReturnedItemsToUse == 3
            ? jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", indexOfItemToReturn).get(indexOfReturnedItemsToUse).text()
            : jasperReportSummary.getReportHtmlPart().select("td[rowspan='2']").get(2).text();
    }

    private String getCurrencyValueFromChart(JasperReportSummary jasperReportSummary, String partName) {
        if (reportsJsonFileName.contains("Assembly")) {
            return jasperReportSummary.getReportHtmlPart()
                .getElementsByAttributeValueContaining("style", "font-size: 10px;").get(73).text();
        }

        if (partName.isEmpty()) {
            return jasperReportSummary.getReportHtmlPart()
                .getElementsByAttributeValue("colspan", "4").get(9).text();
        }

        return jasperReportSummary.getFirstChartData()
            .getChartDataPointByPartName(partName).getFullyBurdenedCost();
    }

    private String getCurrencyValueFromChartComponentCost(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsContainingText("Lifetime Cost").get(5).child(2).text();
    }

    private String getCurrencySettingValueFromChartComponentCost(JasperReportSummary jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsContainingText("Currency").get(5).text();
    }

    private String fixKeyToGetValuesBy(String keyValueToUse) {
        String[] splitVal = StringUtils.splitByCharacterTypeCamelCase(keyValueToUse);
        splitVal[0] = StringUtils.capitalize(splitVal[0]);
        return splitVal[0].concat(" ").concat(splitVal[1]);
    }
}
