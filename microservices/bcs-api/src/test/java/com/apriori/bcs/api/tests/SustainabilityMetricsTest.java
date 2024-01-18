package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.enums.PartFieldsEnum;
import com.apriori.bcs.api.models.response.Results;
import com.apriori.bcs.api.utils.BcsTestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestRulesAPI.class)
public class SustainabilityMetricsTest {
    private SoftAssertions softAssertions;
    private BcsTestUtil bcsTestUtil;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bcsTestUtil = new BcsTestUtil();
    }

    @Test
    @TestRail(id = {28080, 28439})
    @Description("sustainability metrics returned for supported Process Groups" +
        "Fields with value of zero are not omitted from response (eg sustainability metrics)")
    public void createBatchWithSupportedSustainabilityMetrics() {
        final String partName = "26131";
        final File partFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_DIE, partName + ".par");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        QueryParams partQueryParams = new QueryParams()
            .use(PartFieldsEnum.ANNUAL_VOLUME.getPartFieldName(), "1000")
            .use(PartFieldsEnum.BATCH_SIZE.getPartFieldName(), "500")
            .use(PartFieldsEnum.DESCRIPTION.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.EXTERNAL_ID.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.GENERATE_WATCHPOINT_REPORT.getPartFieldName(), "false")
            .use(PartFieldsEnum.PROCESS_GROUP.getPartFieldName(), ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .use(PartFieldsEnum.SCENARIO_NAME.getPartFieldName(), scenarioName)
            .use(PartFieldsEnum.VPE_NAME.getPartFieldName(), DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .use(PartFieldsEnum.FILE_NAME.getPartFieldName(), partName);

        BcsTestUtil bcsTestUtil = new BcsTestUtil()
            .createBatch()
            .addPartToBatch(partQueryParams, partFile)
            .startCosting();

        Results partResults = bcsTestUtil.getPartResult();
        softAssertions.assertThat(partResults.getMaterialCarbon()).isNotNull();
        softAssertions.assertThat(partResults.getProcessCarbon()).isNotNull();
        softAssertions.assertThat(partResults.getLogisticsCarbon()).isEqualTo(0.0);
        softAssertions.assertThat(partResults.getTotalCarbon()).isNotNull();
        softAssertions.assertThat(partResults.getAnnualManufacturingCarbon()).isNotNull();
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
