package foo.filmgur;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.listener.OnFragmentChangedListener;
import foo.filmgur.models.GDAlbum;
import foo.filmgur.models.GDImage;

public class ImagesFragment extends SherlockListFragment {

	static final String TAG = "filmgur";
	
	private OnFragmentChangedListener mListener = null;
	private ActionBar mActionBar;
	private ArrayAdapter<GDImage> imagesad;
	private GDAlbum album;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mListener = (OnFragmentChangedListener) activity;			
		}catch(Exception e){
			Log.e(TAG, "error", e);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		Bundle b = getArguments();
		album = b.getParcelable("album");		
		
		imagesad = new ArrayAdapter<GDImage>(getActivity(), android.R.layout.simple_list_item_1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.albums, container,false);
		setListAdapter(imagesad);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		mActionBar = getSherlockActivity().getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setSubtitle(album.getTitle()+"- Images");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.image, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}	
}
