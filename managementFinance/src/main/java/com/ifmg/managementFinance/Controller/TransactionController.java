package com.ifmg.managementFinance.Controller;

import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Entity.Type;
import com.ifmg.managementFinance.Service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

}
