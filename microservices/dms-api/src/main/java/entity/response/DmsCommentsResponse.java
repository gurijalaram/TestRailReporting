package entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DmsCommentsResponseSchema.json")
public class DmsCommentsResponse extends Pagination {
    public ArrayList<DmsCommentResponse> items;
}
