package com.apriori.bcs.entity.response;

import com.apriori.bcs.utils.CustomerBase;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@JsonRootName("response")
@Schema(location = "CustomerSchema.json")
public class Customer {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;

    private Customer response;
    private String identity;
    private String createdBy;
    private String updatedBy;
    private String name;
    private String description;
    private List<String> emailRegexPatterns;
    private Boolean active;
    private Integer maxCadFileRetentionDays;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private List<Object> oneTimePasswordApplications;
    private String customerType;
    private String cloudReference;
    private String salesforceId;
    private String maxCadFileSize;

}
