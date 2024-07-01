package entities;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import services.Operations;
import services.Validator;

public class Vendas {
	private int idVenda;
	private int qtdLivros;
	private int qtdItens;
	private String formaPagamento;
	private LocalDate dataVenda;
	private Double valorTotal;

	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private List<Item> itensVendidos = new ArrayList();

	Scanner input = new Scanner(System.in);
	Validator valid = new Validator();
	Operations operation = new Operations();
	public Vendas() {

	}

	public void gerarTabelaItens(Vendas venda) {
		double valorItens = 0;
		int totalItens = 0;
		int salesTotalItens = venda.getQtdLivros() + venda.getQtdItens();
		while (totalItens < salesTotalItens) {
			if (venda.getQtdLivros() > 0) {
				Item livro = new LivroVendido();
				do {
					livro = livro.cadastrarItens(venda);
					valorItens += livro.getTotalValue();
					totalItens += livro.getQtdItem();
					if(valorItens > this.getValorTotal()) {
						valorItens = valid.totalValueValidator(valorItens, this.getValorTotal(), livro);
					}
					if(totalItens > salesTotalItens) {
						totalItens = valid.totalItensValidator(totalItens, salesTotalItens, livro);
					}
					itensVendidos.add(livro);
					if (livro.getQtdItem() == this.getQtdLivros() || livro.getQtdItem() == 0 && livro.getValor() == 0)
						break;
				} while (venda.getQtdLivros() > totalItens);

			}

			if (venda.getQtdItens() > 0) {
				Item artReligioso = new ObjetoVendido();
				do {
					artReligioso = artReligioso.cadastrarItens(venda);
					valorItens += artReligioso.getValor()*artReligioso.getQtdItem();
					totalItens += artReligioso.getQtdItem();

					if(valorItens > this.getValorTotal()) {
						valorItens = valid.totalValueValidator(valorItens, this.getValorTotal(), artReligioso);
					}
					if(totalItens > salesTotalItens) {
						totalItens = valid.totalItensValidator(totalItens, salesTotalItens, artReligioso);
					}
					itensVendidos.add(artReligioso);
					if (artReligioso.getQtdItem() == this.getQtdItens() || artReligioso.getQtdItem() == 0 && artReligioso.getValor() == 0)
						break;

				} while (venda.getQtdItens() >= totalItens);

			}

		}

	}

	public Vendas cadastrarVenda(int id) {
		Vendas venda = new Vendas();

		venda.setIdVenda(id);
		setIdVenda(id);

		System.out.println("Digite a quantidade de livros:");
		int quantity = valid.quantityValidator(input.next());
		venda.setQtdLivro(quantity);

		System.out.println("Se houver artigos religiosos digite a quantidade");
		venda.setQtdItens(valid.quantityValidator(input.next()));

		if(venda.getQtdLivros()<=0 && venda.getQtdItens() <=0) {
			System.out.println("Nenhuma venda realizada!");
			return null;
		}
		else
			System.out.println("Qual foi a forma de pagamento?\n1 - PIX\n2 - DINHEIRO");
			String pagamento = valid.paymentTypeValidator(input.next());

			if (pagamento.equals("1")) {
				pagamento = "PIX";
			}
			else  {
				pagamento = "DINHEIRO";
			}

			venda.setFormaPagamento(pagamento);

			System.out.println("Informe a data da venda: dd/mm/yyyy");

			String validDate = input.next();

			while( !valid.salesDateValidator(validDate)) {
				System.err.println("Data inválida por favor digite novamente: ");
				validDate =input.next();
			}

			LocalDate data = LocalDate.parse(validDate, dateFormat);

			venda.setDataVenda(data);

			System.out.println("Digite o valor total da venda:");
			double valorTotal = valid.valueValidator(input.next());

			venda.setValorTotal(valorTotal);

			venda.gerarTabelaItens(venda);

			return venda;
	}


	private void setIdVenda(int idVenda) {
		this.idVenda = idVenda;
	}

	private void setQtdLivro(int qtdLivros) {
		this.qtdLivros = qtdLivros;
	}

	private void setQtdItens(int itens) {
		this.qtdItens = itens;
	}

	private void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	private void setDataVenda(LocalDate dataVenda) {
		this.dataVenda = dataVenda;
	}

	private void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public int getIdVenda() {
		return idVenda;
	}

	public int getQtdLivros() {
		return qtdLivros;
	}

	public int getQtdItens() {
		return qtdItens;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public LocalDate getDataVenda() {
		return dataVenda;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public List<Item> getListOfItens() {
		return itensVendidos;
	}

}
