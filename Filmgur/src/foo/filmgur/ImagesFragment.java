package foo.filmgur;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.models.GDAlbum;
import foo.filmgur.models.GDImage;
import foo.filmgur.tasks.DeleteImagesAsync;
import foo.filmgur.tasks.FetchImagesAsync;
import foo.filmgur.tasks.DownloadImageAsync;
import foo.filmgur.tasks.UploadImageAsync;

public class ImagesFragment extends SherlockListFragment {

	private static final String TAG = "filmgur";
	private static final int REQUEST_CAMERA_ACTION = 10;
	
	private ActionBar mActionBar;
	private ArrayAdapter<GDImage> imagesad;
	private GDAlbum album;
	private File image;
	private ImageView iv;
	protected List<GDImage> selecteditems = new ArrayList<GDImage>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		Bundle b = getArguments();
		album = b.getParcelable("album");		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.images, container,false);
		imagesad = new ArrayAdapter<GDImage>(getActivity(), android.R.layout.simple_list_item_1);
		setListAdapter(imagesad);
		getImages();
		iv = (ImageView) view.findViewById(R.id.dlimage);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		mActionBar = getSherlockActivity().getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setSubtitle(album.getTitle()+"- Images");
		setupActionMode();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.image, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(R.id.camera == item.getItemId()){
			Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			image = new File(
				    Environment.getExternalStoragePublicDirectory(
				        Environment.DIRECTORY_PICTURES
				    ), 
				    album.getTitle()+"_"+timeStamp+".jpg"
				);              
			takePic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
		    startActivityForResult(takePic, REQUEST_CAMERA_ACTION);
		}
		return super.onOptionsItemSelected(item);
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CAMERA_ACTION && resultCode == SherlockActivity.RESULT_OK){
			uploadImage(image);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		DownloadImageAsync idt = new DownloadImageAsync(imagesad.getItem(position).getSrcUrl(), iv);
		idt.execute();
		super.onListItemClick(l, v, position, id);
	}	
	
	private void getImages() {
		FetchImagesAsync fia = new FetchImagesAsync(imagesad, album.getId());
		fia.execute();
	}
	
	private void uploadImage(File image) {
		UploadImageAsync iut = new UploadImageAsync(imagesad, album.getId(), image);
		iut.execute();
	}
	
	private void setupActionMode() {
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			setMultiModal();
		}	
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
					DeleteImagesAsync dit = new DeleteImagesAsync(selecteditems,imagesad);
					dit.execute();
					mode.finish();
				}
				return false;
			}
			
			@Override
			public void onItemCheckedStateChanged(android.view.ActionMode mode,
					int position, long id, boolean checked) {
				if(checked){
					selecteditems.add(imagesad.getItem(position));
					
				}
				else{
					Iterator<GDImage> iter = selecteditems.iterator();
					while(iter.hasNext()){
						GDImage stored = iter.next();
						if(stored == imagesad.getItem(position)){
							iter.remove();
						}
					}
				}
				mode.setTitle("Selected items: "+selecteditems.size());
			}
		});
		
	}

}
