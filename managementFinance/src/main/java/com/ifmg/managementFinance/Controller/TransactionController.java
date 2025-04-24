package com.ifmg.managementFinance.Controller;

import com.ifmg.managementFinance.Entity.Transaction;
import com.ifmg.managementFinance.Service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionServiceImpl;

    @GetMapping("/index")
    public String geral(Model model) {
        List<Transaction> transactions = transactionServiceImpl.findAll();
        model.addAttribute("transactions", transactions);
        return "index";
    }


}
