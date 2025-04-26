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

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;



    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAllByDeletedTransactionsFalse();
    }

    @Override
    public List<Transaction> findAllByDate(Date fromDate, Date toDate) {
        List<Transaction> transactions = new ArrayList<Transaction>();

        for(Transaction transaction : findAll()) {
            if(fromDate.before(transaction.getEntry_date()) && toDate.after(transaction.getEntry_date()))
                transactions.add(transaction);
        }

        return transactions;
    }

    public List<Date> getCurrentMonthRange() {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        Date fromData = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toData = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return List.of(fromData, toData);
    }


    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }



    @Override
    public void update(Transaction transaction, Long id) {
        Transaction currentTransaction = findById(id);
        currentTransaction.setName(transaction.getName());
        currentTransaction.setValue(transaction.getValue());
        currentTransaction.setType(transaction.getType());
        currentTransaction.setEntry_date(transaction.getEntry_date());
        currentTransaction.setRegister_date(new Date());
        currentTransaction.setOperation(transaction.getOperation());

        transactionRepository.save(currentTransaction);
    }


    @Override
    public double getTotalReceived(List<Transaction> l) {
        return l.stream()
                .filter(t -> t.getOperation() == Operation.ENTRADA)
                .mapToDouble(Transaction::getValue)
                .sum();
    }
    @Override
    public double getTotalExpenses(List<Transaction> l) {
        return l.stream()
                .filter(t -> t.getOperation() == Operation.SAIDA)
                .mapToDouble(Transaction::getValue)
                .sum();
    }







}
