package foo.filmgur;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;


public class LoginActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().show();
	}
	
	public void login(View v){
		
		
		TextView debug = (TextView) findViewById(R.id.debug);
		debug.setText("logged IN!");
		
		Log.i(this.getClass().getName(), "logged in!");
 	}
}
