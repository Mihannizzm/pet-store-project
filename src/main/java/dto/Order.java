package dto;

import enums.Status;
import lombok.*;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;

@Data
@Builder(buildMethodName = "builder", setterPrefix = "with")
@EqualsAndHashCode(exclude = "shipDate")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Builder.Default
    private Object id = RandomUtils.nextInt(0, 10000);
    @Builder.Default
    private Object petId = RandomUtils.nextInt(0, 10000);
    @Builder.Default
    private Object quantity = 1;
    @Builder.Default
    private Object status = Status.placed.toString();
    @Builder.Default
    private Object complete = true;
    @Builder.Default
    private Object shipDate = LocalDateTime.now().toString();
}