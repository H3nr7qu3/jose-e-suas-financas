package com.ifmg.managementFinance.Controller;

import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Entity.Type;
import com.ifmg.managementFinance.Service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionServiceImpl;

    @GetMapping("/index")
    public String index(Model model) {

        List<Transaction> transactions = transactionServiceImpl.findAll();
        double totalExpenses = transactionServiceImpl.getTotalExpenses(transactions);
        double totalReceived = transactionServiceImpl.getTotalReceived(transactions);

        model.addAttribute("transactions", transactions);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalReceived", totalReceived);
        model.addAttribute("total", totalReceived + totalExpenses);
        return "index";
    }

    @GetMapping("/filter")
    public String filterByDate(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(value = "action", required = false) String action,  // action vai indicar qual filtro usar
            Model model) {

        List<Transaction> filteredTransactions = transactionServiceImpl.findAll();

        if ("month".equals(action)) {
            fromDate = transactionServiceImpl.getCurrentMonthRange().get(0);
            toDate = transactionServiceImpl.getCurrentMonthRange().get(1);

            filteredTransactions = transactionServiceImpl.findAllByDate(fromDate, toDate);

        } else if ("filter".equals(action)) {

            if (fromDate != null && toDate != null)
                filteredTransactions = transactionServiceImpl.findAllByDate(fromDate, toDate);

        }

        double totalExpenses = transactionServiceImpl.getTotalExpenses(filteredTransactions);
        double totalReceived = transactionServiceImpl.getTotalReceived(filteredTransactions);

        model.addAttribute("transactions", filteredTransactions);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalReceived", totalReceived);
        model.addAttribute("total", totalReceived + totalExpenses);
        return "index";
    }

    @GetMapping("/new-transaction")
    public String newTransaction(Model model) {
        model.addAttribute("types", Type.values());
        model.addAttribute("transaction", new Transaction());

        return "new-transaction";
    }

    @PostMapping("/save")
    public String saveTransaction(@ModelAttribute Transaction transaction) {
        transaction.setRegister_date(new Date());
        transactionServiceImpl.save(transaction);
        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String editTransaction(@PathVariable Long id, Model model) {
        Transaction transaction = transactionServiceImpl.findById(id);

        if (transaction == null)
            return "redirect:/index"; // ou uma página de erro customizada

        model.addAttribute("transaction", transaction);
        model.addAttribute("types", Type.values());

        return "edit-transaction";
    }

    @PostMapping("/update")
    public String updateTransaction(@ModelAttribute Transaction transaction) {
        transactionServiceImpl.update(transaction, transaction.getId());
        return "redirect:/index";
    }

}
