package com.ifmg.managementFinance.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Enum que categoriza as transações por tipo/gasto específico
@AllArgsConstructor
@Getter
public enum Type {
    COMPRAS("Compras"),
    ALIMENTACAO("Alimentação"),
    SAUDE("Saúde"),
    TRANSPORTE("Transporte"),
    SALARIO("Salário"),
    CONTAS("Contas"),
    INVESTIMENTO("Investimento"),
    BEM_ESTAR("Bem-estar");
//    MEGA_SENA("Mega sena"); // Talvez um dia, Seu José

    private final String label;
}
