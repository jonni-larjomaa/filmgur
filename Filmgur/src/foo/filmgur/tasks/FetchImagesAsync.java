package foo.filmgur.tasks;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import foo.filmgur.models.GDImage;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class FetchImagesAsync extends AsyncTask<Void, Void, List<GDImage>> {
	
	private static final String TAG = "filmgur";
	
	private ArrayAdapter<GDImage> ad;
	private String token;
	private String parentId;
	
	public FetchImagesAsync(ArrayAdapter<GDImage> ad, String token, String parentId){
		super();
		this.ad = ad;
		this.token = token;
		this.parentId = parentId;
	}
	
	@Override
	protected List<GDImage> doInBackground(Void... a) {
		return fetchimages();
	}

	@Override
	protected void onPostExecute(final List<GDImage> result) {
		 if(!result.isEmpty()){
			 if(!ad.isEmpty()){
				 ad.clear();
				 Log.i(TAG,"Reset the images listadapter");
			 }
			 Log.i(TAG,"Got: "+result.size()+" images");
			 ad.addAll(result);
			 ad.notifyDataSetChanged();
		 }
	}
	
	public List<GDImage> fetchimages(){
		
		// build query uri.
		Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
		ub.path("/drive/v2/files");
		ub.appendQueryParameter("q", "mimeType='"+GDImage.MIME+"'and '"+parentId+"' in parents");
		ub.appendQueryParameter("fields", "items(title,id,downloadUrl)");
		
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
	
	protected List<GDImage> parseJSON(String json){
		
		List<GDImage> tmplist = new ArrayList<GDImage>();
		
		try {
			JSONObject job = new JSONObject(json);
			JSONArray items = job.getJSONArray("items");
			
			for(int i=0;i < items.length();i++){
				JSONObject imagesob = items.getJSONObject(i);
				GDImage image = new GDImage();
				image.setId(imagesob.getString("id"));
				image.setTitle(imagesob.getString("title"));
				image.setSrcUrl(imagesob.getString("downloadUrl"));
				tmplist.add(image);
			}
			return tmplist;
		} catch (JSONException e) {
			Log.i(TAG,"Something bad happened with json: "+e.getMessage());
		}
		return tmplist;
	}
}
