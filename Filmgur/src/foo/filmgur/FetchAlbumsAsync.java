package foo.filmgur;


import java.util.List;

import foo.filmgur.models.GDAlbums;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class FetchAlbumsAsync extends AsyncTask<GDAlbums, Void, List<String>> {

	private static final String TAG = "filmgur";
	
	private Context ctx;
	private GridView gv;
	private String method;
	
	public FetchAlbumsAsync(Context ctx, GridView gv,String method){
		super();
		this.ctx = ctx;
		this.gv = gv;
		this.method = method;
	}
	
	@Override
	protected List<String> doInBackground(GDAlbums... albums) {
		return albums[0].fetchAlbums();
	}

	@Override
	protected void onPostExecute(List<String> result) {
		 if(result != null){
			 ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, result);
			 gv.setAdapter(adapter);
		 }
	}
}
