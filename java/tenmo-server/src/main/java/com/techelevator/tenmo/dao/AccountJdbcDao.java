package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.AuthenticationController;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class AccountJdbcDao implements AccountDao {

    private User currentUser;
    private Transfer transfer;
    private JdbcTemplate jdbcTemplate;
    private Balance balance;

    public AccountJdbcDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Balance getBalance(int id) {

        Balance balance = null;

        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if(result.next()){
            BigDecimal balance2 = new BigDecimal(String.valueOf(result.getBigDecimal("balance")));
            balance = new Balance(balance2);
        }

        return balance;
    }

    @Override
    public Balance setBalance(Balance balance, int id) {

            String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
            //jdbcTemplate.update(sql, Integer.valueOf(String.valueOf(balance.getBalance())), currentUser.getId());
            jdbcTemplate.update(sql, balance.getBalance(), id);
            balance = getBalance(id);

        return balance;
    }
    @Override
    public BigDecimal getBalanceForAccount(int acctId){
        BigDecimal amount = new BigDecimal(0);
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, acctId);

        if(sqlRowSet.next()){
            amount = sqlRowSet.getBigDecimal("balance");
        }
        return amount;
    }

/*    private int getUserIdFromAcct(int acctId){
        int userId = 0;
        String sql = "SELECT user_id FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, acctId);
        if(results.next()){

            userId = results.getInt("user_id");
        }
        return userId;
    }*/

    @Override
    public void setSenderBalance(int acctFrom, Transfer transfer) {
        BigDecimal senderBalance = getBalanceForAccount(acctFrom);
        BigDecimal transferAmount = transfer.getAmount();
        BigDecimal newBalance = senderBalance.subtract(transferAmount);


        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, acctFrom);

    }

    @Override
    public void setReceiverBalance(int acctId, Transfer transfer) {
        BigDecimal receiverBalance = getBalanceForAccount(acctId);
        BigDecimal transferAmount = transfer.getAmount();

        BigDecimal newBalance = receiverBalance.add(transferAmount);

        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, acctId);

    }

    @Override
    public int getAccIdFromUserId(int userId) {
        int accId = 0;
        String sql = "select account_id from accounts where user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
             accId = results.getInt("account_id");
        }
        return accId;
    }
}
