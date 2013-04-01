package foo.filmgur;

import java.io.IOException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GetAuthToken extends AsyncTask<Void, Void, Void> {
	
	static final String TAG = "filmgur";
	
	protected String email;
	protected String scope;
	protected LoginActivity act;
	
	
	public GetAuthToken(String email, String scope, LoginActivity act  ) {
		super();
		
		this.act = act;
		this.email = email;
		this.scope = scope;
	}

	@Override
	protected Void doInBackground(Void... tokens) {
		
		String token ="none";
		try {
		   token = GoogleAuthUtil.getToken(act, email, scope);
		   Log.i(TAG,"got token: "+token);
		 } catch (GooglePlayServicesAvailabilityException playEx) {
			 
			 Log.e(TAG,"got exception: "+ playEx.getConnectionStatusCode());
			 act.showErrorDialog(playEx.getConnectionStatusCode());	     
		 } catch (UserRecoverableAuthException recoverableException) {
			 
			 Log.i(TAG, "Got recoverable error!");
		     Intent recoveryIntent = recoverableException.getIntent();
		     act.startActivityForResult(recoveryIntent, 100);
		     // Use the intent in a custom dialog or just startActivityForResult.
		 } catch (GoogleAuthException authEx) {
		     // This is likely unrecoverable.
		     Log.e(TAG, "Unrecoverable authentication exception: " + authEx.getMessage(), authEx);
		 } catch (IOException ioEx) {
		     Log.i(TAG, "transient error encountered: " + ioEx.getMessage());
		 }
		return null;
	}

}
