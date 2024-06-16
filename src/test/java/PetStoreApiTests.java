import dto.Order;
import dto.ResponseWithMessage;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.BodyGenerator.getOrderBody;
import static utils.BodyGenerator.getResponseWithMessageBody;

/**
 * Класс содержит минимальный набор тестов для методов store в petstore api.
 * Для запуска тестов с нужным тегом используется команда maven: mvn test -Dgroups=Positive
 */
public class PetStoreApiTests {

    @Test
    @DisplayName("Тест получения категорий животных")
    @Tag("Positive")
    void GetInventoryTest() {
        ExtractableResponse<Response> getInventoryResponse = PetStoreClient.getInventory();
        assertEquals(200, getInventoryResponse.statusCode());
    }

    @Test
    @DisplayName("Тест создания, получения и удаления заказа")
    @Tag("Positive")
    void createChangeAndDeleteOrderPositiveTest() {
        //Создание нового заказа
        Order newOrder = getOrderBody().builder();
        ExtractableResponse<Response> createOrderResponse = PetStoreClient.createOrder(newOrder);
        Order orderResponse = createOrderResponse.body().as(Order.class);
        assertEquals(200, createOrderResponse.statusCode());
        assertEquals(newOrder, orderResponse);
        Integer orderId = (Integer) orderResponse.getId();

        //Получение заказа
        ExtractableResponse<Response> getOrderResponse = PetStoreClient.getOrder(orderId);
        assertEquals(200, getOrderResponse.statusCode());
        assertEquals(newOrder, getOrderResponse.body().as(Order.class));

        //Удаление заказа
        ExtractableResponse<Response> deleteOrderResponse = PetStoreClient.deleteOrder(orderId);
        assertEquals(200, deleteOrderResponse.statusCode());
        ResponseWithMessage deleteOrderMessage = getResponseWithMessageBody()
                .withCode(200)
                .withType("unknown")
                .withMessage(orderId.toString())
                .builder();
        assertEquals(deleteOrderMessage, deleteOrderResponse.body().as(ResponseWithMessage.class));

        //Проверка, что заказ не существует после удаления
        getOrderResponse = PetStoreClient.getOrder(orderId);
        assertEquals(404, getOrderResponse.statusCode());
        ResponseWithMessage getDeletedOrderMessage = getResponseWithMessageBody()
                .withCode(1)
                .withType("error")
                .withMessage("Order not found")
                .builder();
        assertEquals(getDeletedOrderMessage, getOrderResponse.body().as(ResponseWithMessage.class));
    }

    /**
     * По спецификации все поля действительно являются не обязательными, на деле же данное поведение можно считать багом,
     * т.к созданная сущность имеет параметры "petId": 0, "quantity": 0.
     */
    @Test
    @Tag("Positive")
    @DisplayName("Проверка, что все аттрибуты запроса на создание заказа не являются обязательными")
    void createOrderWithNullOnAllAttributesTest() {
        Order newOrderRequest = getOrderBody()
                .withId(null)
                .withPetId(null)
                .withStatus(null)
                .withComplete(null)
                .withQuantity(null)
                .withShipDate(null)
                .builder();

        ExtractableResponse<Response> createOrderResponse = PetStoreClient.createOrder(newOrderRequest);
        assertEquals(200, createOrderResponse.statusCode());
        Long orderId = (Long) createOrderResponse.body().as(Order.class).getId();

        ExtractableResponse<Response> getOrderResponse = PetStoreClient.getOrder(orderId);
        assertEquals(200, getOrderResponse.statusCode());
    }

    @ParameterizedTest(name = "Тест создания заказа при минимальном и максимальном значении аттрибутов")
    @MethodSource("minMaxExample")
    @Tag("Positive")
    void minMaxValueTest(Order newOrder) {
        ExtractableResponse<Response> createOrderResponse = PetStoreClient.createOrder(newOrder);
        assertEquals(200, createOrderResponse.statusCode());
    }

    private static Stream<Object> minMaxExample() {
        return Stream.of(
                Arguments.of(getOrderBody()
                        .withPetId(Integer.MIN_VALUE)
                        .withId(Integer.MIN_VALUE)
                        .withQuantity(Integer.MIN_VALUE)
                        .withStatus(randomAlphabetic(1))
                        .withComplete(false)
                        .builder()),
                Arguments.of(getOrderBody()
                        .withPetId(Integer.MAX_VALUE)
                        .withId(Integer.MAX_VALUE)
                        .withQuantity(Integer.MAX_VALUE)
                        .withStatus(randomAlphabetic(255))
                        .withComplete(true)
                        .builder())
        );
    }


    @Test
    @Tag("Negative")
    @DisplayName("Проверка сообщения об ошибке при удалении несуществующего заказа")
    void deletingNonExistentOrderTest() {
        ExtractableResponse<Response> deleteOrderResponse = PetStoreClient.deleteOrder(RandomUtils.nextInt(0, 9000));
        assertEquals(404, deleteOrderResponse.statusCode());

        ResponseWithMessage getDeletedOrderMessage = getResponseWithMessageBody()
                .withCode(404)
                .withType("unknown")
                .withMessage("Order Not Found")
                .builder();
        assertEquals(getDeletedOrderMessage, deleteOrderResponse.body().as(ResponseWithMessage.class));
    }

    @ParameterizedTest(name = "Проверка невозможности создания заказа при не валидном значении аттрибутов")
    @MethodSource("inValidValueExample")
    @Tag("Negative")
    void invalidValueTest(Order newOrder) {
        ExtractableResponse<Response> createOrderResponse = PetStoreClient.createOrder(newOrder);
        assertEquals(500, createOrderResponse.statusCode());
    }

    private static Stream<Object> inValidValueExample() {
        return Stream.of(
                Arguments.of(getOrderBody()
                        .withPetId(randomAlphabetic(1))
                        .builder()),
                Arguments.of(getOrderBody()
                        .withId(randomAlphabetic(1))
                        .builder()),
                Arguments.of(getOrderBody()
                        .withQuantity(randomAlphabetic(1))
                        .builder()),
                Arguments.of(getOrderBody()
                        .withComplete(randomAlphabetic(1))
                        .builder()),
                Arguments.of(getOrderBody()
                        .withShipDate(randomAlphabetic(1))
                        .builder())
        );
    }
}