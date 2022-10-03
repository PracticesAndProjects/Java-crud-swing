import extensions.CustomTableModel;
import models.Endereco;
import repository.EnderecoRepository;
import services.EnderecoService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen {

	// Método main, execução de aplicação
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tabela CRUD");
		frame.setContentPane(new MainScreen().mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null); // Seta a janela para o meio da tela
		frame.setVisible(true);
	}


	////////////////////////////////////////////////////////////////
	////////////////// Instâncias dos componentes //////////////////
	//////// Binding delas definidos nos arquivos xml .form ////////
	////////////////////////////////////////////////////////////////
	private JPanel             mainPanel;
	private JPanel             searchPanel;
	private JLabel             searchLabel;
	private JTextField         searchField;
	private JButton            consultarButton;
	private JPanel             buttonsPanel;
	private JButton            addBtn;
	private JButton            patchBtn;
	private JButton            deleteBtn;
	private JPanel             tablePanel;
	private JTable             mainTable;
	private JScrollPane        tableScrollPane;
	private CustomTableModel   dados;
	private EnderecoRepository enderecoRepository;
	private EnderecoService    enderecoService;


	//	Construtor
	public MainScreen() {

		/* Instancia camadas de serviço/repositorio */
		this.autowireInstances();

		/* Seta o model da tabela */
		enderecoService.setTableStructure(this.mainTable);

		/* Consulta inicial no DB */
		this.enderecoRepository.getAddressList(dados);

		/* Evento de adição de registro (Abre novo frame) */
		addBtn.addActionListener(new ActionListener() { /* Adiciona o listener ao instanciar essa classe */
			@Override
			public void actionPerformed(ActionEvent e) {
				/* Criação do frame de inserção de dados */
				JFrame dataInsertionFrame = new JFrame("Inserir dados");
				dataInsertionFrame.setContentPane(new DataInserterScreen(dados, false, enderecoRepository).getMainPanel());
				dataInsertionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dataInsertionFrame.pack();
				dataInsertionFrame.setLocationRelativeTo(null); // Seta a janela para o meio da tela
				dataInsertionFrame.setVisible(true);
			}
		});

		/* Evento de exclusão de registro */
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int selectedRow = mainTable.getSelectedRow();

				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "No row selected");
					return;
				}

				/* Abre modal de confirmação e salva o valor selecionado em
				uma var */
				Integer confirmationResult =
					 JOptionPane.showConfirmDialog(null, "O registro selecionado " +
						  "será deletado, você tem certeza disso ?");

				/* Deleta o registro se for selecionado "sim" */
				if (confirmationResult == 0) {
					enderecoRepository.deleteAddress(dados, selectedRow);
					JOptionPane.showMessageDialog(null, "Linha removida com sucesso!", "Exclusão de dados", 1);
				}
			}
		});

		/* Evento de edição de registro */
		patchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame             dataInsertionFrame = new JFrame("Inserir dados");
				DataInserterScreen dataInserterScreen = new DataInserterScreen(dados, true, enderecoRepository);

				int selectedRow = mainTable.getSelectedRow();
				dataInserterScreen.setSelectedRow(selectedRow);

				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "No row selected");
					return;
				}
				Endereco enderecoToEdit = new Endereco(
					 (String) dados.getValueAt(selectedRow, 0),
					 (String) dados.getValueAt(selectedRow, 1),
					 (String) dados.getValueAt(selectedRow, 2),
					 (String) dados.getValueAt(selectedRow, 3),
					 (String) dados.getValueAt(selectedRow, 4)
				);

				dataInserterScreen.setFieldsOnEdit(enderecoToEdit);

				dataInsertionFrame.setContentPane(dataInserterScreen.getMainPanel());
				dataInsertionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dataInsertionFrame.pack();
				dataInsertionFrame.setLocationRelativeTo(null); // Seta a janela para o meio da tela
				dataInsertionFrame.setVisible(true);

			}
		});

		consultarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enderecoService.searchAddress(searchField.getText());
			}
		});

		/* FIM Construtor */
	}

	private void autowireInstances() {
		this.dados = new CustomTableModel();
		this.enderecoRepository = new EnderecoRepository();
		this.enderecoService = new EnderecoService(this.enderecoRepository, this.dados);
	}

}