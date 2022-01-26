package com.apriori.cirapi.utils;

import com.apriori.cirapi.entity.enums.CIRAPIEnum;
import com.apriori.cirapi.entity.response.ReportStatusResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.itextpdf.text.pdf.PdfDocument;
import org.apache.http.HttpStatus;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.bouncycastle.cert.ocsp.Req;
import org.fit.pdfdom.PDFDomTree;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Base64;
import java.util.HashMap;
//https://conqbaci02/jasperserver-pro/rest_v2/reports/aPriori/reports/DTC%20Metrics/casting/castingDTC/inputControls/values

public class JasperReportUtil {

    private static final String JSESSION_ID = "JSESSIONID=4A0A72BF7ABEC8F269EACB448DF5B74D";

    public static void myJasperTest() {
        ReportStatusResponse response = generateDTCreport();

        ReportStatusResponse exportedReport = doReportExport(response);

        getReportData(response.getRequestId(), exportedReport.getId());

//        getReportComponentData(response.getRequestId(), exportedReport.getId());

    }

    private static void getReportComponentData(final String requestId, final String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs, InputStream.class)
            .inlineVariables(requestId, exportId)
            .headers(new HashMap<String, String>() {
                {
                    put("Cookie", JSESSION_ID);
                    put("Accept", " application/json");
                }
            });

        InputStream pdfData = (InputStream) HTTPRequest.build(requestEntity).get().getResponseEntity();

        try (FileOutputStream outputStream = new FileOutputStream(new File("E:\\TestFile.pdf"))) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = pdfData.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {

        }
    }

    private static void getReportData(final String requestId, final String exportId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs, InputStream.class)
            .inlineVariables(requestId, exportId)
            .headers(new HashMap<String, String>() {
                {
                    put("Cookie", JSESSION_ID);
                    put("Accept", " application/json");
                }
            });

        InputStream pdfData = (InputStream) HTTPRequest.build(requestEntity).get().getResponseEntity();

        try (FileOutputStream outputStream = new FileOutputStream(new File("E:\\TestFile.pdf"))) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = pdfData.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }


    // convert to html

//            PdfDocument pdf = new PdfDocument();
//            pdf.loadFromFile("New Zealand.pdf");
//            //Save to HTML format
//            pdf.saveToFile("ToHTML.html", FileFormat.HTML);

            PDDocument pdf = PDDocument.load(new File("E:\\TestFile.pdf"));
            Writer output = new PrintWriter("E:\\pdf.html", "utf-8");
            new PDFDomTree().writeText(pdf, output);

            output.close();

        } catch (Exception e) {

        }

//        File file = new File("E:\\TestFile.pdf");

