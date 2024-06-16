package dto;

import enums.Status;
import lombok.*;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "shipDate")
public class OrderResponse {
    private Integer id;
    private Integer petId;
    private Integer quantity;
    private String status;
    private Boolean complete;
    private String shipDate;
}