package com.apriori.cidappapi.entity.request;

import com.apriori.reader.file.user.UserCredentials;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishRequest implements Serializable {
    private String assignedTo;
    @Builder.Default
    private String costMaturity = "Initial".toUpperCase();
    @Builder.Default
    private Boolean override = false;
    @Builder.Default
    private String status = "New".toUpperCase();
    private String scenarioName;
    private List<GroupItems> groupItems;
    private Options options;
    private UserCredentials user;
}
