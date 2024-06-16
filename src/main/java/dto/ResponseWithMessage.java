package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(buildMethodName = "builder", setterPrefix = "with")
public class ResponseWithMessage {
    private int code;
    private String type;
    private String message;
}