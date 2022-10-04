package services;

import models.Endereco;
import repository.EnderecoRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
		} catch (Exception exception) {
			exception.printStackTrace();
			showErrorMessage("Não foi possível construir o modelo da tabela!");
		}
	}

	public boolean createAddress(Endereco endereco){
		Endereco createdAddress = this.enderecoRepository.createAddress(endereco);

		try{
			if (createdAddress == null) throw new Exception();

			dados.addRow(endereco.getModelObject());
			return true;
		}catch (Exception exception){
			exception.printStackTrace();
			showErrorMessage("Não foi possível adicionar um novo endereço");
			return false;
		}

	}

	public void searchAddress(String searchParam) {
		List<Endereco> list = enderecoRepository.searchAdress(searchParam);

		try {
			if (list == null) {
				throw new Exception();
			} else {
				dados.setRowCount(0);

				for (Endereco item : list
				) {
					dados.addRow(item.getModelObject());
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			showErrorMessage("Falha ao pesquisar registros");
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
