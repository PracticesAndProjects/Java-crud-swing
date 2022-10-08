package repository;

import models.Endereco;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoRepository {

    final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
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
        } finally {
            closeConnection(conn);
        }
    }

    public List<Endereco> searchAdress(String querySearch) {
        Connection conn = openConnection();
        try {
            if (conn == null) return null;
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM endereco e WHERE e.RUA LIKE CONCAT('%', ?, '%')"
            );
            statement.setString(1, querySearch);
            ResultSet resultSet = statement.executeQuery();
            List<Endereco> r = this.serializeResultSet(resultSet);
            statement.close();
            resultSet.close();
            return r;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            closeConnection(conn);
        } finally {
            closeConnection(conn);
        }
            return null;
    }

    public void deleteAddress(Endereco endereco) throws Exception {
        Connection conn = openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM endereco WHERE CEP = ?"
            );
            statement.setString(1, endereco.getCEP());
            statement.executeUpdate();
            statement.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception();
        } finally {
            closeConnection(conn);
        }

    }

    public Endereco createAddress(Endereco endereco) {
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

            if (!(result > 0)) {
                throw new Exception();
            }
            return endereco;
        } catch (SQLException ex) {
            throw new Exception();
        } finally {
            closeConnection(conn);
            return null;
        }
    }

    public void patchAddress(Endereco endereco, int selectedRow, DefaultTableModel dados) throws Exception {
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
            /* Gera exceção de integridade pois o JDBC não está lançando esta exceção */
            if (resultCepExists.getInt(1) > 0 && !(dados.getValueAt(selectedRow, 0).equals(endereco.getCEP()))) {
                throw new SQLIntegrityConstraintViolationException();
            }
            ;

            int result = statement.executeUpdate();

            if (result == 0) {
                throw new SQLException();
            }

            statement.close();
        } catch (SQLIntegrityConstraintViolationException exception) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException exception) {
            throw new SQLException();
        } finally {
            closeConnection(conn);
        }
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

    private List<Endereco> serializeResultSet(ResultSet rs) {
        try {
            List<Endereco> list = new ArrayList<>();
            while (rs.next()) {
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
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}