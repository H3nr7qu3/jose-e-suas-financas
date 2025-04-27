package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indica que essa classe é um serviço gerenciado pelo Spring
public class TrashServiceImpl implements TrashService {

    @Autowired
    TrashRepository trashRepository; // Repositório de transações na lixeira

    @Autowired
    TransactionRepository transactionRepository; // Repositório de transações ativas

    @Override
    public List<Transaction> findAllDeleted() {
        // Retorna todas as transações deletadas, ou seja, as transações na lixeira
        return trashRepository.findByDeletedTransactionsTrue();
    }

    @Override
    public void restore(Long id) {
        // Restaura uma transação deletada, movendo de volta para as transações ativas
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
        transaction.setDeletedTransactions(false); // Marca como não deletada (restaurada)
        transactionRepository.save(transaction); // Persiste a mudança no banco de dados
    }

    @Override
    public void deletePermanently(Long id) {
        // Deleta permanentemente uma transação da lixeira, removendo do banco de dados
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));
        transactionRepository.delete(transaction); // Remove a transação do banco de dados
    }

    @Override
    public void delete(Long id) {
        // Marca uma transação como deletada
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada: " + id));
        transaction.setDeletedTransactions(true);   // Marca a transação como deletada
        transactionRepository.save(transaction);    // Persiste a mudança no banco de dados
    }

}
