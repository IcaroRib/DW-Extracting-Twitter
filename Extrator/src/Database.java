import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;


public class Database {
	
	private Connection conn = ConnectionDB.connect();;
	
	public User procurarUsuario(User user) throws SQLException{
		
		String query = "SELECT * FROM usuario_twitter WHERE id_usuario_twitter = " + user.getId();
		Statement st = ((Connection) conn).createStatement();
		System.out.println(query);
		ResultSet rs = st.executeQuery(query);	
		
		User selectedUser = new User(); 
		while(rs.next()){
			selectedUser.setId(rs.getLong("id_usuario_twitter"));
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
			
			String query = "INSERT INTO usuario_twitter (id_usuario_twitter, nome, qtd_seguidores) VALUES ( '" + user.getId() +				
					 "', '" + user.getNome() +
					 "', '" + user.getQtdSeguidores() + "')";
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
	
	public int inserirPost(Post post) throws UnsupportedEncodingException{
		
		try {
			
			this.insertAutor(post.getAutor());			
			int idhashtag = this.insertHashtag(post);
			int idLocal = 1;
			if(post.getLocal() != null){
				idLocal = this.insertLugar(post);
			}
			Timestamp sqlTime = new Timestamp(post.getData().getTime());
			java.sql.Date sqlDate = new java.sql.Date (post.getData().getTime());
			
			if(post.getIdTweetOrigin() != null){
				this.inserirPost(post.getPostOrigin());
				
				if(this.procurarPost(post) == 0){
				
					String query = "INSERT INTO post (id_post, texto, data, hora, id_usuario_twitter,qtd_likes,qtd_retweets,id_tweet_orig,id_local,hashtag_idhashtag) "+ ""
							+ "VALUES ( '" + post.getId() 
							+ "', '" + post.getConteudo()
							+ "', '" + sqlDate 
							+ "', '" + sqlTime
							+ "', '" + post.getAutor().getId()
							+ "', '" + post.getQtdLikes()
							+ "', '" + post.getQtdRetweets()
							+ "', '" + post.getIdTweetOrigin()
							+ "', '" + idLocal
							+ "' , '" + idhashtag + "')";
					
					Statement st = ((Connection) conn).createStatement();
					System.out.println(query);
					st.execute(query); 
					
					return 1;
				}
			}
			
			else{
			
				if(this.procurarPost(post) == 0){
					String query = "INSERT INTO post (id_post, texto, data, hora, id_usuario_twitter,qtd_likes,qtd_retweets,id_local,hashtag_idhashtag) "+ ""
							+ "VALUES ( '" + post.getId() 
							+ "', '" + post.getConteudo()
							+ "', '" + sqlDate 
							+ "', '" + sqlTime
							+ "', '" + post.getAutor().getId()
							+ "', '" + post.getQtdLikes()
							+ "', '" + post.getQtdRetweets()
							+ "', '" + idLocal
							+ "' , '" + idhashtag + "')";
					
					Statement st = ((Connection) conn).createStatement();
					System.out.println(query);
					st.execute(query); 
					
					return 1;
				}
			}
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			ConnectionDB.desconnect();
		}
		return 0;	
		
	}

	private int insertHashtag(Post post) throws SQLException {
		String query = "SELECT * FROM hashtag WHERE hashtag = '" + post.getHashtag() + "'";
		Statement st = ((Connection) conn).createStatement();
		System.out.println(query);
		ResultSet rs = st.executeQuery(query);	
		
		while(rs.next()){
			return rs.getInt("idhashtag");
		}
		query = "INSERT INTO hashtag(hashtag) VALUES ( '" + post.getHashtag() + "')";
		System.out.println(query);
		int idhashtag = st.executeUpdate(query);
		query = "INSERT INTO hashtag_has_assunto_twitter(hashtag_idhashtag,assunto_twitter_id_assunto_twitter) VALUES ( '" + idhashtag + "', ' 1')";
		System.out.println(query);
		st.executeUpdate(query);
		
		return idhashtag;
	}
	
	private int insertLugar(Post post) throws SQLException{
		String query = "SELECT * FROM local WHERE latitude = '" + post.getLocal().getLatitude() + "' and longitude = '" + post.getLocal().getLongitude() + "'";
		Statement st = ((Connection) conn).createStatement();
		System.out.println(query);
		ResultSet rs = st.executeQuery(query);	
		
		while(rs.next()){
			return rs.getInt("id_local");
		}
		MapSearch ms = new MapSearch();
		ArrayList<String> locais = ms.procurarLocais(post.getLocal().getLatitude(), post.getLocal().getLongitude());
		query = "INSERT INTO local(cidade,estado,pais,latitude,longitude) VALUES ('" + locais.get(0)
				+ "','" + locais.get(1)
				+ "','" + locais.get(2)
				+ "','" + post.getLocal().getLatitude()
				+ "','" + post.getLocal().getLongitude()
				+ "')";
		System.out.println(query);
		return st.executeUpdate(query);		
		
	}
	
}
