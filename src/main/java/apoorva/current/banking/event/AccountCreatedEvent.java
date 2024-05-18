package apoorva.current.banking.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountCreatedEvent {

    private final String userId;
    private final BigDecimal initialBalance;
    private final String currency;
    private final LocalDateTime timestamp;


    public AccountCreatedEvent(String userId, BigDecimal initialBalance, String currency) {
        // Ensure that fields are properly initialized
        if (userId == null || initialBalance == null || currency == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }

        this.userId = userId;
        this.initialBalance = initialBalance;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "AccountCreatedEvent{" +
                "userId='" + userId + '\'' +
                ", initialBalance=" + initialBalance +
                ", currency='" + currency + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
