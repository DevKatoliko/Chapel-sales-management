package services;

import java.util.Scanner;

import entities.Item;

public class Operations {
	
	Validator valid;

	public Item alterItem(Item item, int flag) {
		Scanner in = new Scanner(System.in);
		if(flag == 1) {
			String option = "";
			do {
				System.out.println((item.getNomeItem().equals("Livro")) ?
						"Informe o campo que deseja alterar do livro" :
						"Informe o campo que deseja alterar do artigo religioso");
				System.out.println("1 - Quantidade\n2 - Valor\n0 - Sair");
				option = in.next();
				switch(option) {
					case "1":
						int quantity = valid.quantityValidator(in.next());
						item.setQtdItem(quantity);
						break;
					case "2":
						double value= valid.valueValidator(in.next());
						item.setValor(value);
						break;
				}
			}while(!option.equals("0"));
		}
		if(flag == 2) {

			System.out.println((item.getNomeItem().equals("Livro")) ?
					"Informe uma quantidade de livros" :
					"Informe uma quantidade de artigos religiosos");

			int quantity = valid.quantityValidator(in.next());
			item.setQtdItem(quantity);
		}
		return item;
	}
}
