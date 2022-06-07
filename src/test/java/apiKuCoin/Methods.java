package apiKuCoin;

import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Methods {
    public List<TickerData> getTickers() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.kucoin.com/api/v1/market/allTickers")
                .then().log().body()
                .extract().jsonPath().getList("data.ticker", TickerData.class);
    }
}
