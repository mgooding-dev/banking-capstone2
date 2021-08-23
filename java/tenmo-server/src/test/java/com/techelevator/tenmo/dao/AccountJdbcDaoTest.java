package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountJdbcDaoTest extends TenmoDaoTests{

    private static final Balance BALANCE1 = new Balance(new BigDecimal("1000.00"));
    private static final Transfer TRANSFER1 = new Transfer(1, 2, 2, 111, 333, new BigDecimal("55.50"));
    private static final Transfer TRANSFER2 = new Transfer(2, 2, 2, 222, 333, new BigDecimal("25.25"));

    private AccountJdbcDao sut;
    private Balance testBalance;

    @Before
    public void setup() {
        sut = new AccountJdbcDao(dataSource);
        testBalance = new Balance(new BigDecimal("1500.00"));
    }

    @Test
    public void getBalance() {

        Assert.assertEquals("Balances differ",sut.getBalance(1111).getBalance(), BALANCE1.getBalance());
    }

    @Test
    public void setBalance() {
        Balance balance = sut.getBalance(3333);
        balance.setBalance(new BigDecimal("1500.00"));

        sut.setBalance(balance, 3333);
        Balance newBalance = sut.getBalance(3333);

        assertBalancesMatch("Balances do not match.", newBalance, balance);


    }
    @Test
    public void getBalanceForAccount() {
        Assert.assertEquals("Balances do not match", BALANCE1.getBalance(), sut.getBalanceForAccount(111));
    }

    @Test
    public void setSenderBalance() {
        Balance balance = sut.getBalance(1111);
        balance.setBalance(new BigDecimal("944.50"));

        sut.setSenderBalance(111, TRANSFER1);
        Balance newBalance = sut.getBalance(1111);

        assertBalancesMatch("Balances do not match", balance, newBalance);

    }

    @Test
    public void setReceiverBalance() {
        Balance balance = sut.getBalance(2222);
        balance.setBalance(new BigDecimal("2025.25"));

        sut.setReceiverBalance(222, TRANSFER2);
        Balance newBalance = sut.getBalance(2222);

        assertBalancesMatch("Balances do not match", balance, newBalance);
    }

    @Test
    public void getAccIdFromUserId() {
        int actualId = sut.getAccIdFromUserId(1111);
        int expectedId = 111;
        Assert.assertEquals("Account numbers do not match", actualId, expectedId);
    }

    private void assertBalancesMatch(String message, Balance expected, Balance actual){
        Assert.assertEquals(message, expected.getBalance(), actual.getBalance());
    }

}