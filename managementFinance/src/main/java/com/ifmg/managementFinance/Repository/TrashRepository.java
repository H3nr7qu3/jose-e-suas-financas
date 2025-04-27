package com.ifmg.managementFinance.Repository;

import com.ifmg.managementFinance.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositório focado em transações que estão "deletadas" (ou seja, na lixeira)
// Herda de JpaRepository, que já fornece os métodos básicos (CRUD)
public interface TrashRepository extends JpaRepository<Transaction, Long> {

    // Busca todas as transações com o campo deletedTransactions = true
    List<Transaction> findByDeletedTransactionsTrue();
}
