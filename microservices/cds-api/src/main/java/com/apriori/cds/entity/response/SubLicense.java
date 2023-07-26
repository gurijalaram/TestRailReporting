package com.apriori.cds.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateDeserializer_yyyyMMdd;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SubLicenseSchema.json")
@JsonRootName("response")
@Data
public class SubLicense {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String name;
    private String uuid;
    private List<String> licensedModuleNames = null;
    private Integer maxNumUsers;
    private Integer numPurchasedUsers;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateDeserializer_yyyyMMdd.class)
    private LocalDate expiresAt;
    private String licenseSignature;
    private Integer numAssignedUsers;
}
