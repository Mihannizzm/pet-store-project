package utils;

import dto.Order;
import dto.ResponseWithMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BodyGenerator {

    public static Order.OrderBuilder getOrderBody() {
        return Order.builder();
    }

    public static ResponseWithMessage.ResponseWithMessageBuilder getResponseWithMessageBody() {
        return ResponseWithMessage.builder();
    }
}