package com.apriori.acs.entity.response.acs.designGuidance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InfosByTopics {
    @JsonProperty("@type")
    public String attype;
    public String suggestedMin;
    public String vpe;
    public GcdItem gcd;
    public String attributeText;
    public int decimalPlaces;
    public String current;
    public CustomItem custom;
    public String attributeId;
    public String messageId;
    public ProcessItem process;
    public String messageText;
    public String suggestedText;
    public String currentText;
    public String categoryText;
    public boolean suggestedIsMultiValued;
    public String category;
    public String rule;
    public int priority;
}
