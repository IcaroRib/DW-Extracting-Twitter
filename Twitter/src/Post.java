import java.util.Date;

import twitter4j.GeoLocation;


public class Post {
	
	private Long id;
	private Long idTweetOrigin;
	private String conteudo;
	private Date data;
	private GeoLocation local;
	private String sentimento;
	private int qtdLikes;
	private int qtdRetweets;
	private int qtdCitacoes;
	private int qtdRepostas;
	private String hashtag;
	private String assunto;
	private User autor;	
	private Post postOrigin;
	private String fonte;
	
	
	
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	public GeoLocation getLocal() {
		return local;
	}
	public void setLocal(GeoLocation geoLocation) {
		this.local = geoLocation;
	}
	public User getAutor() {
		return autor;
	}
	public void setAutor(User autor) {
		this.autor = autor;
	}
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getIdTweetOrigin() {
		return idTweetOrigin;
	}
	public void setIdTweetOrigin(Long idTweetOrigin) {
		this.idTweetOrigin = idTweetOrigin;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date date) {
		this.data = date;
	}
	public String getSentimento() {
		return sentimento;
	}
	public void setSentimento(String sentimento) {
		this.sentimento = sentimento;
	}
	public int getQtdLikes() {
		return qtdLikes;
	}
	public void setQtdLikes(int qtdLikes) {
		this.qtdLikes = qtdLikes;
	}
	public int getQtdRetweets() {
		return qtdRetweets;
	}
	public void setQtdRetweets(int qtdRetweets) {
		this.qtdRetweets = qtdRetweets;
	}
	public int getQtdCitacoes() {
		return qtdCitacoes;
	}
	public void setQtdCitacoes(int qtdCitacoes) {
		this.qtdCitacoes = qtdCitacoes;
	}
	public int getQtdRepostas() {
		return qtdRepostas;
	}
	public void setQtdRepostas(int qtdRepostas) {
		this.qtdRepostas = qtdRepostas;
	}
	public Post getPostOrigin() {
		return postOrigin;
	}
	public void setPostOrigin(Post postOrigin) {
		this.postOrigin = postOrigin;
	}
	
	
}
