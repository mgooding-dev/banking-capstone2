package com.techelevator.tenmo.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BalanceTest {

    @Test
    public void testBalance_amount(){
        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("100"));

        Assert.assertEquals(balance.getBalance(), new BigDecimal(100));
    }


}