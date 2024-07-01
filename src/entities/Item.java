package entities;

import java.util.Scanner;

public abstract class Item {
	private int venda;
	public String tipo;
	private String nome;
	private int quantidade;
	private Double valor;

	Scanner input = new Scanner(System.in);
	abstract public Item cadastrarItens(Vendas venda);

	public int getVenda() {
		return venda;
	}

	public void setVenda(int venda) {

		this.venda = venda;
	}

	public String getNomeItem() {
		return nome;
	}

	public void setNomeItem(String nomeItem) {
		this.nome = nomeItem;
	}

	public int getQtdItem() {
		return quantidade;
	}

	public void setQtdItem(int qtdItem) {

		this.quantidade = qtdItem;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getTotalValue () {
		return this.quantidade*this.valor;
	}
	@Override
	public String toString() {
		StringBuilder bd = new StringBuilder();
		bd.append(getVenda() + " | "
				+ tipo + " | "
				+ getNomeItem() + " | "
				+ getQtdItem() + " | "
				+ getValor());
		return bd.toString();
	}
}


