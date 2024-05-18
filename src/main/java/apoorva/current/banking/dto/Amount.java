package apoorva.current.banking.dto;

import java.math.BigDecimal;

import apoorva.current.banking.entity.Transaction;


public class Amount {
    private BigDecimal amount;
    private String currency;
    private Transaction.DebitCredit debitOrCredit; 

    public Amount(BigDecimal amount, String currency, Transaction.DebitCredit debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Transaction.DebitCredit getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(Transaction.DebitCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }
}
