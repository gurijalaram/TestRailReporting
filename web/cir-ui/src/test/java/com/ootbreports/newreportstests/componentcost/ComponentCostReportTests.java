package com.ootbreports.newreportstests.componentcost;

import static com.apriori.TestSuiteType.TestSuite.REPORTS;

import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import utils.JasperApiAuthenticationUtil;

public class ComponentCostReportTests extends JasperApiAuthenticationUtil {
    private static final String reportsJsonFileName = JasperApiEnum.COMPONENT_COST.getEndpoint();
    private static final String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private static JasperApiUtils jasperApiUtils;

    @Before
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName);
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {3329})
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        jasperApiUtils.genericComponentCostCurrencyTest();
    }
}
