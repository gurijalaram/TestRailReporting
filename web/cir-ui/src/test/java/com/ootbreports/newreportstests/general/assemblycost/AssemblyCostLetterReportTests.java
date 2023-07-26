package com.ootbreports.newreportstests.general.assemblycost;

import com.apriori.testrail.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import utils.JasperApiAuthenticationUtil;

public class AssemblyCostLetterReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.ASSEMBLY_COST_LETTER.getEndpoint();
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @TestRail(id = {13712})
    @Description("Input controls - Currency code")
    public void testCurrency() {
        jasperApiUtils.genericAssemblyCostCurrencyTest();
    }
}
