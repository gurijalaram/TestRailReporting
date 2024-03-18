package com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.JASPER_API;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cir.api.JasperReportSummaryIncRawData;
import com.apriori.cir.api.enums.JasperApiInputControlsPathEnum;
import com.apriori.cir.api.utils.ReportComponentsResponse;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ScenarioActivityReportTests extends JasperApiAuthenticationUtil {

    private String reportsJsonFileName = JasperApiEnum.SCENARIO_ACTIVITY.getEndpoint();
    private JasperApiInputControlsPathEnum reportsNameForInputControls = JasperApiInputControlsPathEnum.SCENARIO_ACTIVITY;
    private String exportSetName = ExportSetEnum.ROLL_UP_A.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(JASPER_API)
    @TmsLink("16195")
    @TestRail(id = 16195)
    @Description("Input Controls - Trending Period - Main Report")
    public void testTrendingPeriod() {
        ArrayList<JasperReportSummaryIncRawData> jasperReportSummaries = jasperApiUtils.scenarioActivityReportGenerationTwoTrendingPeriodsIncRawData();
        JasperReportSummaryIncRawData jasperReportSummaryDaily = jasperReportSummaries.get(0);
        JasperReportSummaryIncRawData jasperReportSummaryYearly = jasperReportSummaries.get(1);

        assertThat(jasperReportSummaryDaily, is(notNullValue()));
        assertThat(jasperReportSummaryYearly, is(notNullValue()));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryDaily), is(equalTo("Daily")));
        assertThat(getTrendingValueAboveChart(jasperReportSummaryYearly), is(equalTo("Yearly")));

        String currentDayOfMonth = DateTimeFormatter.ofPattern("dd").format(LocalDateTime.now());
        String currentMonth = DateTimeFormatter.ofPattern("MM").format(LocalDateTime.now());
        assertThat(getXCategories(jasperReportSummaryDaily), is(equalTo("[[2019/07/29, 2019/07/30, 2021/04/07]]")));
        assertThat(getXCategories(jasperReportSummaryYearly), is(equalTo(String.format("[[2019/%s/%s, 2021/%s/%s]]", currentMonth, currentDayOfMonth, currentMonth, currentDayOfMonth))));
    }

    private String getTrendingValueAboveChart(JasperReportSummaryIncRawData jasperReportSummary) {
        return jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "2").get(4).siblingElements().get(2).child(0).text();
    }

    private String getXCategories(JasperReportSummaryIncRawData jasperReportSummary) {
        ReportComponentsResponse reportComponentsResponse = jasperReportSummary.getChartDataRaw();
        List<Object> serviceItemOneValues = Collections.singletonList(reportComponentsResponse.getInfoItemOne().getHcinstancedata().getServices().get(0).getDataItem());
        return Collections.singletonList(((LinkedHashMap<?, ?>) serviceItemOneValues.get(0)).get("xCategories")).toString();
    }
}
