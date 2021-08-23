package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {

    Balance getBalance(int id);

    Balance setBalance(Balance balance, int id);

    void setSenderBalance(int acctId, Transfer transfer);

    void setReceiverBalance(int acctId, Transfer transfer);

    BigDecimal getBalanceForAccount(int acctId);

    int getAccIdFromUserId(int userId);


}
