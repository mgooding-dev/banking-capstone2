package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Balance {

    BigDecimal balance;
    private BigDecimal transferAmount;

    public Balance(BigDecimal balance) {
        this.balance = balance;
    }
    public Balance(){}

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

/*    public void transfer(Balance balanceTo, BigDecimal transferAmount){
        balance = balance.subtract(transferAmount);
        balanceTo.balance = balanceTo.balance.add(transferAmount);
    }*/
}
