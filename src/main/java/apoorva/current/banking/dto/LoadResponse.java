package apoorva.current.banking.dto;


import lombok.Data;

@Data
public class LoadResponse {

    private String userId;
    private String messageId;
    private Amount balance;

    // Default constructor
    public LoadResponse() {}

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public Amount getBalance() {
        return balance;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

}
