package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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