import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.IOUtils;
import com.google.gson.JsonObject;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class App {
	
	public static void main(String[] args) throws InterruptedException {
		
		Extrator ex = new Extrator();
		ex.gerarConexao();
		while(true){
			try{				
				if(ex.ExtrairTweets() == false){
					Thread.currentThread().sleep(2000);
					System.out.println("--------------- Todas as hashtags foram baixadas ------------- ");
					Thread.currentThread().sleep(2000);
					System.out.println("..................");
					break;
				}
			}
			catch(Exception e){ 
				e.printStackTrace();
				Thread.currentThread().sleep(2000);
				continue;
			}
		}   
	}

}
