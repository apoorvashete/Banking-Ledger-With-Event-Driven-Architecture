package apoorva.current.banking.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDeclinedEvent {

    private final String userId;
    private final String messageId;
    private final BigDecimal amountAttempted;
    private final String currency;
    private final String reason;
    private final BigDecimal currentBalance;
    private final LocalDateTime timestamp;

    // Constructor
    public TransactionDeclinedEvent(String userId, String messageId, BigDecimal amountAttempted, String currency, String reason, BigDecimal currentBalance) {
        this.userId = userId;
        this.messageId = messageId;
        this.amountAttempted = amountAttempted;
        this.currency = currency;
        this.reason = reason;
        this.currentBalance = currentBalance;
        this.timestamp = LocalDateTime.now(); // Record the event creation timestamp
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public BigDecimal getAmountAttempted() {
        return amountAttempted;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReason() {
        return reason;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TransactionDeclinedEvent{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", amountAttempted=" + amountAttempted +
                ", currency='" + currency + '\'' +
                ", reason='" + reason + '\'' +
                ", currentBalance=" + currentBalance +
                ", timestamp=" + timestamp +
                '}';
    }

}
