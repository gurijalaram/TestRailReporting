package com.apriori.acs.entity.response.acs.designGuidance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class InfosByTopics {
    @JsonProperty("DTC_MESSAGES")
    private List<GenericGuidanceTopics> DTC_MESSAGES;
}
