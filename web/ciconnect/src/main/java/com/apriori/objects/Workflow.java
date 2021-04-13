package com.apriori.objects;

public class Workflow {
    private String name;
    private WorkflowSchedule schedule;
    private String scheduleType;

    public String getScheduleType() {
        return scheduleType.toLowerCase();
    }

    public Workflow setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
        return this;
    }

    public String getName() {
        return name;
    }

    public Workflow setName(String name) {
        this.name = name;
        return this;
    }

    public WorkflowSchedule getSchedule() {
        return schedule;
    }

    public Workflow setSchedule(WorkflowSchedule schedule) {
        this.schedule = schedule;
        return this;
    }
}
