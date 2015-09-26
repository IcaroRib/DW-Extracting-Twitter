import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.IOUtils;


public class MapSearch {
    
    public ArrayList<String> procurarLocais(double lat, double lon){
    	
    	String link = "https://maps.googleapis.com/maps/api/geocode/json?address="+lat+","+lon+"&key=AIzaSyA9fw7bHztDsAGqH1b8n3MLuICDQSykeEY";    	
    	ArrayList<String> locais = new ArrayList<String>();
    	
    	try {
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
			IOUtils.copy(conn.getInputStream(), output);
			output.close();
			JSONObject my_obj = new JSONObject(output.toString());
			JSONArray arrayResult = my_obj.getJSONArray("results");
			
			JSONArray arrayLocal = arrayResult.getJSONObject(0).getJSONArray("address_components");
			JSONObject objLocal;
			String cidade = "",estado = "",pais="";
			
			for (int i = 0; i < arrayLocal.length(); i++) {
				 objLocal = arrayLocal.getJSONObject(i);			
				
				if(objLocal.getString("types").toString().contains("administrative_area_level_2")){
					cidade = objLocal.getString("long_name");										
				}
				else if(objLocal.getString("types").toString().contains("administrative_area_level_1")){
					estado = objLocal.getString("long_name");
				}
				else if(objLocal.getString("types").toString().contains("country")){
					pais = objLocal.getString("long_name");
				}
				
			}
			
			locais.add(cidade);
	    	locais.add(estado);
	    	locais.add(pais);
			
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    	
		return locais;
    	
    }

}
