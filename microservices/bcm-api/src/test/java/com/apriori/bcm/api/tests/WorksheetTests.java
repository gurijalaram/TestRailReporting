package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class WorksheetTests extends BcmUtil {
    private final BcmUtil bcmUtil = new BcmUtil();
    private static SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private final String componentType = "PART";

    @Test
    @TestRail(id = 28963)
    @Description("Verify worksheet creation")
    public void verifyWorksheetCreation() {
        String name = new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = bcmUtil.createWorksheet(name);
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29156)
    @Description("Verify worksheet creation - already exists error")
    public void verifyWorksheetCreationAlreadyExistError() {
        String name = new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = bcmUtil.createWorksheet(name);
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);

        ResponseWrapper<ErrorResponse> responseError = bcmUtil.createWorksheetAlreadyExists(name);
        softAssertions.assertThat((responseError.getResponseEntity().getMessage()).contains("already exists"));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29276)
    @Description("Verify worksheet list is returned")
    public void verifyWorksheetList() {
        String name = new GenerateStringUtil().saltString("name");

        bcmUtil.createWorksheet(name);

        WorkSheets worksheetsList = bcmUtil.getWorksheets().getResponseEntity();
        softAssertions.assertThat(worksheetsList.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(worksheetsList.getItems().stream().filter(worksheet -> worksheet.getName().equals(name)).collect(Collectors.toList()).get(0)).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29277)
    @Description("Verify creating input rows in the worksheet")
    public void verifyCreateInputRowInWorksheet() {

        ScenarioItem scenarioItem = getPart();
        String worksheetIdentity = createWorksheet();

        ResponseWrapper<WorkSheetInputRowResponse> responseWorksheetInputRow =
            bcmUtil.createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        softAssertions.assertThat(responseWorksheetInputRow.getResponseEntity().getWorksheetId()).isNotNull();
        softAssertions.assertThat(responseWorksheetInputRow.getResponseEntity().getComponentIdentity())
            .isEqualTo(scenarioItem.getComponentIdentity());
        softAssertions.assertThat(responseWorksheetInputRow.getResponseEntity().getScenarioIdentity())
            .isEqualTo(scenarioItem.getScenarioIdentity());
        softAssertions.assertAll();
    }

    private ScenarioItem getPart() {
        ResponseWrapper<ComponentResponse> components = cssComponent.postSearchRequest(testingUser, componentType);
        return components.getResponseEntity().getItems().stream()
            .findFirst().orElse(null);
    }

    private String createWorksheet() {
        String name = new GenerateStringUtil().saltString("name");
        ResponseWrapper<WorkSheetResponse> worksheet =
            bcmUtil.createWorksheet(name);

        return worksheet.getResponseEntity().getIdentity();
    }
}
