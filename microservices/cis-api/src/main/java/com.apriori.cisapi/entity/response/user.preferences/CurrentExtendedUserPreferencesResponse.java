package com.apriori.cisapi.entity.response.user.preferences;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(location = "CurrentExtendedUserPreferencesResponseSchema.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "response")
public class CurrentExtendedUserPreferencesResponse extends Pagination {
    private String identity;
    private String updatedBy;
    private String createdBy;
    private String createdByName;
    private String updatedByName;
    private String name;
    private String type;
    private String value;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX.class)
    private LocalDateTime updatedAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX.class)
    private LocalDateTime createdAt;

    private String userIdentity;
    private String avatarColor;
    private String customerIdentity;
}

