package foo.filmgur.tasks;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import foo.filmgur.FilmgurActivity;
import foo.filmgur.models.GDAlbum;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class CreateAlbumAsync extends AsyncTask<Void, Void, GDAlbum> {
	
	private static final String TAG = "filmgur";
	
	private String title;
	private ArrayAdapter<GDAlbum> ad;
	
	public CreateAlbumAsync(String title,ArrayAdapter<GDAlbum> ad){
		super();
		this.title = title;
		this.ad = ad;
	}
	
	@Override
	protected GDAlbum doInBackground(Void... v) {
		return createAlbum(title);
	}

	@Override
	protected void onPostExecute(GDAlbum result) {
		ad.add(result);
		ad.notifyDataSetChanged();
	}
	
	private GDAlbum createAlbum(String albumname){
		
		// build query uri.
		Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
		ub.path("/drive/v2/files");
		ub.appendQueryParameter("fields", "title,id");
		
		Log.d(TAG,"URL to request: "+ub.build().toString());
		
		try {
			JSONObject job = new JSONObject();
			job.put("title", albumname);
			job.put("mimeType", "application/vnd.google-apps.folder");
			
			Log.i(TAG,"Json body: "+job.toString());
			
			// make httpget mehtod. and add auhtorization header..
			HttpPost post = new HttpPost(ub.build().toString());
			post.addHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
			
			StringEntity entity = new StringEntity(job.toString(),HTTP.UTF_8);
			entity.setContentType("application/json");
			post.setEntity(entity);
			
			Log.d(TAG,"Request: "+post.getRequestLine());
			
			// create client to execute the request
			HttpClient client = new DefaultHttpClient();
			return parseJSON(client.execute(post, new BasicResponseHandler()));
			
		} catch (ClientProtocolException e) {
			Log.e(TAG,"Bad Request: "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG,"could read from network");
		} catch (JSONException e) {
			Log.e(TAG,"Something bad happened with json: "+e.getMessage());
		}
		
		return null;
	}
	
	protected GDAlbum parseJSON(String json){
		
		GDAlbum album = new GDAlbum();
		
		try {
			JSONObject albumjob = new JSONObject(json);
			album.setId(albumjob.getString("id"));
			album.setTitle(albumjob.getString("title"));
			
			return album;
		} catch (JSONException e) {
			Log.i(TAG,"Something bad happened with json: "+e.getMessage());
		}
		return null;
	}
}
