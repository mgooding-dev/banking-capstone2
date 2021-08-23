package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferJdbcDao implements TransferDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao accountDao;

    public TransferJdbcDao(DataSource dataSource){

        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public Transfer getTransfer(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, transfers.account_from, transfers.account_to, transfers.amount " +
                "FROM transfers " +
                "WHERE transfers.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if(results.next()) {

            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTypeId(),
                transfer.getStatus(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        return getTransfer(newId);
    }

    @Override
    public List<Transfer> findAllTransfers(int id) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "select * from transfers " +
                "join accounts on accounts.account_id = transfers.account_from or accounts.account_id = transfers.account_to " +
                "where accounts.user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        while(result.next()){
            Transfer transfer = mapRowToTransfer(result);
            transferList.add(transfer);
        }
        return transferList;
    }


    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTypeId(results.getInt("transfer_type_id"));
        transfer.setStatus(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));

        return transfer;
    }

}
