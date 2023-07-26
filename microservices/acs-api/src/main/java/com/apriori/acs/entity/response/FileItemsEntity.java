package com.apriori.acs.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileItemsEntity {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String customerIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String applicationIdentity;
    private String userIdentity;
    private String filename;
    private String folder;
    @JsonProperty("filesize")
    private String fileSize;
    private String md5hash;
}