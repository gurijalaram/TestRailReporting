package com.apriori.cis.api.models.request.discussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternalDiscussionAttributes {
    private String attribute;
    private String attributeDisplayValue;
    private String subject;
}
