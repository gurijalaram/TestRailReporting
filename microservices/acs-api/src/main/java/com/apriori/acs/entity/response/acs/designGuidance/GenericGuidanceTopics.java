package com.apriori.acs.entity.response.acs.designGuidance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GenericGuidanceTopics {
    @JsonProperty("@type")
    private String attype;
    private String suggestedMin;
    private String vpe;
    private GcdItem gcd;
    private String attributeText;
    private Integer decimalPlaces;
    private String current;
    private CustomItem custom;
    private String attributeId;
    private String messageId;
    private ProcessItem process;
    private String messageText;
    private String suggestedText;
    private String currentText;
    private String categoryText;
    private Boolean suggestedIsMultiValued;
    private String category;
    private String rule;
    private Integer priority;
}
