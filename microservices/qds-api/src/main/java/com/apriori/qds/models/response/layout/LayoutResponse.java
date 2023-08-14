package com.apriori.qds.models.response.layout;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "LayoutResponseSchema.json")
public class LayoutResponse {
    private String identity;
    private String createdBy;
    private String updatedBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String userIdentity;
    private String applicationIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String name;
    private boolean published;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime publishedAt;
    private ArrayList<ViewElementResponse> viewElements;
}
