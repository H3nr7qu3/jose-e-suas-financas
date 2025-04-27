package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Operation;
import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Repository.TransactionRepository;
import com.ifmg.managementFinance.Repository.TrashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe de implementação do serviço de transações
@Service // Indica que essa classe é um serviço, gerenciada pelo Spring
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository; // Injeção do repositório de transações

    @Override
    public List<Transaction> findAll() {
        // Retorna todas as transações não deletadas
        return transactionRepository.findAllByDeletedTransactionsFalse();
    }

    @Override
    public List<Transaction> findAllByDate(Date fromDate, Date toDate) {
        // Filtra transações com base em um intervalo de datas
        List<Transaction> transactions = new ArrayList<Transaction>();

        // Itera sobre todas as transações e verifica se a data de entrada está dentro do intervalo
        for (Transaction transaction : findAll()) {
            if (fromDate.before(transaction.getEntry_date()) && toDate.after(transaction.getEntry_date())) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    public List<Date> getCurrentMonthRange() {
        // Retorna o intervalo de datas do mês atual
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1); // Primeiro dia do mês
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth()); // Último dia do mês

        // Converte as datas para o formato Date
        Date fromData = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toData = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return List.of(fromData, toData);
    }

    @Override
    public Transaction findById(Long id) {
        // Busca uma transação pelo ID. Se não encontrar, retorna null.
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Transaction transaction) {
        // Salva uma nova transação no banco de dados
        transactionRepository.save(transaction);
    }

    @Override
    public void update(Transaction transaction, Long id) {
        // Atualiza os detalhes de uma transação existente
        Transaction currentTransaction = findById(id);
        if (currentTransaction != null) {
            currentTransaction.setName(transaction.getName());
            currentTransaction.setValue(transaction.getValue());
            currentTransaction.setType(transaction.getType());
            currentTransaction.setEntry_date(transaction.getEntry_date());
            currentTransaction.setRegister_date(new Date()); // Atualiza a data de registro
            currentTransaction.setOperation(transaction.getOperation());

            transactionRepository.save(currentTransaction); // Salva as alterações
        }
    }

    @Override
    public double getTotalReceived(List<Transaction> l) {
        // Calcula o total de transações do tipo "ENTRADA"
        return l.stream()
                .filter(t -> t.getOperation() == Operation.ENTRADA) // Filtra por operações de "ENTRADA"
                .mapToDouble(Transaction::getValue) // Extrai o valor de cada transação
                .sum(); // Soma todos os valores filtrados
    }

    @Override
    public double getTotalExpenses(List<Transaction> l) {
        // Calcula o total de transações do tipo "SAÍDA"
        return l.stream()
                .filter(t -> t.getOperation() == Operation.SAIDA) // Filtra por operações de "SAÍDA"
                .mapToDouble(Transaction::getValue) // Extrai o valor de cada transação
                .sum(); // Soma todos os valores filtrados
    }
}
