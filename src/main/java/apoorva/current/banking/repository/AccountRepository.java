package apoorva.current.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apoorva.current.banking.entity.Account;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long>{

    // Method to find an account by userId
    Optional<Account> findByUserId(String userId);

}
