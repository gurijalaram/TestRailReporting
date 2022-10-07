package com.apriori.qds.entity.request.projects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private String identity;
    private String name;
    private String description;
    private String status;
    private String type;
    public ProjectProfile projectProfile;
}
