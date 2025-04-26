package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    /* CRIAR METODO DE DELETAR
    @Override
    public void delete(Long id) {

    }
     */

    /* CRIAR METODO DE ATUALIZAR
    @Override
    public void update(Long id) {

    }
     */

    @Override
    public double getTotalExpenses(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions)
            if(transaction.getValue()<0) total += transaction.getValue();

        return total;
    }
    @Override
    public double getTotalReceived(List<Transaction> transactions) {
        double total = 0;
        for (Transaction transaction : transactions)
            if(transaction.getValue()>0) total += transaction.getValue();

        return total;
    }


}
