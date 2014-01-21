package org.wanghai.CameraTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;

public class GetIP extends Activity {
	String ipname = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set it to full-screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
     	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);        
      
      	final Builder builder = new AlertDialog.Builder(this);   // setting a alert dialog
		builder.setTitle("login dialog");                       // hint of alert dialog title
		
		//setup the layout
		TableLayout loginForm = (TableLayout)getLayoutInflater().inflate( R.layout.login, null);		
		final EditText iptext = (EditText)loginForm.findViewById(R.id.ipedittext);				
		builder.setView(loginForm);                              // set 
		
		//create a login button
		builder.setPositiveButton("Login"
			// login listener
			, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// seting the login information
					//ipname = iptext.getText().toString().trim();
					ipname = "140.116.164.19";
					Bundle data = new Bundle();
					data.putString("ipname",ipname);					
					Intent intent = new Intent(GetIP.this,CameraTest.class);
					intent.putExtras(data);
					startActivity(intent);
				}
			});
		// create a canceal button
		builder.setNegativeButton("Canceal"
			,  new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					//not to login
					System.exit(1);
				}
			});
		//show the dialog
		builder.create().show();
	}
}