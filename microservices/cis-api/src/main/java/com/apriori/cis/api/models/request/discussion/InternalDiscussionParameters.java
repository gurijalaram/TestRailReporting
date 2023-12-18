package com.apriori.cis.api.models.request.discussion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternalDiscussionParameters {
    private String type;
    private String status;
    private String description;
    private String subject;
    private InternalDiscussionAttributes attributes;
    private List<InternalDiscussionComments> comments;
}
