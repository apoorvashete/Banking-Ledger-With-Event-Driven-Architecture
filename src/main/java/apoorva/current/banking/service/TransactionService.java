package apoorva.current.banking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apoorva.current.banking.dto.Amount;
import apoorva.current.banking.dto.AuthorizationRequest;
import apoorva.current.banking.dto.AuthorizationResponse;
import apoorva.current.banking.dto.LoadRequest;
import apoorva.current.banking.dto.LoadResponse;
import apoorva.current.banking.entity.Account;
import apoorva.current.banking.entity.Transaction;
import apoorva.current.banking.event.AccountCreatedEvent;
import apoorva.current.banking.event.FundsLoadedEvent;
import apoorva.current.banking.event.TransactionAuthorizedEvent;
import apoorva.current.banking.event.TransactionDeclinedEvent;
import apoorva.current.banking.store.InMemoryEventStore;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TransactionService {

    private final InMemoryEventStore eventStore;
    private EventReplayer eventReplayer;

    @Autowired
    public TransactionService(EventReplayer eventReplayer, InMemoryEventStore eventStore) {
        this.eventReplayer = eventReplayer;
        this.eventStore = eventStore;
    }
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    //Load service

    @Transactional
    public LoadResponse loadFunds(LoadRequest request) {
        // Load the latest state of the account
        Account latestAccount = eventReplayer.loadLatestState(request.getUserId());

        // Create and publish the AccountCreatedEvent if account not present
        if (latestAccount == null) {
            AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent(
                request.getUserId(), 
                BigDecimal.ZERO, 
                request.getTransactionAmount().getCurrency()
                );

                publishEvent(accountCreatedEvent);

            // Initialize account state after creation
            latestAccount = eventReplayer.loadLatestState(request.getUserId());

        }

        // Create and publish the FundsLoadedEvent
        FundsLoadedEvent fundsLoadedEvent = new FundsLoadedEvent(
            request.getUserId(),
            request.getMessageId(),
            request.getTransactionAmount().getAmount(),
            request.getTransactionAmount().getCurrency(),
            BigDecimal.ZERO // New balance will be calculated via replay
        );
        publishEvent(fundsLoadedEvent);

        // Reload the latest state to reflect the new balance
        latestAccount = eventReplayer.loadLatestState(request.getUserId());

        // New balance
        Amount newBalance = new Amount(latestAccount.getBalance(), latestAccount.getCurrency(), Transaction.DebitCredit.CREDIT);
        LoadResponse response = new LoadResponse();
        response.setUserId(request.getUserId());
        response.setMessageId(request.getMessageId());
        response.setBalance(newBalance);
        return response;
    }

    private void publishEvent(Object event) {
        eventStore.addEvent(event);
        logger.info("Event published and stored: " + event);
    }

    public List<Object> getAllEvents() {
        return eventStore.getEvents();
    }

    //Authorise service
    
    @Transactional
    public AuthorizationResponse authorize(AuthorizationRequest request) {

        // Retrieve the latest state from the event store
        Account latestAccount = eventReplayer.loadLatestState(request.getUserId());

        AuthorizationResponse response = new AuthorizationResponse();
        response.setUserId(request.getUserId());
        response.setMessageId(request.getMessageId());

        BigDecimal requestedAmount = request.getTransactionAmount().getAmount();
        BigDecimal originalBalance = latestAccount == null ? BigDecimal.ZERO : latestAccount.getBalance();

        // Check if the account exists and has sufficient funds
        if (latestAccount == null || originalBalance.compareTo(requestedAmount) < 0 || requestedAmount.compareTo(BigDecimal.ZERO) <= 0 ) {
            // Insufficient funds, decline transaction
            String reason = latestAccount == null ? "Account not found" : "Insufficient funds";
            TransactionDeclinedEvent declinedEvent = new TransactionDeclinedEvent(
                request.getUserId(),
                request.getMessageId(),
                requestedAmount,
                request.getTransactionAmount().getCurrency(),
                reason,
                originalBalance
            );
            publishEvent(declinedEvent);
            response.setResponseCode("DECLINED");
            response.setBalance(new Amount(originalBalance, request.getTransactionAmount().getCurrency(), Transaction.DebitCredit.DEBIT));
        } else {
            // Sufficient funds, approve transaction
            BigDecimal newBalance = originalBalance.subtract(requestedAmount);

            TransactionAuthorizedEvent authorizedEvent = new TransactionAuthorizedEvent(
                request.getUserId(),
                request.getMessageId(),
                requestedAmount,
                request.getTransactionAmount().getCurrency(),
                newBalance
            );
            publishEvent(authorizedEvent);
            response.setResponseCode("APPROVED");
            response.setBalance(new Amount(newBalance, request.getTransactionAmount().getCurrency(), Transaction.DebitCredit.DEBIT));
        }
        return response;
    }
}
