package com.ootbreports.newreportstests.scenariocomparison;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

public class ScenarioComparisonReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/scenariocomparison/ScenarioComparisonReportRequest");
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(testCaseId = {"3305"})
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        jasperApiUtils.scenarioComparisonCurrencyTest();
    }
}
