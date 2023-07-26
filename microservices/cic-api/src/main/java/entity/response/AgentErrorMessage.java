package entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.ErrorResponse;
import com.apriori.deserializers.DateTimeDeserializer_Epoch;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class AgentErrorMessage {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_Epoch.class)
    private LocalDateTime timestamp;
    private Integer status;
    private String method;
    private String error;
    private ArrayList<ErrorResponse> errors;
    private String message;
    private String path;
}
