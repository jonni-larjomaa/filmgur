package foo.filmgur;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.models.GDAlbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

public class AlbumsFragment extends SherlockFragment{
	
	static final String TAG = "filmgur";
	
	protected String token;
	private GridView albumsgv;
	protected ArrayAdapter<GDAlbum> albumsad;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getArguments();
		token = b.getString("TOKEN");
		
		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setSubtitle("Albums");
		setHasOptionsMenu(true);

		albumsad = new ArrayAdapter<GDAlbum>(getActivity(), android.R.layout.simple_list_item_1);
		
		showAlbums();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.albums, container,false);
		
		albumsgv = (GridView) view.findViewById(R.id.gridView1);
		albumsgv.setAdapter(albumsad);
		
		return view;
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.albums, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(R.id.create_album == item.getItemId()){
			Log.i(TAG,"menuitem clicked: "+item.getTitle());
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

			alert.setTitle("Title");
			alert.setMessage("Message");

			// Set an EditText view to get user input 
			final EditText input = new EditText(getActivity());
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  String value = input.getText().toString();
			  Log.i(TAG,"Albumname is: "+value);
			  createAlbum(value);
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
			alert.show();
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showAlbums() {
		FetchAlbumsAsync fas = new FetchAlbumsAsync(albumsad,token);
		fas.execute();
	}
	
	private void createAlbum(String title){
		CreateAlbumAsync caa = new CreateAlbumAsync(title,albumsad,token);
		caa.execute();
	}
}
