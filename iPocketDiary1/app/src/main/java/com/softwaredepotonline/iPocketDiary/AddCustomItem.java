package com.softwaredepotonline.iPocketDiary;

import com.softwaredepotonline.iPocketDiary.PocketDiary.information;
import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.DialogInterface;
import android.content.Intent;


public class AddCustomItem extends Activity implements OnClickListener
{
	Button saveCustomItem;
	private Long mSubFolderId,mItemID;
	Integer intOld; 
	private PocketDbAdapter mDbHelper;
	private EditText CustomItem;
	String mItemName,mItemDataType,mItemType;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mDbHelper = new PocketDbAdapter(this);
		mDbHelper.open();
		Bundle extras = getIntent().getExtras();
		mSubFolderId = savedInstanceState!=null ? savedInstanceState.getLong(information.SubFolderID): null;
		if(mSubFolderId == null)
		{
			  mSubFolderId = extras !=null? extras.getLong(information.SubFolderID) : null;
			 
		}
		intOld = savedInstanceState!=null ? savedInstanceState.getInt(subfolder.intOld): null;
		if(intOld == null)
		{
			intOld = extras !=null? extras.getInt(subfolder.intOld) : 0;
			 
		}
		setContentView(R.layout.custom_item_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final TextView centertext = (TextView) findViewById(R.id.center_text);
        centertext.setText("Add Custom Item");
		CustomItem = (EditText)findViewById(R.id.CustomItem);
		final Spinner s1 = (Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.CustomItemType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v,
                      int position, long id) 
            {
            	String selectedItemString = (String)s1.getSelectedItem();

            	mItemType = selectedItemString;
            	
            	if(mItemType.equals("Numeric"))
            	{
            		mItemDataType = information.ITEM_NUMERIC;
            	}
            	else if(mItemType.equals("Text"))
            	{
            		mItemDataType = information.ITEM_TEXT;
            	}
            	else if(mItemType.equals("Text Box"))
            	{
            		mItemDataType = information.ITEM_MULTITEXT;
            	}
            	else if(mItemType.equals("Date Format"))
            	{
            		mItemDataType = information.ITEM_DATE;
            	}
            	else
            	{
            		mItemDataType = information.ITEM_TEXT;
            	}

            }
            
            public void onNothingSelected(AdapterView<?> arg0) 
            {
            	
            }
       });
        saveCustomItem = (Button)findViewById(R.id.saveCustomItem);
        saveCustomItem.setOnClickListener(this);
    }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        outState.putLong(information.SubFolderID, mSubFolderId);
        outState.putInt(subfolder.intOld, intOld);
    }
	
	@Override      
	public void onClick(View v)
	{
		mItemName = CustomItem.getText().toString();
		if(v.getId()==R.id.saveCustomItem)
    	{
			if(mItemName.length()>0)
			{
				
				if(mItemID==null)
	    		{
					long id;
					if(intOld==1)
	    			{
						id = mDbHelper.CreateInformation(mItemDataType, mItemName, mSubFolderId);
	    			}
	    			else
	    			{
	    				id = mDbHelper.CreateInformation(mItemDataType, mItemName, 0);
	    			}
					
	    			if(id > 0)
	    			{
	    				mItemID = id;
	    			
	    			}
	    		}
	    		Intent intent = new Intent();
	    		intent.putExtra(information.SubFolderID , mSubFolderId);
	    		intent.putExtra("mItemID", mItemID);
	    		setResult(RESULT_OK, intent);
				finish();
	            return;
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            	alertDialog.setTitle("Item Name");
				alertDialog.setMessage("Please enter Item Name.");
            	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
   	             public void onClick(DialogInterface dialog, int which) 
   	             {
   	                     return;
   	             }
   	         	});
            	alertDialog.show();
			}
    		
    		  		
    	}
	}
	
}
