package com.apriori.acs.models.response.workorders.upload;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/FileUploadResponse.json")
public class FileUploadResponse {
    private FileUpload response;

    public FileUpload getResponse() {
        return response;
    }

    public FileUploadResponse setResponse(FileUpload response) {
        this.response = response;
        return this;
    }
}
