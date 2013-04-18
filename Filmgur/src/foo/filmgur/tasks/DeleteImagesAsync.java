package foo.filmgur.tasks;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import foo.filmgur.FilmgurActivity;
import foo.filmgur.models.GDImage;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class DeleteImagesAsync extends AsyncTask<Void, GDImage, Void> {

	
private final static String TAG = "filmgur";
	
	private List<GDImage> ditems;
	private ArrayAdapter<GDImage> imagesad;
	
	public DeleteImagesAsync(List<GDImage> ditems, ArrayAdapter<GDImage> imagesad) {
		this.ditems = ditems;
		this.albumsad = imagesad;
	}

	@Override
	protected Void doInBackground(Void...items) {
		deleteItems();
		return null;
	}

	private Void deleteItems() {
		
		Iterator<GDImage> iter = ditems.iterator();
		
		HttpDelete delete = new HttpDelete();
		DefaultHttpClient client = new DefaultHttpClient();
		
		while(iter.hasNext()){
			try {
				GDImage image = iter.next();
				URI url = new URI("https://www.googleapis.com/drive/v2/files/"+image.getId());
				delete.setURI(url);
				delete.setHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
				client.execute(delete);
				Log.i(TAG,"album: "+image.getTitle()+" deleted");
				publishProgress(image);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(GDImage... values) {
		albumsad.remove(values[0]);
		albumsad.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}
}
