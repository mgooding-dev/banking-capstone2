package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class TenmoController {

    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;


    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        return accountDao.getBalance(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path="/balance/{id}", method = RequestMethod.PUT)
    public void setBalance(@RequestBody Balance balance,
                            @PathVariable int id)
        {

        accountDao.setBalance(balance, id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> allUsers() {
        return userDao.findAll();
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer){
//        if(transfer.getAmount().compareTo(accountDao.getBalanceForAccount(transfer.getAccountFrom())) >=0){
            Transfer xfer = transferDao.createTransfer(transfer);
            updateBalance(transfer);
            return xfer;
//        }
//        else {
//            return null; I'm at a loss
//        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public int getAccId(@PathVariable int id){
        return accountDao.getAccIdFromUserId(id);
    }

    //take the transfer and separate into two separate accounts
    public void updateBalance(Transfer transfer){
        accountDao.setReceiverBalance(transfer.getAccountTo(), transfer);
        accountDao.setSenderBalance(transfer.getAccountFrom(), transfer);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/allTransfers/{id}", method = RequestMethod.GET)
    public List<Transfer> allTransfers(@PathVariable int id){
        return transferDao.findAllTransfers(id);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "/singleTransfer/{id}", method = RequestMethod.GET)
    public Transfer individualTransferById(@PathVariable int id){
        return transferDao.getTransfer(id);
    }

}

