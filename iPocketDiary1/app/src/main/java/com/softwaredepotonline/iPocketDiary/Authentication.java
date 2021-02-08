package com.softwaredepotonline.iPocketDiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
public class Authentication extends Activity implements OnClickListener
{
	Button buttonOk;
	Button btnChangePassword;
	String md5hash;
	//String correctMd5;
	private AlertDialog alertDialog;
	private EditText pass; 
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.auth_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final TextView centertext = (TextView) findViewById(R.id.center_text);
        centertext.setText(R.string.title_authentication);
        
        pass=(EditText)findViewById(R.id.password);
        buttonOk=(Button)findViewById(R.id.ok);
        buttonOk.setOnClickListener(this);
        btnChangePassword=(Button)findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(this);
    }
    public void onClick(View v)
	{
    	if(v.getId()==R.id.ok)
    	{
	    	//Bundle bundle= this.getIntent().getExtras();
	    	SharedPreferences passwdfile = getSharedPreferences(LoginView.PASSWORD_PREF_KEY,0);
			String correctMd5 = passwdfile.getString(LoginView.PASSWORD_PREF_KEY,null);
	    	//correctMd5= bundle.getString("correctMd5");
	    	md5hash= LoginView.getMd5Hash(pass.getText().toString());
		    if(md5hash.equals(correctMd5))
			{
		    	Intent categoryIntent=new Intent(this,ItemList.class);
		    	startActivity(categoryIntent);
		    	finish();
			}
		    else
		    {
		    	pass.setText("");
		    	alertDialog= new AlertDialog.Builder(this).create();
		    	alertDialog.setTitle("Login Failed!!!");
		    	alertDialog.setMessage("Invalid Password.");
		    	alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
		    	{
					public void onClick(DialogInterface dialog, int which) 
					{
						return;
						
					}
				});
		    	
		    	alertDialog.show();
		    }
    	}
    	else if(v.getId()==R.id.btnChangePassword)
    	{
    		Intent changePasswordIntent=new Intent(this,ChangePasswordView.class);
	    	startActivity(changePasswordIntent);
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
	

