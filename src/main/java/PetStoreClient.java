import dto.Order;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class PetStoreClient {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://petstore.swagger.io/v2/store")
            .setContentType(ContentType.JSON)
            .log(LogDetail.METHOD)
            .log(LogDetail.URI)
            .log(LogDetail.BODY)
            .build();

    private static final ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(LogDetail.BODY)
            .build();

    public static ExtractableResponse<Response> getInventory() {
        return given()
                .spec(requestSpec)
                .when()
                .get("/inventory")
                .then()
                .spec(responseSpec)
                .extract();
    }

    public static ExtractableResponse<Response> createOrder(Order requestBody) {
        return given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post("/order")
                .then()
                .spec(responseSpec)
                .extract();
    }

    public static ExtractableResponse<Response> getOrder(Object orderId) {
        return given()
                .spec(requestSpec)
                .when()
                .get("/order/" + orderId)
                .then()
                .spec(responseSpec)
                .extract();
    }

    public static ExtractableResponse<Response> deleteOrder(Object orderId) {
        return given()
                .spec(requestSpec)
                .when()
                .delete("/order/" + orderId)
                .then()
                .spec(responseSpec)
                .extract();
    }
}