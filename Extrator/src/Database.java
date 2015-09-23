import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Database {
	
	private Connection conn = ConnectionDB.connect();;
	
	public User procurarUsuario(User user) throws SQLException{
		
		//String query = "SELECT * FROM usuario INNER JOIN id_local_origem WHERE id_local_origem = id_local and id_usuario_twitter = " + user.getId();
		String query = "SELECT * FROM usuario_twitter WHERE id_usuario_twitter = " + user.getId();
		Statement st = ((Connection) conn).createStatement();
		System.out.println(query);
		ResultSet rs = st.executeQuery(query);	
		
		User selectedUser = new User(); 
		while(rs.next()){
			selectedUser.setId(rs.getLong("id_usuario_twitter"));
			selectedUser.setDataNascimento(rs.getDate("data_nascimento"));
			//selectedUser.setLocal(rs.getString("cidade") + " " + rs.getString("estado") + " " + rs.getString("pais"));
			selectedUser.setNome(rs.getString("nome"));
			selectedUser.setQtdSeguidores(rs.getInt("qtd_seguidores"));
			return selectedUser;			
		}	
		
		return null;
		
	}
	
	public void insertAutor(User user) throws SQLException{
		
		User novoAutor = this.procurarUsuario(user);

		if (novoAutor == null) {
			
			java.sql.Date sqlDate = new java.sql.Date (user.getDataNascimento().getTime());
			String query = "INSERT INTO usuario_twitter (id_usuario_twitter, nome, qtd_seguidores, data_nascimento) VALUES ( '" + user.getId() +				
					 "', '" + user.getNome() +
					 "', '" + user.getQtdSeguidores() + "' , '" + sqlDate + "')";
			Statement st = ((Connection) conn).createStatement();
			System.out.println(query);
			st.execute(query);
			
		}
		
		else{
			
			if(novoAutor.getQtdSeguidores() != user.getQtdSeguidores()){
				
				String query = "UPDATE usuario_twitter SET qtd_seguidores = '" + user.getQtdSeguidores()	+
								"' WHERE id_usuario_twitter = ' " + user.getId() + "'";
				
				Statement st = ((Connection) conn).createStatement();
				System.out.println(query);
				st.executeUpdate(query);
				
			}				
		}
		
	}
	
	public long procurarPost(Post post) throws SQLException{
		
		String query = "SELECT id_post FROM post WHERE id_post = " + post.getId();
		Statement st = ((Connection) conn).createStatement();
		System.out.println(query);
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()){
			return rs.getLong("id_post");
		}		
		
		return 0;
	}
	
	public void inserirPost(Post post){
		
		try {
			
			this.insertAutor(post.getAutor());			
			long idAssunto = this.insertAssunto(post);
			Timestamp sqlTime = new Timestamp(post.getData().getTime());
			java.sql.Date sqlDate = new java.sql.Date (post.getData().getTime());
			
			if(post.getIdTweetOrigin() != null){
				this.inserirPost(post.getPostOrigin());
				
				if(this.procurarPost(post) == 0){
				
					String query = "INSERT INTO post (id_post, texto, data, hora, id_usuario_twitter,qtd_likes,qtd_retweets,id_tweet_orig,id_assunto) "+ ""
							+ "VALUES ( '" + post.getId() 
							+ "', '" + post.getConteudo()
							+ "', '" + sqlDate 
							+ "', '" + sqlTime
							+ "', '" + post.getAutor().getId()
							+ "', '" + post.getQtdLikes()
							+ "', '" + post.getQtdRetweets()
							+ "', '" + post.getIdTweetOrigin()
							+ "' , '" + idAssunto + "')";
					
					Statement st = ((Connection) conn).createStatement();
					System.out.println(query);
					st.execute(query); 
				}
			}
			
			else{
			
				if(this.procurarPost(post) == 0){
					String query = "INSERT INTO post (id_post, texto, data, hora, id_usuario_twitter,qtd_likes,qtd_retweets,id_assunto) "+ ""
							+ "VALUES ( '" + post.getId() 
							+ "', '" + post.getConteudo()
							+ "', '" + sqlDate 
							+ "', '" + sqlTime
							+ "', '" + post.getAutor().getId()
							+ "', '" + post.getQtdLikes()
							+ "', '" + post.getQtdRetweets()
							+ "' , '" + idAssunto + "')";
					
					Statement st = ((Connection) conn).createStatement();
					System.out.println(query);
					st.execute(query); 
				}
			}
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			ConnectionDB.desconnect();
		}	
		
	}

	private int insertAssunto(Post post) throws SQLException {
		String query = "SELECT * FROM assunto_twitter WHERE hashtag = '" + post.getHashtag() + "' and assunto = '" +post.getAssunto()  + "'";
		Statement st = ((Connection) conn).createStatement();
		System.out.println(query);
		ResultSet rs = st.executeQuery(query);	
		
		while(rs.next()){
			return rs.getInt("id_assunto_twitter");
		}
		query = "INSERT INTO assunto_twitter(hashtag,assunto) VALUES ( '" + post.getHashtag() + "', '" + post.getAssunto() + "')";
		System.out.println(query);
		return st.executeUpdate(query);
	}
	
}
