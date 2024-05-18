package apoorva.current.banking.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionAuthorizedEvent {

    private final String userId;
    private final String messageId;
    private final BigDecimal amountAuthorized;
    private final String currency;
    private final BigDecimal newBalance;
    private final LocalDateTime timestamp;

    public TransactionAuthorizedEvent(String userId, String messageId, BigDecimal amountAuthorized, String currency, BigDecimal newBalance) {
        this.userId = userId;
        this.messageId = messageId;
        this.amountAuthorized = amountAuthorized;
        this.currency = currency;
        this.newBalance = newBalance;
        this.timestamp = LocalDateTime.now(); // Set the current timestamp
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public BigDecimal getAmountAuthorized() {
        return amountAuthorized;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TransactionAuthorizedEvent{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", amountAuthorized=" + amountAuthorized +
                ", currency='" + currency + '\'' +
                ", newBalance=" + newBalance +
                ", timestamp=" + timestamp +
                '}';
    }

}
