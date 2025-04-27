document.addEventListener('DOMContentLoaded', () => {
    // Campo de exibição formatada
    const valueInput = document.getElementById('value-formatted');
    // Campo oculto com valor bruto
    const originalInput = document.getElementById('original-value');
    // Rádios de operação
    const radios = document.querySelectorAll('input[name="operation"]');
    // Formatador de moeda BRL
    const formatter = new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL',
        minimumFractionDigits: 2
    });

    function processValue() {
      // Remove não dígitos e obtém centavos
      const cents = parseInt(valueInput.value.replace(/\D/g, ''), 10) || 0;
      // Converte para reais
      const amount = cents / 100;
      // Atualiza valor bruto
      originalInput.value = amount.toFixed(2);
      // Formata com R$
      valueInput.value = formatter.format(amount);
    }

    processValue();
    valueInput.addEventListener('input', processValue);
    radios.forEach(r => r.addEventListener('change', processValue));
});
