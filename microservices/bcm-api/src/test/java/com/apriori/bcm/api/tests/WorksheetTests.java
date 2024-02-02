package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class WorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private final BcmUtil bcmUtil = new BcmUtil();
    private final String componentType = "PART";
    private CssComponent cssComponent = new CssComponent();

    // TODO - add clean up method once API will be available

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

        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        String worksheetIdentity = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

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

    @Test
    @TestRail(id = 29733)
    @Description("Verify getting specific worksheet")
    public void verifyGetSpecificWorkSheet() {
        ResponseWrapper<WorkSheetResponse> worksheetCreated =
            bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"));

        ResponseWrapper<WorkSheetResponse> worksheetGet =
            bcmUtil.getWorksheet(WorkSheetResponse.class, worksheetCreated.getResponseEntity().getIdentity(), HttpStatus.SC_OK);

        softAssertions.assertThat(worksheetGet.getResponseEntity())
            .isEqualTo(worksheetCreated.getResponseEntity());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29734)
    @Description("Verify getting specific worksheet 400 error identity is invalid")
    public void verifyGetWorkSheetWrongIdentity() {
        ResponseWrapper<ErrorResponse> worksheetGet =
            bcmUtil.getWorksheet(ErrorResponse.class, "fake9876", HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(worksheetGet.getResponseEntity().getMessage())
            .isEqualTo("'identity' is not a valid identity.");
        softAssertions.assertThat(worksheetGet.getResponseEntity().getPath())
            .isEqualTo("/worksheets/fake9876");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29735)
    @Description("Verify getting specific worksheet 404 error worksheet does not exist")
    public void verifyGetWorkSheetDoesNotExist() {
        bcmUtil.getWorksheet(ErrorResponse.class, "CYTTG999999L", HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(id = 29736)
    @Description("Verify getting worksheet rows")
    public void verifyGetWorksheetRows() {
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        String worksheetIdentity = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowResponse> responseWorksheetInputRow =
            bcmUtil.createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            bcmUtil.getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems())
            .isNotEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29740)
    @Description("Verify getting worksheet rows for empty worksheet with no rows")
    public void verifyGetWorksheetRowsWithoutRows() {
        String worksheetIdentity = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            bcmUtil.getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems())
            .isEmpty();
        softAssertions.assertAll();
    }

}
