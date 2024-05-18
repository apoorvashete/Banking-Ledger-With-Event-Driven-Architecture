package apoorva.current.banking.service;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
// import apoorva.current.banking.service.EventReplayer;
// import apoorva.current.banking.service.TransactionService;
import apoorva.current.banking.store.InMemoryEventStore;


class TransactionServiceTest {

    @Mock
    private EventReplayer eventReplayer;

    @Mock
    private InMemoryEventStore eventStore;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(eventReplayer, eventStore);
    }

    @Test
    public void testLoadFundsNewAccount(){

        // Setup
        LoadRequest request = new LoadRequest();
        request.setUserId("user12");
        request.setMessageId("msg001");
        Amount amount = new Amount(new BigDecimal("50.00"), "USD", Transaction.DebitCredit.CREDIT);
        request.setTransactionAmount(amount);

        // Then return a newly created Account
        Account newAccount = new Account();
        newAccount.setUserId("user12");
        newAccount.setBalance(new BigDecimal("50.00"));
        newAccount.setCurrency("USD");
        when(eventReplayer.loadLatestState(request.getUserId())).thenReturn(null).thenReturn(newAccount);

        // Execute
        LoadResponse response = transactionService.loadFunds(request);

        // Verify
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventStore, times(2)).addEvent(eventCaptor.capture());

        assertTrue(eventCaptor.getAllValues().get(0) instanceof AccountCreatedEvent);
        assertTrue(eventCaptor.getAllValues().get(1) instanceof FundsLoadedEvent);

        // The expected balance after loading 50.00 into the new account
        assertEquals(new BigDecimal("50.00"), response.getBalance().getAmount());
    }
    
    @Test
    public void testAuthorizeApproved() {

        // Setup
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId("user12");
        request.setMessageId("msg002");
        Amount amount = new Amount(new BigDecimal("50.00"), "USD", Transaction.DebitCredit.DEBIT);
        request.setTransactionAmount(amount);

        // Mocking an existing account with sufficient funds
        Account existingAccount = new Account();
        existingAccount.setUserId("user12");
        existingAccount.setBalance(new BigDecimal("100.00")); // Sufficient funds
        existingAccount.setCurrency("USD");

        when(eventReplayer.loadLatestState(request.getUserId())).thenReturn(existingAccount);

        // Execute
        AuthorizationResponse response = transactionService.authorize(request);

        // Verify
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventStore, times(1)).addEvent(eventCaptor.capture());

        // Check if TransactionAuthorizedEvent was published
        assertTrue(eventCaptor.getValue() instanceof TransactionAuthorizedEvent);

        // Validate the response
        assertEquals("APPROVED", response.getResponseCode());
        assertEquals(new BigDecimal("50.00"), response.getBalance().getAmount());

    }

    @Test
    public void testAuthorizeDeclined() {

        // Setup
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId("user12");
        request.setMessageId("msg003");
        Amount amount = new Amount(new BigDecimal("150.00"), "USD", Transaction.DebitCredit.DEBIT);
        request.setTransactionAmount(amount);

        // Mocking an existing account with insufficient funds
        Account existingAccount = new Account();
        existingAccount.setUserId("user12");
        existingAccount.setBalance(new BigDecimal("50.00")); // Insufficient funds
        existingAccount.setCurrency("USD");

        when(eventReplayer.loadLatestState(request.getUserId())).thenReturn(existingAccount);

        // Execute
        AuthorizationResponse response = transactionService.authorize(request);

        // Verify
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventStore, times(1)).addEvent(eventCaptor.capture());

        // Check if TransactionDeclinedEvent was published
        assertTrue(eventCaptor.getValue() instanceof TransactionDeclinedEvent);

        // Validate the response
        assertEquals("DECLINED", response.getResponseCode());
        assertEquals(new BigDecimal("50.00"), response.getBalance().getAmount());
    }

    //Zero Balance aurothrization 

    @Test
    public void testAuthorizeZeroBalance() {
        // Setup
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId("user12");
        request.setMessageId("msg004");
        Amount amount = new Amount(new BigDecimal("10.00"), "USD", Transaction.DebitCredit.DEBIT);
        request.setTransactionAmount(amount);

        // Mocking an existing account with zero balance
        Account existingAccount = new Account();
        existingAccount.setUserId("user12");
        existingAccount.setBalance(BigDecimal.ZERO);
        existingAccount.setCurrency("USD");

        when(eventReplayer.loadLatestState(request.getUserId())).thenReturn(existingAccount);

        // Execute
        AuthorizationResponse response = transactionService.authorize(request);

        // Verify
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventStore, times(1)).addEvent(eventCaptor.capture());

        // Check if TransactionDeclinedEvent was published
        assertTrue(eventCaptor.getValue() instanceof TransactionDeclinedEvent);

        // Validate the response
        assertEquals("DECLINED", response.getResponseCode());
        assertEquals(BigDecimal.ZERO, response.getBalance().getAmount());
    }

    // Negative debit authorization

    @Test
    public void testAuthorizeNegativeBalance() {
        // Setup
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserId("user12");
        request.setMessageId("msg005");
        Amount amount = new Amount(new BigDecimal("-20.00"), "USD", Transaction.DebitCredit.DEBIT);
        request.setTransactionAmount(amount);

        // Mocking an existing account with a negative balance
        Account existingAccount = new Account();
        existingAccount.setUserId("user12");
        existingAccount.setBalance(new BigDecimal("100.00")); // Negative balance
        existingAccount.setCurrency("USD");

        when(eventReplayer.loadLatestState(request.getUserId())).thenReturn(existingAccount);

        // Execute
        AuthorizationResponse response = transactionService.authorize(request);

        // Verify
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventStore, times(1)).addEvent(eventCaptor.capture());

        // Check if TransactionDeclinedEvent was published
        assertTrue(eventCaptor.getValue() instanceof TransactionDeclinedEvent);

        // Validate the response
        assertEquals("DECLINED", response.getResponseCode());
        assertEquals(new BigDecimal("100.00"), response.getBalance().getAmount());
    }


}
