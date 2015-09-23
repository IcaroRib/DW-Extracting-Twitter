import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;


public class ConnectionDB {
	
	private static String url = "jdbc:mysql://localhost/twitter";
	private static String username = "root";
	private static String password = "JME.megasin-02";
	
	static public java.sql.Connection connect(){
		
		java.sql.Connection conexao = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conexao = DriverManager.getConnection(url, username, password);
		}
		
		catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conexao;
		
	}
	
	public static boolean desconnect() { 
		
		try { 
			ConnectionDB.connect().close(); 
			return true; 
		} 
		
		catch (SQLException e) { 
			
			return false; 
			
		} 
		
	} 
	
}