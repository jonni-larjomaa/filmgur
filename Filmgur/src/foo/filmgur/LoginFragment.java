package foo.filmgur;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.auth.GoogleAuthUtil;

import foo.filmgur.tasks.GetAuthToken;



public class LoginFragment extends SherlockFragment implements OnClickListener{

	static final String TAG = "filmgur";
	static final String SCOPE = "oauth2:https://www.googleapis.com/auth/drive";
	
	static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
	
	private AccountManager mAccountManager;
    
    private String[] mNamesArray;
    private String mEmail;
	private Spinner mAccountTypesSpinner;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		mNamesArray = getAccountNames();
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.login, container, false);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item,R.id.spinnerItem, mNamesArray);
		
		mAccountTypesSpinner = (Spinner) view.findViewById(R.id.spinner1);
		mAccountTypesSpinner.setAdapter(adapter);
		
		Button login = (Button) view.findViewById(R.id.loginbutton);
		login.setOnClickListener(this);
		
		return view;
	}

	
	@Override
	public void onStart() {
		super.onStart();
		final ActionBar actionbar = getSherlockActivity().getSupportActionBar();
		actionbar.setSubtitle(R.string.login);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 100 && resultCode == Activity.RESULT_OK){
			GetAuthToken gat = new GetAuthToken(mEmail, SCOPE, getActivity());
			gat.execute();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Get all accounts from the phone.
	 */
	private String[] getAccountNames() {
        mAccountManager = AccountManager.get(getActivity());
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

	@Override
	public void onClick(View v) {
		mEmail = mNamesArray[mAccountTypesSpinner.getSelectedItemPosition()];
		GetAuthToken gat = new GetAuthToken(mEmail, SCOPE, getActivity());
		gat.execute();
		
	}
}
