package com.apriori.cds.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Features {
    private Boolean workOrderStatusUpdatesEnabled;
    private String identity;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
}
