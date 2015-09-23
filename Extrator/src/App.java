import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class App {
	
	static public Post setRecursivo(Status status){
		
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
		post.setHashtag("#dilma");
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
			post.setPostOrigin(App.setRecursivo(status.getQuotedStatus()));			
		}
		else{
			post.setIdTweetOrigin(null);
		}
		
		return post;
		
	}
			
	public static void main(String[] args) {
		Twitter twitter = TwitterFactory.getSingleton();
		Database db = new Database();
		
	    Query query = new Query("#Dilma");
	    query.setSince("2015-01-01");
	    query.setUntil("2015-09-11");
	    query.setCount(106);
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
			
				
				for (Status status : result.getTweets()) {
					
					Post post = App.setRecursivo(status);
					//postsSelecionados.add(post);
					db.inserirPost(post);
					maxId = post.getId();	
					System.out.println();
					System.out.println("-------");
					System.out.println();
					
					
				}
			
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	}

}
