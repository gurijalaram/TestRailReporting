package com.apriori.fms.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "FilesSchema.json")
public class FilesResponse extends Pagination {
    private List<FileResponse> items;
    private FilesResponse response;
}
