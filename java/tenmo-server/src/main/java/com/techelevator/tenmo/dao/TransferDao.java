package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getTransfer(int id);
    Transfer createTransfer(Transfer transfer);
    List<Transfer> findAllTransfers(int id);



}
