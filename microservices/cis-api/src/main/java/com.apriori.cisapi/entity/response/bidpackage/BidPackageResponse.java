package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageResponseSchema.json")
public class BidPackageResponse {
    private String identity;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String name;
    private String createdByName;
    private String description;
    private String status;
    private String updatedBy;
    private String updatedByName;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String customerIdentity;
    private String assignedTo;
    private List<BidPackageItemResponse> items;
    private List<BidPackageProjectResponse> projects;
}