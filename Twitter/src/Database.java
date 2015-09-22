import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
	
	private Connection conn;
	
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
	
	public void insertAutor(Post post) throws SQLException{
		
		User novoAutor = this.procurarUsuario(post.getAutor());

		if (novoAutor == null) {
			
			java.sql.Date sqlDate = new java.sql.Date (post.getAutor().getDataNascimento().getTime());
			String query = "INSERT INTO usuario_twitter (id_usuario_twitter, nome, qtd_seguidores, data_nascimento) VALUES ( '" + post.getAutor().getId() +				
					 "', '" + post.getAutor().getNome() +
					 "', '" + post.getAutor().getQtdSeguidores() + "' , '" + sqlDate + "')";
			Statement st = ((Connection) conn).createStatement();
			System.out.println(query);
			st.execute(query);
			
		}
		
		else{
			
			if(novoAutor.getQtdSeguidores() != post.getAutor().getQtdSeguidores()){
				
				String query = "UPDATE usuario_twitter SET qtd_seguidores = '" + post.getAutor().getQtdSeguidores()	+
								"' WHERE id_usuario_twitter = ' " + post.getAutor().getId() + "'";
				
				Statement st = ((Connection) conn).createStatement();
				System.out.println(query);
				st.executeUpdate(query);
				
			}				
		}
		
	}
	
	public void inserirPost(Post post){
		
		conn = ConnectionDB.connect();

		try {
			
			this.insertAutor(post);
			//String query 
			
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			ConnectionDB.desconnect();
		}	
		
	}

}
