package com.shedin.adressant;

import com.shedin.adressant.sender.EmailRating;
import com.shedin.adressant.sender.SenderResponse;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
@SpringBootTest
class SenderTest {

    private final static String PATH = "http://localhost:8080/api/v1/sender";
    private final static String PATH_RATING = PATH + "/rating";
    private final static String EMAIL = "email";
    private final static String REQUEST_ID_HEADER = "X-Request-ID";

    @BeforeAll
    static void logging() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    void emptyEmailTest() {
        given().param(EMAIL, StringUtils.EMPTY)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void nullEmailTest() {
        given().param(EMAIL, (Object) null).when().get(PATH).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void noEmailParameterTest() {
        given().when().get(PATH).then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("Valid good email check")
    @RepeatedTest(value = 2, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void validGoodEmailTest() {
        String testEmail = "test@gmail.com";
        SenderResponse response = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.getDomain()).as("Email").isEqualTo(testEmail);
        soft.assertThat(response.getDomainId()).as("Email id size").hasSize(36);
        soft.assertThat(response.getRate()).as("Rate").isEqualTo(0.0);
        soft.assertThat(response.isTemporary()).as("Is temporary").isFalse();
        soft.assertAll();
    }

    @DisplayName("Valid temporary email check")
    @RepeatedTest(value = 2, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void validTemporaryEmailTest() {
        String testEmail = "test@mailinator.com";
        SenderResponse response = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.getDomain()).as("Email").isEqualTo(testEmail);
        soft.assertThat(response.getDomainId()).as("Email id size").hasSize(36);
        soft.assertThat(response.getRate()).as("Rate").isEqualTo(0.0);
        soft.assertThat(response.isTemporary()).as("Is temporary").isTrue();
        soft.assertAll();
    }

    @DisplayName("Update temporary and free email status")
    @RepeatedTest(value = 2, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void updateTemporaryAndFreeEmailStatusTest() {
        String testEmail = "testtemporarymail@mailinator.com";
        SenderResponse response = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.getDomain()).as("Email").isEqualTo(testEmail);
        soft.assertThat(response.getDomainId()).as("Email id size").hasSize(36);
        soft.assertThat(response.getRate()).as("Rate").isEqualTo(0.0);
        soft.assertThat(response.isTemporary()).as("Is temporary").isTrue();
        soft.assertThat(response.isFree()).as("Is temporary").isTrue();
        soft.assertAll();
    }

    @DisplayName("Update free email status")
    @RepeatedTest(value = 2, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void updateFreeEmailStatusTest() {
        String testEmail = "2second@gmail.com";
        SenderResponse response = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.getDomain()).as("Email").isEqualTo(testEmail);
        soft.assertThat(response.getDomainId()).as("Email id size").hasSize(36);
        soft.assertThat(response.getRate()).as("Rate").isEqualTo(0.0);
        soft.assertThat(response.isTemporary()).as("Is temporary").isFalse();
        soft.assertThat(response.isFree()).as("Is free").isTrue();
        soft.assertAll();
    }

    @DisplayName("Update temporary email status")
    @RepeatedTest(value = 2, name = "{displayName} - repetition {currentRepetition} of {totalRepetitions}")
    void updateTemporaryEmailStatusTest() {
        String testEmail = "third@maillei.net";
        SenderResponse response = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.getDomain()).as("Email").isEqualTo(testEmail);
        soft.assertThat(response.getDomainId()).as("Email id size").hasSize(36);
        soft.assertThat(response.getRate()).as("Rate").isEqualTo(0.0);
        soft.assertThat(response.isTemporary()).as("Is temporary").isTrue();
        soft.assertThat(response.isFree()).as("Is free").isFalse();
        soft.assertAll();
    }

    @DisplayName("Rate logic checking")
    @Test
    void checkRateConstraintsLogic() {
        String notRegisteredEmail = "noemail@mail.com";
        given().contentType(ContentType.JSON)
                .header(REQUEST_ID_HEADER, UUID.randomUUID())
                .body(EmailRating.builder().email(notRegisteredEmail).domainRating(1).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
        String registeredEmail = "testtemporarymail@mailinator.com";
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(registeredEmail).domainRating(-1).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(registeredEmail).domainRating(0).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(registeredEmail).domainRating(10.1).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(registeredEmail).domainRating(9.25).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(registeredEmail).domainRating(00.25).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email("").domainRating(9.2).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(null).domainRating(9.2).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("Update rate user flow")
    @Test
    void updateRateUserFlow() {
        String testEmail = RandomStringUtils.randomAlphanumeric(10) + "_test@gmail.com";
        SenderResponse initialSenderResponse = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(initialSenderResponse.getRate()).as("Initial rate").isEqualTo(0.0);

        double firstRateToUpdate = 0.1;
        EmailRating firstEmailRating = given().contentType(ContentType.JSON)
                .header(REQUEST_ID_HEADER, UUID.randomUUID())
                .body(EmailRating.builder().email(testEmail).domainRating(firstRateToUpdate).build())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        assertions.assertThat(firstEmailRating).as("First rate update").isEqualTo(EmailRating.builder().email(testEmail).domainRating(firstRateToUpdate).build());

        double secondRateToUpdate = 10.0;
        EmailRating secondEmailRating = given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(testEmail).domainRating(secondRateToUpdate).build())
                .header(REQUEST_ID_HEADER, UUID.randomUUID())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        assertions.assertThat(secondEmailRating).as("Second rate update").isEqualTo(EmailRating.builder().email(testEmail).domainRating(secondRateToUpdate).build());

        double thirdRateToUpdate = 4.6;
        EmailRating thirdEmailRating = given().contentType(ContentType.JSON)
                .body(EmailRating.builder().email(testEmail).domainRating(thirdRateToUpdate).build())
                .header(REQUEST_ID_HEADER, UUID.randomUUID())
                .when().put(PATH_RATING)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(EmailRating.class);
        assertions.assertThat(thirdEmailRating).as("Third rate update").isEqualTo(EmailRating.builder().email(testEmail).domainRating(thirdRateToUpdate).build());

        double expectedRating = (firstRateToUpdate + secondRateToUpdate + thirdRateToUpdate) / 3;
        SenderResponse finalSenderResponse = given().param(EMAIL, testEmail)
                .when().get(PATH)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().as(SenderResponse.class);
        assertions.assertThat(finalSenderResponse.getRate()).as("Final rate").isEqualTo(expectedRating);

    }
}
