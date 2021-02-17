package com.apriori.apibase.services.cid.objects.response.upload;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "FileUploadResponse.json")
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
