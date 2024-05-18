package apoorva.current.banking.service;

import java.util.HashMap;

import apoorva.current.banking.entity.Account;
import apoorva.current.banking.event.AccountCreatedEvent;
import apoorva.current.banking.event.FundsLoadedEvent;
import apoorva.current.banking.event.TransactionAuthorizedEvent;
import lombok.Getter;

@Getter
public class AccountAggregator {

    private Account account;

    private HashMap<String, Account> accounts;

    public AccountAggregator(String userId) {
        accounts = new HashMap<>();
    }

    // Apply methods to handle different event types
    public void apply(AccountCreatedEvent event) {
        Account newAccount = new Account();
        newAccount.setUserId(event.getUserId());
        newAccount.setBalance(event.getInitialBalance());
        newAccount.setCurrency(event.getCurrency());
        // Store new account in the HashMap
        accounts.put(event.getUserId(), newAccount);
    }

    public void apply(FundsLoadedEvent event) {
        // Update in hashmap; fetch by event.get(userID)
        Account account = accounts.get(event.getUserId());
        if (account != null) {
            account.setBalance(account.getBalance().add(event.getAmountLoaded()));
            account.setCurrency(event.getCurrency());
        }
    }

    public void apply(TransactionAuthorizedEvent event) {
        // Update in hashmap; fetch by event.get(userID)
        Account account = accounts.get(event.getUserId());
        if (account != null) {
            account.setBalance(account.getBalance().subtract(event.getAmountAuthorized()));
            account.setCurrency(event.getCurrency());
        }
    }

    
    public Account getCurrentState(String userId) {
        return accounts.get(userId);
    }
}
