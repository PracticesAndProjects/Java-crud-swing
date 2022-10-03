import extensions.CustomTableModel;
import models.Endereco;
import repository.EnderecoRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataInserterScreen {
	private CustomTableModel dados;

	public DataInserterScreen(CustomTableModel dados, boolean isEditing, EnderecoRepository enderecoRepository) {
		/* Seta se o modal é de edição */
		this.isEditing = isEditing;

		/* Modelo de dados a ser manipulado (vem da MainScreen) */
		this.dados = dados;

		/* Evento de salvar registro */
		salvarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				/* Gera objeto com os valores dos inputs*/
				Endereco endereco = new Endereco(cepField.getText(), ruaField.getText(), bairroField.getText(), cidadeField.getText(), ufField.getText());

				/* Verifica se algum deles está vazio e para a execução, se sim, mostrando aviso */
				for (Object text : endereco.getModelObject()) {
					if (text.equals("")) {
						JOptionPane.showMessageDialog(null, "Campos vazios");
						return;
					}
				}

				boolean failed;
				if (isEditing) {
					failed = enderecoRepository.patchAddress(dados, endereco, selectedRow);

				} else {
					/* se não estiverem vazio, adiciona o registro */
					failed = enderecoRepository.createAddress(dados, endereco);
				}

				/* Procura a janela pai desse panel e faz o dispose da mesma */
				disposeFrame();

				/* Aviso de dado salvo com sucesso */
				if (!failed) saveSuccessDialog();
				if (failed) saveFailedDialog();
			}
		});
	}

	private void disposeFrame() {
		/* Procura a janela pai desse panel e faz o dispose da mesma */
		JFrame topLevel = (JFrame) mainPanel.getTopLevelAncestor();
		topLevel.setVisible(false);
		topLevel.dispose();
	}

	private void saveSuccessDialog() {
		JOptionPane.showMessageDialog(null, "Dados salvos com " +
			 "sucesso!");
	}

	private void saveFailedDialog() {
		JOptionPane.showMessageDialog(null, "Dados não puderam ser salvos!", "Erro", JOptionPane.WARNING_MESSAGE);
	}

	public void setFieldsOnEdit(Endereco endereco) {
		getCepField().setText(endereco.getCEP());
		getRuaField().setText(endereco.getRua());
		getBairroField().setText(endereco.getBairro());
		getCidadeField().setText(endereco.getCidade());
		getUfField().setText(endereco.getUf());
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	private int        selectedRow;
	private boolean    isEditing;
	private JPanel     mainPanel;
	private JTextField cepField;
	private JButton    salvarButton;
	private JTextField ruaField;
	private JTextField bairroField;
	private JTextField ufField;
	private JTextField cidadeField;


	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public JTextField getCepField() {
		return cepField;
	}

	public void setCepField(JTextField cepField) {
		this.cepField = cepField;
	}

	public JTextField getRuaField() {
		return ruaField;
	}

	public void setRuaField(JTextField ruaField) {
		this.ruaField = ruaField;
	}

	public JTextField getBairroField() {
		return bairroField;
	}

	public void setBairroField(JTextField bairroField) {
		this.bairroField = bairroField;
	}

	public JTextField getUfField() {
		return ufField;
	}

	public void setUfField(JTextField ufField) {
		this.ufField = ufField;
	}

	public JTextField getCidadeField() {
		return cidadeField;
	}

	public void setCidadeField(JTextField cidadeField) {
		this.cidadeField = cidadeField;
	}
}
