package com.apriori.dds.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRequestParameters {
    private String status;
    private String content;
    private List<String> mentionedUserEmails;
}