package com.apriori.dms.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentViewParameters {
    private String participantIdentity;
    private String userCustomerIdentity;
    private String userIdentity;
}
