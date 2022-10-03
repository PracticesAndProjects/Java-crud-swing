package services;

import repository.EnderecoRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnderecoService {


	public void setTableStructure(JTable table) {
		try {
			this.dados.addColumn("CEP");
			this.dados.addColumn("Rua");
			this.dados.addColumn("Bairro");
			this.dados.addColumn("Cidade");
			this.dados.addColumn("UF");
			table.setModel(dados);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		} catch (Exception exception){
			exception.printStackTrace();
			showErrorMessage("Não foi possível construir o modelo da tabela!");
		}
	}

	public void searchAddress(String searchParam) {
		ResultSet result = enderecoRepository.searchAdress(searchParam);

		if (result == null) {
			showErrorMessage("Falha ao pesquisar registros");
		} else {
			dados.setRowCount(0);
			try {
				while (result.next()) {
					String[] rowToAdd = {
						 result.getString("CEP"),
						 /* TODO: Refatorar para montar uma lista de Endereco no repository e receber aqui */
						 result.getString("RUA"),
						 result.getString("BAIRRO"),
						 result.getString("CIDADE"),
						 result.getString("UF"),
						 };
					dados.addRow(rowToAdd);
				}
					result.close();
			} catch (SQLException exception) {
				exception.printStackTrace();
				showErrorMessage("Falha ao pesquisar registros");
			}

		}
	}

	;


	public void showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(
			 null, msg, "Erro", JOptionPane.ERROR_MESSAGE
		);
	}

	public void showWarningMessage(String msg) {
		JOptionPane.showMessageDialog(
			 null, msg, "Aviso", JOptionPane.WARNING_MESSAGE
		);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(
			 null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE
		);
	}

	private EnderecoRepository enderecoRepository;
	private int                selectedRow;
	private DefaultTableModel  dados;


	/* Constructors */
	public EnderecoService(EnderecoRepository enderecoRepository, DefaultTableModel dados) {
		this.dados = dados;
		this.enderecoRepository = enderecoRepository;
	}

	/* Getters & Setters */
	public EnderecoRepository getRepository() {
		return enderecoRepository;
	}

	public void setRepository(EnderecoRepository enderecoRepository) {
		this.enderecoRepository = enderecoRepository;
	}

	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public DefaultTableModel getDados() {
		return dados;
	}

	public void setDados(DefaultTableModel dados) {
		this.dados = dados;
	}
}
