package apiReqresTest;

import io.qameta.allure.Flaky;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));

        Assert.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void successRegisterTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        SuccessRegistration successRegistration = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessRegistration.class);

        Assert.assertNotNull(successRegistration.getId());
        Assert.assertNotNull(successRegistration.getToken());

        Assert.assertEquals(id, successRegistration.getId());
        Assert.assertEquals(token, successRegistration.getToken());
    }

    @Test
    public void unSuccessUserRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());
        Registration user = new Registration("sydney@fife", "");
        UnSuccessRegistration unSuccessRegistration = given()
                .body(user)
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessRegistration.class);

        Assert.assertEquals("Missing password", unSuccessRegistration.getError());
    }

    @Test
    public void sortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<ColorsData> colors = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(sortedYears, years);
    }

    @Test
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecificationUnique(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    @Flaky
    @Test()
    public void timeTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        UserTime user = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);
        String regEx = "(.{5})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regEx, "");
        Assert.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regEx, ""));
    }
}
