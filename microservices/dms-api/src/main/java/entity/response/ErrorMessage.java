package entity.response;

import com.apriori.utils.ErrorResponse;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_Epoch;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Schema(location = "ErrorMessageSchema.json")
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    @JsonDeserialize(using = DateTimeDeserializer_Epoch.class)
    private LocalDateTime timestamp;
    private Integer status;
    private String method;
    private String error;
    private ArrayList<ErrorResponse> errors;
    private String message;
    private String path;
}
