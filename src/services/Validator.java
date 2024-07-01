package services;

import java.util.Scanner;


import entities.Item;

public class Validator {
	
	Operations operation;

	public int quantityValidator(String quantity) {
		Scanner in = new Scanner(System.in);
		while(!quantity.matches("[0-9]+")) {
			System.err.println("Não é um número, por favor digite um número.");
			quantity = in.next();
		}
		int validQuantity = Integer.parseInt(quantity);

		if(validQuantity < 0 )
			validQuantity = 0;

		return validQuantity;
	}


	public double valueValidator(String value) {
		Scanner in = new Scanner(System.in);
		if(!value.matches("[0-9.]+")) {
			while(!value.matches("[0-9.]+")) {
				System.err.println("Não é um número, por favor digite um número.");
				value = in.next();
			}
		}
		double validValue= Double.parseDouble(value);
		if(validValue < 0)
			validValue = 0;
		
		return validValue;
	}

	public boolean salesDateValidator(String date) {
		if(!date.matches("^(0[1-9]|1[0-9]|2[0-9]|3[0-1])/(0[1-9]|1[0-2])/(\\d{4})$")) {
			return false;
		}
		else
			return true;
	}

	public String paymentTypeValidator(String payment) {
		Scanner in = new Scanner(System.in);
		while(!payment.equals("1") && !payment.equals("2")) {
			System.err.println("Inválido");
			System.out.println("Qual foi a forma de pagamento?\n1 - PIX\n2 - DINHEIRO");
			payment = in.next();
		}
		return payment;
	}

	public int itemQuantityValidator(String quantity, int max) {
		int validQuantity = quantityValidator(quantity);
		Scanner in = new Scanner(System.in);
		while(validQuantity > max ) {
			if(validQuantity > max) {
				System.err.println("Quantidade maior do que o número de vendas!Por favor informe uma nova quantidade: ");
				String newQuantity = in.next();
				validQuantity = quantityValidator(newQuantity);
			}
		}
		
		return validQuantity;
	}

	public double itemValueValidator(String value, double max) {
		double validPrice = valueValidator(value);
		Scanner in = new Scanner(System.in);
		while(validPrice > max) {
			System.err.println("Valor R$ " + validPrice + " é maior que o valor total da venda (R$ " + max + ")\n"
							+ "Por favor digite o valor correto: ");
			String newValue = in.next();
			validPrice = valueValidator(newValue);
		}
		
		return validPrice;
	}

	public double totalValueValidator(double value, double maxValue, Item item) {
		while(value > maxValue) {
			System.err.println("Valor inconsistente. Por favor informe os dados corretos");
			value-=item.getTotalValue();
			item = operation.alterItem(item,1);
			value+=item.getTotalValue();
		}

		return value;
	}

	public int totalItensValidator(int total, int maxTotal, Item item) {
		while(total > maxTotal) {
			System.err.println("Quantidade inconsistente. Por favor informe os dados corretos");
			total -= item.getQtdItem();
			item = operation.alterItem(item,2);
			total+=item.getQtdItem();
		}
		return total;
	}
}
