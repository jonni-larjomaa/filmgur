package foo.filmgur.tasks;


import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
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

public class ImageUploadTask extends AsyncTask<Void, Void, GDImage> {
	
	private static final String TAG = "filmgur";
	private ArrayAdapter<GDImage> ad;
	private String parentId;
	private File image;
	
	
	public ImageUploadTask(ArrayAdapter<GDImage> ad,String parentId, File image){
		super();
		this.ad = ad;
		this.parentId = parentId;
		this.image = image;
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
			JSONObject job = new JSONObject();
			job.put("title", image.getName());
			job.put("mimeType", GDImage.MIME);
			
			JSONArray jar = new JSONArray();
			jar.put(new JSONObject().put("id",parentId));
			
			job.put("parents", jar);
			
			Log.i(TAG,"Json body: "+job.toString());
			
			// build query uri.
			Builder ub = Uri.parse("https://www.googleapis.com").buildUpon();
			ub.path("/upload/drive/v2/files");
			ub.appendQueryParameter("uploadType", "media");
			ub.appendQueryParameter("fields", "title,id,downloadUrl");
			Log.d(TAG,"URL to request: "+ub.build().toString());
			
			// make httpget mehtod. and add auhtorization header..
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(ub.build().toString());
			Log.i(TAG,"Request URL: "+ub.build().toString());
			
			// set headers for image upload
			post.addHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
			post.addHeader("Content-Type", GDImage.MIME);
			
			// set fileEntity
			FileEntity fe = new FileEntity(image, HTTP.UTF_8);
			fe.setContentType(GDImage.MIME);
			post.setEntity(fe);
			
			// create client to execute the request
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
	
	protected GDImage parseJSON(String json){
		
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
	
	private GDImage setMetaData(GDImage gdimg){
		return gdimg;
	}
}
