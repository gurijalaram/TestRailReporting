package com.apriori.sds.api.models.request;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.request.component.GroupItems;
import com.apriori.shared.util.models.request.component.Options;

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
