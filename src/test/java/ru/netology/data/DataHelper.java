package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    public static AuthInfo getAuthInfoWithTestData() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static CardInfo getFistCardInfo() {
        return new CardInfo("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0001");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0002");
    }

    public static int generateValidAmount(int balance) {
        return new Random().nextInt(balance)+1;
    }

    public static AuthInfo generateRandomUser() {
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }


    public static String generateRandomLogin() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        Faker faker = new Faker(new Locale("en"));
        return faker.internet().password();
    }


    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class CardInfo {
        String id;
        String number;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class VerificationInfo {
        String login;
        String code;
    }
}
