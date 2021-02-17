package com.apriori.apibase.services.fms.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "fms/FmsFilesSchema.json")
public class FileResponses extends Pagination {
    private List<FileResponse> items;
    private FileResponses response;

    public FileResponses getResponse() {
        return this.response;
    }

    public FileResponses setResponse(FileResponses response) {
        this.response = response;
        return this;
    }

    public List<FileResponse> getItems() {
        return this.items;
    }

    public FileResponses setItems(List<FileResponse> items) {
        this.items = items;
        return this;
    }
}
