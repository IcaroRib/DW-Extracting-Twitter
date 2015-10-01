import java.io.UnsupportedEncodingException;
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
	
	private String[] hashTags = {"#aecio","#lula","#golpeNÃO","#dilmaralasuamandada", "#dilmagolpista","#dilmafica","#pt","#dilma","#foradilma"};
	//private String[] hashTags = {"#dilmaralasuamandada"};
	private ConfigurationBuilder builder = new ConfigurationBuilder();
	private Twitter twitter;
	Database db = new Database();
	
	public Post setRecursivo(Status status, String hashtag){
		
		User autor = new User();
		if(status.getUser() != null){
			autor.setDataNascimento(status.getUser().getCreatedAt());
			autor.setId(status.getUser().getId());
			autor.setNome(status.getUser().getName().replace("'", "").replaceAll("\\P{Print}", ""));
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
	
	public boolean ExtrairTweets() throws UnsupportedEncodingException, TwitterException{	
		
		for (String hashTag : hashTags) {	
			System.out.println(hashTag);
			Query query = new Query(hashTag);
		    query.setSince("2015-01-01");
		    QueryResult result;
		    long maxId = 0;
		    int cont = 0;
			try {
				
				for (int i = 0; i < 1000; i++) {
					
					if (cont > 3){break;}
					
					query.setCount(100);
					if(i !=0){
						query.setMaxId(maxId);}
					
					result = twitter.search(query);;
					ArrayList<Post> postsSelecionados = new ArrayList<Post>();
					System.out.println(result.getCount() * (i+1));
					
					//System.out.println(result.getTweets().size());
					if(result.getTweets().size() == 0){
						break;
					}
					
					
					for (Status status : result.getTweets()) {
						Post post = this.setRecursivo(status,hashTag);
						cont += db.inserirPost(post);
						maxId = post.getId();	
						System.out.println();
						System.out.println("-------");
						System.out.println();
						
						if (cont > 3){break;}
						
						
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
