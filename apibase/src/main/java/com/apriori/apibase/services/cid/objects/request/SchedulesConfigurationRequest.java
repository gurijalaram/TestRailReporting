package com.apriori.apibase.services.cid.objects.request;

/**
 * Request data example
 * "schedules" : [ {
 *         "type" : "SIMPLE",
 *         "startTime" : "1578921720000",
 *         "repeatCount" : 0,
 *         "repeatInterval" : 0
 *  } ],
 */
public class SchedulesConfigurationRequest {
    private String type;
    private long startTime = System.currentTimeMillis();
    private Integer repeatCount;
    private Integer repeatInterval;

    public String getType() {
        return type;
    }

    public SchedulesConfigurationRequest setType(String type) {
        this.type = type;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public SchedulesConfigurationRequest setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public SchedulesConfigurationRequest setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public SchedulesConfigurationRequest setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
        return this;
    }
}
