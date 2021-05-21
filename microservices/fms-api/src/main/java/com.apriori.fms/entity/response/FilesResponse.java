package com.apriori.fms.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "FilesSchema.json")
public class FilesResponse extends Pagination {
    private List<FileResponse> items;

    private FilesResponse response;

    public FilesResponse getResponse() {
        return this.response;
    }

    public FilesResponse setResponse(FilesResponse response) {
        this.response = response;
        return this;
    }

    public List<FileResponse> getItems() {
        return this.items;
    }

    public FilesResponse setItems(List<FileResponse> items) {
        this.items = items;
        return this;
    }
}
