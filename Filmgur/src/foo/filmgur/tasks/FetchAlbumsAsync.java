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

import foo.filmgur.FilmgurActivity;
import foo.filmgur.models.GDAlbum;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class FetchAlbumsAsync extends AsyncTask<Void, Void, List<GDAlbum>> {
	
	private static final String TAG = "filmgur";
	
	private ArrayAdapter<GDAlbum> ad;
	
	public FetchAlbumsAsync(ArrayAdapter<GDAlbum> ad){
		super();
		this.ad = ad;

	}
	
	@Override
	protected List<GDAlbum> doInBackground(Void... a) {
		return fetchAlbums();
	}

	@Override
	protected void onPostExecute(final List<GDAlbum> result) {
		 if(!result.isEmpty()){
			 if(!ad.isEmpty()){
				 ad.clear();
			 }
			 ad.addAll(result);
			 ad.notifyDataSetChanged();
		 }
	}
	
	public List<GDAlbum> fetchAlbums(){
		
		// build query uri.
		Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
		ub.path("/drive/v2/files");
		ub.appendQueryParameter("q", "mimeType='"+GDAlbum.MIME+"'");
		ub.appendQueryParameter("fields", "items(title,id)");
		
		// make httpget mehtod. and add auhtorization header..
		HttpGet get = new HttpGet(ub.build().toString());
		get.addHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
		
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
	
	protected List<GDAlbum> parseJSON(String json){
		
		List<GDAlbum> tmplist = new ArrayList<GDAlbum>();
		
		try {
			JSONObject job = new JSONObject(json);
			JSONArray items = job.getJSONArray("items");
			
			for(int i=0;i < items.length();i++){
				JSONObject albumsob = items.getJSONObject(i);
				GDAlbum album = new GDAlbum();
				album.setId(albumsob.getString("id"));
				album.setTitle(albumsob.getString("title"));
				tmplist.add(album);
			}
			return tmplist;
		} catch (JSONException e) {
			Log.i(TAG,"Something bad happened with json: "+e.getMessage());
		}
		return tmplist;
	}
}
