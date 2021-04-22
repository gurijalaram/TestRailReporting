package com.apriori.features;

import com.apriori.objects.Workflow;
import com.apriori.objects.WorkflowSchedule;
import com.apriori.pageobjects.EditWorkflowPage;
import com.apriori.pageobjects.NewWorkflowPage;
import com.apriori.pageobjects.WorkflowPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;
import utils.ScheduleFactory;
import utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewWorkflowFeatures {
    private static final Logger logger = LoggerFactory.getLogger(NewWorkflowFeatures.class);

    private WebDriver driver;
    private WorkflowPage workflowPage;
    private NewWorkflowPage newWorkflowPage;
    private Map<String, Object> values;
    private Map<String, Boolean> valuesB;
    private PageUtils pageUtils;
    private int newWorkflowIteration;
    private ScheduleFactory scheduleFactory;

    private EditWorkflowPage editWorkflowPage;
    private ArrayList<Workflow> workflows;

    public NewWorkflowFeatures(WebDriver driver) {
        this.driver = driver;
        this.workflowPage = new WorkflowPage(this.driver);
        this.newWorkflowPage = new NewWorkflowPage(driver);
        this.pageUtils = new PageUtils(driver);
        this.scheduleFactory = new ScheduleFactory();
        newWorkflowIteration = 1;
    }
    
    /**
     * Retrieve the state of the fields on the "New Workflow Details" tab
     *
     * @return Value map of the field states
     */
    public Map<String, Boolean> doDetailsTabFieldInspection() {
        valuesB = new HashMap();

        Boolean nameFieldExists = newWorkflowPage.nameFieldExists();
        valuesB.put("name", nameFieldExists);
        Boolean descriptionFieldExists = newWorkflowPage.descriptionFiledExistis();
        valuesB.put("description", descriptionFieldExists);
        Boolean connectorDropdownExists = newWorkflowPage.connectorDropdownExists();
        valuesB.put("connector", connectorDropdownExists);
        Map<String, Boolean> scheduleTabsExist = newWorkflowPage.schedulesExist();

        for (Map.Entry<String, Boolean> entry : scheduleTabsExist.entrySet()) {
            valuesB.put(entry.getKey(), entry.getValue());
        }

        return valuesB;
    }

    public Map<String, Object> doFieldInputInspectionOverload(String field) {
        return doFieldInputInspection(field, false, true, true);
    }

    public Map<String, Object> doFieldInputInspectionUnsupportedSpecial(String field) {
        return doFieldInputInspection(field, false, true, true);
    }

    public Map<String, Object> doFieldInputInspectionSpecial(String field) {
        return doFieldInputInspection(field, false, true, false);
    }

    public Map<String, Object> doFieldInputInspectionMin(String field) {
        return doFieldInputInspection(field, true, false, false);
    }

    public Map<String, Object> doFieldInputInspection(String field) {
        return doFieldInputInspection(field, false, false, false);
    }

    /**
     * Create new workflow with field text of different lengths
     *
     * @param field the field to input text to
     * @param useMinimum if true, the minimum input length will be used
     * @param useSpecialCharacters if true, the input will contain allowed special characters
     * @return
     */
    public Map<String, Object> doFieldInputInspection(String field, boolean useMinimum,
                                                       boolean useSpecialCharacters, boolean useUnsuportedSpecial) {
        values = new HashMap<>();
        String name = "";
        String description = "";
        switch (field.toUpperCase()) {
            case "NAME":
                name = generateWorflowName(useMinimum, useSpecialCharacters, useUnsuportedSpecial);
                description = Constants.DEFAULT_WORKFLOW_DESCRIPTION;
                break;
            case "DESCRIPTION":
                name = UIUtils.saltString(Constants.DEFAULT_WORKFLOW_NAME);
                description = UIUtils.generateString(Constants.MAXIMUM_WORKFLOW_DESCRIPTION_CHARACTERS);
                break;
            default:
                logger.debug("Field doesn't exist: " + field);
                return null;
        }

        newWorkflowPage.createNewWorkflow(name, description, newWorkflowIteration);
        workflowPage.refreshPage();
        Boolean workflowExisits = workflowPage.workflowExists(name);
        values.put("workflowExists", workflowExisits);
        values.put("name", name);
        return values;
    }

    /**
     * Check the schedule functionality
     * @return
     */
    public Map<String, Object> checkScheduleFunctionality() {
        editWorkflowPage = new EditWorkflowPage(driver);
        String scheduleName = "0 0 0 0 0 Schedule Test - ";
        String wfName;
        workflows = new ArrayList<>();
        Workflow workflow;
        ArrayList<String> workflowNames = new ArrayList<>();

        // Minutes
        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getMinuteSchedule(3), "minutes");
        workflows.add(workflow);
        workflowNames.add(wfName);

        //Hourly
        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getHourlySchedule("01", "10"), "hourly");
        workflows.add(workflow);
        workflowNames.add(wfName);

        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getHourSchedule(5), "hourly-every");
        workflows.add(workflow);
        workflowNames.add(wfName);

        //Daily
        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getDailySchedule("01", "10"), "daily");
        workflows.add(workflow);
        workflowNames.add(wfName);

        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getDailySchedule(4, "01", "10"),
                "daily-every");
        workflows.add(workflow);
        workflowNames.add(wfName);

        //Weekly
        wfName = UIUtils.saltStringUUID(scheduleName);
        ArrayList<WorkflowSchedule.WeekDay> weekDays = new ArrayList<>();
        weekDays.add(WorkflowSchedule.WeekDay.FRIDAY);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getWeeklySchedule(weekDays, "12", "30"),
                "weekly");
        workflows.add(workflow);
        workflowNames.add(wfName);

        //Monthly
        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName, scheduleFactory.getMonthlySchedule(5,3, "12", "30"),
                "monthly-every");
        workflows.add(workflow);
        workflowNames.add(wfName);

        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName,
                scheduleFactory.getMonthlySchedule(WorkflowSchedule.MonthlyOccurance.THIRD,
                WorkflowSchedule.WeekDay.SATURDAY, 3, "01", "10"), "monthly");
        workflows.add(workflow);
        workflowNames.add(wfName);

        //Yearly
        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName,
                scheduleFactory.getYearlySchedule(WorkflowSchedule.Month.DECEMBER, 3,
                "01",
                "10"), "yearly-every");
        workflows.add(workflow);
        workflowNames.add(wfName);

        wfName = UIUtils.saltStringUUID(scheduleName);
        workflow = setWorkflowScheduleAndName(wfName,
                scheduleFactory.getYearlySchedule(WorkflowSchedule.MonthlyOccurance.FOURTH,
                WorkflowSchedule.Month.FEBRUARY, WorkflowSchedule.WeekDay.MONDAY, "00", "00"), "yearly");
        workflows.add(workflow);
        workflowNames.add(wfName);

        values = doCreateEditScheduleCheck();
        values.put("workflows", workflowNames);
        return  values;
    }

    /**
     * Checks the state of the NEXT button during different stages of data entry. The NEXT button should
     * only be enabled if a nme is provided and a connector is selected
     *
     * @param workflowTab The tab containing the NEXT button to check. The supported tabs
     *                    are "DETAILS", "QUERY" and "CONNECTOR"
     * @return Map containg the data entry stage and NEXT button's state
     */
    public Map<String, Boolean> checkNWFNextButtonState(String workflowTab) {
        valuesB = new HashMap<>();
        boolean enabled;

        switch (workflowTab.toUpperCase()) {
            case "DETAILS":
                enabled = checkNextButtonState("NAME");
                valuesB.put("name-only", enabled);
                enabled = checkNextButtonState("DESCRIPTION");
                valuesB.put("add-description", enabled);
                enabled = checkNextButtonState("CONNECTOR");
                valuesB.put("select-connector", enabled);
                break;
            default:
                logger.debug("Invalid workflow tab: " + workflowTab);
                return null;
        }

        return valuesB;
    }

    /**
     * Checks if the NEXT button is enabled
     *
     * @param field The field to select a value or enter data
     * @return True if the NEXT button is enabled
     */
    public Boolean checkNextButtonState(String field) {
        switch (field.toUpperCase()) {
            case "NAME":
                newWorkflowPage.fillWorkflowNameField(Constants.DEFAULT_WORKFLOW_NAME);
                break;
            case "DESCRIPTION":
                newWorkflowPage.fillWorkflowDescriptionField(Constants.DEFAULT_WORKFLOW_DESCRIPTION);
                break;
            case "CONNECTOR":
                newWorkflowPage.selectWorkflowConnector();
                break;
            default:
                logger.debug("Invalid details tab field: " + field);
                return null;
        }

        Map<String, Boolean> states = newWorkflowPage.getNavigationButtonState(NewWorkflowPage.Tab.DETAILS,
                NewWorkflowPage.NavigationButton.NEXT);
        return states.get("enabled");
    }

    /**
     * Generate a workflow name of varying length, with or without special characters
     *
     * @param useMinimum If true, the name string contains only a single character
     * @param useSpecialCharacters If true, the name will contain all s
     * @param useUnsuportedSpecial If true, the name will contain unsupporte
     * @return name string
     */
    private String generateWorflowName(boolean useMinimum, boolean useSpecialCharacters, boolean useUnsuportedSpecial) {
        String prefix = "0 0 0 0 Automation - ";
        char[] specialCharacters = new char[3];
        specialCharacters[0] = '_';
        specialCharacters[1] = ' ';
        specialCharacters[2] = '-';
        char[] unsupportedSpecialCharacters = new char[1];
        unsupportedSpecialCharacters[0] = '@';
        int stringSize = 0;

        if (useMinimum) {
            return "a";
        }

        stringSize = Constants.MAXIMUM_WORKFLOW_NAME_CHARACTERS - prefix.length();

        if (useSpecialCharacters) {
            stringSize -= specialCharacters.length;
            return UIUtils.generateString(stringSize, specialCharacters);
        }

        if (useUnsuportedSpecial) {
            stringSize -= unsupportedSpecialCharacters.length;
            return UIUtils.generateString(stringSize, unsupportedSpecialCharacters);
        }

        return UIUtils.generateString(stringSize);
    }

    /**
     * Creates a new workflow with a name and schedule
     *
     * @param name workflow name
     * @param schedule workflow schedule
     */
    private Workflow setWorkflowScheduleAndName(String name, WorkflowSchedule schedule, String scheduleType) {
        Workflow workflow = new Workflow();
        workflow.setSchedule(schedule)
                .setName(name)
                .setScheduleType(scheduleType);
        return workflow;
    }

    /**
     * Creates a batch of workflows with schedules set
     *
     * @return
     */
    private Map<String, Object> doCreateEditScheduleCheck() {
        values = new HashMap<>();

        for (Workflow workflow : workflows) {
            workflowPage.newWorkflow();
            newWorkflowPage.createNewWorkflow(workflow.getName(), "", true, true,
                    workflow.getSchedule(), newWorkflowIteration);
            newWorkflowIteration += 1;
            values.put(workflow.getScheduleType().concat("-schedule-workflow-exists"),
                    workflowPage.workflowExists(workflow.getName()));
        }

        return values;
    }
}
