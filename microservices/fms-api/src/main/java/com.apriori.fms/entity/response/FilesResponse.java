package com.apriori.fms.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "FilesSchema.json")
public class FilesResponse extends Pagination {
    private List<FileResponse> items;
    private FilesResponse response;
}
