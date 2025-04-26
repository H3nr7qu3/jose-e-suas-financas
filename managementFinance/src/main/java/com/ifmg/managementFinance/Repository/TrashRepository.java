package com.ifmg.managementFinance.Repository;

import com.ifmg.managementFinance.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDeletedTransactionsTrue();
}
