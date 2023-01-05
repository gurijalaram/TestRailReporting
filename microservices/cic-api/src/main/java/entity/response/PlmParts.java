package entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "PlmPartsSchema.json")
public class PlmParts {
    private List<PlmPart> items;
}