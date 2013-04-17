package foo.filmgur;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.models.GDAlbum;
import foo.filmgur.models.GDImage;
import foo.filmgur.tasks.FetchImagesAsync;
import foo.filmgur.tasks.ImageDownloadTask;
import foo.filmgur.tasks.ImageUploadTask;

public class ImagesFragment extends SherlockListFragment {

	private static final String TAG = "filmgur";
	private static final int REQUEST_CAMERA_ACTION = 10;
	
	private ActionBar mActionBar;
	private ArrayAdapter<GDImage> imagesad;
	private GDAlbum album;
	private File image;
	private ImageView iv;

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
		ImageDownloadTask idt = new ImageDownloadTask(imagesad.getItem(position).getSrcUrl(), iv);
		idt.execute();
		super.onListItemClick(l, v, position, id);
	}	
	
	private void getImages() {
		FetchImagesAsync fia = new FetchImagesAsync(imagesad, album.getId());
		fia.execute();
	}
	
	private void uploadImage(File image) {
		ImageUploadTask iut = new ImageUploadTask(imagesad, album.getId(), image);
		iut.execute();
	}

}
