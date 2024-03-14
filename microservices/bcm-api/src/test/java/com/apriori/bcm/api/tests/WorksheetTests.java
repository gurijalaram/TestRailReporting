package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.InputRowsGroupsResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class WorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private final String componentType = "PART";
    private CssComponent cssComponent = new CssComponent();
    private String worksheetIdentity;

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
        }
    }

    @Test
    @TestRail(id = 28963)
    @Description("Verify worksheet creation")
    public void verifyWorksheetCreation() {
        String name = new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = createWorksheet(name);
        worksheetIdentity = response.getResponseEntity().getIdentity();
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29156)
    @Description("Verify worksheet creation - already exists error")
    public void verifyWorksheetCreationAlreadyExistError() {
        String name = new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = createWorksheet(name);
        worksheetIdentity = response.getResponseEntity().getIdentity();
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);

        ResponseWrapper<ErrorResponse> responseError = createWorksheetAlreadyExists(name);
        softAssertions.assertThat(responseError.getResponseEntity().getMessage()).contains("already exists");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29276)
    @Description("Verify worksheet list is returned")
    public void verifyWorksheetList() {
        String name = new GenerateStringUtil().saltString("name");

        worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();

        WorkSheets worksheetsList = getWorksheets().getResponseEntity();
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

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
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
            createWorksheet(GenerateStringUtil.saltString("name"));
        worksheetIdentity = worksheetCreated.getResponseEntity().getIdentity();

        ResponseWrapper<WorkSheetResponse> worksheetGet =
            getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK);

        softAssertions.assertThat(worksheetGet.getResponseEntity())
            .isEqualTo(worksheetCreated.getResponseEntity());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29734)
    @Description("Verify getting specific worksheet 400 error identity is invalid")
    public void verifyGetWorkSheetWrongIdentity() {
        ResponseWrapper<ErrorResponse> worksheetGet =
            getWorksheet(ErrorResponse.class, "fake9876", HttpStatus.SC_BAD_REQUEST);

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
        ResponseWrapper<ErrorResponse> error =
            getWorksheet(ErrorResponse.class, "CYTTG999999L", HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(error.getResponseEntity().getMessage())
            .isEqualTo("Resource 'Worksheet' with identity 'CYTTG999999L' was not found");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29736)
    @Description("Verify getting worksheet rows")
    public void verifyGetWorksheetRows() {
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems())
            .isNotEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 29740)
    @Description("Verify getting worksheet rows for empty worksheet with no rows")
    public void verifyGetWorksheetRowsWithoutRows() {
        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems())
            .isEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29742, 29744, 29745})
    @Description("Verify Edit of public input row")
    public void editPublicInputRow() {
        String notExistingRowIdentity = "000000000000";
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream().filter(item -> item.getScenarioPublished().equals(true))
                .findFirst().orElse(null);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        String inputRowIdentity = createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            worksheetIdentity).getResponseEntity().getIdentity();

        ErrorResponse editInvalidInputRow =
            editPublicInputRow(ErrorResponse.class, worksheetIdentity, "0000000", HttpStatus.SC_BAD_REQUEST).getResponseEntity();
        softAssertions.assertThat(editInvalidInputRow.getMessage()).isEqualTo("'inputRowIdentity' is not a valid identity.");

        InputRowsGroupsResponse editNotExistingInputRow =
            editPublicInputRow(InputRowsGroupsResponse.class, worksheetIdentity, notExistingRowIdentity, HttpStatus.SC_OK).getResponseEntity();
        softAssertions.assertThat(editNotExistingInputRow.getFailures().get(0).getError())
            .isEqualTo(String.format("Input Row with Identity: '%s'  does not exist in Worksheet with Identity: '%s'", notExistingRowIdentity, worksheetIdentity));

        InputRowsGroupsResponse editedRows =
            editPublicInputRow(InputRowsGroupsResponse.class, worksheetIdentity, inputRowIdentity, HttpStatus.SC_OK).getResponseEntity();
        softAssertions.assertThat(editedRows.getSuccesses().get(0).getInputRowIdentity())
            .isEqualTo(inputRowIdentity);
        softAssertions.assertAll();
    }
}
