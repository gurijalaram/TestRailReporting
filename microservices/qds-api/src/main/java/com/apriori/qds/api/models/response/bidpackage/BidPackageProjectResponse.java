package com.apriori.qds.api.models.response.bidpackage;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "QdsBidPackageProjectResponseSchema.json")
public class BidPackageProjectResponse {
    public List<BidPackageProjectItemResponse> items;
    public List<BidPackageProjectUserResponse> users;
    private String identity;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String updatedBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String name;
    private String description;
    private String organizationName;
    private String owner;
    private String type;
    private String status;
    private String bidPackageIdentity;
    private BidPackageProjectProfileResponse projectProfile;
    private String displayName;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime dueAt;
    private String ownerFullName;
    private String ownerUserIdentity;
}
