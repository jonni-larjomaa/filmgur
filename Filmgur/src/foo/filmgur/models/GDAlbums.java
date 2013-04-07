package foo.filmgur.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

public class GDAlbums {

	private static final String TAG = "filmgur";
	
	private static final String MIME = "mimeType='application/vnd.google-apps.folder'";
	private static final String FILTER = "items(downloadUrl,mimeType,title)";
	
	private String token;
	
	public GDAlbums(String access_token){
		token = access_token;
	}
	
	
	public List<String> fetchAlbums(){
		
		// build query uri.
		Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
		ub.path("/drive/v2/files");
		ub.appendQueryParameter("q", MIME);
		ub.appendQueryParameter("fields", FILTER);
		
		Log.d(TAG,"URL to request: "+ub.build().toString());
		
		// make httpget mehtod. and add auhtorization header..
		HttpGet get = new HttpGet(ub.build().toString());
		get.addHeader("Authorization", "Bearer "+token);
		
		Log.d(TAG,"Request: "+get.getRequestLine());
		
		// create client to execute the request
		HttpClient client = new DefaultHttpClient();
		
		try {
			return parseJSON(client.execute(get, new BasicResponseHandler()));
		} catch (ClientProtocolException e) {
			Log.e(TAG,"Bad Request: "+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,"could read from network");
		}
		return null;
	}
	
	protected List<String> parseJSON(String json){
		
		List<String> tmp = new ArrayList<String>();
		try {
			JSONObject job = new JSONObject(json);
			JSONArray items = job.getJSONArray("items");
			
			for(int i=0;i < items.length();i++){
				JSONObject pic = items.getJSONObject(i);
				tmp.add(pic.getString("title"));
			}
			return tmp;
		} catch (JSONException e) {
			Log.i(TAG,"Something bad happened with json: "+e.getMessage());
		}
		return null;
	}
	
	protected void createAlbum(String albumname){
			
		// build query uri.
		Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
		ub.path("/drive/v2/files");
		ub.appendQueryParameter("fields", FILTER);
		
		Log.d(TAG,"URL to request: "+ub.build().toString());
		
		try {
			JSONObject job = new JSONObject();
			job.put("title", albumname);
			job.put("mimeType", MIME);
			
			
			// make httpget mehtod. and add auhtorization header..
			HttpPost post = new HttpPost(ub.build().toString());
			post.addHeader("Authorization", "Bearer "+token);
			
			StringEntity entity = new StringEntity(job.toString(),HTTP.UTF_8);
			entity.setContentType("application/json");
			post.setEntity(entity);
			
			Log.d(TAG,"Request: "+post.getRequestLine());
			
			// create client to execute the request
			HttpClient client = new DefaultHttpClient();
			
			client.execute(post, new BasicResponseHandler());
			
		} catch (ClientProtocolException e) {
			Log.e(TAG,"Bad Request: "+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,"could read from network");
		} catch (JSONException e) {
			Log.e(TAG,"Something bad happened with json: "+e.getMessage());
		}
	}
	
	
}
