package com.apriori.qds.entity.response.projects;

import com.apriori.qds.entity.request.projects.ProjectProfile;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonRootName("response")
@Schema(location = "ProjectResponseSchema.json")
public class ProjectResponse {

    private String identity;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String name;
    private String description;
    private String type;
    private String status;
    private String bidPackageIdentity;
    private ProjectProfile projectProfile;
}