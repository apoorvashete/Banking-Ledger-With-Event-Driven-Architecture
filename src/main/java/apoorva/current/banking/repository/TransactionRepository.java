package apoorva.current.banking.repository;


import apoorva.current.banking.entity.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    // Inherits CRUD operations
}
