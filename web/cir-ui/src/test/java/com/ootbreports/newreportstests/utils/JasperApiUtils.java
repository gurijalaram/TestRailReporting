package com.ootbreports.newreportstests.utils;

import static com.apriori.utils.TestHelper.logger;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.reports.DtcScoreEnum;
import com.apriori.utils.enums.reports.JasperCirApiPartsEnum;

import com.google.common.base.Stopwatch;
import lombok.Data;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.nodes.Element;
import utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
public class JasperApiUtils {
    private static final SoftAssertions softAssertions = new SoftAssertions();
    private ReportRequest reportRequest;
    private String reportsJsonFileName;
    private String exportSetName;
    private String jSessionId;

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
     * Generic method that sets one particular value in the input controls
     *
     * @param keyToSet String - key of the value to set
     * @param valueToSet String - value which to set
     * @return JasperReportSummary instance
     */
    public JasperReportSummary genericTestCore(String keyToSet, String valueToSet) {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        InputControl inputControls = jasperReportUtil.getInputControls();
        String currentExportSet = inputControls.getExportSetName().getOption(exportSetName).getValue();
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = !valueToSet.isEmpty()
            ? setReportParameterByName(reportRequest, Constants.INPUT_CONTROL_NAMES.get(keyToSet), valueToSet) :
            reportRequest;
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", currentExportSet);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "latestExportDate", currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummary = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        return jasperReportSummary;
    }

    /**
     * Generic test for currency in Assembly Cost Reports (both A4 and Letter)
     */
    public void genericAssemblyCostCurrencyTest() {
        JasperApiUtils jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);

