package com.apriori.sds.entity.request;

import com.apriori.cidappapi.entity.request.GroupItems;
import com.apriori.cidappapi.entity.request.Options;
import com.apriori.reader.file.user.UserCredentials;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublishRequest {
    private String assignedTo;
    private String costMaturity;
    private Boolean override;
    private String status;
    private String scenarioName;
    private List<GroupItems> groupItems;
    private Options options;
    private Boolean locked;
    private UserCredentials user;
    private boolean publishSubComponents;
}
