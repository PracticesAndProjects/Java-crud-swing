package services;

import models.Endereco;
import repository.EnderecoRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

	///////////////////////////////////////////////
	/////////////// OPERAÇÕES CRUD ////////////////
	///////////////////////////////////////////////
	public void getAddressList(DefaultTableModel dados) throws Exception {
		List<Endereco> listaInicial = enderecoRepository.getAddressList();
		for(Endereco endereco : listaInicial){
			dados.addRow(endereco.getModelObject());
		}
	}

	public void createAddress(Endereco endereco) throws Exception {
		Endereco createdAddress = this.enderecoRepository.createAddress(endereco);
		dados.addRow(endereco.getModelObject());
	}

	public void patchAddress(Endereco endereco) throws Exception {
		enderecoRepository.patchAddress(endereco, selectedRow, dados);
		endereco.setDataOnSelectedRow(dados, selectedRow);
	}

	public void searchAddress(String searchParam) throws Exception {
		List<Endereco> list = enderecoRepository.searchAdress(searchParam);

		if (list == null) {
			throw new Exception();
		} else {
			dados.setRowCount(0);

			for (Endereco item : list
			) {
				dados.addRow(item.getModelObject());
			}
		}
	}

	public void deleteAddress(DefaultTableModel dados, int selectedRow) throws Exception {
		Endereco endToDelete = new Endereco(dados, selectedRow);
		enderecoRepository.deleteAddress(endToDelete);
		dados.removeRow(selectedRow);
	}

	///////////////////////////////////////////////
	///////////// FIM OPERAÇÕES CRUD //////////////
	///////////////////////////////////////////////


	/* Métodos helpers */
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

	public int showConfirmationDialog(String msg){
		return JOptionPane.showConfirmDialog(null, msg);
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
