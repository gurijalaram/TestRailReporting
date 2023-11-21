package com.apriori.cir.ui.tests.ootbreports.newreportstests.digitalfactoryperformance;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.cir.api.JasperReportSummary;
import com.apriori.cir.api.enums.CirApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiEnum;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.utils.JasperApiUtils;
import com.apriori.cir.ui.utils.JasperApiAuthenticationUtil;
import com.apriori.shared.util.enums.CurrencyEnum;
import com.apriori.shared.util.enums.ExportSetEnum;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DigitalFactoryPerformanceDetailsReportTests extends JasperApiAuthenticationUtil {
    private String reportsJsonFileName = JasperApiEnum.DIGITAL_FACTORY_PERFORMANCE_DETAILS.getEndpoint();
    private CirApiEnum reportsNameForInputControls = CirApiEnum.DIGITAL_FACTORY_PERFORMANCE_DETAILS;
    private String exportSetName = ExportSetEnum.COST_OUTLIER_THRESHOLD_ROLLUP.getExportSetName();
    private JasperApiUtils jasperApiUtils;

    @BeforeEach
    public void setupJasperApiUtils() {
        jasperApiUtils = new JasperApiUtils(jSessionId, exportSetName, reportsJsonFileName, reportsNameForInputControls);
    }

    @Test
    @TmsLink("29641")
    @TestRail(id = 26941)
    @Description("Input Controls - Currency Code - Details Report")
    public void testCurrencyCode() {
        JasperReportSummary gbpJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(CurrencyEnum.GBP.getCurrency());
        ArrayList<String> gbpAssertValues = getAssertValues(gbpJasperReportSummary);

        JasperReportSummary usdJasperReportSummary = jasperApiUtils.genericTestCoreCurrencyAndDateOnlyDigitalFactoryPerfTests(CurrencyEnum.USD.getCurrency());
        ArrayList<String> usdAssertValues = getAssertValues(usdJasperReportSummary);

        assertAll("Grouped Currency Assertions",
            () -> assertEquals(gbpAssertValues.get(0), CurrencyEnum.GBP.getCurrency()),
            () -> assertEquals(usdAssertValues.get(0), CurrencyEnum.USD.getCurrency()),
            () -> assertNotEquals(gbpAssertValues.get(0), usdAssertValues.get(0)),
            () -> assertNotEquals(gbpAssertValues.get(1), usdAssertValues.get(1)),
            () -> assertNotEquals(gbpAssertValues.get(2), usdAssertValues.get(2))
        );
    }

    private ArrayList<String> getAssertValues(JasperReportSummary jasperReportSummary) {
        ArrayList<String> assertValues = new ArrayList<>();
        assertValues.add(jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "6").get(5).child(0).text());
        List<Element> gbpApCostValues = jasperReportSummary.getReportHtmlPart().getElementsByAttributeValue("colspan", "3");
        assertValues.add(gbpApCostValues.get(7).text());
        assertValues.add(gbpApCostValues.get(27).text());
        return assertValues;
    }
}
