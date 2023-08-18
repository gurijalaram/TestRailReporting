package com.apriori.pageobjects.workflows.schedule.details;

import com.apriori.http.utils.GenerateStringUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowSchedule {
    public enum Schedule {
        MINUTES,
        HOUR,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public enum WeekDay {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    public enum MonthlyOccurance {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    public enum Month {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER
    }

    private Schedule schedule;
    private MonthlyOccurance monthlyOccurance;
    private ArrayList<WeekDay> weekDays;
    private WeekDay weekDay;
    private Month month;
    private Integer numberOfMinutes;
    private Integer numberOfHours;
    private Integer numberOfDays;
    private Integer dayOfMonth;
    private Integer numberOfMonths;
    private boolean selectEveryHour;
    private boolean selectDayInEveryMonth;
    private boolean selectEveryDay;
    private boolean selectEveryYear;
    private String startHour;
    private String startMinutes;

    public String getWeekDay() {
        return getWeekDayString(weekDay);
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isSelectEveryYear() {
        return selectEveryYear;
    }

    public void setSelectEveryYear(boolean selectEveryYear) {
        this.selectEveryYear = selectEveryYear;
    }

    public Integer getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(Integer numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public boolean isSelectDayInEveryMonth() {
        return selectDayInEveryMonth;
    }

    public void setSelectDayInEveryMonth(boolean selectDayInEveryMonth) {
        this.selectDayInEveryMonth = selectDayInEveryMonth;
    }

    public void setSelectEveryDay(boolean selectEveryDay) {
        this.selectEveryDay = selectEveryDay;
    }

    public boolean isSelectEveryHour() {
        return selectEveryHour;
    }

    public void setSelectEveryHour(boolean selectEveryHour) {
        this.selectEveryHour = selectEveryHour;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Integer getNumberOfMinutes() {
        return numberOfMinutes;
    }

    public void setNumberOfMinutes(Integer numberOfMinutes) {
        this.numberOfMinutes = numberOfMinutes;
    }

    public Integer getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(Integer numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public ArrayList getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(ArrayList<WeekDay> weekDays) {
        ArrayList weekDayList = new ArrayList();
        for (WeekDay weekDay : weekDays) {
            weekDayList.add(getWeekDayString(weekDay));
        }
        this.weekDays = weekDayList;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        Integer hour = Integer.parseInt(startHour);

        if (hour > 23) {
            this.startHour = "00";
            return;
        }

        this.startHour = startHour;
    }

    public String getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(String startMinutes) {
        Integer minutes = Integer.parseInt(startMinutes);

        if (minutes > 59) {
            this.startMinutes = "00";
            return;
        }

        this.startMinutes = startMinutes;
    }

    public boolean isSelectEveryDay() {
        return selectEveryDay;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonthlyOccurance() {
        return GenerateStringUtil.getFirstLetterUpperCase(monthlyOccurance.toString());
    }

    public void setMonthlyOccurance(MonthlyOccurance monthlyOccurance) {
        this.monthlyOccurance = monthlyOccurance;
    }

    public String getMonth() {
        return GenerateStringUtil.getFirstLetterUpperCase(month.toString());
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    /**
     * Returns a string based on the enum value provided, with only the first letter capitalized.
     * This is the way values are displayed in dropdown options
     *
     * @param weekDay
     * @return
     */
    private String getWeekDayString(WeekDay weekDay) {
        return GenerateStringUtil.getFirstLetterUpperCase(weekDay.toString());
    }
}
