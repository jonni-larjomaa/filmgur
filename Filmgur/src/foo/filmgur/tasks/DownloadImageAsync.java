package foo.filmgur.tasks;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import foo.filmgur.FilmgurActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageAsync extends AsyncTask<Void, Void, Bitmap> {

	private final static String TAG = "filmgur";
	
	private String uri;
	private ImageView iv;
	
	public DownloadImageAsync(String uri, ImageView iv) {
		this.uri = uri;
		this.iv = iv;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		
		return downloadImage();
	}

	private Bitmap downloadImage() {
		
		
		HttpGet get = new HttpGet(uri);
		get.addHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);		
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			
			Log.i(TAG,"Got response: "+response.getStatusLine()+" and content with length: "+response.getEntity().getContentLength());
			
			InputStream is = response.getEntity().getContent();
			Bitmap bm = BitmapFactory.decodeStream(is);
			return bm;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		if(result != null){
			Log.i(TAG,"got this far! yay");
			iv.setImageBitmap(result);
		}
		super.onPostExecute(result);
	}
	
}
