package conexaoMySQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


// Inicio da classe de conexão
public class ConnectionMySql {
	public static String status = "Não conectou...";

	// Método construtor da classe
	public ConnectionMySql() {

	}

	public static java.sql.Connection getConnectionMySQL(){
		Connection connection = null;
		String driverName = "com.mysql.cj.jdbc.Driver";

		try {
			Class.forName(driverName);

		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		String serverName = "localhost";
		String dataBase = "livrariacapela";

		String url = "jdbc:mysql://"+serverName+"/"+dataBase+"?useSSL=false";

		String user = "root";
		String password = "root";

		try {
			connection = DriverManager.getConnection(url,user,password);
		}catch(SQLException e) {
			System.out.println("Erro de conexão...");
			e.printStackTrace();
		}
		return connection;

	}

	public void consultar() {
		Statement s=null;
		Connection connection = getConnectionMySQL();

		try {
			s = connection.createStatement();
		}catch(SQLException e) {
			e.printStackTrace();
		}

		ResultSet r = null;

		try {
			r= s.executeQuery("SELECT * FROM VENDAS_MARCO_ABRIL");
		}catch(SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Id Quantidade de Livros  Quantidade de Itens  Forma de pagamento  Data da venda  Valor total da venda");
			while(r.next()) {
				System.out.println(r.getInt("idVenda")
							+ "       " +r.getInt("quantidadeLivros")
							+ "              " +r.getInt("quantidadeArtigos")
							+ "               " +r.getString("formaDePagamento")
							+ "               " +r.getDate("dataVenda")
							+ "                "  +r.getDouble("valorTotal"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(r!=null) {
				try {
					r.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(s !=null) {
				try {
					s.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}

			if(connection !=null) {
				try {
					connection.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
