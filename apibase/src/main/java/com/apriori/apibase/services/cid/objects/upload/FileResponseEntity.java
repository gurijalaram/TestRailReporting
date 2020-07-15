package com.apriori.apibase.services.cid.objects.upload;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileUploadSchema.json")
public class FileResponseEntity {

    @JsonProperty
    private FileUploadEntity response;

    public FileUploadEntity getResponse() {
        return response;
    }

    public FileResponseEntity setResponse(FileUploadEntity response) {
        this.response = response;
        return this;
    }
}
