document.addEventListener('DOMContentLoaded', () => {
    const valueInput = document.getElementById('value-formatted');
    const originalInput = document.getElementById('original-value');
    const radios = document.querySelectorAll('input[name="operation"]');
    const formatter = new Intl.NumberFormat('pt-BR', {
        style: 'currency', currency: 'BRL', minimumFractionDigits: 2
    });

    function processValue() {
        const cents = parseInt(valueInput.value.replace(/\D/g, ''), 10) || 0;
        const amount = cents / 100;
        originalInput.value = amount.toFixed(2); // sempre positivo
        valueInput.value = formatter.format(amount);
    }

    processValue();
    valueInput.addEventListener('input', processValue);
    radios.forEach(r => r.addEventListener('change', processValue));
});
