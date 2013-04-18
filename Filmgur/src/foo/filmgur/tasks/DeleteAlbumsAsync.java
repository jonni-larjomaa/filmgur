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
import foo.filmgur.models.GDAlbum;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class DeleteAlbumsAsync extends AsyncTask<Void, GDAlbum, Void> {

	private final static String TAG = "filmgur";
	
	private List<GDAlbum> ditems;
	private ArrayAdapter<GDAlbum> albumsad;
	
	public DeleteAlbumsAsync(List<GDAlbum> ditems, ArrayAdapter<GDAlbum> albumsad) {
		this.ditems = ditems;
		this.albumsad = albumsad;
	}

	@Override
	protected Void doInBackground(Void...items) {
		deleteItems();
		return null;
	}

	private Void deleteItems() {
		
		Iterator<GDAlbum> iter = ditems.iterator();
		
		HttpDelete delete = new HttpDelete();
		DefaultHttpClient client = new DefaultHttpClient();
		
		while(iter.hasNext()){
			try {
				GDAlbum album = iter.next();
				URI url = new URI("https://www.googleapis.com/drive/v2/files/"+album.getId());
				delete.setURI(url);
				delete.setHeader("Authorization", "Bearer "+FilmgurActivity.accessToken);
				client.execute(delete);
				Log.i(TAG,"album: "+album.getTitle()+" deleted");
				publishProgress(album);
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
	protected void onProgressUpdate(GDAlbum... values) {
		albumsad.remove(values[0]);
		albumsad.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}
}
