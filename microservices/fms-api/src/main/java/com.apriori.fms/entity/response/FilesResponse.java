package com.apriori.fms.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "FilesSchema.json")
public class FilesResponse extends Pagination {
    private List<FileResponse> items;
    private FilesResponse response;
}
