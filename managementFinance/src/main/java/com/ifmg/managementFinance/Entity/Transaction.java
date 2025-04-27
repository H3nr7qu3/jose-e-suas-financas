package com.ifmg.managementFinance.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor // Gera construtor com todos os campos
@NoArgsConstructor  // Gera construtor vazio
@Data               // Gera getters, setters, toString, equals, etc.
@Entity             // Marca a classe como uma entidade JPA
@Table(name = "transactions") // Define o nome da tabela no banco
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente pelo banco
    private Long id;

    private String name;  // Nome da transação
    private double value; // Valor da transação

    @Enumerated(EnumType.STRING)
    private Type type;    // Tipo/categoria da transação (

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date entry_date; // Data da transação (informada pelo usuário)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date register_date; // Data de registro (definida automaticamente)

    @Enumerated(EnumType.STRING)
    private Operation operation; // Tipo de operação (ENTRADA ou SAIDA)

    @PrePersist
    protected void onCreate() {
        // Antes de salvar no banco, define a data de registro como a data atual
        this.register_date = new Date();
    }

    private Boolean deletedTransactions = false; //Indica se a transação está "deletada" (trash)
}
