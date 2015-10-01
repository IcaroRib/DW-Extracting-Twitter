import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class Extrator {
	
	private String[] hashTags = {"#dilma","#foradilma","#pt","#aecio","#lula","#golpeNÃO","#dilmaralasuamandada", "#dilmagolpista","#dilmafica"};
	//private String[] hashTags = {"#dilmaralasuamandada"};
	private ConfigurationBuilder builder = new ConfigurationBuilder();
	private Twitter twitter;
	private ArrayList<String> arrayIds = new ArrayList<String>();
	private Database db = new Database();
	
	public void procurarIds() throws IOException{
		
		BufferedReader buffRead = new BufferedReader(new FileReader("C:/Users/Icaro/Desktop/workspace_dw/trunk")); 
		String linha = ""; 
		while (true) { 
			if (linha != null) { 
				arrayIds.add(linha);
				} 
			else {
				break; }
			linha = buffRead.readLine(); } 
		buffRead.close();		
	}
	
	public Post setRecursivo(Status status, String hashtag){
		
		User autor = new User();
		if(status.getUser() != null){
			autor.setDataNascimento(status.getUser().getCreatedAt());
			autor.setId(status.getUser().getId());
			autor.setNome(status.getUser().getName().replace("'", "").replaceAll("\\p{C}", ""));
			autor.setQtdSeguidores(status.getUser().getFollowersCount());
			autor.setQtdAmigos(status.getUser().getFriendsCount());
			autor.setLocal(status.getUser().getLocation().replace("'", ""));
		}
		
		Post post = new Post();
		post.setAssunto("Politica");
		post.setHashtag(hashtag);
		post.setConteudo(status.getText().replace("'", "").replaceAll("\\p{C}", ""));
		post.setAutor(autor);
		post.setId(status.getId());
		post.setData(status.getCreatedAt());
		post.setLocal(status.getGeoLocation());
		post.setFonte(status.getSource());
		post.setQtdRetweets(status.getRetweetCount());
		post.setQtdLikes(status.getFavoriteCount());
		post.setQtdRepostas(0);
		post.setQtdCitacoes(0);
		post.setSentimento("");
		
		if(status.getQuotedStatusId() > -1 && status.getQuotedStatus() != null){
			post.setPostOrigin(this.setRecursivo(status.getQuotedStatus(),hashtag));			
		}
		else{
			post.setIdTweetOrigin(null);
		}
		
		return post;
		
	}

	public void gerarConexao(){

		builder.setOAuthAccessToken("500104909-6Qxa6wQnjrxXVxPgCrxQdn4WGOv3yTQRd6Ma14jE");
		builder.setOAuthAccessTokenSecret("YcXUwhngD4V4ArNCJQ0I7DFk68ra7s40NZZACXJS5qLn0");
		builder.setOAuthConsumerKey("WHd1P7KT3vMMSb6SM4vEHY36J");
		builder.setOAuthConsumerSecret("efntb5HjIRp5XutfLskvZmH5k9Fk7PTkQ6MEYozVlTJHSzkOSA");
		
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();
	}
	
	
	public boolean RecuperarTweets(){		
		
		for (String id : arrayIds) {	
			System.out.println(id);
			try {
				System.out.println(id);
				Query query = new Query(id);
			    QueryResult result;
			    //toDo		    
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		return false;
		
	}
	public boolean ExtrairTweets() throws UnsupportedEncodingException, TwitterException{	
		
		for (String hashTag : hashTags) {	
			System.out.println(hashTag);
			Query query = new Query(hashTag);
		    query.setSince("2015-09-22");			
			query.setUntil(db.primeiroInserido(hashTag));
		    QueryResult result;
		    long maxId = 0;
		    int cont = 0;
		    int cont2 = 0;
			try {
			    
				for (int i = 0; i < 1000; i++) {
										
					query.setCount(100);
					if(i !=0){
						query.setMaxId(maxId);}
					
					result = twitter.search(query);;
					ArrayList<Post> postsSelecionados = new ArrayList<Post>();
					System.out.println(result.getCount() * (i+1));
					
					//System.out.println(result.getTweets().size());
					if(result.getTweets().size() == 0){
						System.out.println("Aqui");
						break;
					}
					
					
					for (Status status : result.getTweets()) {
						Post post = this.setRecursivo(status,hashTag);
						cont += db.inserirPost(post);
						maxId = post.getId();	
						System.out.println();
						System.out.println("-------");
						System.out.println();
						
						/*if (cont > 10){
							cont2 +=1;
							break;}*/
						
						
					}
				
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		return false;
		
		
	}



}
