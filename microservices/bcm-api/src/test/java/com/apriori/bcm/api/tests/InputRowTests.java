package com.apriori.bcm.api.tests;

import static com.apriori.css.api.enums.CssSearch.SCENARIO_PUBLISHED_EQ;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.InputRowsGroupsResponse;
import com.apriori.bcm.api.models.response.MultipleInputRowsResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class InputRowTests extends BcmUtil {
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

    @Test
    @TestRail(id = 30016)
    @Description("Private input row cannot be edited")
    public void notEditPrivateRow() {
        ScenarioItem cssComponentResponses = cssComponent.getBaseCssComponents(testingUser, SCENARIO_PUBLISHED_EQ.getKey() + false).get(0);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        String inputRowIdentity = createWorkSheetInputRow(cssComponentResponses.getComponentIdentity(),
            cssComponentResponses.getScenarioIdentity(),
            worksheetIdentity).getResponseEntity().getIdentity();

        InputRowsGroupsResponse editedRows =
            editPublicInputRow(InputRowsGroupsResponse.class, worksheetIdentity, inputRowIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(editedRows.getFailures().get(0).getError())
            .isEqualTo(String.format("Input Row with Identity: '%s' refers to private scenario in Worksheet with Identity: '%s'", inputRowIdentity, worksheetIdentity));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29931, 29932, 29933})
    @Description("Verify adding multiple input rows for a worksheet")
    public void addMultipleRows() {
        List<ScenarioItem> scenarioItem =
            new ArrayList<>(cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems());
        ScenarioItem scenario1 = scenarioItem.get(0);
        ScenarioItem scenario2 = scenarioItem.get(1);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        List<String> componentIdentityScenarioIdentity = Arrays.asList(scenario1.getComponentIdentity() + "," + scenario1.getScenarioIdentity(),
            scenario2.getComponentIdentity() + "," + scenario2.getScenarioIdentity());
        MultipleInputRowsResponse addRows =
            addMultipleInputRows(MultipleInputRowsResponse.class, worksheetIdentity, componentIdentityScenarioIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(addRows.getSuccesses().get(0).getScenarioIdentity()).isEqualTo(scenario1.getScenarioIdentity());

        MultipleInputRowsResponse addExistingRows =
            addMultipleInputRows(MultipleInputRowsResponse.class, worksheetIdentity, componentIdentityScenarioIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(addExistingRows.getFailures().get(0).getError()).contains("already exists");

        List<String> emptyList = Arrays.asList(" " + "," + " ", " " + "," + " ");
        ErrorResponse addEmptyList =
            addMultipleInputRows(ErrorResponse.class, worksheetIdentity, emptyList, HttpStatus.SC_BAD_REQUEST).getResponseEntity();
        softAssertions.assertThat(addEmptyList.getMessage()).contains("should not be null");
        softAssertions.assertAll();
    }
}