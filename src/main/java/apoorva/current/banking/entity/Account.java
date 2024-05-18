package apoorva.current.banking.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "accounts")
@Data  // This annotation is a shortcut for @Getter, @Setter, @ToString, @EqualsAndHashCode, and @RequiredArgsConstructor

public class Account {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically increment primary key
    private Long accountId;

    @Column(name = "user_id", unique = true, nullable = false)  // Ensure user_id cannot be null
    private String userId;  // String to accommodate UUIDs or other string identifiers
    
    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)  // Assuming currency codes are 3-letter ISO codes
    private String currency;

}
