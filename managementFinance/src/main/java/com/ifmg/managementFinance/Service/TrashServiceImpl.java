package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;

import com.ifmg.managementFinance.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrashServiceImpl implements TrashService {
    @Autowired
    TrashRepository trashRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAllByDeletedTransactionsFalse();
    }

    @Override
    public List<Transaction> findAllDeleted() {
        return trashRepository.findByDeletedTransactionsTrue();
    }

    @Override
    public void restore(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
        transaction.setDeletedTransactions(false); //pq???
        transactionRepository.save(transaction);
    }

    @Override
    public void deletePermanently(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
        transactionRepository.delete(transaction); // aqui deleta real do banco
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada: " + id));
        transaction.setDeletedTransactions(true);   // marca como deletado
        transactionRepository.save(transaction);    // persiste a mudança
    }

}
