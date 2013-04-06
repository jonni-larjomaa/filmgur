package foo.filmgur;

import java.io.IOException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GetAuthToken extends AsyncTask<Void, Void, String> {
	
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
	protected String doInBackground(Void... v) {
		
		String token = null;
		
		try {
		    token = GoogleAuthUtil.getToken(act, email, scope);
		} catch (GooglePlayServicesAvailabilityException playEx) {
			 
			 Log.e(TAG,"got exception: "+ playEx.getConnectionStatusCode());
			 act.showErrorDialog(playEx.getConnectionStatusCode());	     
		 } catch (UserRecoverableAuthException recoverableException) {
			 
		     Intent recoveryIntent = recoverableException.getIntent();
		     act.startActivityForResult(recoveryIntent, 100);
		 } catch (GoogleAuthException authEx) {
		     Log.e(TAG, "Unrecoverable authentication exception: " + authEx.getMessage(), authEx);
		 } catch (IOException ioEx) {
		     Log.i(TAG, "transient error encountered: " + ioEx.getMessage());
		 }
		return token;
	}

	@Override
	protected void onPostExecute(String result) {
		
		if(result != null){
			Log.i(TAG,"Got authentication token: "+result);
			Intent albums = new Intent(act, AlbumsActivity.class);
			albums.putExtra("TOKEN", result);
		    act.startActivity(albums);
		}
	}

	
}
