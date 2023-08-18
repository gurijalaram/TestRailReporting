package com.apriori.dms.models.response;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

public class DmsParticipant {
    public String identity;
    public String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    public LocalDateTime createdAt;
    public String userCustomerIdentity;
    public String userIdentity;
}
