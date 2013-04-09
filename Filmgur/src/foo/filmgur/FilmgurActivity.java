package foo.filmgur;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.*;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.common.GooglePlayServicesUtil;

import foo.filmgur.listener.OnFragmentChangedListener;

public class FilmgurActivity extends SherlockFragmentActivity implements OnFragmentChangedListener, OnBackStackChangedListener {

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().show();
		
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		Fragment fragment = new LoginFragment();
		transaction.replace(R.id.container, fragment, "Login");
		transaction.addToBackStack("Login");
		transaction.commit();
	}

	@Override
	public void onFragmentChanged(int layoutResId, Bundle bundle) {
		if(R.layout.albums == layoutResId){
			
		}

	}

	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub

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
