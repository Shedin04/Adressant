package com.shedin.adressant;

import com.shedin.adressant.sender.EmailRating;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yaml")
public class EmailRatingTest {

    private final static String PATH = "http://localhost:8080/api/v1/sender";
    private final static String PATH_RATING = PATH + "/rating";
    private final static String EMAIL_PARAM = "email";
    private final static String REQUEST_ID_HEADER = "X-Request-ID";
    private String email;

    @Value("${time.to.block.userid}")
    private long timeToBlock;

    @BeforeAll
    static void logging() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeEach
    public void setup() {
        email = RandomStringUtils.randomAlphanumeric(10) + "_test@gmail.com";
        given().param(EMAIL_PARAM, email)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK);
    }

    @DisplayName("Flow when a new userId is blocked and then unblocked")
    @Test
    void newUseridFlowTest() {
        UUID uuid = UUID.randomUUID();
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);

        try {
            TimeUnit.MINUTES.sleep(timeToBlock);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @DisplayName("Flow when an existing userId is already blocked and then unblocked")
    @Test
    @Order(1)
    void existingBlockUseridFlowTest() {
        String uuid = "11111112-3d0d-6b9b-64d6-dc40e0125bd2";
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
        try {
            TimeUnit.MINUTES.sleep(timeToBlock);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @DisplayName("Flow when an existing userId is not blocked, and then block and unblocked")
    @Test
    void existingUnblockUseridFlowTest() {
        String uuid = "11111111-3d0d-6b9b-64d6-dc40e0125bd1";
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
        try {
            TimeUnit.MINUTES.sleep(timeToBlock);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, uuid)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @DisplayName("Case when userId/email is invalid")
    @Test
    void sendRatingWhenInfoIsInvalidTest() {
        String email = "email@mail.com";
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, "123")
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON).header(REQUEST_ID_HEADER, UUID.randomUUID())
                .body(EmailRating.builder().email(email).domainRating(5).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
