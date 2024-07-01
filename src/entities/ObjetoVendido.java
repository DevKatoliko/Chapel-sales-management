package entities;

import services.Validator;

public class ObjetoVendido extends Item{
	Validator valid = new Validator();

	public ObjetoVendido() {
		this.tipo = "Art. Religioso";
	}

	@Override
	public Item cadastrarItens(Vendas venda) {
		Item artReligioso = new ObjetoVendido();
		artReligioso.setVenda(venda.getIdVenda());
		System.out.println("Digite o nome do artigo religioso: ");
		artReligioso.setNomeItem(input.nextLine());
		System.out.println("Digite a quantidade desse artigo religioso: ");
		int articleQuantity = valid.itemQuantityValidator(input.next(), venda.getQtdItens());
		artReligioso.setQtdItem(articleQuantity);
		System.out.println("Digite o valor do artigo religioso:");
		double articleValue = valid.itemValueValidator(input.next(), venda.getValorTotal());
		artReligioso.setValor(articleValue);

		return artReligioso;
	}
}
