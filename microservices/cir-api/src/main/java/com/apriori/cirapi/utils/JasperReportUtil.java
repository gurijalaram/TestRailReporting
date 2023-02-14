package com.apriori.cirapi.utils;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.enums.CIRAPIEnum;
import com.apriori.cirapi.entity.request.ReportExportRequest;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.entity.response.InputControl;
import com.apriori.cirapi.entity.response.ReportStatusResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class JasperReportUtil {

    private String jasperSessionValue = "JSESSIONID=%s";

    public static JasperReportUtil init(final String jasperSessionId) {
        return new JasperReportUtil(jasperSessionId);
    }

    public JasperReportUtil(final String jasperSessionId) {
        this.jasperSessionValue = String.format(jasperSessionValue, jasperSessionId);
    }

    public InputControl getInputControls() {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.DTC_METRICS, InputControl.class)
            .inlineVariables("%20")
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlEncodingEnabled(false);

        ResponseWrapper<InputControl> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    public JasperReportSummary generateJasperReportSummary(ReportRequest reportRequest) {
        ReportStatusResponse response = this.generateReport(reportRequest);
        ReportStatusResponse exportedReport = this.doReportExport(response);

        return JasperReportSummary.builder()
            .reportHtmlPart(this.getReportHtmlData(response.getRequestId(),
                exportedReport.getId())
            )
            .chartDataPoints(this.getReportChartData(response.getRequestId(),
                exportedReport.getId())
            )
            .build();
    }

    private ReportStatusResponse generateReport(ReportRequest reportRequest) {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.REPORT_EXECUTIONS, ReportStatusResponse.class)
            .headers(initHeadersWithJSession())
            .body(reportRequest)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> responseResponseWrapper = HTTPRequest.build(requestEntity).post();

        return responseResponseWrapper.getResponseEntity();
    }

    private ReportStatusResponse doReportExport(ReportStatusResponse response) {
        RequestEntity doExportRequest = RequestEntityUtil.init(CIRAPIEnum.REPORT_EXPORT_BY_REQUEST_ID, ReportStatusResponse.class)
            .inlineVariables(response.getRequestId())
            .headers(initHeadersWithJSession())
            .body(ReportExportRequest.initFromJsonFile())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ReportStatusResponse> exportDataResponseWrapper = HTTPRequest.build(doExportRequest).post();

        return exportDataResponseWrapper.getResponseEntity();
    }

    @SneakyThrows
    private Document getReportHtmlData(final String requestId, final String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs, InputStream.class)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        InputStream htmlData = (InputStream) HTTPRequest.build(requestEntity).get().getResponseEntity();

        return Jsoup.parse(htmlData, "UTF-8", "/aPriori/reports/");
    }

    private List<ChartDataPoint> getReportChartData(final String requestId, final String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs, null)
            .inlineVariables(requestId, exportId)
            .headers(initHeadersWithJSession())
            .expectedResponseCode(HttpStatus.SC_OK);

        return parseJsonResponse(HTTPRequest.build(requestEntity).get()
            .getBody()
        );
    }

    @SneakyThrows
    private List<ChartDataPoint> parseJsonResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode dataNode = objectMapper.readTree(jsonResponse)
            .findValue("series");

        if (dataNode != null) {
            dataNode = dataNode.findValue("data");
        }

        return dataNode != null ? objectMapper.readerFor(new TypeReference<List<ChartDataPoint>>() {}).readValue(dataNode) : null;
    }

    private HashMap<String, String> initHeadersWithJSession() {
        return new HashMap<String, String>() {
            {
                put("Cookie", "userLocale=en_US; userTimezone=America/New_York; " + jasperSessionValue);
            }
        };
    }
}
