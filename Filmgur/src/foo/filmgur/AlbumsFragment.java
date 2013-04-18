package foo.filmgur;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.listener.OnFragmentChangedListener;
import foo.filmgur.models.GDAlbum;
import foo.filmgur.tasks.CreateAlbumAsync;
import foo.filmgur.tasks.DeleteAlbumsAsync;
import foo.filmgur.tasks.FetchAlbumsAsync;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AlbumsFragment extends SherlockListFragment{
	
	static final String TAG = "filmgur";
	protected ArrayAdapter<GDAlbum> albumsad;
	
	private LayoutAnimationController mAnimateList;
	private ActionBar mActionBar;
	private OnFragmentChangedListener mListener = null;
	protected List<GDAlbum> selecteditems = new ArrayList<GDAlbum>();
	//private ActionMode.Callback mActionModeCallback;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			mListener = (OnFragmentChangedListener) activity;			
		}catch(Exception e){
			Log.e(TAG, "error", e);
		}
		
		mAnimateList = AnimationUtils.loadLayoutAnimation(getSherlockActivity(), R.anim.album_list_fade_in);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.albums, container,false);
		albumsad = new ArrayAdapter<GDAlbum>(getSherlockActivity(), R.layout.album_item_view,R.id.albumtitle);
		setListAdapter(albumsad);
		showAlbums();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getListView().setLayoutAnimation(mAnimateList);
		getListView().startLayoutAnimation();
	}

	@Override
	public void onStart() {
		super.onStart();
		
		mActionBar = getSherlockActivity().getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setSubtitle("Albums");
		setupActionMode();
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
			AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());

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

	// fetching of album metadata to display dirve albums in view.
	private void showAlbums() {
		FetchAlbumsAsync fas = new FetchAlbumsAsync(albumsad);
		fas.execute();
	}
	
	// create albums asynctask
	private void createAlbum(String title){
		CreateAlbumAsync caa = new CreateAlbumAsync(title,albumsad);
		caa.execute();
	}
	
	/**
	 * select action with fallback when multiple modal cannot be supported.
	 */
	private void setupActionMode() {
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			setMultiModal();
		}
		else{
			setModal();
		}		
	}

	private void setModal() {
		// TODO Auto-generated method stub
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setMultiModal() {
		
		ListView listview = getListView();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listview.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public boolean onPrepareActionMode(android.view.ActionMode mode,
					android.view.Menu menu) {
				return false;
			}
			
			@Override
			public void onDestroyActionMode(android.view.ActionMode mode) {
				
			}
			
			@Override
			public boolean onCreateActionMode(android.view.ActionMode mode,
					android.view.Menu menu) {
				if(selecteditems.size() > 0){
					selecteditems.clear();
				}
				android.view.MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.context_menu, menu);
				return true;
			}
			
			@Override
			public boolean onActionItemClicked(android.view.ActionMode mode,
					android.view.MenuItem item) {
				if(item.getItemId() == R.id.remove){
					DeleteAlbumsAsync dit = new DeleteAlbumsAsync(selecteditems,albumsad);
					dit.execute();
					mode.finish();
				}
				return false;
			}
			
			@Override
			public void onItemCheckedStateChanged(android.view.ActionMode mode,
					int position, long id, boolean checked) {
				if(checked){
					selecteditems.add(albumsad.getItem(position));
				}
				else{
					Iterator<GDAlbum> iter = selecteditems.iterator();
					while(iter.hasNext()){
						GDAlbum stored = iter.next();
						if(stored == albumsad.getItem(position)){
							iter.remove();
						}
					}
				}
			}
		});
		
	}
}
