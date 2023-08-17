package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APISQLTest {

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
        var amount = DataHelper.generateValidAmount(firstCardBalance);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getFistCardInfo().getNumber(), DataHelper.getSecondCardInfo().getNumber(), amount);
        APIHelper.generateQueryTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFistCardInfo().getId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getId());
        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));


    }
}
