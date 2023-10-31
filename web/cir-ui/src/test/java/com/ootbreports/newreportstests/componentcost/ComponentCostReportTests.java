package com.ootbreports.newreportstests.componentcost;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.apriori.cir.enums.CirApiEnum;
import com.apriori.enums.ExportSetEnum;
import com.apriori.testrail.TestRail;

import com.ootbreports.newreportstests.utils.JasperApiEnum;
import com.ootbreports.newreportstests.utils.JasperApiUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.JasperApiAuthenticationUtil;

public class ComponentCostReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.COMPONENT_COST.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.COMPONENT_COST;
    private String exportSetName = ExportSetEnum.TOP_LEVEL.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @Tag(REPORTS_API)
    @TestRail(id = 3329)
    @Description("Verify Currency Code input control is working correctly")
    public void testCurrencyCode() {
        jasperApiUtils.genericComponentCostCurrencyTest();
    }
}
