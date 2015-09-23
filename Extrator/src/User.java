import java.util.Date;


public class User {
	
	private long id;
	private String nome;
	private int qtdSeguidores;
	private Date dataNascimento;
	private String local;
	private int qtdAmigos;
	
	
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public int getQtdAmigos() {
		return qtdAmigos;
	}
	public void setQtdAmigos(int i) {
		this.qtdAmigos = i;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getQtdSeguidores() {
		return qtdSeguidores;
	}
	public void setQtdSeguidores(int qtdSeguidores) {
		this.qtdSeguidores = qtdSeguidores;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date date) {
		this.dataNascimento = date;
	}
		
	
}
