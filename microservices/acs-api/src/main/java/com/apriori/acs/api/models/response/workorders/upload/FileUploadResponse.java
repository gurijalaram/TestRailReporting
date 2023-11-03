package com.apriori.acs.api.models.response.workorders.upload;

import com.apriori.shared.util.annotations.Schema;

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
