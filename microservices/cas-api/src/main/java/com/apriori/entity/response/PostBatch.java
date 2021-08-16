package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "PostBatchSchema.json")
public class PostBatch {
    private PostBatch response;
    private String identity;
    private String id;
    private String customerIdentity;
    private String filename;
    private Integer itemCount;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;

    public PostBatch getResponse() {
        return response;
    }

    public String getIdentity() {
        return identity;
    }

    public String getId() {
        return id;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public String getFilename() {
        return filename;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }
}
