package com.apriori.validators;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import utils.Constants;
import utils.TableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationValidator {
    TableUtils tableUtils;

    final List<String> aprioriCostOptions;

    public NotificationValidator(WebDriver driver) {
        tableUtils = new TableUtils(driver);

        aprioriCostOptions = new ArrayList<>();
        aprioriCostOptions.add(Constants.EMAIL_APRIORICOST_FULLY);
        aprioriCostOptions.add(Constants.EMAIL_APRIORICOST_MATERIAL);
        aprioriCostOptions.add(Constants.EMAIL_APRIORICOST_PART);
    }

    public void validateEmailTab(Map<String, Object> values) {
        Assert.assertTrue("Email tab title was incorrect", values.get("title").equals("Email"));
        Assert.assertEquals("Wrong email template is selected", Constants.EMAIL_TEMPLATE_DFC,
                values.get("dfm-template").toString());
        Assert.assertFalse("Next button is enabled before all fields have been filled in",
                (boolean)values.get("before-fields-filled"));
        Assert.assertEquals("Default email recipient type was incorrect", Constants.EMAIL_RECIPIENT_FIELD, values.get("default-recipient"));
        Assert.assertEquals("Email Recipient Type - constant, was not selected", Constants.EMAIL_RECIPIENT_CONSTANT,
                values.get("recipient-type-constant"));
        Assert.assertEquals("Unable to add email recipient", Constants.USER_EMAIL, values.get("recipient"));
        Assert.assertEquals("Unable to add comma separated email recipents",  Constants.EMAIL_RECIPIENTS, values.get(
                "recipient-csv"));
        Assert.assertTrue("Comma separated email recipents with spaces did raise an error", (boolean)values.get(
                "error-displayed"));
        Assert.assertTrue("", values.get("default-rounding").equals("Yes"));
        Assert.assertEquals("Cost Rounding label was incorrect", Constants.COST_ROUNDING_LABEL, values.get("rounding" +
                "-label"));
        Assert.assertTrue("Cost rounding - No was not selected", values.get("rounding-no").equals("No"));
        Assert.assertTrue("Missing aPriori cost options", tableUtils.actualListContainsAllItems(aprioriCostOptions,
                (List<String>)values.get("cost-options")));
        Assert.assertEquals("Default Apriori cost rounding was in correct", Constants.EMAIL_APRIORICOST_FULLY, values.get("default-cost"));
        Assert.assertEquals("Default Ariori Cost label was incorrect", Constants.APRIORI_COST_LABEL, values.get(
                "cost-label"));
        Assert.assertEquals("Apriori Cost Material not selected", Constants.EMAIL_APRIORICOST_MATERIAL, values.get(
                "cost-material"));
        Assert.assertTrue("Next button not enabled after all email fields filled in", (boolean)values.get("after" +
                "-fields-filled"));
        Assert.assertTrue("Next button not enabled after none template selected", (boolean)values.get("after-none" +
                        "-selected"));

    }
}
