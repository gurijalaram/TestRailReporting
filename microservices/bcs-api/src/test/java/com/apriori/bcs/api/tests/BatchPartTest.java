package com.apriori.bcs.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.controller.MultiPartResources;
import com.apriori.bcs.api.controller.ReportResources;
import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.enums.ReportTypeEnum;
import com.apriori.bcs.api.models.request.parts.NewPartRequest;
import com.apriori.bcs.api.models.request.reports.ReportParameters;
import com.apriori.bcs.api.models.request.reports.ReportRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.bcs.api.models.response.Parts;
import com.apriori.bcs.api.models.response.Results;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.PDFDocument;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class BatchPartTest {

    private static Integer number_of_parts = Integer.parseInt(PropertiesContext.get("bcs.number_of_parts"));
    private SoftAssertions softAssertions;

    @BeforeEach
    public void setupTest() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {9111})
    @Description("Test costing scenario" +
        "1. Create a new batch, " +
        "2. Add 10 parts to batch " +
        "3. Wait for the costing process to complete for all parts" +
        "4. Log Parts costing results.")
    public void cost10Parts() {
        List<PartData> partDataList = new TestDataService().getPartsFromCloud(number_of_parts);
        SoftAssertions softAssertions = new SoftAssertions();
        Batch batch = BatchResources.createBatch().getResponseEntity();
        MultiPartResources.addPartsToBatch(partDataList, batch.getIdentity());
        softAssertions.assertThat(MultiPartResources.waitUntilBatchPartsCostingAreCompleted(batch.getIdentity())).isTrue();
        Parts parts = BatchPartResources.getBatchPartById(batch.getIdentity()).getResponseEntity();
        MultiPartResources.summarizeAndLogPartsCostingInfo(parts)
            .stream().forEach(part -> softAssertions.assertThat(part.getState()).isEqualTo(BCSState.COMPLETED.toString()));
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {4280})
    @Description("Create Batch and Add Part to a batch")
    public void createBatchPart() {
        ResponseWrapper<Batch> batchResponse = BatchResources.createBatch();
        Batch batchObject = batchResponse.getResponseEntity();
        softAssertions.assertThat(batchObject.getState()).isEqualTo(BCSState.CREATED.toString());

        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(batchObject.getIdentity());
        softAssertions.assertThat(partResponse.getResponseEntity().getState()).isEqualTo(BCSState.CREATED.toString());

        BatchResources.checkAndCancelBatch(batchObject);
    }

    @Test
    @TestRail(id = {7954})
    @Description("Create Batch, Add Part to a batch, wait until part is costed and get results")
    public void createBatchPartAndGetResults() {
        Batch batch = BatchResources.createBatch().getResponseEntity();
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        assertTrue(BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), part.getIdentity(), BCSState.COMPLETED), "Track and verify Batch Part Costing is completed");
        ResponseWrapper<Results> resultsResponse = HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.RESULTS_BY_BATCH_PART_IDS,
                    batch.getIdentity(),
                    part.getIdentity(),
                    Results.class)
                .expectedResponseCode(HttpStatus.SC_OK)
        ).get();

        softAssertions.assertThat(resultsResponse.getResponseEntity().getCostingStatus()).isEqualTo("COST_COMPLETE");
        BatchResources.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(id = {9544})
    @Description("Create 2 Batches, Add Part to a batch1, get the part for batch2 and verify error")
    public void createBatchPartFromMismatchedBatch() {
        Batch batch1 = BatchResources.createBatch().getResponseEntity();
        Part part = BatchPartResources.createNewBatchPartByID(batch1.getIdentity()).getResponseEntity();
        Batch batch2 = BatchResources.createBatch().getResponseEntity();
        softAssertions.assertThat(batch2.getState()).isEqualTo(BCSState.CREATED.toString());

        HTTPRequest.build(
            BatchPartResources.getBatchPartRequestEntity(
                    BCSAPIEnum.BATCH_PART_BY_BATCH_PART_IDS,
                    batch2.getIdentity(),
                    part.getIdentity(),
                    ErrorMessage.class)
                .expectedResponseCode(HttpStatus.SC_NOT_FOUND)
        ).get();
        BatchResources.checkAndCancelBatch(batch1);
        BatchResources.checkAndCancelBatch(batch2);
    }

    @Test
    @TestRail(id = {4280, 4279})
    @Description("Get part to a batch")
    public void getBatchParts() {
        Batch batch = BatchResources.createBatch().getResponseEntity();
        BatchPartResources.createNewBatchPartByID(batch.getIdentity());
        ResponseWrapper<Parts> partsResponse = BatchPartResources.getBatchPartById(batch.getIdentity());
        softAssertions.assertThat(partsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
        BatchResources.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(id = {8108})
    @Description("Create part with invalid form data: annualVolume ")
    public void createBatchPartsInvalidFormDataAnnualVolume() {
        Batch batch = BatchResources.createBatch().getResponseEntity();
        NewPartRequest newPartRequest = BatchPartResources.newPartRequest();
        newPartRequest.setAnnualVolume(123);
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartByID(newPartRequest, batch.getIdentity());

        softAssertions.assertThat(partResponse.getResponseEntity().getState()).isEqualTo(BCSState.CREATED.toString());
        BatchResources.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(id = {4280})
    @Description("Create part with Valid UDA Field in form data")
    public void createBatchPartWithValidUDAField() {
        Batch batch = BatchResources.createBatch().getResponseEntity();
        ResponseWrapper<Part> partResponse = BatchPartResources.createNewBatchPartWithValidUDA(batch.getIdentity());
        softAssertions.assertThat(partResponse.getResponseEntity().getState()).isEqualTo(BCSState.CREATED.toString());
        BatchResources.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(id = {29586, 29598})
    @Issue("PFE-6804")
    @Description("Test costing scenario for /reports - DFM Multiple Part Summary Report Configuration" +
        "1. Create a new batch, " +
        "2. Add parts to batch " +
        "3. Start Batch costing and wait until all batch parts costing are completed" +
        "4. Create and Export a reports with different report parameters" +
        "5. Assert the report content")
    public void verifyDfmReportForBatchParts() {
        List<PartData> partDataList = new TestDataService().getPartsData("BcsReportPartData.json");
        ReportRequest reportRequestTestData = ReportResources.getReportRequestData();
        List<Results> partsResult = new ArrayList<>();
        Batch batch = BatchResources.createBatch().getResponseEntity();
        MultiPartResources.addPartsToBatch(partDataList, batch.getIdentity());
        BatchResources.startBatchCosting(batch);
        softAssertions.assertThat(BatchResources.waitUntilBatchCostingReachedExpected(batch.getIdentity())).isTrue();
        softAssertions.assertThat(MultiPartResources.waitUntilBatchPartsCostingAreCompleted(batch.getIdentity())).isTrue();
        Parts parts = BatchPartResources.getBatchPartById(batch.getIdentity()).getResponseEntity();
        MultiPartResources.summarizeAndLogPartsCostingInfo(parts)
            .stream().forEach(part -> softAssertions.assertThat(part.getState()).isEqualTo(BCSState.COMPLETED.toString()));

        parts.getItems().forEach(part -> {
            Results results = BatchPartResources.getBatchPartResults(batch.getIdentity(), part.getIdentity()).getResponseEntity();
            partsResult.add(results);
        });
        ReportParameters reportParameters = ReportParameters.builder()
            .roundToDollar(null)
            .currencyCode("USD")
            .riskRating(new String[] {"All"})
            .costMetric("Fully Burdened Cost")
            .massMetric("Finish Mass")
            .sortMetric("Part Number")
            .build();

        reportRequestTestData.setExternalId(batch.getExternalId());
        reportRequestTestData.setScopedIdentity(batch.getIdentity());
        reportRequestTestData.setReportTemplateIdentity(ReportResources.getReportTemplateId(ReportTypeEnum.BATCH_REPORT));
        reportRequestTestData.setReportParameters(reportParameters);

        PDFDocument pdfByPartNumber = ReportResources.createAndDownloadReport(reportRequestTestData);
        partsResult.forEach(results -> {
            softAssertions.assertThat(pdfByPartNumber.getDocumentContents().contains(results.getRoughMass().toString())).isTrue();
            softAssertions.assertThat(pdfByPartNumber.getDocumentContents().contains("SOCKET WELD FLANGE(SOCKET FLANGE 150-NPS3)")).isTrue();

        });

        reportParameters = ReportParameters.builder()
            .roundToDollar(null)
            .currencyCode("USD")
            .riskRating(new String[] {"All"})
            .costMetric("Piece Part Cost")
            .massMetric("Rough Mass")
            .sortMetric("Process Group")
            .build();
        reportRequestTestData.setReportParameters(reportParameters);
        PDFDocument pdfByProcessGroup = ReportResources.createAndDownloadReport(reportRequestTestData);
        partsResult.forEach(results -> {
            softAssertions.assertThat(pdfByProcessGroup.getDocumentContents().contains(results.getRoughMass().toString())).isTrue();
        });
    }

    @AfterEach
    public void afterEach() {
        softAssertions.assertAll();
    }
}
