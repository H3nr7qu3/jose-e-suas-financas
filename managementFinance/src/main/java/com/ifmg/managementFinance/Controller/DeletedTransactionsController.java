package com.ifmg.managementFinance.Controller;

import com.ifmg.managementFinance.Service.TrashServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deleted-transactions")
public class DeletedTransactionsController {

    @Autowired
    private TrashServiceImpl trashService; // Serviço que lida com transações deletadas

    @GetMapping("/trash")
    public String showTrash(Model model) {
        // Adiciona ao modelo a lista de transações deletadas para exibição
        model.addAttribute("deletedTransactions", trashService.findAllDeleted());
        return "trash"; // Retorna a view da lixeira
    }

    @GetMapping("/restore/{id}")
    public String restoreTransaction(@PathVariable Long id) {
        trashService.restore(id); // Restaura transação
        return "redirect:/deleted-transactions/trash"; // Redireciona para a lixeira
    }

    @GetMapping("/delete-permanently/{id}")
    public String deletePermanently(@PathVariable Long id) {
        trashService.deletePermanently(id); // Exclui permanentemente
        return "redirect:/deleted-transactions/trash";
    }
}
