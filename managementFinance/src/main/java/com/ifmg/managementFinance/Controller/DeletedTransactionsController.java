package com.ifmg.managementFinance.Controller;

import com.ifmg.managementFinance.Service.TransactionServiceImpl;
import com.ifmg.managementFinance.Entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/deleted-transactions")
public class DeletedTransactionsController {

    @Autowired
    private TransactionServiceImpl transactionService;

    @GetMapping("/trash")
    public String showTrash(Model model) {
        List<Transaction> deletedTransactions = transactionService.findAllDeleted();
        model.addAttribute("deletedTransactions", deletedTransactions);
        return "trash";
    }

    @GetMapping("/restore/{id}")
    public String restoreTransaction(@PathVariable Long id) {
        transactionService.restore(id);
        // Redireciona de volta para a lista de lixeira completa
        return "redirect:/deleted-transactions/trash";
    }

    @GetMapping("/delete-permanently/{id}")
    public String deletePermanently(@PathVariable Long id) {
        transactionService.deletePermanently(id);
        // Redireciona de volta para a lista de lixeira completa
        return "redirect:/deleted-transactions/trash";
    }


}
