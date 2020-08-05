package com.apriori.apibase.services.cid.objects.response.upload;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileUploadSchema.json")
public class FileUploadResponse {

    @JsonProperty
    private FileUpload response;

    public FileUpload getResponse() {
        return response;
    }

    public FileUploadResponse setResponse(FileUpload response) {
        this.response = response;
        return this;
    }
}
