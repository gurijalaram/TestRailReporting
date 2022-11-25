package com.apriori.qms.entity.response.scenariodiscussion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attributes {
    private String attribute;
    private String attributeDisplayValue;
    private String componentType;
    private String models;
    private String pinDetails;
    private String subject;
}
