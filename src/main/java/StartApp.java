import views.MainScreen;

import javax.swing.*;

public class StartApp {


	/*
	 * Estrutura do projeto:
	 *
	 *      Package configurations - Contém classe com as variáveis de
	 *      configuração do jdbc (senha, url, etc).
	 *
	 *      Package views - Contém classes de configuração dos componentes
	 *      e comportamento dos botões.
	 *
	 *      Package services - Contém classe que executa regra de negócio e
	 *      comunica com a camada repository
	 *
	 *      Package repository - Contém a classe que vai fazer a comunicação
	 *      com o banco de dados
	 *
	 *      Package models - Contém as classes de representação das entidades
	 *
	 *      Package enums - Contém um fake enum (classe com propriedades static)
	 *      utilizado no modal de confirmação
	 *
	 *      IDE utilizada: IDEA Intellij Community Edition (Swing UI Designer -> GUI Form)
	 *
	 *      >>>     Para modificar os componentes visualmente       <<<
	 *      >>>     é necessário utilizar a IDE IDEA Intellij       <<<
	 *      >>>     Os .forms não são compatíveis com o netbeans    <<<
	 * */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tabela CRUD");
		frame.setSize(680, 680);
		frame.setContentPane(new MainScreen().getMainPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null); // Seta a janela para o meio da tela
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
