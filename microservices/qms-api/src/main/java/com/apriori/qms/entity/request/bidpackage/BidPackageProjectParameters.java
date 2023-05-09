package com.apriori.qms.entity.request.bidpackage;

import com.apriori.utils.json.serializers.DateTimeSerializer_yyyyMMddTHHmmssZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidPackageProjectParameters {
    private String name;
    private String description;
    private String status;
    private String type;
    public BidPackageProjectProfile projectProfile;
    private String displayName;
    @JsonSerialize(using = DateTimeSerializer_yyyyMMddTHHmmssZ.class)
    private LocalDateTime dueAt;
    private List<BidPackageItemRequest> items;
    private List<BidPackageProjectUserParameters> users;
}
