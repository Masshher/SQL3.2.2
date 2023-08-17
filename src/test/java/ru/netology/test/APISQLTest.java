package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class APISQLTest {

    @AfterAll
    static void teardown() {
        cleanDatabase();
    }

    @Test
    public void validTransferMoneyFromFirstToSecond() {
        var authinfo = DataHelper.getAuthInfoWithTestData();
        APIHelper.makeQueryToLogin(authinfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authinfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var firstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var secondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        var amount = DataHelper.generateValidAmount(secondCardBalance);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getFistCardInfo().getNumber(), DataHelper.getSecondCardInfo().getNumber(), amount);
        APIHelper.generateQueryTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));
    }

    @Test
    public void validTransferMoneyFromSecondToFirst() {
        var authinfo = DataHelper.getAuthInfoWithTestData();
        APIHelper.makeQueryToLogin(authinfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authinfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var firstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var secondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        var amount = DataHelper.generateValidAmount(secondCardBalance);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getSecondCardInfo().getNumber(),
                DataHelper.getFistCardInfo().getNumber(), amount);
        APIHelper.generateQueryTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        assertAll(() -> assertEquals(secondCardBalance - amount, actualSecondCardBalance),
                () -> assertEquals(firstCardBalance + amount, actualFirstCardBalance));
    }

    @Test
    public void shouldGetErrorIfUserNotInDatabase() {
        var authinfo = DataHelper.generateRandomUser();
        APIHelper.makeQueryToLogin(authinfo,400);
    }

    @Test
    public void shouldGetErrorTransferMoreThanBalance() {
        var authinfo = DataHelper.getAuthInfoWithTestData();
        APIHelper.makeQueryToLogin(authinfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authinfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var firstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var secondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        var amount = firstCardBalance + 1000;
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getSecondCardInfo().getNumber(),
                DataHelper.getFistCardInfo().getNumber(), amount);
        APIHelper.generateQueryTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        assertAll(() -> assertEquals(secondCardBalance - amount, actualSecondCardBalance),
                () -> assertEquals(firstCardBalance + amount, actualFirstCardBalance));
    }
}
