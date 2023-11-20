package com.apriori.cid.api.models.response.preferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Successes {
    private String identity;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private String createdByName;
    private String updatedByName;
    private String name;
    private String type;
    private String value;
}