        JasperReportUtil jasperReportUtil = JasperReportUtil.init(jSessionId);
        String currentDateTime = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT).format(LocalDateTime.now());

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportSetName", exportSetName);
        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, "exportDate", currentDateTime);

        Stopwatch timer = Stopwatch.createUnstarted();
        timer.start();
        JasperReportSummary jasperReportSummaryGBP = jasperReportUtil.generateJasperReportSummary(reportRequest);
        timer.stop();
        logger.debug(String.format("Report generation took: %s", timer.elapsed(TimeUnit.SECONDS)));

        String currencyValueGBP = jasperReportSummaryGBP.getReportHtmlPart().getElementsContainingText("Currency").get(6).parent().child(3).text();
        String capInvValueGBP = jasperReportSummaryGBP.getReportHtmlPart().getElementsContainingText("Capital Investments").get(6).parent().child(3).text();

        reportRequest = jasperApiUtils.setReportParameterByName(reportRequest, Constants.INPUT_CONTROL_NAMES.get("Currency"), CurrencyEnum.USD.getCurrency());
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
        String currencyAssertValue = CurrencyEnum.USD.getCurrency();
        JasperReportSummary jasperReportSummaryUsd = genericTestCore("Component Cost Currency", currencyAssertValue);

        String currentCurrencyAboveChart = getCurrentCurrencyFromAboveChart(jasperReportSummaryUsd, areBubblesPresent);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        currencyAssertValue = CurrencyEnum.GBP.getCurrency();
        JasperReportSummary jasperReportSummaryGbp = genericTestCore("Component Cost Currency", currencyAssertValue);

        currentCurrencyAboveChart = getCurrentCurrencyFromAboveChart(jasperReportSummaryGbp, areBubblesPresent);
        softAssertions.assertThat(currentCurrencyAboveChart).isEqualTo(currencyAssertValue);

        String usdCurrencyValue = areBubblesPresent
            ? getCurrencyValueFromChart(jasperReportSummaryUsd, partName)
            : getCurrencyValueFromChart(jasperReportSummaryUsd, "");
        String gbpCurrencyValue = areBubblesPresent
            ? getCurrencyValueFromChart(jasperReportSummaryGbp, partName)
            : getCurrencyValueFromChart(jasperReportSummaryGbp, "");

        softAssertions.assertThat(usdCurrencyValue).isNotEqualTo(gbpCurrencyValue);

        softAssertions.assertAll();
    }

    public void genericComponentCostCurrencyTest(String partName, boolean areBubblesPresent) {
        String currencyAssertValue = CurrencyEnum.USD.getCurrency();
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

        softAssertions.assertAll();
    }

    /**
     * Generic test to be used on any dtc report
     *
     * @param partNames - List of Strings of part names for use in the test
     * @param miscData - String array of data to be used in the test
     */
    public void genericDtcTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        int i = 0;
        for (String partName : partNames) {
            softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().get(i).getPartName()).isEqualTo(partName);
            i++;
        }

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(1).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    public void genericCostMetricCostOutlierTest(List<String> miscData) {
        JasperReportSummary jasperReportSummary = genericTestCore(miscData.get(0), miscData.get(1));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscData.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(4).toString().contains(miscData.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    public void genericCostMetricCostOutlierDetailsTest(List<String> partList, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        int i = 0;
        List<Element> partElementsFromPage = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "_jrHyperLink ReportExecution");
        for (String partName : partList) {
            softAssertions.assertThat(partElementsFromPage.get(i).child(0).text()).isEqualTo(partName);
            i++;
        }
        jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("class", "_jrHyperLink ReportExecution").get(0).children();

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText(miscDataList.get(0).split(" ")[0]);
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(5).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for any dtc details report
     *
     * @param partNames - list of Strings containing the parts to use
     * @param miscData - String array of data to be used in the test
     */
    public void genericDtcDetailsTest(List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
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

        softAssertions.assertAll();
    }

    /**
     * Generic test for dtc score
     *
     * @param areBubblesPresent - boolean which specifies what report type is being used
     * @param partNames - List of Strings of part names
     * @param miscData - String array of data to be used in the test
     */
    public void genericDtcScoreTest(boolean areBubblesPresent, List<String> partNames, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        String assertValue = miscDataList.get(1).isEmpty() ? DtcScoreEnum.ALL.getDtcScoreName() : miscDataList.get(1);

        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        if (areBubblesPresent) {
            int i = 0;
            for (String partName : partNames) {
                softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().get(i).getPartName()).isEqualTo(partName);
                i++;
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

        softAssertions.assertAll();
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

        int i = 0;
        for (String partName : partNames) {
            partName = miscDataList.get(1).equals(ProcessGroupEnum.CASTING_SAND.getProcessGroup()) ? partName.replace(" (Initial)", "") : partName;
            softAssertions.assertThat(jasperReportSummary.getFirstChartData().getChartDataPoints().get(i).getPartName()).isEqualTo(partName);
            i++;
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
        List<String> miscDataList = Arrays.asList(miscData);
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

        softAssertions.assertAll();
    }

    /**
     * Generic test for minimum annual spend input control on a dtc report
     *
     * @param areBubblesPresent - boolean to specify the type of report
     */
    public void genericMinAnnualSpendDtcTest(boolean areBubblesPresent) {
        String minimumAnnualSpendValue = "7820000";
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

        softAssertions.assertAll();
    }

    /**
     * Generic test for minimum annual spend input control on a dtc details report
     */
    public void genericMinAnnualSpendDtcDetailsTest() {
        String minimumAnnualSpendValue = "7820000";
        JasperReportSummary jasperReportSummary = genericTestCore("Minimum Annual Spend", minimumAnnualSpendValue);

        String minAnnualSpendValue = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "3").get(15).text();
        softAssertions.assertThat(minAnnualSpendValue).isEqualTo("10,013,204.23");
        softAssertions.assertThat(minAnnualSpendValue).isNotEqualTo(minimumAnnualSpendValue);

        softAssertions.assertAll();
    }

    /**
     * Generic test for sort order input control on a dtc report
     *
     * @param miscData - List of Strings of data for the test
     * @param partNames - List of Strings of part names for the test
     * @param assertFigures - List of Doubles for the assertion of Annual Spend values
     */
    public void genericSortOrderDtcTest(List<String> partNames, List<Double> assertFigures, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperReportSummary jasperReportSummary = genericTestCore(miscDataList.get(0), miscDataList.get(1));

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().get(0).getPartName()).isEqualTo(partNames.get(0));
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPoints().get(1).getPartName()).isEqualTo(partNames.get(1));

        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName(partNames.get(0)).getPropertyByName("annualSpend").getValue()).isEqualTo(assertFigures.get(0));
        softAssertions.assertThat(jasperReportSummary.getChartData().get(0).getChartDataPointByPartName(partNames.get(1)).getPropertyByName("annualSpend").getValue()).isEqualTo(assertFigures.get(1));

        List<Element> elements = jasperReportSummary.getReportHtmlPart().getElementsContainingText("Sort");
        List<Element> tdResultElements = elements.stream().filter(element -> element.toString().startsWith("<td")).collect(Collectors.toList());
        softAssertions.assertThat(tdResultElements.get(0).parent().children().get(7).toString().contains(miscDataList.get(1))).isEqualTo(true);

        softAssertions.assertAll();
    }

    /**
     * Generic test for sort order input control on a dtc report
     *
     * @param partNames - List of Strings of part names for the test
     * @param assertValues - List of Strings for the assertion of Annual Spend values
     * @param miscData - String array of data to be used in the test
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

        softAssertions.assertAll();
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
            // get by usd or gbp then number 7, find a better way for component cost report?
            //jasperReportSummary.getReportHtmlPart().getElementsContainingText("Currency").get(5)
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
