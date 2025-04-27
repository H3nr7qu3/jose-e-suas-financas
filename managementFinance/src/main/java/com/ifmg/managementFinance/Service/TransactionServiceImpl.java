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

        // Ajusta a data de 'toDate' para incluir o final do dia
        Date nextDay = new Date(toDate.getTime() + 24 * 60 * 60 * 1000L);

        // Itera sobre todas as transações e verifica se a data de entrada está dentro do intervalo
        for (Transaction transaction : findAll()) {
            Date entryDate = transaction.getEntry_date();
            // Adiciona transação se a data de entrada estiver dentro do intervalo de datas fornecido
            if (!entryDate.before(fromDate) && !entryDate.after(nextDay)) {
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

        return List.of(fromData, toData); // Retorna a lista com as duas datas
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
            // Atualiza os campos da transação com os novos valores
            currentTransaction.setName(transaction.getName());
            currentTransaction.setValue(transaction.getValue());
            currentTransaction.setType(transaction.getType());
            currentTransaction.setEntry_date(transaction.getEntry_date());
            currentTransaction.setRegister_date(new Date()); // Atualiza a data de registro
            currentTransaction.setOperation(transaction.getOperation());

            // Salva as alterações da transação
            transactionRepository.save(currentTransaction);
        }
    }

    @Override
    public double getTotalReceived(List<Transaction> l) {
        // Calcula o total de transações do tipo "ENTRADA"
        double total = 0;
        for (Transaction t : l) {
            if (t.getOperation() == Operation.ENTRADA) {
                total += t.getValue(); // Soma os valores das transações de "ENTRADA"
            }
        }
        return total;
    }

    @Override
    public double getTotalExpenses(List<Transaction> l) {
        // Soma o valor absoluto de cada transação de saída
        double total = 0;
        for (Transaction t : l) {
            if (t.getOperation() == Operation.SAIDA) {
                total += Math.abs(t.getValue()); // Soma os valores absolutos das transações de "SAÍDA"
            }
        }
        return total;
    }
}
