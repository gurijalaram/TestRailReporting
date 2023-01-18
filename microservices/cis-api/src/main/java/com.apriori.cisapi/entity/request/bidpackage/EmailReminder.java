package com.apriori.cisapi.entity.request.bidpackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailReminder {
    private boolean active;
    private String startDuration;
    private String frequencyValue;
}
