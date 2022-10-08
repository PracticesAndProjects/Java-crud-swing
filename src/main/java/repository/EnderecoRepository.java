package repository;

import configurations.DatabaseConfig;
import models.Endereco;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoRepository {

	final String DRIVER       = DatabaseConfig.DRIVER;
	final String DATABASE_URL = DatabaseConfig.DATABASE_URL;
	final String DB_PASSWORD  = DatabaseConfig.DB_PASSWORD;
	final String DB_USER      = DatabaseConfig.DB_USER;

	public List<Endereco> getAddressList () throws Exception {
		Connection        conn      = null;
		PreparedStatement statement = null;
		try {
			conn = openConnection();
			statement = conn.prepareStatement(
				"SELECT * FROM endereco"
			                                 );
			ResultSet      resultSet    = statement.executeQuery();
			List<Endereco> listaInicial = new ArrayList<>();
			while ( resultSet.next() ) {
				Endereco endSingular = new Endereco(
					resultSet.getString("CEP"),
					resultSet.getString("RUA"),
					resultSet.getString("BAIRRO"),
					resultSet.getString("CIDADE"),
					resultSet.getString("UF")
				);
				listaInicial.add(endSingular);
			}
			return listaInicial;
		} catch ( SQLException ex ) {
			System.out.println(ex.getMessage());
		} finally {
			closeConnection(conn);
		}
		return null;
	}

	public List<Endereco> searchAdress (String querySearch) throws Exception {
		Connection conn = null;
		try {
			conn = openConnection();
			PreparedStatement statement = conn.prepareStatement(
				"SELECT * FROM endereco e WHERE e.RUA LIKE CONCAT('%', ?, '%')"
			                                                   );
			statement.setString(1, querySearch);
			ResultSet resultSet = statement.executeQuery();
			return this.serializeResultSet(resultSet);
		} catch ( SQLException ex ) {
			System.out.println(ex.getMessage());
			closeConnection(conn);
		} finally {
			closeConnection(conn);
		}
		return null;
	}

	public void deleteAddress (Endereco endereco) throws Exception {
		Connection conn = null;
		try {
			conn = openConnection();
			PreparedStatement statement = conn.prepareStatement(
				"DELETE FROM endereco WHERE CEP = ?"
			                                                   );
			statement.setString(1, endereco.getCEP());
			statement.executeUpdate();
			statement.close();
		} catch ( Exception ex ) {
			System.out.println(ex.getMessage());
			throw new Exception();
		} finally {
			closeConnection(conn);
		}

	}

	public Endereco createAddress (Endereco endereco) throws Exception {
		Connection conn = null;
		try {
			conn = openConnection();
			PreparedStatement statement = conn.prepareStatement(
				"INSERT INTO endereco VALUES (?, ?, ?, ?, ?)"
			                                                   );

			statement.setString(1, endereco.getCEP());
			statement.setString(2, endereco.getRua());
			statement.setString(3, endereco.getBairro());
			statement.setString(4, endereco.getCidade());
			statement.setString(5, endereco.getUf());

			int result = statement.executeUpdate();

			if ( !( result > 0 ) ) {
				throw new Exception();
			}
			return endereco;
		} catch ( SQLException ex ) {
			throw new Exception();
		} finally {
			closeConnection(conn);
		}
	}

	public void patchAddress (
		Endereco endereco,
		int selectedRow,
		DefaultTableModel dados
	                         ) throws Exception {
        /* Declaração das variáveis no escopo fora do try catch para poder trabalhar com eles no
        bloco finally */
		Connection        conn               = null;
		PreparedStatement statement          = null;
		PreparedStatement statementCountCeps = null;
		try {
			conn = openConnection();
			statement = conn.prepareStatement(
				"UPDATE endereco SET CEP = ?, RUA = ?, BAIRRO = ?, CIDADE = ?, UF = ? WHERE CEP = ?"
			                                 );

			statementCountCeps = conn.prepareStatement(
				"SELECT COUNT(*) FROM endereco WHERE CEP = ?"
			                                          );

			statementCountCeps.setString(1, endereco.getCEP());

			statement.setString(1, endereco.getCEP());
			statement.setString(2, endereco.getRua());
			statement.setString(3, endereco.getBairro());
			statement.setString(4, endereco.getCidade());
			statement.setString(5, endereco.getUf());
			statement.setString(6, endereco.getCEP());

			ResultSet doesCepExists = statementCountCeps.executeQuery();
			doesCepExists.next();

			/* Gera exceção de integridade manualmente pois o JDBC não está lançando esta exceção */
			if ( doesCepExists.getInt(1) > 0 && !( dados.getValueAt(
				selectedRow,
				0
			                                                       ).equals(endereco.getCEP()) ) ) {
				throw new SQLIntegrityConstraintViolationException();
			}
			;

			int result = statement.executeUpdate();
			if ( result == 0 ) {
				throw new SQLException();
			}
			statementCountCeps.close();
			statement.close();
		} catch ( SQLIntegrityConstraintViolationException exception ) {
			throw new SQLIntegrityConstraintViolationException();
		} catch ( SQLException exception ) {
			throw new SQLException();
		} finally {
			closeConnection(conn);
		}
	}

	private List<Endereco> serializeResultSet (ResultSet rs) {
		try {
			List<Endereco> list = new ArrayList<>();
			while ( rs.next() ) {
				Endereco end = new Endereco(
					rs.getString(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5)
				);
				list.add(end);
			}
			return list;
		} catch ( Exception exception ) {
			exception.printStackTrace();
			return null;
		}
	}

	private void closeConnection (Connection conn) {
		try {
			if ( conn != null ) {
				conn.close();
			}
		} catch ( SQLException ex ) {
			System.out.println(ex.getMessage());
		}
	}

	private Connection openConnection () throws Exception {
		try {
			Class.forName(DRIVER);
			return DriverManager.getConnection(DATABASE_URL, DB_USER, DB_PASSWORD);
		} catch ( SQLException |
			ClassNotFoundException ex ) {
			throw new Exception("Erro de conexão com o banco de dados!");
		}
	}

}