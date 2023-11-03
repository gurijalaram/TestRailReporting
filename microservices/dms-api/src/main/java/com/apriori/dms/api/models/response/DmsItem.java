package com.apriori.dms.api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DmsItem {

    private DmsAssignee assignee;
    private DmsAttributes attributes;
    private List<DmsCommentResponse> comments;
    private String createdAt;
    private String createdBy;
    private String description;
    private String identity;
    private List<DmsParticipant> participants;
    private String status;

}
