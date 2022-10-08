package models;

import javax.swing.table.DefaultTableModel;

public class Endereco {

	private String CEP;
	private String rua;
	private String bairro;
	private String cidade;
	private String uf;

	/* CUSTOM METHODS */
	public Object[] getModelObject() {
		return new Object[]{
			 this.CEP,
			 this.rua,
			 this.bairro,
			 this.cidade,
			 this.uf,
			 };
	}

	public boolean isInvalid(){
		Object[] array = this.getModelObject();

		for (Object item:
		array ) {
			if (item.equals("")){
				return true;
			}
		}
		return false;
	}

	/* CONSTRUTOR */
	public Endereco(String CEP, String rua, String bairro, String cidade, String uf) {
		this.CEP = CEP;
		this.rua = rua;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
	}

	public Endereco(DefaultTableModel dados, int selectedRow) {
		this.setDataOnSelectedRow(dados, selectedRow);
	}

	public void setDataOnSelectedRow (DefaultTableModel dados, int selectedRow) {
		this.setCEP((String) dados.getValueAt(selectedRow, 0));
		this.setRua((String) dados.getValueAt(selectedRow, 1));
		this.setBairro((String) dados.getValueAt(selectedRow, 2));
		this.setCidade((String) dados.getValueAt(selectedRow, 3));
		this.setUf((String) dados.getValueAt(selectedRow, 4));
	}

	/* SETTERS & GETTERS */
	public String getCEP() {
		return CEP;
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
}
