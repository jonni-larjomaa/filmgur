package foo.filmgur;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.GooglePlayServicesUtil;

import foo.filmgur.listener.OnFragmentChangedListener;

public class FilmgurActivity extends SherlockFragmentActivity implements OnFragmentChangedListener, OnBackStackChangedListener {

	
	private ActionBar mActionBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mActionBar = getSupportActionBar();
		mActionBar.show();
		
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Fragment fragment = new LoginFragment();
		transaction.replace(R.id.container, fragment, "Login");
		transaction.addToBackStack("Login");
		transaction.commit();
	}

	@Override
	public void onFragmentChanged(int layoutResId, Bundle bundle) {
		Fragment fragment = null;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if(R.layout.albums == layoutResId){
			fragment = new AlbumsFragment();
			if(bundle != null){
				fragment.setArguments(bundle);
			}
			transaction.replace(R.id.container, fragment, "Albums");
			transaction.addToBackStack("albums");
			transaction.commit();
		}

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		final int itemId = item.getItemId();
		if(itemId == android.R.id.home){
			// clean up back stack
			int num = getSupportFragmentManager().getBackStackEntryCount();
			if(num > 0){
				// do not clean up the root fragment
				for(int i = 0; i < (num - 1); i++){
					getSupportFragmentManager().popBackStack();		
				}	
			}			
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onBackStackChanged() {
		// the root fragment is the first one
		final int entryCount = getSupportFragmentManager().getBackStackEntryCount();
		if(entryCount == 1){
			mActionBar.setTitle(R.string.app_name);
			mActionBar.setDisplayHomeAsUpEnabled(false);
		}else if(entryCount == 0){
			// no fragment in stack, so destroy the activity
			finish();
		}
	}

	/**
     * This method is a hook for background threads and async tasks that need to launch a dialog.
     * It does this by launching a runnable under the UI thread.
     */
    public void showErrorDialog(final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Dialog d = GooglePlayServicesUtil.getErrorDialog(
                  code,
                  FilmgurActivity.this,
                  100);
              d.show();
            }
        });
    }
}
