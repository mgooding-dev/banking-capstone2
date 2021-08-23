package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TenmoService {


    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public TenmoService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public Balance getBalance(String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<Balance> entity = new HttpEntity<>(httpHeaders);

        Balance balance = restTemplate.exchange(baseUrl + "/balance", HttpMethod.GET,
                entity, Balance.class).getBody();
        return balance;
    }

    public User[] getUsers(String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<User> entity = new HttpEntity<>(httpHeaders);

        User[] users = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, entity, User[].class).getBody();

        return users;
    }

    public Transfer transfer(String token, Transfer transfer){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        transfer = restTemplate.exchange(baseUrl + "/transfer", HttpMethod.POST, entity, Transfer.class).getBody();
        if(transfer != null) {
            return transfer;
        }
        else {
            return null;
        }
    }
   /* public Balance setBalance(String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<Balance> entity = new HttpEntity<>(httpHeaders);


        Balance balance = restTemplate.exchange(baseUrl + )
    }*/

    public int getAccId(String token, int id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<User> entity = new HttpEntity<>(httpHeaders);
        int acctId = restTemplate.exchange(baseUrl + "/accounts/" + id , HttpMethod.GET, entity, Integer.class).getBody();
        return acctId;

    }
    public Transfer[] listAllTransfers(String token, int id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(httpHeaders);
        Transfer[] transferList = restTemplate.exchange(baseUrl + "/allTransfers/" + id, HttpMethod.GET, entity, Transfer[].class).getBody();
        return transferList;
    }
    public Transfer singleTransfer(String token, int id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(httpHeaders);
        Transfer transfer = restTemplate.exchange(baseUrl + "/singleTransfer/" + id, HttpMethod.GET, entity, Transfer.class).getBody();
        return transfer;
    }
}
