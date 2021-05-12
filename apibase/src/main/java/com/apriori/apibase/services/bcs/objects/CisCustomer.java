package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.CustomerBase;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSX;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;


@Schema(location = "cds/CdsCustomerSchema.json")
public class CisCustomer extends CustomerBase {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSX.class)
    private LocalDateTime createdAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSX.class)
    private LocalDateTime updatedAt;

    private CisCustomer response;

    public CisCustomer getResponse() {
        return this.response;
    }

    public CisCustomer setResponse(CisCustomer response) {
        this.response = response;
        return this;
    }
}
