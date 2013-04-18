package foo.filmgur.tasks;


import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import foo.filmgur.FilmgurActivity;
import foo.filmgur.models.GDImage;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class UploadImageAsync extends AsyncTask<Void, Void, GDImage> {
	
	private static final String TAG = "filmgur";
	private ArrayAdapter<GDImage> ad;
	private String parentId;
	private File image;
	private HttpClient client;
	
	
	public UploadImageAsync(ArrayAdapter<GDImage> ad,String parentId, File image){
		super();
		this.ad = ad;
		this.parentId = parentId;
		this.image = image;
		
		this.client = new DefaultHttpClient();
	}
	
	@Override
	protected GDImage doInBackground(Void... v) {
		return uploadImage();
	}

	@Override
	protected void onPostExecute(GDImage result) {
		if(result != null){
			ad.add(result);
			ad.notifyDataSetChanged();
		}
	}
	
	private GDImage uploadImage(){		
		try {
			
			// build query uri.
			Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
			ub.path("/upload/drive/v2/files");
			ub.appendQueryParameter("uploadType", "media");
			ub.appendQueryParameter("fields", "id");
			Log.d(TAG,"URL to request: "+ub.build().toString());
			
			// make httpget mehtod. and add auhtorization header..
			HttpPost post = new HttpPost(ub.build().toString());
			Log.i(TAG,"Request URL: "+ub.build().toString());
			
			// set headers for image upload
			post.addHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
			post.addHeader("Content-Type", GDImage.MIME);
			
			// set fileEntity
			FileEntity fe = new FileEntity(image, HTTP.UTF_8);
			fe.setContentType(GDImage.MIME);
			post.setEntity(fe);
			
			String resp = client.execute(post, new BasicResponseHandler());
			// create client to execute the request
			return createImageModel(setMetaData(resp));
			
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
	
	private String setMetaData(String json) throws JSONException, ClientProtocolException, IOException{
		
		JSONObject job = new JSONObject(json);
		GDImage img = new GDImage();
		img.setId(job.getString("id"));
		
		Log.i(TAG,"Setting metadata for image with id: "+img.getId());
		
		JSONObject metajob = new JSONObject();
		metajob.put("title", image.getName());
		metajob.put("mimeType", GDImage.MIME);

		JSONArray jar = new JSONArray();
		jar.put(new JSONObject().put("id",parentId));
		metajob.put("parents", jar);
		
		Log.i(TAG,"Metadata json body: "+metajob.toString());
		
		Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
		ub.path("/drive/v2/files/"+img.getId());
		ub.appendQueryParameter("fields", "title,id,downloadUrl");
		
		HttpPut put = new HttpPut(ub.build().toString());
		Log.i(TAG,"Request URL: "+ub.build().toString());
		// set headers for image upload
		put.addHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
		
		// set fileEntity
		StringEntity fe = new StringEntity(metajob.toString(), HTTP.UTF_8);
		fe.setContentType("application/json");		
		put.setEntity(fe);
		
		String resp = client.execute(put, new BasicResponseHandler());
		client.getConnectionManager().shutdown();
		return resp;
	}
	
	protected GDImage createImageModel(String json){
		
		GDImage image = new GDImage();
		
		Log.i(TAG,"JSON received: "+json);
		
		try {
			JSONObject imagejob = new JSONObject(json);
			image.setId(imagejob.getString("id"));
			image.setTitle(imagejob.getString("title"));
			image.setSrcUrl(imagejob.getString("downloadUrl"));
			
			return image;
		} catch (JSONException e) {
			Log.i(TAG,"Something bad happened with json: "+e.getMessage());
		}
		return null;
	}
}
