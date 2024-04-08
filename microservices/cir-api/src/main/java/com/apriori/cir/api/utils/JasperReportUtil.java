package com.apriori.cir.api.utils;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.JasperReportSummaryIncRawDataAsString;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.enums.ReportChartType;
import com.apriori.cir.api.models.request.ReportExportRequest;
import com.apriori.cir.api.models.request.ReportRequest;
import com.apriori.cir.api.models.response.ChartData;
import com.apriori.cir.api.models.response.ChartDataPoint;
import com.apriori.cir.api.models.response.ChartDataPointProperty;
import com.apriori.cir.api.models.response.InputControl;
import com.apriori.cir.api.models.response.ReportStatusResponse;
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
import java.util.List;

@Slf4j
public class JasperReportUtil {
    private static HashMap<String, Integer> inputControlsIndexMapComponentCost;
    private static HashMap<String, Integer> inputControlsIndexMapScenarioComparison;
    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private long WAIT_TIME = 30;

    private String jasperSessionValue = "JSESSIONID=%s";


    public static JasperReportUtil init(final String jasperSessionId) {
        initialiseInputControlsComponentCostHashMap();
        initialiseInputControlsScenarioComparisonHashMap();
        return new JasperReportUtil(jasperSessionId);
    }

    public JasperReportUtil(final String jasperSessionId) {
        this.jasperSessionValue = String.format(jasperSessionValue, jasperSessionId);
    }

    // TODO z: fix it threads
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

    public UpdatedInputControlsRootItem getInputControlsModified(JasperApiInputControlsPathEnum value, String valueNameToSet, String valueToSet, String exportSet) {
        List<UpdatedInputControlsPayloadInputsItem> genericInputList;
        if (value.toString().contains("scenarioComparison")) {
            genericInputList = createGenericInputListScenarioComparison();
        } else {
            genericInputList = createGenericInputListComponentCost();
        }
        if (value.getEndpointString().contains("scenarioComparison") && valueToSet.equals("bhegan")) {
            genericInputList = setCreatedByLastModifiedByCriteria(genericInputList, valueNameToSet, valueToSet);
        }
        HashMap<String, Integer> icMapToUse = value.toString().contains("scenarioComparison") ? inputControlsIndexMapScenarioComparison : inputControlsIndexMapComponentCost;
        genericInputList.get(icMapToUse.get(valueNameToSet)).setValue(Collections.singletonList(valueToSet));
        if (!exportSet.isEmpty()) {
            genericInputList.get(icMapToUse.get("exportSetName")).setValue(Collections.singletonList(exportSet));
        }

        ReportParameter reportParameter = new ReportParameter();
        reportParameter.reportParameter.addAll(genericInputList);

        RequestEntity requestEntity = new RequestEntity()
            .body(reportParameter)
            .endpoint(value)
            .returnType(UpdatedInputControlsRootItem.class)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        return (UpdatedInputControlsRootItem) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

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
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);
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
        final JsonNode dataNode = OBJECT_MAPPER.readTree(jsonResponse);
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

        return dataNode != null ? OBJECT_MAPPER.readerFor(new TypeReference<List<ChartDataPoint>>() {
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

    private List<UpdatedInputControlsPayloadInputsItem> createGenericInputListComponentCost() {
        List<UpdatedInputControlsPayloadInputsItem> listOfInputObjects = new ArrayList<>();

        List<String> nameList = Arrays.asList("exportSetName", "componentType", "latestExportDate",
            "createdBy", "lastModifiedBy", "componentNumber", "scenarioName", "componentSelect", "componentCostCurrencyCode");

        List<String> valueList = Arrays.asList("~NOTHING~", "~NOTHING~", "", "~NOTHING~", "~NOTHING~", "%", "~NOTHING~", "1", "USD");

        for (int i = 0; i < 9; i++) {
            listOfInputObjects.add(UpdatedInputControlsPayloadInputsItem.builder()
                .name(nameList.get(i))
                .value(Collections.singletonList(valueList.get(i)))
                .limit(100)
                .offset(0)
                .build());
        }

        return listOfInputObjects;
    }

    private List<UpdatedInputControlsPayloadInputsItem> createGenericInputListScenarioComparison() {
        List<UpdatedInputControlsPayloadInputsItem> listOfInputObjects = new ArrayList<>();

        List<String> nameList = Arrays.asList("useLatestExport", "earliestExportDate", "latestExportDate",
            "exportSetName", "allExportIDs", "componentType", "createdBy", "lastModifiedBy", "partNumber",
            "scenarioName", "scenarioToCompareIDs", "scenarioIDs", "currencyCode");

        List<String> valueList = Arrays.asList("Scenario", "2015-12-06 06:24:08", "2024-04-04 07:24:08", "~NOTHING~",
            "~NOTHING~", "~NOTHING~", "~NOTHING~", "~NOTHING~", "%", "~NOTHING~", "187", "187", "USD");

        for (int i = 0; i < 13; i++) {
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
        int indexToGet = valueToSet.equals("createdBy") ? 3 : 4;
        // numbers above wrong if not sc report, hash map fix?
        inputList.get(indexToGet).setCriteria(criteriaToSet);
        return inputList;
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
}
