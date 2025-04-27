package com.ifmg.managementFinance.Repository;

import com.ifmg.managementFinance.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Interface de repositório para a entidade Transaction
// Herda de JpaRepository, que já fornece os métodos básicos (CRUD)
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Busca todas as transações que não estão marcadas como deletadas (ou seja, não estão na lixeira)
    List<Transaction> findAllByDeletedTransactionsFalse();
}
