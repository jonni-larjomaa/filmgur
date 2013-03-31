package foo.filmgur;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.auth.GoogleAuthUtil;


public class LoginActivity extends SherlockActivity {

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
	
	private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }
	
	public void login(View v){
		Log.i(this.getClass().getName(), "logged in!");
 	}
}
