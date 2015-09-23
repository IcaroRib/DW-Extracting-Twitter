import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class Extrator {
	
	private String[] hashTags = {"#foradilma","#dilmagolpista","#dilmafica","#pt",""};
	private ConfigurationBuilder builder = new ConfigurationBuilder();
	
	public Post setRecursivo(Status status, String hashtag){
		
		User autor = new User();
		if(status.getUser() != null){
			autor.setDataNascimento(status.getUser().getCreatedAt());
			autor.setId(status.getUser().getId());
			autor.setNome(status.getUser().getName().replace("'", ""));
			autor.setQtdSeguidores(status.getUser().getFollowersCount());
			autor.setQtdAmigos(status.getUser().getFriendsCount());
			autor.setLocal(status.getUser().getLocation().replace("'", ""));		
		}
		
		Post post = new Post();
		post.setAssunto("Politica");
		post.setHashtag(hashtag);
		post.setConteudo(status.getText().replace("'", ""));
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

	public Twitter gerarConexao(){

		builder.setOAuthAccessToken("500104909-6Qxa6wQnjrxXVxPgCrxQdn4WGOv3yTQRd6Ma14jE");
		builder.setOAuthAccessTokenSecret("YcXUwhngD4V4ArNCJQ0I7DFk68ra7s40NZZACXJS5qLn0");
		builder.setOAuthConsumerKey("WHd1P7KT3vMMSb6SM4vEHY36J");
		builder.setOAuthConsumerSecret("efntb5HjIRp5XutfLskvZmH5k9Fk7PTkQ6MEYozVlTJHSzkOSA");
		
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		return factory.getInstance();
	}
	
	public void ExtrairTweets(){
		Twitter twitter = this.gerarConexao();		
		Database db = new Database();
		
		for (String hashTag : hashTags) {
			
			Query query = new Query(hashTag);
		    query.setSince("2015-01-01");
		    if (hashTag.equals("#foradilma")){
		    query.setUntil("2015-09-09");}
		    QueryResult result;
		    long maxId = 0;
			try {
				
				for (int i = 0; i < 1000; i++) {			
					
					query.setCount(100);
					if(i !=0){
						query.setMaxId(maxId);}
					
					result = twitter.search(query);;
					ArrayList<Post> postsSelecionados = new ArrayList<Post>();
					System.out.println(result.getCount() * (i+1));
					
					if(result.getTweets().size() == 0){
						break;
					}
					
					for (Status status : result.getTweets()) {
						
						Post post = this.setRecursivo(status,hashTag);
						db.inserirPost(post);
						maxId = post.getId();	
						System.out.println();
						System.out.println("-------");
						System.out.println();
						
						
					}
				
				}
			} catch (TwitterException e) {
				continue;
			}
			
		}
		
		
	}



}
