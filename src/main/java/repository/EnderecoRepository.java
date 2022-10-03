package repository;

import models.Endereco;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class EnderecoRepository {

	final String DRIVER       = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;databaseName=enderecos;" +
		 "encrypt=false";

	public void getAddressList(DefaultTableModel dados) {
		Connection conn = openConnection();
		try {
			PreparedStatement statement = conn.prepareStatement(
				 "SELECT * FROM endereco"
			);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String[] rowToAdd = {
					 resultSet.getString("CEP"),
					 resultSet.getString("RUA"),
					 resultSet.getString("BAIRRO"),
					 resultSet.getString("CIDADE"),
					 resultSet.getString("UF"),
					 };

				dados.addRow(rowToAdd);
			}
			statement.close();
			resultSet.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		closeConnection(conn);
	}

	public ResultSet searchAdress(String querySearch) {
		Connection conn = openConnection();
		try {
			if (conn == null) return null;
			PreparedStatement statement = conn.prepareStatement(
				 "SELECT * FROM endereco e WHERE e.RUA LIKE CONCAT('%', ?, '%')"
			);
			statement.setString(1, querySearch);
			ResultSet resultSet = statement.executeQuery();
//			statement.close();
			return resultSet;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		closeConnection(conn);
		return null;
	}

	public void deleteAddress(DefaultTableModel dados, int rowToDelete) {
		Connection conn = openConnection();
		try {
			String cepToDelete = (String) dados.getValueAt(rowToDelete, 0);
			PreparedStatement statement = conn.prepareStatement(
				 "DELETE FROM endereco WHERE CEP = ?"
			);
			statement.setString(1, cepToDelete);
			int result = statement.executeUpdate();
			dados.removeRow(rowToDelete);
			statement.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

		closeConnection(conn);
	}

	public boolean createAddress(DefaultTableModel dados, Endereco endereco) {
		Connection conn = openConnection();
		try {
			PreparedStatement statement = conn.prepareStatement(
				 "INSERT INTO endereco VALUES (?, ?, ?, ?, ?)"
			);

			statement.setString(1, endereco.getCEP());
			statement.setString(2, endereco.getRua());
			statement.setString(3, endereco.getBairro());
			statement.setString(4, endereco.getCidade());
			statement.setString(5, endereco.getUf());

			int result = statement.executeUpdate();
			dados.addRow(endereco.getModelObject());
			statement.close();
		} catch (SQLException ex) {
			if (ex.getSQLState().startsWith("23")) {
				JOptionPane.showMessageDialog(null, "CEP já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(null, "Erro ao tentar inserir no banco de dados!", "Erro",
				 JOptionPane.ERROR_MESSAGE);
			return true;
		}
		closeConnection(conn);
		return false;
	}

	public boolean patchAddress(DefaultTableModel dados, Endereco endereco, int selectedRow) {
		Connection conn = openConnection();
		try {
			PreparedStatement statement = conn.prepareStatement(
				 "UPDATE endereco SET CEP = ?, RUA = ?, BAIRRO = ?, CIDADE = ?, UF = ? WHERE CEP = ?"
			);

			PreparedStatement statementCountCeps = conn.prepareStatement(
				 "SELECT COUNT(*) FROM endereco WHERE CEP = ?"
			);

			statementCountCeps.setString(1, endereco.getCEP());

			statement.setString(1, endereco.getCEP());
			statement.setString(2, endereco.getRua());
			statement.setString(3, endereco.getBairro());
			statement.setString(4, endereco.getCidade());
			statement.setString(5, endereco.getUf());
			statement.setString(6, endereco.getCEP());

			ResultSet resultCepExists = statementCountCeps.executeQuery();
			resultCepExists.next();
			if (resultCepExists.getInt(1) > 0 && !(dados.getValueAt(selectedRow, 0).equals(endereco.getCEP()))) {
				throw new SQLIntegrityConstraintViolationException();
			}
			;

			int result = statement.executeUpdate();

			if (result == 0) {
				throw new SQLException();
			}

			int i = 0;
			for (Object text : endereco.getModelObject()) {
				dados.setValueAt(text, selectedRow, i);
				i++;
			}
			statement.close();
		} catch (SQLIntegrityConstraintViolationException exception) {
			JOptionPane.showMessageDialog(null, "CEP já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, "Erro ao tentar inserir no banco de dados!", "Erro",
				 JOptionPane.ERROR_MESSAGE);
			return true;
		}
		closeConnection(conn);
		return false;
	}

	private void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private Connection openConnection() {
		try {
			Class.forName(DRIVER);
			return DriverManager.getConnection(DATABASE_URL, "sa", "123456");
		} catch (SQLException |
			 ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
}