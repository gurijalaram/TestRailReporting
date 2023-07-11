package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectResponseSchema.json")
public class BidPackageProjectResponse {
    private String identity;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String updatedBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String name;
    private String description;
    private String type;
    private String status;
    private String bidPackageIdentity;
    private BidPackageProjectProfileResponse projectProfile;
    public List<Object> items;
    public List<BidPackageProjectUserResponse> users;
    private String organizationName;
    private String owner;
    private String displayName;
    private String ownerFullName;
    private String ownerUserIdentity;
}
