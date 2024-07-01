package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dbOperations.Tables;
import entities.Item;
import entities.Vendas;

public class Main {

	public static void main(String[] args) throws SQLException {
		Scanner input = new Scanner(System.in);
		Tables tb = new Tables();
		String nomeTabela ="";
		String opcao = "";
		List<Vendas> listaDeVendas = new ArrayList();
		int id = 0;
		System.out.print("Digite o nome da tabela: Venda_");
		nomeTabela = input.nextLine();
		do {
			System.out.println(
							  "1 - Cadastrar venda"
							+ "\n2 - Listar Vendas"
							+ "\n3 - Criar tabela no Excel"
							+ "\n4 - Sair");
			opcao = input.next();
			switch(opcao) {
			/** In this case the program verify if a table already exist. 
			If yes: It does that to take from the database the last id registered to sum for the next sale id**/
			case "1":
				if(Tables.verificarExistenciaTabela("Vendas_"+nomeTabela)){
					id = tb.retornarIdTabelaVendas("Vendas_"+nomeTabela);
					id+=1;
				}else id+=1;
				Vendas venda = new Vendas();
				venda = venda.cadastrarVenda(id);
				if(venda != null) {
					listaDeVendas.add(venda);
					tb.createTableVenda(nomeTabela, venda);

				}
				break;
			case "2":

				if(!listaDeVendas.isEmpty()) {
					System.out.println("TABELA DE VENDAS");
					listaDeVendas.stream().forEach(v ->{System.out.println(v);});			
				}
				else System.out.println("A lista está vazia!");
				
				if(!listaDeVendas.isEmpty()) {
					System.out.println("ITENS VENDIDOS");
					
					listaDeVendas.stream().forEach(v -> {
						v.getListOfItens().stream().forEach(i -> {
							System.out.println(i);})
						;});
					}
				break;

			case "3":
				if(Tables.verificarExistenciaTabela("Vendas_"+nomeTabela)){
					tb.createExcelSpreadsheetVendas(nomeTabela);
				}else
					System.out.println("Tabela " + nomeTabela + " não existe");
			}


	}
		while (!opcao.equalsIgnoreCase("4"));

	}

}
