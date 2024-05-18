package apoorva.current.banking.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FundsLoadedEvent {

    private final String userId;
    private final String messageId;
    private final BigDecimal amountLoaded;
    private final String currency;
    private final BigDecimal newBalance;
    private final LocalDateTime timestamp;

    public FundsLoadedEvent(String userId, String messageId, BigDecimal amountLoaded, String currency, BigDecimal newBalance) {
        this.userId = userId;
        this.messageId = messageId;
        this.amountLoaded = amountLoaded;
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

    public BigDecimal getAmountLoaded() {
        return amountLoaded;
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
        return "FundsLoadedEvent{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", amountLoaded=" + amountLoaded +
                ", currency='" + currency + '\'' +
                ", newBalance=" + newBalance +
                ", timestamp=" + timestamp +
                '}';
    }

}
