package foo.filmgur;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import foo.filmgur.models.GDAlbums;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class AlbumsActivity extends SherlockActivity{
	
	static final String TAG = "filmgur";
	
	protected String token;
	private ActionBar mActionBar;
	private GridView albumsgv;
	private GDAlbums gdalbums;
	private FetchAlbumsAsync fas;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_albums);
		
		Bundle data = getIntent().getExtras();
		token = data.getString("TOKEN");
		
		// do not call the method getActionBar()
		mActionBar = getSupportActionBar();
		mActionBar.setSubtitle("Albums");
		
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.setHomeButtonEnabled(true);
		
		gdalbums = new GDAlbums(token);
		
		albumsgv = (GridView) findViewById(R.id.gridView1);
		fas = new FetchAlbumsAsync(this, albumsgv);
		fas.execute(gdalbums);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.albums, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		if(R.id.create_album == item.getItemId()){
			Log.i(TAG,"menuitem clicked: "+item.getTitle());
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Title");
			alert.setMessage("Message");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  String value = input.getText().toString();
			  Log.i(TAG,"Albumname is: "+value);
			  
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

			alert.show();
			// see http://androidsnippets.com/prompt-user-input-with-an-alertdialog
			
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
}
