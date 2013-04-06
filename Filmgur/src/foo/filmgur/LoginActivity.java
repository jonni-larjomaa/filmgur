package foo.filmgur;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;



public class LoginActivity extends SherlockActivity {

	static final String TAG = "filmgur";
	static final String SCOPE = "oauth2:https://www.googleapis.com/auth/drive";
	
	static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
	
	private AccountManager mAccountManager;
    private Spinner mAccountTypesSpinner;
    
    private String[] mNamesArray;
    private String mEmail;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		getSupportActionBar().show();
			
		mNamesArray = getAccountNames();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mNamesArray);
        mAccountTypesSpinner = (Spinner) findViewById(R.id.spinner1);
        mAccountTypesSpinner.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 100 && resultCode == RESULT_OK){
			GetAuthToken gat = new GetAuthToken(mEmail, SCOPE, this);
			gat.execute();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public void login(View v){
		
		mEmail = mNamesArray[mAccountTypesSpinner.getSelectedItemPosition()];
		
		GetAuthToken gat = new GetAuthToken(mEmail, SCOPE, this);
		gat.execute();
 	}

	/**
	 * Get all accounts from the phone.
	 */
	private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
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
                  LoginActivity.this,
                  100);
              d.show();
            }
        });
    }
}
