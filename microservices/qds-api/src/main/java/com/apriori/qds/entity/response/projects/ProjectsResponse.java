package com.apriori.qds.entity.response.projects;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ProjectsResponseSchema.json")
public class ProjectsResponse extends Pagination {
    private List<ProjectResponse> items;
}
