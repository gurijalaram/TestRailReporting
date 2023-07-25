package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPackageItem {
    private String identity;
    private String bidPackageIdentity;
    private String projectIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
    private String iterationIdentity;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
}
