package entities;

import services.Validator;

public class LivroVendido extends Item{
	Validator valid = new Validator();

	public LivroVendido() {
		this.tipo = "Livro";
	}

	@Override
	public Item cadastrarItens(Vendas venda) {
		Item livro = new LivroVendido();
		livro.setVenda(venda.getIdVenda());
		System.out.println("Digite o nome do livro: ");
		livro.setNomeItem(input.nextLine());
		System.out.println("Digite a quantidade desse livro: ");
		int bookQuantity = valid.itemQuantityValidator(input.next(), venda.getQtdLivros());
		livro.setQtdItem(bookQuantity);
		System.out.println("Digite o valor do livro");
		double bookValue = valid.itemValueValidator(input.next(), venda.getValorTotal());
		livro.setValor(bookValue);


		return livro;
	}


}
