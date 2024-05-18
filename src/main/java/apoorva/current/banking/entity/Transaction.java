package apoorva.current.banking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Table(name = "transactions")
@Data  // Lombok annotation to generate boilerplate code

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = true)
    private String userId;

    @Column(nullable = true)
    private String msgId;

    @Enumerated(EnumType.STRING)  // Use Enumerated to ensure data integrity
    @Column(nullable = false)
    private TransactionType transactionType; //LOAD or AUTHORIZATION

    @Column(nullable = true)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DebitCredit debitOrCredit; //CREDIT or DEBIT

    @Column(nullable = true)
    private String currency;

    @Column(nullable = true)
    private LocalDateTime timestamp = LocalDateTime.now();  // Default to current time

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // e.g., APPROVED, DECLINED


    // Enum for transaction types
    public enum TransactionType {
        LOAD, AUTHORIZE
    }

    // Enum for debit or credit
    public enum DebitCredit {
        CREDIT, DEBIT;
        
        public static DebitCredit fromString(String s) {
            return DebitCredit.valueOf(s.toUpperCase());
        }
    }

    // Enum for approve or decline
    public enum TransactionStatus {
        APPROVED, DECLINED
    }

    // Getter and setter for the status field
    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    //Account entity and each transaction is linked to an Account
    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    private Account account;


}


