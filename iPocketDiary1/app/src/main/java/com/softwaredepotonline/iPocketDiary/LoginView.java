package com.softwaredepotonline.iPocketDiary;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import java.security.*;
import android.util.Log;
import java.math.*;


public class LoginView extends Activity implements OnClickListener
{
	public static final String PASSWORD_PREF_KEY = "passwd";
	Button btnSavePassword;
	private EditText pass1;
	private EditText pass2;
	private AlertDialog alertDialog;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.setpassword);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final TextView centertext = (TextView) findViewById(R.id.center_text);
        centertext.setText("Set Password");
        pass1 = (EditText) findViewById(R.id.password);
		pass2 = (EditText) findViewById(R.id.password_confirm);
        btnSavePassword = (Button)findViewById(R.id.ok);
        btnSavePassword.setOnClickListener(this);
        
    }
    public void onClick(View v)
    {
    	String p1 = pass1.getText().toString();
		String p2 = pass2.getText().toString();
		if (p1.equals(p2)) 
		{
			
				if (p1.length() >= 6 || p2.length() >= 6)
				{
					Editor passwdfile = getSharedPreferences(LoginView.PASSWORD_PREF_KEY, 0).edit();
					String md5hash = getMd5Hash(p1);
					passwdfile.putString(LoginView.PASSWORD_PREF_KEY,
							md5hash);
					passwdfile.commit();
					alertDialog = new AlertDialog.Builder(this).create();
	            	//alertDialog.setTitle("Passwords must be at least 6 characters")
					alertDialog.setMessage("If you forgot password, there is no way to get back.");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						@Override
			             public void onClick(DialogInterface dialog, int which) 
			             {
							Intent categoryIntent= new Intent(LoginView.this,ItemList.class);
					    	startActivity(categoryIntent);
					    	finish();
			            	return;
			             }
			         }); 
	
	            	
				}
				else
				{
					if(p1.length()==0)
					{
						alertDialog = new AlertDialog.Builder(this).create();
		            	alertDialog.setTitle("Password");
						alertDialog.setMessage("Please enter password.");
		            	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   	             public void onClick(DialogInterface dialog, int which) 
		   	             {
		   	                     return;
		   	             }
		   	         	});
					}
					else
					{
						alertDialog = new AlertDialog.Builder(this).create();
		            	alertDialog.setTitle("Password");
						alertDialog.setMessage("Passwords must be at least 6 characters.");
		            	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   	             public void onClick(DialogInterface dialog, int which) {
		   	                     return;
		   	             }
		   	         	});
					}
	
				}
			

				
		}
		else 
		{
			pass1.setText("");
			pass2.setText("");
			alertDialog = new AlertDialog.Builder(this).create();
        	//.setTitle("Passwords do not match")
			alertDialog.setMessage("Passwords do not match");
        	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) 
	             {
	                     return;
	             }
	         }); 

        	
		}
		
		alertDialog.show();	
		
    }
    public static String getMd5Hash(String input) {
		try	{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1,messageDigest);
			String md5 = number.toString(16);
	    	
			while (md5.length() < 32)
				md5 = "0" + md5;
	    	
			return md5;
		} catch(NoSuchAlgorithmException e) {
			Log.e("MD5", e.getMessage());
			return null;
		}
	}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) 
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {
	    	Intent intent = new Intent();
	    	setResult(RESULT_OK,intent);
	    	finish();
	    	
	    }
	    return true;
	}
    
}
