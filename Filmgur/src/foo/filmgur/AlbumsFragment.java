package foo.filmgur;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
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
import android.widget.ListView;
import android.widget.Toast;

public class AlbumsFragment extends SherlockListFragment{
	
	static final String TAG = "filmgur";
	
	protected String token;
	protected ArrayAdapter<GDAlbum> albumsad;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		Bundle b = getArguments();
		token = b.getString("TOKEN");
		
		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setSubtitle("Albums");
		
		albumsad = new ArrayAdapter<GDAlbum>(getActivity(), android.R.layout.simple_list_item_1);
		
		showAlbums();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.albums, container,false);
		setListAdapter(albumsad);
		
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
			alert.show();
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getActivity(), albumsad.getItem(position).getId(), Toast.LENGTH_SHORT).show();
		super.onListItemClick(l, v, position, id);
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
