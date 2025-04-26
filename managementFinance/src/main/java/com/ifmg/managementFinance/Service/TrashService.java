package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;

import java.util.List;

public interface TrashService {
    List<Transaction> findAll();
    List<Transaction> findAllDeleted();
    void restore(Long id);
    void deletePermanently(Long id);
    void delete(Long id);
}
