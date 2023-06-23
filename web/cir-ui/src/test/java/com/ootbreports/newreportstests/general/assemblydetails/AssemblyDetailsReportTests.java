package com.ootbreports.newreportstests.general.assemblydetails;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;

import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.JasperApiTest;
import utils.Constants;
import utils.JasperApiAuthenticationUtil;

public class AssemblyDetailsReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = Constants.API_REPORTS_PATH.concat("/general/assemblydetails/AssemblyDetailsReportRequest");
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Category(JasperApiTest.class)
    @TestRail(testCaseId = {"1922"})
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        jasperApiUtils.genericDtcCurrencyTest(
            ExportSetEnum.TOP_LEVEL.getExportSetName(),
            false
        );
    }
}
