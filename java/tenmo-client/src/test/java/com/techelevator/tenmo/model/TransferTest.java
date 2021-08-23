package com.techelevator.tenmo.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransferTest {

    @Test
    public void transferTest_default_constructor(){
        Transfer transfer = new Transfer();
        transfer.setTransferId(1);
        transfer.setTypeId(2);
        transfer.setStatus(2);
        transfer.setAccountFrom(10);
        transfer.setAccountTo(11);
        transfer.setAmount(new BigDecimal("100"));

        Assert.assertEquals(transfer.getTransferId(), 1);
        Assert.assertEquals(transfer.getTypeId(), 2);
        Assert.assertEquals(transfer.getStatus(), 2);
        Assert.assertEquals(transfer.getAccountFrom(), 10);
        Assert.assertEquals(transfer.getAccountTo(), 11);
        Assert.assertEquals(transfer.getAmount(), new BigDecimal("100"));
    }

}