package com.ifmg.managementFinance.Service;

import com.ifmg.managementFinance.Entity.Transaction;

import java.util.List;

// Interface de serviço para manipulação de transações na "lixeira" (transações deletadas)
public interface TrashService {

    // Retorna apenas as transações que foram deletadas (in trash)
    List<Transaction> findAllDeleted();

    // Restaura uma transação deletada, movendo de volta para a lista de transações ativas
    void restore(Long id);

    // Exclui permanentemente uma transação, removendo da lixeira
    void deletePermanently(Long id);

    // Marca uma transação como deletada
    void delete(Long id);
}
