package com.softwaredepotonline.iPocketDiary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import java.util.Timer;
import java.util.TimerTask;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnTouchListener;

public class aPocketDiary extends Activity implements OnTouchListener{
	private static final int ACTIVITY_START=0;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.main);
    	RelativeLayout r = (RelativeLayout)findViewById(R.id.pocketwelcome);
    	startnew();
    	r.setOnTouchListener(this);
      	
    }	
    public boolean onTouch (View v, MotionEvent event ) 
    {
    	startnew();
    	return true;
    	
    }

    public void startnew()
    {
    	Timer timer = new Timer();
        timer.schedule(new TimerTask() 
        {
	            public void run() 
	            {
	            	try
	        		{
	        			Intent loginIntent;
	        			SharedPreferences passwdfile = getSharedPreferences(LoginView.PASSWORD_PREF_KEY,0);
	        			String correctMd5 = passwdfile.getString(LoginView.PASSWORD_PREF_KEY,null);
	        		
	        			if(correctMd5 != null)
	        			{
	        				//Bundle bundle= new Bundle();
	        				//bundle.putString("correctMd5", correctMd5);
	        				loginIntent= new Intent(aPocketDiary.this,Authentication.class);
	        				//loginIntent.putExtras(bundle);
	        			    				
	        			}
	        			else
	        			{
	        				loginIntent= new Intent(aPocketDiary.this,LoginView.class);
	        			
	        			}
	        			startActivityForResult(loginIntent,ACTIVITY_START);
	        			
	        								
	        		}
	        		catch (Throwable er) 
	        		{
	        			Toast.makeText(aPocketDiary.this, "Error "+er.getMessage(), Toast.LENGTH_LONG).show();

	        		} 
	            }
	        }, 3000);
    	
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        startnew();
       
    }

 }