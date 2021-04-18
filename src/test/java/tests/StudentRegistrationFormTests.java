package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class StudentRegistrationFormTests extends TestBase {
    Faker faker = new Faker();

    String firstName = faker.name().firstName(),
            lastName = faker.name().lastName(),
            email = faker.internet().emailAddress(),
            mobile = faker.phoneNumber().subscriberNumber(10),
            currentAddress = faker.address().fullAddress();

    @Test
    @DisplayName("Testing student can register")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Max Demenko")
    void studentCanRegister() {
        step("Open registration form", () -> {
            open("https://demoqa.com/automation-practice-form");
            $(".practice-form-wrapper").shouldHave(text("Student Registration Form"));
                });
        step("Fill students registration form", () -> {
            step("Enter general information", () -> {
                $("#firstName").setValue(firstName);
                $("#lastName").setValue(lastName);
                $("#userEmail").setValue(email);
                $(byText("Male")).click();
                $("#userNumber").setValue(mobile);
            });
            step("Enter birthday date", () -> {
                $("#dateOfBirthInput").click();
                $(".react-datepicker__month-select").selectOption("June");
                $(".react-datepicker__year-select").selectOptionByValue("2000");
                $$(".react-datepicker__day--001").first().click();
            });
            step("Select subjects", () -> {
                $("#subjectsContainer").click();
                $("#subjectsInput").setValue("Maths").pressEnter();
                $("#subjectsInput").setValue("English").pressEnter();
                    });
            step("Select hobbies", () -> {
                $(byText("Sports")).click();
                $(byText("Music")).click();
            });
            step("Select picture", () -> {
                $("#uploadPicture").uploadFromClasspath("image.jpg");
            });
            step("Enter address", () -> {
                $("#currentAddress").setValue(currentAddress);
                $("#state").click();
                $("#react-select-3-input").setValue("NCR").pressEnter();
                $("#city").click();
                $("#react-select-4-input").setValue("Delhi").pressEnter();
            });
        });
        step("Submit form", () -> $("#submit").click());

        step("Verify form submit", () -> {
            $(".modal-header").shouldHave(text("Thanks for submitting the form"));
            $(byText(firstName + " " + lastName)).shouldBe(visible);
            $(byText("Male")).shouldBe(visible);
            $(".modal-content").shouldHave(text(email),
                    text(mobile), text("01 June,2000"), text("Maths, English"),
                    text("Sports, Music"), text(currentAddress), text("NCR Delhi"), text("image.jpg"));
        });
    }
}