//        try ( FileOutputStream fos = new FileOutputStream(file); ) {
//            // To be short I use a corrupted PDF string, so make sure to use a valid one if you want to preview the PDF file
//
//            byte[] decoder = Base64.getDecoder().decode(pdfData);
//
//            fos.write(decoder);
//            System.out.println("PDF File Saved");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            //Create Document instance.
//            Document document = new Document();
//
//            //Create OutputStream instance.
//            OutputStream outputStream =
//                new FileOutputStream("E:\\TestFile.pdf");
//
//            //Create PDFWriter instance.
//            PdfWriter.getInstance(document, outputStream);
//
//            //Open the document.
//            document.open();
//
//            //Add content to the document.
//            document.add(new Paragraph(pdfData));
//
//            //Close document and outputStream.
//            document.close();
//            outputStream.close();
//        } catch (Exception e) {
//            throw new IllegalArgumentException();
//        }
    }

    private static ReportStatusResponse doReportExport(ReportStatusResponse response) {
        RequestEntity doExportRequest = RequestEntityUtil.init(CIRAPIEnum.REPORT_EXPORT_BY_REQUEST_ID, ReportStatusResponse.class)
            .inlineVariables(response.getRequestId())
            .headers(new HashMap<String, String>() {
                {
                    put("Cookie", JSESSION_ID);
                    put("Accept", "application/json");
                }
            })
            .customBody("{\n" +
                "  \"outputFormat\": \"pdf\",\n" +
                "  \"pages\": 1,\n" +
                "  \"attachmentsPrefix\": \"/jasperserver-pro/rest_v2/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/\",\n" +
                "  \"allowInlineScripts\": false,\n" +
                "  \"markupType\": \"embeddable\",\n" +
                "  \"baseUrl\": \"/jasperserver-pro\",\n" +
                "  \"clearContextCache\": true\n" +
                "}");

        ResponseWrapper<ReportStatusResponse> exportDataResponseWrapper = HTTPRequest.build(doExportRequest).post();
        Assert.assertEquals(exportDataResponseWrapper.getStatusCode(), HttpStatus.SC_OK);

        return exportDataResponseWrapper.getResponseEntity();
    }

    private static ReportStatusResponse generateDTCreport() {
        RequestEntity requestEntity = RequestEntityUtil.init(CIRAPIEnum.CASTING_DTC_REPORT, ReportStatusResponse.class)
            .headers(new HashMap<String, String>() {
                {
                    put("Cookie", JSESSION_ID);
                }
            })
            .customBody("{\n" +
                "  \"reportUnitUri\": \"/aPriori/reports/DTC Metrics/casting/castingDTC\",\n" +
                "  \"async\": true,\n" +
                "  \"allowInlineScripts\": false,\n" +
                "  \"markupType\": \"embeddable\",\n" +
                "  \"interactive\": true,\n" +
                "  \"freshData\": false,\n" +
                "  \"saveDataSnapshot\": false,\n" +
                "  \"transformerKey\": null,\n" +
                "  \"pages\": 1,\n" +
                "  \"attachmentsPrefix\": \"/jasperserver-pro/rest_v2/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/\",\n" +
                "  \"baseUrl\": \"/jasperserver-pro\",\n" +
                "  \"parameters\": {\n" +
                "    \"reportParameter\": [\n" +
                "      {\n" +
                "        \"name\": \"useLatestExport\",\n" +
                "        \"value\": [\n" +
                "          \"Scenario\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"earliestExportDate\",\n" +
                "        \"value\": [\n" +
                "          \"2021-09-22T07:31:32\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"latestExportDate\",\n" +
                "        \"value\": [\n" +
                "          \"2022-01-20T06:31:32\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"exportSetName\",\n" +
                "        \"value\": [\n" +
                "          \"528\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"rollup\",\n" +
                "        \"value\": [\n" +
                "          \"140200\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"costMetric\",\n" +
                "        \"value\": [\n" +
                "          \"Fully Burdened Cost\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"massMetric\",\n" +
                "        \"value\": [\n" +
                "          \"Finish Mass\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"sortOrder\",\n" +
                "        \"value\": [\n" +
                "          \"Manufacturing - Casting Issues\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"currencyCode\",\n" +
                "        \"value\": [\n" +
                "          \"USD\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"annualSpendMin\",\n" +
                "        \"value\": [\n" +
                "          \"0\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"processGroup\",\n" +
                "        \"value\": [\n" +
                "          \"Casting - Die\",\n" +
                "          \"Casting - Sand\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"exportEventId\",\n" +
                "        \"value\": [\n" +
                "          \"581\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"metricStatistic\",\n" +
                "        \"value\": [\n" +
                "          \"6,d1=24,d2=36,d3=145,d4=389,d5=0,d6=2,d7=5,d8=.025105140130856,d9=.0278243491736251,da=.0353725240774374\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"dtcScore\",\n" +
                "        \"value\": [\n" +
                "          \"High\",\n" +
                "          \"Medium\",\n" +
                "          \"Low\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"outlierDistance\",\n" +
                "        \"value\": [\n" +
                "          \"1.5\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"partsSelect\",\n" +
                "        \"value\": []\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        ResponseWrapper<ReportStatusResponse> responseResponseWrapper = HTTPRequest.build(requestEntity).post();
        Assert.assertEquals(responseResponseWrapper.getStatusCode(), HttpStatus.SC_OK);

        return responseResponseWrapper.getResponseEntity();
    }
}
