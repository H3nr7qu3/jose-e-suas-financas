package com.ifmg.managementFinance.Controller;

import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Entity.Type;
import com.ifmg.managementFinance.Service.TransactionServiceImpl;
import com.ifmg.managementFinance.Service.TrashServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/") // Define que todas as rotas deste controller começam pela raiz "/"
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionServiceImpl; // Serviço que lida com as transações

    @Autowired
    TrashServiceImpl trashService; // Serviço que lida com transações deletadas (trash)

    // Página principal que exibe todas as transações e totais
    @GetMapping("/")
    public String index(Model model) {
        List<Transaction> transactions = transactionServiceImpl.findAll(); // Busca todas transações
        double totalExpenses = transactionServiceImpl.getTotalExpenses(transactions); // Soma das despesas
        double totalReceived = transactionServiceImpl.getTotalReceived(transactions); // Soma dos recebimentos

        return populateModel(model, transactions);
    }

    // Filtro de transações por data ou por mês atual
    @GetMapping("/filter")
    public String filterByDate(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "action", required = false) String action,
            Model model) {

        List<Transaction> filteredTransactions = transactionServiceImpl.findAll(); // Começa com todas as transações

        if ("month".equals(action)) {
            // Se clicou pra ver o mês atual
            fromDate = transactionServiceImpl.getCurrentMonthRange().get(0);
            toDate = transactionServiceImpl.getCurrentMonthRange().get(1);
            filteredTransactions = transactionServiceImpl.findAllByDate(fromDate, toDate);

        } else if ("filter".equals(action)) {
            // Filtro manual pelas datas fornecidas
            if (fromDate != null || toDate != null) {
                if(fromDate == null ^ toDate == null) { // XOR: Apenas um dos dois pode estar certo
                    // Erro: Falta alguma das datas
                    model.addAttribute("errorMessage", "Insira duas datas para o filtro.");
                    return populateModel(model, filteredTransactions);
                }
                else if (fromDate.after(toDate)) {
                    // Erro: data inicial depois da final
                    model.addAttribute("errorMessage", "A data inicial não pode ser maior que a data final.");
                    return populateModel(model, filteredTransactions);
                }
                // Filtro válido
                filteredTransactions = transactionServiceImpl.findAllByDate(fromDate, toDate);
            }
        }

        return populateModel(model, filteredTransactions);
    }

    private String populateModel(Model model, List<Transaction> transactions) {
        double totalExpenses = transactionServiceImpl.getTotalExpenses(transactions);
        double totalReceived = transactionServiceImpl.getTotalReceived(transactions);

        model.addAttribute("transactions", transactions);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalReceived", totalReceived);
        model.addAttribute("total", totalReceived + totalExpenses);

        return "index";
    }

    // Página para criar nova transação
    @GetMapping("/new-transaction")
    public String newTransaction(Model model) {
        model.addAttribute("types", Type.values()); // Tipos de transações (enum)
        model.addAttribute("transaction", new Transaction()); // Objeto vazio para o formulário
        return "new-transaction"; // View de criação
    }

    // Salva uma nova transação no banco
    @PostMapping("/save")
    public String saveTransaction(@ModelAttribute Transaction transaction, @RequestParam("operation") String operation) {
        transaction.setRegister_date(new Date()); // Define a data de registro como agora

        if(operation.equals("SAIDA")) {
            transaction.setValue(transaction.getValue() * -1); // Se for despesa, valor é negativo
        }

        transactionServiceImpl.save(transaction); // Salva no banco
        return "redirect:/"; // Redireciona pra página principal
    }

    // Edita uma transação existente
    @GetMapping("/edit/{id}")
    public String editTransaction(@PathVariable Long id, Model model) {
        Transaction transaction = transactionServiceImpl.findById(id);

        if (transaction == null) {
            return "redirect:/"; // Se não achar, volta pro início
        }

        model.addAttribute("transaction", transaction);
        model.addAttribute("types", Type.values());

        // NÃO atualiza nada no banco aqui.
        return "edit-transaction"; // View da edição
    }

    // Atualiza transação já existente
    @PostMapping("/update")
    public String updateTransaction(@ModelAttribute Transaction transaction) {
        transactionServiceImpl.update(transaction, transaction.getId()); // Atualiza com os dados do form
        return "redirect:/";
    }

    // Deleta (move pra lixeira) uma transação
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        trashService.delete(id); // Move a transação pra lixeira
        return "redirect:/";
    }
}
