package com.ootbreports.newreportstests.utils;

import static com.apriori.utils.TestHelper.logger;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CirApiEnum;
import com.apriori.cirapi.entity.enums.InputControlsEnum;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;

import com.google.common.base.Stopwatch;
import lombok.Data;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
public class JasperApiUtils {
    private static final SoftAssertions softAssertions = new SoftAssertions();
    private CirApiEnum reportValueForInputControls;
    private ReportRequest reportRequest;
    private String reportsJsonFileName;
    private String exportSetName;
    private String processGroupName;
    private String jSessionId;

    /**
     * Default constructor for this class
     *
     * @param jSessionId - String for authentication/session
     * @param exportSetName - String of the export set which should be set
     * @param reportsJsonFileName - String of the right json file to use to be sent to the api
     */
    public JasperApiUtils(String jSessionId, String exportSetName, String reportsJsonFileName, CirApiEnum reportNameForInputControls) {
        this.reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);
        this.reportValueForInputControls = reportNameForInputControls;
        this.jSessionId = jSessionId;
        this.exportSetName = exportSetName;
        this.reportsJsonFileName = reportsJsonFileName;
    }

    /**
     * Second constructor for this class (not default)
     *
     * @param jSessionId - String for authentication/session
     * @param exportSetName - String of the export set which should be set
     * @param processGroup - String of process group which should be set
     * @param reportsJsonFileName - String of the right json file to use to be sent to the api
     */
    public JasperApiUtils(String jSessionId, String exportSetName, ProcessGroupEnum processGroup, String reportsJsonFileName) {
        this.reportRequest = ReportRequest.initFromJsonFile(reportsJsonFileName);
        this.jSessionId = jSessionId;
        this.exportSetName = exportSetName;
        this.processGroupName = processGroup.getProcessGroup();
        this.reportsJsonFileName = reportsJsonFileName;
    }

    /**
     * Generic method that sets one particular value in the input controls
     *
     * @param keyToSet String - key of the value to set
     * @param valueToSet String - value which to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCore(String keyToSet, String valueToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();

        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        if (!valueToSet.isEmpty()) {
            setReportParameterByName(InputControlsEnum.valueOf(keyToSet.toUpperCase().replace(" ", "_")).getInputControlId(), valueToSet);
        }

        if (processGroupName != null) {
            String processGroupId = inputControls.getProcessGroup().getOption(processGroupName).getValue();
            setReportParameterByName("processGroup", processGroupId);
        }

        setReportParameterByName("exportSetName", currentExportSet);
        setReportParameterByName("latestExportDate", currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic method for testing currency where export set is not relevant
     *
     * @param currencyToSet - currency that is to be set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreCurrencyOnly(String currencyToSet) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);

        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyToSet);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());
        setReportParameterByName("exportDate", currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic test for reports that require project rollup and currency only to be specified
     *
     * @param projectRollupName - String of project rollup to use
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCoreProjectRollupAndCurrencyOnly(String currencyString, String projectRollupName) {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls(reportValueForInputControls);

        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), currencyString);
        String projectRollupValue = inputControls.getProjectRollup().getOption(projectRollupName).getValue();
        setReportParameterByName(InputControlsEnum.PROJECT_ROLLUP.getInputControlId(), projectRollupValue);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic test for currency in Assembly Cost Reports (both A4 and Letter)
     */
    public void genericAssemblyCostCurrencyTest() {
        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        setReportParameterByName("exportSetName", exportSetName);
        setReportParameterByName("exportDate", currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummaryGBP = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s seconds", timer.elapsed(TimeUnit.SECONDS)));

        String currencyValueGBP = jasperReportSummaryGBP.getReportHtmlPart().getElementsContainingText("Currency").get(6).parent().child(3).text();
        String capInvValueGBP = jasperReportSummaryGBP.getReportHtmlPart().getElementsContainingText("Capital Investments").get(6).parent().child(3).text();

        setReportParameterByName(InputControlsEnum.CURRENCY.getInputControlId(), CurrencyEnum.USD.getCurrency());
        JasperReportSummary jasperReportSummaryUSD = jasperReportUtil.generateJasperReportSummary(reportRequest);

        String currencyValueUSD = jasperReportSummaryUSD.getReportHtmlPart().getElementsContainingText("Currency").get(6).parent().child(3).text();
        String capInvValueUSD = jasperReportSummaryUSD.getReportHtmlPart().getElementsContainingText("Capital Investments").get(6).parent().child(3).text();

        softAssertions.assertThat(currencyValueGBP).isNotEqualTo(currencyValueUSD);
        softAssertions.assertThat(capInvValueGBP).isNotEqualTo(capInvValueUSD);
    }

    /**
     * Generic test of currency code for use on a dtc report
     *
     * @param partName - String of partName which is to be used
     * @param areBubblesPresent - boolean which states if bubbles are present or not
     */
    public void genericDtcCurrencyTest(String partName, boolean areBubblesPresent) {
        /*String currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = genericTestCore("Currency", currencyAssertValue);

        String currentCurrencyAboveChart = getCurrentCurrencyFromAboveChart(jasperReportSummaryUsd, areBubblesPresent);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = genericTestCore("Currency", currencyAssertValue);

        currentCurrencyAboveChart = getCurrentCurrencyFromAboveChart(jasperReportSummaryGbp, areBubblesPresent);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        String usdCurrencyValue = areBubblesPresent
            ? getCurrencyValueFromChart(jasperReportSummaryUsd, partName)
            : getCurrencyValueFromChart(jasperReportSummaryUsd, "");
        String gbpCurrencyValue = areBubblesPresent
            ? getCurrencyValueFromChart(jasperReportSummaryGbp, partName)
            : getCurrencyValueFromChart(jasperReportSummaryGbp, "");

        softAssertions.assertThat(usdCurrencyValue).isNotEqualTo(gbpCurrencyValue);

        softAssertions.assertAll();*/
    }

    /**
     * Generic method that tests currency functionality in the Scenario Comparison report
     */
    public void scenarioComparisonCurrencyTest() {
        /*String currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = genericTestCore("Currency", currencyAssertValue);

        List<Element> currencyGBPSettingElementList = jasperReportSummaryGbp.getReportHtmlPart()
            .getElementsContainingText("Currency").get(5).children();
        List<String> currencySettingGBP = Arrays.asList(
            currencyGBPSettingElementList.get(3).text(),
            currencyGBPSettingElementList.get(4).text(),
            currencyGBPSettingElementList.get(5).text()
        );

        List<Element> currencyValueGBPElementList = jasperReportSummaryGbp.getReportHtmlPart()
            .getElementsContainingText("FULLY BURDENED COST").get(6).parent().children();
        List<String> currencyValuesFbcGBP = Arrays.asList(
            currencyValueGBPElementList.get(3).text(),
            currencyValueGBPElementList.get(4).text(),
            currencyValueGBPElementList.get(5).text()
        );
        currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = genericTestCore("Currency", currencyAssertValue);

        List<Element> currencySettingUSDElementList = jasperReportSummaryUsd.getReportHtmlPart()
            .getElementsContainingText("Currency").get(5).children();
        List<String> currencySettingUSD = Arrays.asList(
            currencySettingUSDElementList.get(3).text(),
            currencySettingUSDElementList.get(4).text(),
            currencySettingUSDElementList.get(5).text()
        );

        List<Element> currencyValueUSDElementList = jasperReportSummaryUsd.getReportHtmlPart()
            .getElementsContainingText("FULLY BURDENED COST").get(6).parent().children();
        List<String> currencyValuesFbcUSD = Arrays.asList(
            currencyValueUSDElementList.get(3).text(),
            currencyValueUSDElementList.get(4).text(),
            currencyValueUSDElementList.get(5).text()
        );

        int i = 0;
        for (String currentCurrencySettingGBP : currencySettingGBP) {
            softAssertions.assertThat(currentCurrencySettingGBP).isNotEqualTo(currencySettingUSD.get(i));
            i++;
        }

        int j = 0;
        for (String currencyCurrencyValueGBP : currencyValuesFbcGBP) {
            softAssertions.assertThat(currencyCurrencyValueGBP).isNotEqualTo(currencyValuesFbcUSD.get(j));
            j++;
        }

        softAssertions.assertAll();*/
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
        /*String currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = genericTestCore("Component Cost Currency", currencyAssertValue);

        String currentCurrencyAboveChart = getCurrencySettingValueFromChartComponentCost(jasperReportSummaryUsd).substring(10);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = genericTestCore("Component Cost Currency", currencyAssertValue);

        currentCurrencyAboveChart = getCurrencySettingValueFromChartComponentCost(jasperReportSummaryGbp).substring(10);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        String usdCurrencyValue = getCurrencyValueFromChartComponentCost(jasperReportSummaryUsd);
        String gbpCurrencyValue = getCurrencyValueFromChartComponentCost(jasperReportSummaryGbp);

        softAssertions.assertThat(usdCurrencyValue).isNotEqualTo(gbpCurrencyValue);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test to be used on any dtc report
     *
     * @param partNames - List of Strings of part names for use in the test
     * @param miscData - String array of data to be used in the test
     */
    public void genericDtcTest(List<String> partNames, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        for (String partName : partNames) {
            softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().toString().contains(partName)).isEqualTo(true);
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for Cost Metric within Cost Outlier Report
     *
     * @param miscData - List of Strings of relevant data to use in the test
     */
    public void genericCostMetricCostOutlierTest(List<String> miscData) {
        /*JasperReportSummary jasperReportSummary = genericTestCore(miscData.get(0), miscData.get(1));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscData.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(4).toString().contains(miscData.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for Cost Metric within Cost Outlier Details Report
     *
     * @param miscData - List of Strings of relevant data to use in the test
     */
    public void genericCostMetricCostOutlierDetailsTest(List<String> partList, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partElementsFromPage = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "_jrHyperLink ReportExecution");
        for (String partName : partList) {
            softAssertions.assertThat(partElementsFromPage.toString().contains(partName)).isEqualTo(true);
        }
        jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "_jrHyperLink ReportExecution").get(0).children();

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(5).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for any dtc details report
     *
     * @param partNames - list of Strings containing the parts to use
     * @param miscData - String array of data to be used in the test
     */
    public void genericDtcDetailsTest(List<String> partNames, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");

        int i = 6;
        for (String partName : partNames) {
            String assertPartName = partName.contains("Initial") ? partName.replace(" (Initial)", "") : partName.replace(" (Bulkload)", "");
            softAssertions.assertThat(partNumberElements.get(i).toString().contains(assertPartName)).isEqualTo(true);
            i = !partName.equals(JasperCirApiPartsEnum.PLASTIC_MOULDED_CAP_THICKPART.getPartName()) ? i + 1 : i;
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        int itemIndex = miscDataList.get(0).equals("DTC Score") ? 2 : 1;
        softAssertions.assertThat(tdResultElements.get(itemIndex).parent().children().toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for dtc score
     *
     * @param areBubblesPresent - boolean which specifies what report type is being used
     * @param partNames - List of Strings of part names
     * @param miscData - String array of data to be used in the test
     */
    public void genericDtcScoreTest(boolean areBubblesPresent, List<String> partNames, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
        String assertValue = miscDataList.get(1).isEmpty() ? DtcScoreEnum.ALL.getDtcScoreName() : miscDataList.get(1);

        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

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

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0)).get(6)
            .parent().children().get(11).text()).isEqualTo(assertValue);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test of process group input control on any dtc report
     *
     * @param partNames - List of Strings of part names for the test
     * @param miscData - String array of data to be used in the test
     */
    public void genericProcessGroupDtcTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        String pgToSet = miscDataList.get(1).equals(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            || miscDataList.get(1).equals(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            ? miscDataList.get(1) : "";
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), pgToSet);

        for (String partName : partNames) {
            partName = miscDataList.get(1).equals(ProcessGroupEnum.CASTING_SAND.getProcessGroup()) ? partName.replace(" (Initial)", "") : partName;
            softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().toString().contains(partName)).isEqualTo(true);
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for process group on a dtc details report
     *
     * @param partNames - List of Strings of part names for the test
     * @param miscData - String array of data to be used in the test
     */

    public void genericProcessGroupDtcDetailsTest(List<String> partNames, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        List<Element> partNumberElements = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "5");
        int i = 6;
        for (String partName : partNames) {
            String partNameWithoutScenarioName = partName.contains(" (Initial)") ? partName.replace(" (Initial)", "") : partName.replace(" (sand casting)", "");
            partNameWithoutScenarioName = partName.contains(" (Bulkload)") ? partName.replace(" (Bulkload)", "") : partNameWithoutScenarioName;
            softAssertions.assertThat(partNumberElements.get(i).toString().contains(partNameWithoutScenarioName)).isEqualTo(true);
            i++;
        }

        softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsContainingText("Process Group").get(6)
            .siblingElements().get(10).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for minimum annual spend input control on a dtc report
     *
     * @param areBubblesPresent - boolean to specify the type of report
     */
    public void genericMinAnnualSpendDtcTest(boolean areBubblesPresent) {
        /*String minimumAnnualSpendValue = "7820000";
        JasperReportSummary jasperReportSummary = genericTestCore("Minimum Annual Spend", minimumAnnualSpendValue);

        if (areBubblesPresent) {
            softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().size()).isEqualTo(1);
            softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().get(0).getAnnualSpend()).isNotEqualTo(minimumAnnualSpendValue);
        } else {
            for (int i = 0; i < 6; i++) {
                softAssertions.assertThat(jasperReportSummary.getChartData().get(i).getChartDataPoints().size()).isEqualTo(1);
                softAssertions.assertThat(jasperReportSummary.getChartData().get(i).getChartDataPoints().get(0).getAnnualSpend()).isNotEqualTo(minimumAnnualSpendValue);
            }
        }

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for minimum annual spend input control on a dtc details report
     */
    public void genericMinAnnualSpendDtcDetailsTest() {
        /*String minimumAnnualSpendValue = "7820000";
        JasperReportSummary jasperReportSummary = genericTestCore("Minimum Annual Spend", minimumAnnualSpendValue);

        String minAnnualSpendValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "3").get(15).text();
        softAssertions.assertThat(minAnnualSpendValue).isEqualTo("10,013,204.23");
        softAssertions.assertThat(minAnnualSpendValue).isNotEqualTo(minimumAnnualSpendValue);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for sort order input control on a dtc report
     *
     * @param miscData - List of Strings of data for the test
     * @param partNames - List of Strings of part names for the test
     * @param assertFigures - List of Doubles for the assertion of Annual Spend values
     */
    public void genericSortOrderDtcTest(List<String> partNames, List<Double> assertFigures, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().get(0).getPartName()).isEqualTo(partNames.get(0));
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().get(1).getPartName()).isEqualTo(partNames.get(1));

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName(partNames.get(0)).getPropertyByName("annualSpend").getValue()).isEqualTo(assertFigures.get(0));
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName(partNames.get(1)).getPropertyByName("annualSpend").getValue()).isEqualTo(assertFigures.get(1));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Generic test for sort order input control on a dtc report
     *
     * @param partNames - List of Strings of part names for the test
     * @param assertValues - List of Strings for the assertion of Annual Spend values
     * @param miscData - String array of data to be used in the test
     */
    public void genericSortOrderDtcDetailsTest(List<String> partNames, List<String> assertValues, String... miscData) {
        /*List<String> miscDataList = Arrays.asList(miscData);
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
            i = assertValues.get(1).startsWith("7") ? 15 : 9;
            String colspanToUse = assertValues.get(1).startsWith("7") ? "3" : "4";
            for (String assertValue : assertValues) {
                softAssertions.assertThat(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", colspanToUse).get(i).text()).isEqualTo(assertValue);
                i = assertValues.get(1).startsWith("7") ? 20 : 11;
            }
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();*/
    }

    /**
     * Sets a particular report parameter within the ReportRequest instance
     *
     * @param valueToGet String the key of the value to set
     * @param valueToSet String the value which to set
     * @return ReportRequest instance with specified parameter set
     */
    public void setReportParameterByName(String valueToGet, String valueToSet) {
        this.reportRequest.getParameters().getReportParameterByName(valueToGet)
            .setValue(Collections.singletonList(valueToSet));
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

        List<Element> gbpSpanElements = generateAndReturnReportCurrencyOnly(
            CurrencyEnum.GBP.getCurrency()).getReportHtmlPart()
            .getElementsByAttributeValue(attributeNameId, tableId).get(0)
            .getElementsByTag(tagName);

        List<Element> usdSpanElements = generateAndReturnReportCurrencyOnly(
            CurrencyEnum.USD.getCurrency()).getReportHtmlPart()
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

    private ArrayList<String> getScenarioCycleTimeValues(String currencyToGet) {
        return generateAndReturnReportCurrencyOnly(currencyToGet)
            .getFirstChartData().getChartDataPoints()
            .stream().map(e -> e.getPropertyByName("Scenario Cycle Time (s)").getValue().toString())
            .collect(Collectors.toCollection(ArrayList::new)
            );
    }

    private JasperReportSummary generateAndReturnReportCurrencyOnly(String currency) {
        return genericTestCoreCurrencyOnly(currency);
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
}
