package com.apriori.cir.api.utils;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.enums.ReportChartType;
import com.apriori.cir.api.models.enums.InputControlsEnum;
import com.apriori.cir.api.models.request.ReportExportRequest;
import com.apriori.cir.api.models.request.ReportRequest;
import com.apriori.cir.api.models.response.ChartData;
import com.apriori.cir.api.models.response.ChartDataPoint;
import com.apriori.cir.api.models.response.ChartDataPointProperty;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.ReportStatusResponse;
import com.apriori.shared.util.enums.ReportNamesEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class JasperReportUtil {
    private static HashMap<String, Integer> inputControlsIndexMapAssemblyCostA4;
    private static HashMap<String, Integer> inputControlsIndexMapAssemblyDetails;
    private static HashMap<String, Integer> inputControlsIndexMapComponentCost;
    private static HashMap<String, Integer> inputControlsIndexMapScenarioComparison;
    private static HashMap<String, Integer> inputControlsIndexMapUpgradePartComparison;
    private static HashMap<String, JasperApiInputControlsPathEnum> inputControlsModifiedUrlMap;
    private static LinkedHashMap<String, String> assemblyCostA4ICModifiedNamesValuesMap;
    private static LinkedHashMap<String, String> assemblyDetailsICModifiedNamesValuesMap;
    private static LinkedHashMap<String, String> componentCostICModifiedNamesValuesMap;
    private static LinkedHashMap<String, String> scenarioComparisonICModifiedNamesValuesMap;
    private static LinkedHashMap<String, String> upgradePartComparisonICModifiedNamesValuesMap;
    private static HashMap<String, LinkedHashMap<String, String>> inputControlsModifiedValueNameMasterList;
    private static HashMap<String, HashMap<String, Integer>> inputControlsIndexMapMaster;
    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestEntity requestEntity;
    private long waitTime = 30;

    private String jasperSessionValue = "JSESSIONID=%s";


    public static JasperReportUtil init(final String jasperSessionId) {
        initialiseAllHashMaps();
        return new JasperReportUtil(jasperSessionId);
    }

    public JasperReportUtil(final String jasperSessionId) {
        this.jasperSessionValue = String.format(jasperSessionValue, jasperSessionId);
    }

    // TODO z: fix it threads

    /**
     * Gets input controls as they initially are
     *
     * @param value - enum of report input controls path to use
     * @return InputControl - object of the various input controls
     */
    public InputControl getInputControls(JasperApiInputControlsPathEnum value) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(value)
            .returnType(InputControl.class)
            .headers(initHeadersWithJSession())
            .inlineVariables("%20")
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        if (value.toString().startsWith("SHEET")) {
            requestEntity.inlineVariables("%20", "%20");
        }

        ResponseWrapper<InputControl> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    /**
     * Gets modified Input Controls (for tests that require IC filtering assertions)
     *
     * @param klass - Class of type to return
     * @param setCriteria - boolean to determine if criteria should be set
     * @param miscData - rest of data (report name, IC to set, IC value to set and export set name)
     * @param <T> - generic so it will work for multiple reports
     * @return Response Wrapper instance with type specified in klass parameter
     */
    public <T> ResponseWrapper<T> getInputControlsModified(Class<T> klass, boolean setCriteria, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperApiInputControlsPathEnum urlValue = inputControlsModifiedUrlMap.get(miscDataList.get(0));

        List<UpdatedInputControlsPayloadInputsItem> genericInputList = createGenericInputList(miscDataList.get(0));

        if (urlValue.getEndpointString().contains("scenarioComparison") && setCriteria) {
            genericInputList = setCreatedByLastModifiedByCriteria(genericInputList, miscDataList.get(1), "bhegan");
        }

        HashMap<String, Integer> icMapToUse = inputControlsIndexMapMaster.get(miscDataList.get(0));

        if (urlValue.getEndpointString().contains("assemblyDetails") && setCriteria) {
            genericInputList.get(icMapToUse.get("exportSetName")).setCriteria("top-level-0");
        }

        if (!miscDataList.get(1).isEmpty() && !miscDataList.get(2).isEmpty()) {
            genericInputList.get(icMapToUse.get(miscDataList.get(1))).setValue(Collections.singletonList(miscDataList.get(2)));
        }

        if (!miscDataList.get(miscDataList.size() - 1).isEmpty()) {
            genericInputList.get(icMapToUse.get(InputControlsEnum.EXPORT_SET_NAME.getInputControlId()))
                .setValue(Collections.singletonList(miscDataList.get(miscDataList.size() - 1)));
        }

        ReportParameter reportParameter = new ReportParameter();
        reportParameter.reportParameter.addAll(genericInputList);

        requestEntity = new RequestEntity()
            .body(reportParameter)
            .endpoint(urlValue)
            .returnType(klass)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        return HTTPRequest.build(requestEntity).post();
    }

    public <T> ResponseWrapper<T> getInputControlsModifiedUpgradePartComparison(Class<T> klass, boolean setCriteria, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperApiInputControlsPathEnum urlValue = inputControlsModifiedUrlMap.get(miscDataList.get(0));

        List<UpdatedInputControlsPayloadInputsItem> genericInputList = createGenericInputList(miscDataList.get(0));

        if (urlValue.getEndpointString().contains("scenarioComparison") && setCriteria) {
            genericInputList = setCreatedByLastModifiedByCriteria(genericInputList, miscDataList.get(1), "bhegan");
        }

        HashMap<String, Integer> icMapToUse = inputControlsIndexMapMaster.get(miscDataList.get(0));

        if (urlValue.getEndpointString().contains("assemblyDetails") && setCriteria) {
            genericInputList.get(icMapToUse.get("exportSetName")).setCriteria("top-level-0");
        }

        if (!miscDataList.get(1).isEmpty() && !miscDataList.get(2).isEmpty()) {
            genericInputList.get(icMapToUse.get(miscDataList.get(1))).setValue(Collections.singletonList(miscDataList.get(2)));
        }

        if (miscDataList.size() > 4) {
            if (!miscDataList.get(3).isEmpty() && !miscDataList.get(4).isEmpty()) {
                genericInputList.get(icMapToUse.get(miscDataList.get(3))).setValue(Collections.singletonList(miscDataList.get(4)));
            }
        }

        if (miscDataList.size() > 6) {
            if (!miscDataList.get(5).isEmpty() && !miscDataList.get(6).isEmpty()) {
                genericInputList.get(icMapToUse.get(miscDataList.get(5))).setValue(Collections.singletonList(miscDataList.get(6)));
            }
        }

        if (!miscDataList.get(miscDataList.size() - 1).isEmpty() && miscDataList.get(miscDataList.size() - 2).equals(InputControlsEnum.EXPORT_SET_NAME.getInputControlId())) {
            genericInputList.get(icMapToUse.get(InputControlsEnum.EXPORT_SET_NAME.getInputControlId()))
                .setValue(Collections.singletonList(miscDataList.get(miscDataList.size() - 1)));
        }

        ReportParameter reportParameter = new ReportParameter();
        reportParameter.reportParameter.addAll(genericInputList);

        requestEntity = new RequestEntity()
            .body(reportParameter)
            .endpoint(urlValue)
            .returnType(klass)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        if (urlValue.toString().contains("UPGRADE")) {
            requestEntity.inlineVariables("%20");
        }

        return HTTPRequest.build(requestEntity).post();
    }

    public <T> ResponseWrapper<T> getInputControlsModifiedUpgradePartComparisonSetTwoExportSets(Class<T> klass, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperApiInputControlsPathEnum urlValue = inputControlsModifiedUrlMap.get(miscDataList.get(0));

        List<UpdatedInputControlsPayloadInputsItem> genericInputList = createGenericInputList(miscDataList.get(0));

        HashMap<String, Integer> icMapToUse = inputControlsIndexMapMaster.get(miscDataList.get(0));

        genericInputList.get(icMapToUse.get(InputControlsEnum.EXPORT_SET_NAME.getInputControlId()))
            .setValue(Arrays.asList(miscDataList.get(2), miscDataList.get(3)));

        ReportParameter reportParameter = new ReportParameter();
        reportParameter.reportParameter.addAll(genericInputList);

        requestEntity = new RequestEntity()
            .body(reportParameter)
            .endpoint(urlValue)
            .returnType(klass)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        if (urlValue.toString().contains("UPGRADE")) {
            requestEntity.inlineVariables("%20");
        }

        return HTTPRequest.build(requestEntity).post();
    }

    public <T> ResponseWrapper<T> getInputControlsModifiedAssDetCreatedAndLastModifiedBy(Class<T> klass, boolean setCriteria, String... miscData) {
        List<String> miscDataList = Arrays.asList(miscData);
        JasperApiInputControlsPathEnum urlValue = inputControlsModifiedUrlMap.get(miscDataList.get(0));

        List<UpdatedInputControlsPayloadInputsItem> genericInputList = createGenericInputList(miscDataList.get(0));

        HashMap<String, Integer> icMapToUse = inputControlsIndexMapMaster.get(miscDataList.get(0));

        if (urlValue.getEndpointString().contains("assemblyDetails") && setCriteria) {
            genericInputList.get(icMapToUse.get("createdBy")).setCriteria("Stephen");
            genericInputList.get(icMapToUse.get("lastModifiedBy")).setCriteria("Bob");
        }

        if (!miscDataList.get(1).isEmpty() && !miscDataList.get(2).isEmpty()) {
            genericInputList.get(icMapToUse.get(miscDataList.get(1))).setValue(Collections.singletonList(miscDataList.get(2)));
        }

        if (!miscDataList.get(3).isEmpty()) {
            genericInputList.get(icMapToUse.get(InputControlsEnum.EXPORT_SET_NAME.getInputControlId()))
                .setValue(Collections.singletonList(miscDataList.get(3)));
        }

        ReportParameter reportParameter = new ReportParameter();
        reportParameter.reportParameter.addAll(genericInputList);

        RequestEntity requestEntity = new RequestEntity()
            .body(reportParameter)
            .endpoint(urlValue)
            .returnType(klass)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Generates jasper report summary
     *
     * @param reportRequest - ReportRequest instance to use in api request
     * @return JasperReportSummary instance that contains returned data
     */
    public JasperReportSummary generateJasperReportSummary(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        String requestId = response.getRequestId();
        String reportId = exportedReport.getId();

        return JasperReportSummary.builder()
            .reportHtmlPart(this.getReportHtmlData(requestId,
                reportId)
            )
            .chartData(this.getReportChartData(requestId,
                reportId)
            )
            .build();
    }

    /**
     * Generates jasper report summary including all data
     *
     * @param reportRequest - ReportRequest instance to use in api request
     * @return JasperReprotSummaryIncRawData instance containing all returned data
     */
    public JasperReportSummaryIncRawData generateJasperReportSummaryIncRawData(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        String requestId = response.getRequestId();
        String reportId = exportedReport.getId();

        return JasperReportSummaryIncRawData.builder()
            .reportHtmlPart(this.getReportHtmlData(requestId,
                reportId)
            )
            .chartData(this.getReportChartData(requestId,
                reportId)
            )
            .chartDataRaw(this.getReportChartDataRaw(requestId,
                reportId))
            .build();
    }

    /**
     * Generates jasper report summary including raw data as a string
     *
     * @param reportRequest - ReportRequest instance containing data to use in api request
     * @return JasperReportSummaryIncRawDataAsString - instance of data returned from api
     */
    public JasperReportSummaryIncRawDataAsString generateJasperReportSummaryIncRawDataAsString(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        this.waitUntilReportReady(response.getRequestId(),
            exportedReport.getId());

        String requestId = response.getRequestId();
        String reportId = exportedReport.getId();

        return JasperReportSummaryIncRawDataAsString.builder()
            .reportHtmlPart(this.getReportHtmlData(requestId,
                reportId)
            )
            .chartData(this.getReportChartData(requestId,
                reportId)
            )
            .chartDataRawAsString(this.getReportChartDataRawAsString(requestId,
                reportId))
            .build();
    }

    /**
     * Polling method that waits until a specific report request is completed before returning
     *
     * @param requestId - String of id of the particular request
     * @param exportId - String of id of the export
     */
    @SneakyThrows
    private void waitUntilReportReady(String requestId, String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_STATUS_BY_REQUEST_EXPORT_IDs)
            .returnType(null)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        long initialTime = System.currentTimeMillis() / 1000;

        String responseBodyText;
        do {
            responseBodyText = HTTPRequest.build(requestEntity).get().getBody();
        } while (!responseBodyText.contains("ready")
            && !responseBodyText.contains("failed")
            && ((System.currentTimeMillis() / 1000) - initialTime) < waitTime);
    }

    private ReportStatusResponse generateReport(ReportRequest reportRequest) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_EXECUTIONS)
            .returnType(ReportStatusResponse.class)
            .headers(initHeadersWithJSession())
            .body(reportRequest)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    private ReportStatusResponse doReportExport(ReportStatusResponse response) {
        RequestEntity doExportRequest = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_EXPORT_BY_REQUEST_ID)
            .returnType(ReportStatusResponse.class)
            .inlineVariables(response.getRequestId())
            .headers(initHeadersWithJSession())
            .body(ReportExportRequest.initFromJsonFile())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> exportDataResponseWrapper = HTTPRequest.build(doExportRequest).post();

        return exportDataResponseWrapper.getResponseEntity();
    }

    @SneakyThrows
    private Document getReportHtmlData(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs)
            .returnType(InputStream.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        InputStream htmlData = (InputStream) HTTPRequest.build(requestEntity).get().getResponseEntity();

        return Jsoup.parse(htmlData, "UTF-8", "/aPriori/reports/");
    }

    @SneakyThrows
    private List<ChartData> getReportChartData(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return parseJsonResponse(HTTPRequest.build(requestEntity).get()
            .getBody()
        );
    }

    @SneakyThrows
    private ReportComponentsResponse getReportChartDataRaw(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs)
            .returnType(ReportComponentsResponse.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return (ReportComponentsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    @SneakyThrows
    private String getReportChartDataRawAsString(final String requestId, final String exportId) {
        RequestEntity requestEntity = new RequestEntity()
            .endpoint(JasperApiInputControlsPathEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs)
            .returnType(ReportComponentsResponse.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    @SneakyThrows
    private List<ChartData> parseJsonResponse(final String jsonResponse) {
        final JsonNode dataNode = objectMapper.readTree(jsonResponse);
        List<ChartData> parsedChartData = new ArrayList<>();

        for (JsonNode node : dataNode) {
            ChartData chartData = new ChartData();
            JsonNode chartTypeNode = node.findValue("charttype");

            if (chartTypeNode == null) {
                log.warn("Chart type is not present in JSON response.");
                continue;
            }

            ReportChartType reportChartType = ReportChartType.get(chartTypeNode.asText());

            if (reportChartType == null) {
                log.warn("Failed to parse chart response. Chart type {} ", chartTypeNode.asText());
                continue;
            }

            chartData.setChartType(reportChartType);
            chartData.setChartDataPoints(
                this.parseChart(reportChartType, node)
            );

            parsedChartData.add(chartData);
        }

        return parsedChartData;
    }

    private List<ChartDataPoint> parseChart(final ReportChartType chartType, final JsonNode dataNode) {
        switch (chartType) {
            case BUBBLE_SCATTER:
                return parseBubbleChart(dataNode);
            case BAR:
            case STACKED_BAR:
                return parseStackedBar(dataNode);
            default:
                throw new IllegalArgumentException("Failed to parse chart: " + chartType);
        }
    }

    @SneakyThrows
    private List<ChartDataPoint> parseBubbleChart(JsonNode dataNode) {

        dataNode = dataNode.findValue("series");

        if (dataNode != null) {
            dataNode = dataNode.findValue("data");
        }

        return dataNode != null ? objectMapper.readerFor(new TypeReference<List<ChartDataPoint>>() {
        }).readValue(dataNode) : null;
    }

    @SneakyThrows
    private List<ChartDataPoint> parseStackedBar(JsonNode dataNode) {

        List<ChartDataPoint> mappedChartDataPoints = new ArrayList<>();

        JsonNode partNames = dataNode.findValue("xCategories");
        JsonNode chartValues = dataNode.findValue("series");

        for (int i = 0; i < partNames.size(); i++) {
            ChartDataPoint chartDataPoint = new ChartDataPoint();
            List<ChartDataPointProperty> chartDataPointProperties = new ArrayList<>();

            for (JsonNode chart : chartValues) {
                JsonNode property = chart.findValue("name");
                JsonNode value = chart.findValue("data").get(i).findValue("y");

                if (property != null && value != null) {
                    chartDataPointProperties.add(new ChartDataPointProperty(property.asText(), value.asText()));
                }
            }

            chartDataPoint.setPartName(partNames.get(i).asText());
            chartDataPoint.setProperties(chartDataPointProperties);
            mappedChartDataPoints.add(chartDataPoint);
        }

        return mappedChartDataPoints;
    }

    private HashMap<String, String> initHeadersWithJSession() {
        return new HashMap<>() {
            {
                put("Cookie", "userLocale=en_US; userTimezone=America/New_York; ".concat(jasperSessionValue));
            }
        };
    }

    private List<UpdatedInputControlsPayloadInputsItem> createGenericInputList(String reportName) {
        HashMap<String, String> nameValueListToUse = inputControlsModifiedValueNameMasterList.get(reportName);

        List<UpdatedInputControlsPayloadInputsItem> listOfInputObjects = new ArrayList<>();

        List<String> nameList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (var entry : nameValueListToUse.entrySet()) {
            nameList.add(entry.getKey());
            valueList.add(entry.getValue());
        }

        for (int i = 0; i < nameList.size(); i++) {
            listOfInputObjects.add(UpdatedInputControlsPayloadInputsItem.builder()
                .name(nameList.get(i))
                .value(Collections.singletonList(valueList.get(i)))
                .limit(100)
                .offset(0)
                .build());
        }

        return listOfInputObjects;
    }

    private List<UpdatedInputControlsPayloadInputsItem> setCreatedByLastModifiedByCriteria(List<UpdatedInputControlsPayloadInputsItem> inputList, String valueToSet, String criteriaToSet) {
        int indexToGet = valueToSet.equals("createdBy") ? 6 : 7;
        inputList.get(indexToGet).setCriteria(criteriaToSet);
        return inputList;
    }

    private static void initialiseAllHashMaps() {
        initialiseInputControlsAssemblyCostA4HashMap();
        initialiseInputControlsAssemblyDetailsHashMap();
        initialiseInputControlsComponentCostHashMap();
        initialiseInputControlsScenarioComparisonHashMap();
        initialiseInputControlsUpgradePartComparisonHashMap();

        initialiseInputControlsIndexMapMaster();

        initialiseInputControlsModifiedUrlHashMap();

        initialiseAssemblyCostA4ICModifiedNamesValuesMap();
        initialiseAssemblyDetailsICModifiedNamesValuesMap();
        initialiseComponentCostICModifiedNamesValuesMap();
        initialiseScenarioComparisonICModifiedNamesValuesMap();
        initialiseUpgradePartComparisonICModifiedNamesValuesMap();

        initialiseInputControlsModifiedValueNameMasterList();
    }

    private static void initialiseInputControlsAssemblyCostA4HashMap() {
        inputControlsIndexMapAssemblyCostA4 = new HashMap<>();
        inputControlsIndexMapAssemblyCostA4.put("exportSetName", 0);
        inputControlsIndexMapAssemblyCostA4.put("partNumber", 1);
        inputControlsIndexMapAssemblyCostA4.put("scenarioName", 2);
        inputControlsIndexMapAssemblyCostA4.put("exportDate", 3);
        inputControlsIndexMapAssemblyCostA4.put("currencyCode", 4);
    }

    private static void initialiseInputControlsAssemblyDetailsHashMap() {
        inputControlsIndexMapAssemblyDetails = new HashMap<>();
        inputControlsIndexMapAssemblyDetails.put("useLatestExport", 0);
        inputControlsIndexMapAssemblyDetails.put("earliestExportDate", 1);
        inputControlsIndexMapAssemblyDetails.put("latestExportDate", 2);
        inputControlsIndexMapAssemblyDetails.put("exportSetName", 3);
        inputControlsIndexMapAssemblyDetails.put("allExportIds", 4);
        inputControlsIndexMapAssemblyDetails.put("createdBy", 5);
        inputControlsIndexMapAssemblyDetails.put("lastModifiedBy", 6);
        inputControlsIndexMapAssemblyDetails.put("assemblyNumber", 7);
        inputControlsIndexMapAssemblyDetails.put("scenarioName", 8);
        inputControlsIndexMapAssemblyDetails.put("assemblySelect", 9);
        inputControlsIndexMapAssemblyDetails.put("currencyCode", 10);
    }

    private static void initialiseInputControlsComponentCostHashMap() {
        inputControlsIndexMapComponentCost = new HashMap<>();
        inputControlsIndexMapComponentCost.put("exportSetName", 0);
        inputControlsIndexMapComponentCost.put("componentType", 1);
        inputControlsIndexMapComponentCost.put("latestExportDate", 2);
        inputControlsIndexMapComponentCost.put("createdBy", 3);
        inputControlsIndexMapComponentCost.put("lastModifiedBy", 4);
        inputControlsIndexMapComponentCost.put("componentNumber", 5);
        inputControlsIndexMapComponentCost.put("scenarioName", 6);
        inputControlsIndexMapComponentCost.put("componentSelect", 7);
        inputControlsIndexMapComponentCost.put("componentCostCurrencyCode", 8);
    }

    private static void initialiseInputControlsScenarioComparisonHashMap() {
        inputControlsIndexMapScenarioComparison = new HashMap<>();
        inputControlsIndexMapScenarioComparison.put("useLatestExport", 0);
        inputControlsIndexMapScenarioComparison.put("earliestExportDate", 1);
        inputControlsIndexMapScenarioComparison.put("latestExportDate", 2);
        inputControlsIndexMapScenarioComparison.put("exportSetName", 3);
        inputControlsIndexMapScenarioComparison.put("allExportIDs", 4);
        inputControlsIndexMapScenarioComparison.put("componentType", 5);
        inputControlsIndexMapScenarioComparison.put("createdBy", 6);
        inputControlsIndexMapScenarioComparison.put("lastModifiedBy", 7);
        inputControlsIndexMapScenarioComparison.put("partNumber", 8);
        inputControlsIndexMapScenarioComparison.put("scenarioName", 9);
        inputControlsIndexMapScenarioComparison.put("scenarioToCompareIDs", 10);
        inputControlsIndexMapScenarioComparison.put("scenarioIDs", 11);
        inputControlsIndexMapScenarioComparison.put("currencyCode", 12);
    }

    private static void initialiseInputControlsUpgradePartComparisonHashMap() {
        inputControlsIndexMapUpgradePartComparison = new HashMap<>();
        inputControlsIndexMapUpgradePartComparison.put("useLatestExport", 0);
        inputControlsIndexMapUpgradePartComparison.put("earliestExportDate", 1);
        inputControlsIndexMapUpgradePartComparison.put("latestExportDate", 2);
        inputControlsIndexMapUpgradePartComparison.put("exportSetName", 3);
        inputControlsIndexMapUpgradePartComparison.put("rollup", 4);
        inputControlsIndexMapUpgradePartComparison.put("rollupNew", 5);
        inputControlsIndexMapUpgradePartComparison.put("exportEventId", 6);
        inputControlsIndexMapUpgradePartComparison.put("processGroup", 7);
        inputControlsIndexMapUpgradePartComparison.put("partNumber", 8);
        inputControlsIndexMapUpgradePartComparison.put("partNumberNew", 9);
        inputControlsIndexMapUpgradePartComparison.put("changeLevel", 10);
        inputControlsIndexMapUpgradePartComparison.put("costMetricsLowThreshold", 11);
        inputControlsIndexMapUpgradePartComparison.put("costMetricsHighThreshold", 12);
        inputControlsIndexMapUpgradePartComparison.put("timeMetricsLowThreshold", 13);
        inputControlsIndexMapUpgradePartComparison.put("timeMetricsHighThreshold", 14);
        inputControlsIndexMapUpgradePartComparison.put("currencyCode", 15);
    }

    private static void initialiseInputControlsModifiedUrlHashMap() {
        inputControlsModifiedUrlMap = new HashMap<>();
        inputControlsModifiedUrlMap.put(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(), JasperApiInputControlsPathEnum.ASSEMBLY_COST_MODIFIED_IC);
        inputControlsModifiedUrlMap.put(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), JasperApiInputControlsPathEnum.ASSEMBLY_DETAILS_MODIFIED_IC);
        inputControlsModifiedUrlMap.put(ReportNamesEnum.COMPONENT_COST.getReportName(), JasperApiInputControlsPathEnum.COMPONENT_COST_MODIFIED_IC);
        inputControlsModifiedUrlMap.put(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), JasperApiInputControlsPathEnum.SCENARIO_COMPARISON_MODIFIED_IC);
        inputControlsModifiedUrlMap.put(ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(), JasperApiInputControlsPathEnum.UPGRADE_PART_COMPARISON_MODIFIED_IC);
    }

    private static void initialiseAssemblyCostA4ICModifiedNamesValuesMap() {
        assemblyCostA4ICModifiedNamesValuesMap = new LinkedHashMap<>();
        assemblyCostA4ICModifiedNamesValuesMap.put("exportSetName", "- - - 0 0 0-piston-assembly-0");
        assemblyCostA4ICModifiedNamesValuesMap.put("partNumber", "PISTON_ASSEMBLY");
        assemblyCostA4ICModifiedNamesValuesMap.put("scenarioName", "Initial");
        assemblyCostA4ICModifiedNamesValuesMap.put("exportDate", "2024-04-23T08:27:47");
        assemblyCostA4ICModifiedNamesValuesMap.put("currencyCode", "USD");
    }

    private static void initialiseAssemblyDetailsICModifiedNamesValuesMap() {
        assemblyDetailsICModifiedNamesValuesMap = new LinkedHashMap<>();
        assemblyDetailsICModifiedNamesValuesMap.put("useLatestExport", "Scenario");
        assemblyDetailsICModifiedNamesValuesMap.put("earliestExportDate", "2024-01-02T03:39:08");
        assemblyDetailsICModifiedNamesValuesMap.put("latestExportDate", "2024-05-01T04:39:08");
        assemblyDetailsICModifiedNamesValuesMap.put("exportSetName", "~NOTHING~");
        assemblyDetailsICModifiedNamesValuesMap.put("allExportIds", "13,14,15,16,17,18,19,20,21,22,23,24");
        assemblyDetailsICModifiedNamesValuesMap.put("createdBy", "~NOTHING~");
        assemblyDetailsICModifiedNamesValuesMap.put("lastModifiedBy", "~NOTHING~");
        assemblyDetailsICModifiedNamesValuesMap.put("assemblyNumber", "%");
        assemblyDetailsICModifiedNamesValuesMap.put("scenarioName", "~NOTHING~");
        assemblyDetailsICModifiedNamesValuesMap.put("assemblySelect", "275");
    }

    private static void initialiseComponentCostICModifiedNamesValuesMap() {
        componentCostICModifiedNamesValuesMap = new LinkedHashMap<>();
        componentCostICModifiedNamesValuesMap.put("exportSetName", "~NOTHING~");
        componentCostICModifiedNamesValuesMap.put("componentType", "~NOTHING~");
        componentCostICModifiedNamesValuesMap.put("latestExportDate", "");
        componentCostICModifiedNamesValuesMap.put("createdBy", "~NOTHING~");
        componentCostICModifiedNamesValuesMap.put("lastModifiedBy", "~NOTHING~");
        componentCostICModifiedNamesValuesMap.put("componentNumber", "%");
        componentCostICModifiedNamesValuesMap.put("scenarioName", "~NOTHING~");
        componentCostICModifiedNamesValuesMap.put("componentSelect", "1");
        componentCostICModifiedNamesValuesMap.put("componentCostCurrencyCode", "USD");
    }

    private static void initialiseScenarioComparisonICModifiedNamesValuesMap() {
        scenarioComparisonICModifiedNamesValuesMap = new LinkedHashMap<>();
        scenarioComparisonICModifiedNamesValuesMap.put("useLatestExport", "Scenario");
        scenarioComparisonICModifiedNamesValuesMap.put("earliestExportDate", "2015-12-06 06:24:08");
        scenarioComparisonICModifiedNamesValuesMap.put("latestExportDate", "2024-04-04 07:24:08");
        scenarioComparisonICModifiedNamesValuesMap.put("exportSetName", "~NOTHING~");
        scenarioComparisonICModifiedNamesValuesMap.put("allExportIDs", "~NOTHING~");
        scenarioComparisonICModifiedNamesValuesMap.put("componentType", "~NOTHING~");
        scenarioComparisonICModifiedNamesValuesMap.put("createdBy", "~NOTHING~");
        scenarioComparisonICModifiedNamesValuesMap.put("lastModifiedBy", "~NOTHING~");
        scenarioComparisonICModifiedNamesValuesMap.put("partNumber", "%");
        scenarioComparisonICModifiedNamesValuesMap.put("scenarioName", "~NOTHING~");
        scenarioComparisonICModifiedNamesValuesMap.put("scenarioToCompareIDs", "187");
        scenarioComparisonICModifiedNamesValuesMap.put("scenarioIDs", "187");
        scenarioComparisonICModifiedNamesValuesMap.put("currencyCode", "USD");
    }

    private static void initialiseUpgradePartComparisonICModifiedNamesValuesMap() {
        upgradePartComparisonICModifiedNamesValuesMap = new LinkedHashMap<>();
        upgradePartComparisonICModifiedNamesValuesMap.put("useLatestExport", "Scenario");
        upgradePartComparisonICModifiedNamesValuesMap.put("earliestExportDate", "2024-01-09T07:20:33");
        upgradePartComparisonICModifiedNamesValuesMap.put("latestExportDate", "2024-05-08T08:20:33");
        upgradePartComparisonICModifiedNamesValuesMap.put("exportSetName", "~NOTHING~");
        upgradePartComparisonICModifiedNamesValuesMap.put("rollup", "~NOTHING~");
        upgradePartComparisonICModifiedNamesValuesMap.put("rollupNew", "298");
        upgradePartComparisonICModifiedNamesValuesMap.put("exportEventId", "24");
        upgradePartComparisonICModifiedNamesValuesMap.put("processGroup", "~NOTHING~");
        upgradePartComparisonICModifiedNamesValuesMap.put("partNumber", "");
        upgradePartComparisonICModifiedNamesValuesMap.put("partNumberNew", "223");
        upgradePartComparisonICModifiedNamesValuesMap.put("changeLevel", "High, Medium, Low, No Change");
        upgradePartComparisonICModifiedNamesValuesMap.put("costMetricsLowThreshold", "10");
        upgradePartComparisonICModifiedNamesValuesMap.put("costMetricsHighThreshold", "20");
        upgradePartComparisonICModifiedNamesValuesMap.put("timeMetricsLowThreshold", "10");
        upgradePartComparisonICModifiedNamesValuesMap.put("timeMetricsHighThreshold", "20");
        upgradePartComparisonICModifiedNamesValuesMap.put("currencyCode", "GBP");
    }

    private static void initialiseInputControlsModifiedValueNameMasterList() {
        inputControlsModifiedValueNameMasterList = new HashMap<>();
        inputControlsModifiedValueNameMasterList.put(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(), assemblyCostA4ICModifiedNamesValuesMap);
        inputControlsModifiedValueNameMasterList.put(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), assemblyDetailsICModifiedNamesValuesMap);
        inputControlsModifiedValueNameMasterList.put(ReportNamesEnum.COMPONENT_COST.getReportName(), componentCostICModifiedNamesValuesMap);
        inputControlsModifiedValueNameMasterList.put(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), scenarioComparisonICModifiedNamesValuesMap);
        inputControlsModifiedValueNameMasterList.put(ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(), upgradePartComparisonICModifiedNamesValuesMap);
    }

    private static void initialiseInputControlsIndexMapMaster() {
        inputControlsIndexMapMaster = new HashMap<>();
        inputControlsIndexMapMaster.put(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(), inputControlsIndexMapAssemblyCostA4);
        inputControlsIndexMapMaster.put(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), inputControlsIndexMapAssemblyDetails);
        inputControlsIndexMapMaster.put(ReportNamesEnum.COMPONENT_COST.getReportName(), inputControlsIndexMapComponentCost);
        inputControlsIndexMapMaster.put(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), inputControlsIndexMapScenarioComparison);
        inputControlsIndexMapMaster.put(ReportNamesEnum.UPGRADE_PART_COMPARISON.getReportName(), inputControlsIndexMapUpgradePartComparison);
    }
}
