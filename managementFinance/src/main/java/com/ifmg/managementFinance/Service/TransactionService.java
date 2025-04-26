package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> findAll();

    Transaction findById(Long id);

    void save(Transaction transaction);

    // void delete(Long id);

    //void update(Long id);

    double getTotalExpenses(List<Transaction> transactions);
    double getTotalReceived(List<Transaction> transactions);
}
