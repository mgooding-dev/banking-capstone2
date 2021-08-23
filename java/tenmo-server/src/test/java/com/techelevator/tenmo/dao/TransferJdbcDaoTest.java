package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class TransferJdbcDaoTest extends TenmoDaoTests {

    private static final Transfer TRANSFER1 = new Transfer(1, 2, 2, 111, 333, new BigDecimal("55.50"));
    private static final Transfer TRANSFER2 = new Transfer(2, 2, 2, 222, 333, new BigDecimal("25.25"));
    private static final Transfer TRANSFER3 = new Transfer(4, 2, 2, 555, 111, new BigDecimal("500"));

    private TransferJdbcDao sut;
    private Transfer testTransfer;

    @Before public void setup(){
        sut = new TransferJdbcDao(dataSource);
        testTransfer = new Transfer(55, 2, 2, 555,
                111, new BigDecimal("555.55"));
    }

    @Test
    public void getTransfer() {
        Transfer transfer = sut.getTransfer(1);
        assertTransfersMatch("Transfers differ", TRANSFER1, transfer);

        Transfer transfer2 = sut.getTransfer(2);
        assertTransfersMatch("Transfers differ", TRANSFER2, transfer2);
    }

    @Test
    public void createTransfer() {
        Transfer createdTransfer = sut.createTransfer(testTransfer);

        Assert.assertNotNull("createTransfer returned null", createdTransfer);

        int newId = createdTransfer.getTransferId();
        Assert.assertTrue("createTransfer failed to return a transfer with an id", newId > 0);

        testTransfer.setTransferId(newId);
        assertTransfersMatch("createTransfer returned transfer with wrong or partial data",
                testTransfer, createdTransfer);
    }

    @Test
    public void findAllTransfers_for_user_id() {
        List<Transfer> transfers = sut.findAllTransfers(1111);
        Assert.assertEquals("findAllTransfers returned wrong number of transfers", 2, transfers.size());
    }

    private void assertTransfersMatch(String message, Transfer expected, Transfer actual){
        Assert.assertEquals(message, expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(message, expected.getTypeId(), actual.getTypeId());
        Assert.assertEquals(message, expected.getStatus(), actual.getStatus());
        Assert.assertEquals(message, expected.getAccountFrom(), actual.getAccountFrom());
        Assert.assertEquals(message, expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(message, expected.getAmount(), actual.getAmount());
    }

}