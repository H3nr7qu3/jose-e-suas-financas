package com.ifmg.managementFinance.Entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.boot.beanvalidation.GroupsPerOperation;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double value;

    @Enumerated(EnumType.STRING)
    private Type type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date entry_date;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date register_date;

    @Enumerated(EnumType.STRING)
    private Operation operation; //gambiarra

    @PrePersist
    protected void onCreate() {
        this.register_date = new Date();
    }

    private Boolean deletedTransactions = false;

}