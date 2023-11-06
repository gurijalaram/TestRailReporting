package com.apriori.cic.ui.utils;

import com.apriori.cic.ui.pageobjects.workflows.schedule.details.WorkflowSchedule;

import java.util.ArrayList;

public class ScheduleFactory {
    private WorkflowSchedule workflowSchedule;

    public ScheduleFactory() {
        workflowSchedule = new WorkflowSchedule();
    }

    /**
     * Return a schedule object for Minutes
     *
     * @param numberOfMinutes
     * @return Minutes schedule
     */
    public WorkflowSchedule getMinuteSchedule(Integer numberOfMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.MINUTES);
        workflowSchedule.setNumberOfMinutes(numberOfMinutes);
        return workflowSchedule;
    }

    /**
     * Returns an every hour schedule
     *
     * @param numberOfHours
     * @return Hourly schedule
     */
    public WorkflowSchedule getHourSchedule(Integer numberOfHours) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.HOUR);
        workflowSchedule.setSelectEveryHour(true);
        workflowSchedule.setNumberOfHours(numberOfHours);
        return workflowSchedule;
    }

    /**
     * Returns an hourly schedule
     *
     * @param startHour two digit starting hour as a string (eg. "00")
     * @param startMinutes two digit starting minutes as a string (eg. "05")
     * @return hourly schedule
     */
    public WorkflowSchedule getHourlySchedule(String startHour, String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.HOUR);
        workflowSchedule.setSelectEveryHour(false);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }

    /**
     * Return an every day schedule
     *
     * @param numberOfDays
     * @param startHour
     * @param startMinutes
     * @return Daily schedule
     */
    public WorkflowSchedule getDailySchedule(Integer numberOfDays, String startHour, String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.DAILY);
        workflowSchedule.setSelectEveryDay(true);
        workflowSchedule.setNumberOfDays(numberOfDays);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }

    /**
     * Return a daily schedule
     *
     * @param startHour
     * @param startMinutes
     * @return Daily schedule
     */
    public WorkflowSchedule getDailySchedule(String startHour, String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.DAILY);
        workflowSchedule.setSelectEveryDay(false);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }

    /**
     * Return a weekly schedule
     *
     * @param weekDays An array of week days (type WorkflowSchedule.WeekDay)
     * @param startHour
     * @param startMinutes
     * @return weekly schedule
     */
    public WorkflowSchedule getWeeklySchedule(ArrayList<WorkflowSchedule.WeekDay> weekDays, String startHour,
                                              String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.WEEKLY);
        workflowSchedule.setWeekDays(weekDays);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }

    /**
     * Returns a day of every month schedule
     *
     * @param dayOfMonth The day of the month
     * @param numberOfMonths Every number of months (eg. Every 3 months)
     * @param startHour
     * @param startMinutes
     * @return Monthly Schedule
     */
    public WorkflowSchedule getMonthlySchedule(Integer dayOfMonth, Integer numberOfMonths, String startHour,
                                               String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.MONTHLY);
        workflowSchedule.setSelectDayInEveryMonth(true);
        workflowSchedule.setDayOfMonth(dayOfMonth);
        workflowSchedule.setNumberOfMonths(numberOfMonths);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }



    /**
     * Returns a day of every month schedule
     *
     * @param occurance The occurance in a month (type is WorkflowSchedule.MonthlyOccurance)
     * @param weekDay The week day to schedule for (type is WorkflowSchedule.WeekDay)
     * @param numberOfMonths Every number of months (eg. Every 3 months)
     * @param startHour
     * @param startMinutes
     * @return Monthly Schedule
     */
    public WorkflowSchedule getMonthlySchedule(WorkflowSchedule.MonthlyOccurance occurance,
                                               WorkflowSchedule.WeekDay weekDay, Integer numberOfMonths,
                                               String startHour,
                                               String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.MONTHLY);
        workflowSchedule.setSelectDayInEveryMonth(false);
        workflowSchedule.setMonthlyOccurance(occurance);
        workflowSchedule.setWeekDay(weekDay);
        workflowSchedule.setNumberOfMonths(numberOfMonths);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }

    /**
     * Returns an every year schedule
     *
     * @param month
     * @param dayOfMonth
     * @param startHour
     * @param startMinutes
     * @return
     */
    public WorkflowSchedule getYearlySchedule(WorkflowSchedule.Month month, Integer dayOfMonth, String startHour,
            String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.YEARLY);
        workflowSchedule.setSelectEveryYear(true);
        workflowSchedule.setMonth(month);
        workflowSchedule.setDayOfMonth(dayOfMonth);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }

    /**
     * Returns an every year schedule
     *
     * @param occurance
     * @param month
     * @param weekDay
     * @param startHour
     * @param startMinutes
     * @return
     */
    public WorkflowSchedule getYearlySchedule(WorkflowSchedule.MonthlyOccurance occurance, WorkflowSchedule.Month month,
                                              WorkflowSchedule.WeekDay weekDay,
                                              String startHour,
                                              String startMinutes) {
        workflowSchedule = new WorkflowSchedule();
        workflowSchedule.setSchedule(WorkflowSchedule.Schedule.YEARLY);
        workflowSchedule.setSelectEveryYear(false);
        workflowSchedule.setMonthlyOccurance(occurance);
        workflowSchedule.setWeekDay(weekDay);
        workflowSchedule.setMonth(month);
        workflowSchedule.setStartHour(startHour);
        workflowSchedule.setStartMinutes(startMinutes);
        return workflowSchedule;
    }
}
