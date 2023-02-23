package com.apriori.pagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleData {
    private String enabled;
    private String schedule;
    private Integer numberOfMinutes;
}
