package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;

import java.util.Date;
import java.util.List;

// Interface que define os métodos de serviço para a entidade Transaction
public interface TransactionService {

    // Retorna todas as transações não deletadas
    List<Transaction> findAll();

    // Retorna transações dentro de um intervalo de datas
    List<Transaction> findAllByDate(Date fromDate, Date toDate);

    // Busca uma transação específica pelo ID
    Transaction findById(Long id);

    // Salva uma nova transação no banco
    void save(Transaction transaction);

    // Atualiza uma transação existente
    void update(Transaction currentTransaction, Long id);

    // Calcula o total de despesas
    double getTotalExpenses(List<Transaction> transactions);

    // Calcula o total de receitas
    double getTotalReceived(List<Transaction> transactions);


}
