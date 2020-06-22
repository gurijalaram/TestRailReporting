package com.apriori.cis.entity.response;

import com.apriori.apibase.services.CustomerBase;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSX;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;


@Schema(location = "CustomerSchema.json")
public class Customer extends CustomerBase {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSX.class)
    private LocalDateTime createdAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSX.class)
    private LocalDateTime updatedAt;

    private Customer response;

    public Customer getResponse() {
        return this.response;
    }

    public Customer setResponse(Customer response) {
        this.response = response;
        return this;
    }
}
