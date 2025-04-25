package com.ifmg.managementFinance.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
//esqueceu né
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double value;
    private Type type;

    // tonin ta no papo
    @Temporal(TemporalType.DATE)
    private Date entry_date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date register_date;

    @PrePersist
    protected void onCreate() {
        this.register_date = new Date();
    }


}
