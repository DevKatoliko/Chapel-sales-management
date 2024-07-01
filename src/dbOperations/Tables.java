package dbOperations;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import conexaoMySQL.ConnectionMySql;
import entities.Item;
import entities.Vendas;


public class Tables {


	public Tables() {

	}
	@SuppressWarnings("resource")
	public void createTableVenda(String nomeTabela, Vendas venda) {
		Connection c = null;
		PreparedStatement ps = null;
		Statement s =null;
		try {
			c = ConnectionMySql.getConnectionMySQL();
			if(!verificarExistenciaTabela("Vendas_"+nomeTabela)) {
			String sqlCriandoTabela = "CREATE TABLE Vendas_"+nomeTabela+"( "
									  +"id int primary key auto_increment,"
									  +"quantidadeLivros int,"
									  +"quantidadeArtigos int,"
									  +"formaDePagamento varchar(10),"
									  +"dataVenda date,"
									  + "valorTotal double(9,2)"
									  + " )";
			ps = c.prepareStatement(sqlCriandoTabela);
			ps.executeUpdate();
			System.out.println("Tabela Vendas_" + nomeTabela + " criada com sucesso!");
			}else
				System.out.println("Tabela já existia!");

			String inserirDados = "INSERT INTO Vendas_"+nomeTabela+"("
									  +"quantidadeLivros,"
									  +"quantidadeArtigos,"
									  +"formaDePagamento,"
									  +"dataVenda,"
									  +"valorTotal)"
									  + "VALUES(?,?,?,?,?)";

				ps = c.prepareStatement(inserirDados);
				ps.setInt(1, venda.getQtdLivros());
				ps.setInt(2, venda.getQtdItens());
				ps.setString(3,venda.getFormaPagamento());
				ps.setDate(4, java.sql.Date.valueOf(venda.getDataVenda()));
				ps.setDouble(5, venda.getValorTotal());
				ps.executeUpdate();

			System.out.println("Dados inseridos com sucesso");


			String criandoTabelaRelatorio = "CREATE TABLE Itens_vendidos_"+nomeTabela +"( "
											 +"idVenda int NOT NULL, "
											 +"FOREIGn KEY(idVenda) REFERENCES Vendas_"+nomeTabela+"(id), "
											 +"tipo varchar(20),"
											 +"nome varchar(90),"
											 +"quantidade int,"
											 +"valor double(9,2)"
											 +" )";
			if(!verificarExistenciaTabela("Itens_vendidos_"+nomeTabela)) {
				ps.executeUpdate(criandoTabelaRelatorio);
				System.out.println("Tabela " + "Itens_vendidos_"+nomeTabela + " criada com sucesso!");

			}else System.out.println("Tabela " + "Itens_vendidos_"+nomeTabela + " já existia!");

			String adicionandoItensNaTabela = "INSERT INTO Itens_vendidos_"+nomeTabela + "( "
											 + "idVenda,"
											 + "tipo,"
											 + "nome,"
											 + "quantidade,"
											 + "valor"
											 + " )"
											 + "VALUES(?, ?, ?, ?, ?)";

				for(Item item : venda.getListOfItens()) {
					ps = c.prepareStatement(adicionandoItensNaTabela);
					ps.setInt(1,item.getVenda());
					ps.setString(2,item.tipo );
					ps.setString(3, item.getNomeItem());
					ps.setInt(4, item.getQtdItem());
					ps.setDouble(5, item.getValor());
					ps.executeUpdate();
				}

			System.out.println("Itens inseridos com sucesso!");

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(ps !=null) {
					ps.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}

			try {
	            if (s != null) {
	                s.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

			try {
				if(c !=null) {
					c.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public  int retornarIdTabelaVendas(String nomeTabela) {
		Connection c = ConnectionMySql.getConnectionMySQL();
		Statement s = null;
		ResultSet rs = null;
		int id = 0;
		try {
			s = c.createStatement();
			String buscarId = "SELECT MAX(id) as lastId FROM "+nomeTabela;
			rs = s.executeQuery(buscarId);
			if(rs.next()) {
				id = rs.getInt("lastId");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(c != null) {
					c.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(s != null) {
					s.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(rs != null) {
					rs.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	public static boolean verificarExistenciaTabela(String nomeTabela) throws SQLException {
		Connection c = ConnectionMySql.getConnectionMySQL();
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SHOW TABLES LIKE '"+nomeTabela+"'");
		return rs.next();
	}

	public void createExcelSpreadsheetVendas(String nomeTabela) {
		Connection c = ConnectionMySql.getConnectionMySQL();
		try {
		Statement s = c.createStatement();
		String searchTable = "SELECT * FROM Vendas_" + nomeTabela;

		ResultSet rs = s.executeQuery(searchTable);

		// Criar um novo wb do Excel
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Vendas"); //Name of the document in the inferior tab

		String[] columnNames = {"Id da Venda",
								"Quantidade de Livros",
								"Quantidade de itens religiosos",
								"Forma de pagamento",
								"Data",
								"Valor total"};
		Class<?>[]columnTypesSales = {Integer.class,Integer.class,Integer.class,String.class,java.sql.Date.class, Double.class};
		String tableTitle = "Tabelas de vendas de " + nomeTabela;

		// Criando os estilos para inserir a Data e o tipo monetário nos doubles
		 CellStyle dateCellStyle = wb.createCellStyle();
         short dateFormat = wb.createDataFormat().getFormat("dd/MM/yyyy");
         dateCellStyle.setDataFormat(dateFormat);

         CellStyle currencyStyle = wb.createCellStyle();
		 DataFormat format = wb.createDataFormat();
		 currencyStyle.setDataFormat(format.getFormat("R$ #,##0.00"));

		 // Combinar os estilos de alinhamento e formato de moeda
         CellStyle combinedStyle = wb.createCellStyle();
         combinedStyle.cloneStyleFrom(currencyStyle);
         combinedStyle.setAlignment(HorizontalAlignment.CENTER);

         // Configurar bordas para as células
         CellStyle borderedStyle = wb.createCellStyle();
         borderedStyle.setBorderBottom(BorderStyle.THIN);
         borderedStyle.setBorderTop(BorderStyle.THIN);
         borderedStyle.setBorderRight(BorderStyle.THIN);
         borderedStyle.setBorderLeft(BorderStyle.THIN);

         // Combinar o estilo de borda com os outros estilos
         CellStyle combinedBorderedStyle = wb.createCellStyle();
         combinedBorderedStyle.cloneStyleFrom(combinedStyle);
         combinedBorderedStyle.setBorderBottom(BorderStyle.THIN);
         combinedBorderedStyle.setBorderTop(BorderStyle.THIN);
         combinedBorderedStyle.setBorderRight(BorderStyle.THIN);
         combinedBorderedStyle.setBorderLeft(BorderStyle.THIN);

		 // Adicionar título na primeira linha que ocupa todas as colunas
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(tableTitle);

		// Mesclar células para o título
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnNames.length - 1));

		// Estilo para o titulo alinhando no centro
		CellStyle alignCenter = wb.createCellStyle();
		alignCenter.setAlignment(HorizontalAlignment.CENTER);
		titleCell.setCellStyle(alignCenter);
		// Criar uma linha para o cabeçalho de colunas da tabela
		Row headerRow = sheet.createRow(1);

		for(int i = 0 ; i < columnNames.length ; i++) {
			Cell cellRow = headerRow.createCell(i);
			cellRow.setCellValue(columnNames[i]);
			cellRow.setCellStyle(alignCenter);
		}

		// A partir da linha 2 começam a ser preenchidos os dados
		int rownum = 2;
		 while(rs.next()) {
			 Row dataRows = sheet.createRow(rownum++);
			 for(int i = 0 ; i < columnNames.length ; i++) {
				 Cell cellRow = dataRows.createCell(i);
				 String valueOfColumn = rs.getString(i+1);
				 if(columnTypesSales[i] == Integer.class) {
					 int intValue = Integer.parseInt(valueOfColumn);
					 cellRow.setCellValue(intValue);
					 cellRow.setCellStyle(alignCenter);
				 }
				 else if(columnTypesSales[i] == Double.class) {
					 double doubleValue = Double.parseDouble(valueOfColumn);
					 cellRow.setCellValue(doubleValue);
					 cellRow.setCellStyle(currencyStyle);

				 }
				 else if(columnTypesSales[i] == java.sql.Date.class) {
					 java.sql.Date saleDate = rs.getDate(i+1);
					 cellRow.setCellValue(saleDate);
					 cellRow.setCellStyle(dateCellStyle);

				 }
				 else {
					 cellRow.setCellValue(valueOfColumn);
					 cellRow.setCellStyle(alignCenter);
				 }
			 }
		 }

		 for(int i = 0 ; i< columnNames.length ; i++) {
			 sheet.autoSizeColumn(i);
		 }

		 rs.close();

		 searchTable = "SELECT * FROM Itens_vendidos_" + nomeTabela;
		 rs = s.executeQuery(searchTable);

		 String[] columnNamesItens = {"Venda","Tipo","Nome","Quantidade","Valor"};
		 Class<?>[] columnTypes = {Integer.class, String.class,String.class,Integer.class,Double.class};

		 XSSFSheet sheet2 = wb.createSheet("Vendas de itens");
		 tableTitle = "Tabela de itens vendidos em " + nomeTabela;

		 titleRow = sheet2.createRow(0);
		 titleCell = titleRow.createCell(0);
		 titleCell.setCellValue(tableTitle);

		 sheet2.addMergedRegion(new CellRangeAddress(0,0,0, columnNamesItens.length -1));
		 titleCell.setCellStyle(alignCenter);

		 headerRow = sheet2.createRow(1);
		 for(int i = 0 ; i < columnNamesItens.length ; i++) {
			 Cell cellRow = headerRow.createCell(i);
			 cellRow.setCellValue(columnNamesItens[i]);
			 cellRow.setCellStyle(alignCenter);
		 }

		 rownum= 2;

		 while(rs.next()) {
			 Row row = sheet2.createRow(rownum++);
			 for(int i = 0 ; i < columnNamesItens.length ; i++) {

				 String value = rs.getString(i+1);
				 Cell cellData = row.createCell(i);

				 if(columnTypes[i] == Integer.class) {
					 int intValue = Integer.parseInt(value);
					 cellData.setCellValue(intValue);
					 cellData.setCellStyle(alignCenter);

				 }
				 else if (columnTypes[i] == Double.class) {
					 double doubleValue = Double.parseDouble(value);
					 cellData.setCellValue(doubleValue);
					 cellData.setCellStyle(currencyStyle);
				 }
				 else {
					 cellData.setCellValue(value);
					 cellData.setCellStyle(alignCenter);
				 }
			 }

		 }
		 for(int i = 0 ; i< columnNames.length ; i++) {
			 sheet2.autoSizeColumn(i);
		 }

		 rs.close();
		 s.close();
		 c.close();

		 // Criando o arquivo
		 FileOutputStream out = new FileOutputStream("C:\\Users\\User\\Desktop\\vendas_"+nomeTabela+".xlsx");
		 wb.write(out);

		 out.close();
		 wb.close();

		 System.out.println("Tabela do Excel criada com sucesso!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
