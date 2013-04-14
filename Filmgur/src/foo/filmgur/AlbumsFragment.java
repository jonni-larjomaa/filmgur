package foo.filmgur;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.listener.OnFragmentChangedListener;
import foo.filmgur.models.GDAlbum;
import foo.filmgur.tasks.CreateAlbumAsync;
import foo.filmgur.tasks.FetchAlbumsAsync;

import android.app.Activity;
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

public class AlbumsFragment extends SherlockListFragment{
	
	static final String TAG = "filmgur";
	
	protected String token;
	protected ArrayAdapter<GDAlbum> albumsad;
	
	private ActionBar mActionBar;
	private OnFragmentChangedListener mListener = null;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			mListener = (OnFragmentChangedListener) activity;			
		}catch(Exception e){
			Log.e(TAG, "error", e);
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		Bundle b = getArguments();
		token = b.getString("TOKEN");
		
		
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.albums, container,false);
		
		albumsad = new ArrayAdapter<GDAlbum>(getActivity(), android.R.layout.simple_list_item_1);
		setListAdapter(albumsad);
		showAlbums();
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		mActionBar = getSherlockActivity().getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setSubtitle("Albums");
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
		super.onListItemClick(l, v, position, id);
		
		Bundle alb = new Bundle();
		alb.putParcelable("album", albumsad.getItem(position));

		mListener.onFragmentChanged(R.layout.images, alb);
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
