package com.softwaredepotonline.iPocketDiary;
import java.util.Calendar;
import com.softwaredepotonline.iPocketDiary.PocketDiary.information;
import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class AddNewItem extends Activity implements OnClickListener
{
	private PocketDbAdapter mDbHelper;
	private Long mSubFolderId, mInfoID;
	String mItemName,mItemValue,mItemType;
	EditText ItemTextNumericDate;
	private static final int ACTIVITY_CREATE_CUSTITEM=5;
	EditText ItemMultiText;
	Button btnCustomItem;
	Button saveItem;
	private AlertDialog alertDialog;
	Integer action,mIntOLD;
	static final int DATE_DIALOG_ID = 1;
    // date and time
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean IsReturn = false;
    private Cursor infoCursor;
    Spinner s1;
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
			  mSubFolderId = extras !=null? extras.getLong(information.SubFolderID) : 0;
			 
		}
		
		mInfoID = savedInstanceState!=null ? savedInstanceState.getLong(information._ID): null;
		if(mInfoID == null)
		{			 
			mInfoID = extras !=null? extras.getLong(information._ID) : null;
		}
		mIntOLD = savedInstanceState!=null ? savedInstanceState.getInt(subfolder.intOld): null;
		if(mIntOLD == null)
		{			 
			mIntOLD = extras !=null? extras.getInt(subfolder.intOld) : 0;
		}
		action = extras !=null? extras.getInt("action"): 0;
		setContentView(R.layout.add_item_layout);
		ItemTextNumericDate = (EditText)findViewById(R.id.ItemTextNumericDate);
		ItemMultiText = (EditText)findViewById(R.id.ItemMultilineText);
		populateFields();
		SetTextBoxProperty();
		
		btnCustomItem = (Button)findViewById(R.id.btnCustomItem);
		saveItem = (Button)findViewById(R.id.saveItem);
		btnCustomItem.setOnClickListener(this);
		saveItem.setOnClickListener(this);
		filldata();
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final TextView centertext = (TextView) findViewById(R.id.center_text);
        centertext.setText("Add New Item");
        
        /*
        if(action.equals(Information.ACTIVITY_EDITITEM))
        {
        	btnCustomItem.setVisibility(Button.INVISIBLE);
        }
        else
        {
        	btnCustomItem.setVisibility(Button.VISIBLE);
        }*/
        
  	}
	public void populateFields()
	{
		if(mInfoID!=null && mInfoID>0)
		{
			Cursor mCursorInfo = mDbHelper.FetchInformation(mInfoID);
			startManagingCursor(mCursorInfo);
			mItemName = mCursorInfo.getString(mCursorInfo.getColumnIndexOrThrow(information.InfoName));
			mItemValue = mCursorInfo.getString(mCursorInfo.getColumnIndexOrThrow(information.InfoValue));
			mItemType = mCursorInfo.getString(mCursorInfo.getColumnIndexOrThrow(information.InfoDataType));
		}
		else
		{
			mItemValue = "";
			mItemType = "-1";
		}
		
	}
	public void SetTextBoxProperty()
	{
		if(mItemType.equals(information.ITEM_NUMERIC))
		{
			ItemTextNumericDate.setVisibility(EditText.VISIBLE);
			ItemMultiText.setVisibility(EditText.INVISIBLE);
			ItemMultiText.setVisibility(EditText.GONE);
			ItemTextNumericDate.setInputType(InputType.TYPE_CLASS_NUMBER);
			mItemType = information.ITEM_NUMERIC;
			if(mItemValue!=null)
			{
				ItemTextNumericDate.setText(mItemValue);
			}
		}
		else if(mItemType.equals(information.ITEM_TEXT))
		{
			ItemTextNumericDate.setVisibility(EditText.VISIBLE);
			ItemMultiText.setVisibility(EditText.INVISIBLE);
			ItemMultiText.setVisibility(EditText.GONE);
			ItemTextNumericDate.setInputType(InputType.TYPE_CLASS_TEXT);
			mItemType = information.ITEM_TEXT;
			if(mItemValue!=null)
			{
				ItemTextNumericDate.setText(mItemValue);
			}
			
		}
		else if(mItemType.equals(information.ITEM_MULTITEXT))
		{
			ItemTextNumericDate.setVisibility(EditText.INVISIBLE);
			ItemMultiText.setVisibility(EditText.VISIBLE);
			ItemTextNumericDate.setVisibility(EditText.GONE);
			//ItemMultiText.setInputType(InputType.TYPE_CLASS_TEXT);
			mItemType = information.ITEM_MULTITEXT;
			if(mItemValue!=null)
			{
				ItemMultiText.setText(mItemValue);
			}
			
		}
		else if(mItemType.equals(information.ITEM_DATE))
		{
			final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);
	        
	        updateDisplay();
	        ItemTextNumericDate.setVisibility(EditText.VISIBLE);
			ItemMultiText.setVisibility(EditText.INVISIBLE);
			ItemMultiText.setVisibility(EditText.GONE);
	        ItemTextNumericDate.setInputType(InputType.TYPE_CLASS_DATETIME);
			mItemType = information.ITEM_DATE;
			ItemTextNumericDate.setOnClickListener(new View.OnClickListener() {

	            public void onClick(View v) {
	                showDialog(DATE_DIALOG_ID);
	            }
	        });
			if(mItemValue!=null)
			{
				ItemTextNumericDate.setText(mItemValue);
			}
		}
		else
		{
			ItemTextNumericDate.setVisibility(EditText.VISIBLE);
			ItemMultiText.setVisibility(EditText.INVISIBLE);
			ItemMultiText.setVisibility(EditText.GONE);
			ItemTextNumericDate.setInputType(InputType.TYPE_CLASS_TEXT);
			mItemType = information.ITEM_TEXT;
			if(mItemValue!=null)
			{
				ItemTextNumericDate.setText(mItemValue);
			}
		}
	}
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) 
        {    
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    } 
	private void updateDisplay() {
		ItemTextNumericDate.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" ")
                    );
    }
	@Override
    protected Dialog onCreateDialog(int id) 
	{
		switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
			}
    return null;
       
    }
	private DatePickerDialog.OnDateSetListener mDateSetListener =
         new DatePickerDialog.OnDateSetListener() 
		{
             public void onDateSet(DatePicker view, int year, int monthOfYear,
                     int dayOfMonth) {
                 mYear = year;
                 mMonth = monthOfYear;
                 mDay = dayOfMonth;
                 updateDisplay();
             }
         };
         
         
    @Override      
	public void onClick(View v)
	{
    	if(v.getId()==R.id.btnCustomItem)
    	{
    		Intent intent = new Intent(this,AddCustomItem.class);
    		if(action.equals(Information.ACTIVITY_EDITITEM))
			{
    			intent.putExtra(information.SubFolderID, mSubFolderId);
    			intent.putExtra(subfolder.intOld, mIntOLD);
    			
			}
			else
			{
				intent.putExtra(information.SubFolderID, 0);
				intent.putExtra(subfolder.intOld, mIntOLD);
			}
    		
    		startActivityForResult(intent,ACTIVITY_CREATE_CUSTITEM); 
    	}
    	else if(v.getId()==R.id.saveItem)
    	{
    		if(mInfoID!=59)
    		{
    			
    			if(ItemTextNumericDate.getVisibility()==0)
    			{
    				mItemValue = ItemTextNumericDate.getText().toString();
    			}
    			else
    			{
    				mItemValue = ItemMultiText.getText().toString();
    			}
    			if(action.equals(Information.ACTIVITY_EDITITEM))
    			{
    				
    					IsReturn = mDbHelper.UpdateItem(mItemType, mItemName, mItemValue, mInfoID);
    		    }
    			else if(action.equals(AddNewItem.ACTIVITY_CREATE_CUSTITEM))
    			{
    				IsReturn = mDbHelper.AddNewItem(mItemType, mItemName, mItemValue, mSubFolderId);
    			}
    			else if(action.equals(Information.ACTIVITY_ADDNEWITEM))
    			{
    				IsReturn = mDbHelper.AddNewItem(mItemType, mItemName, mItemValue, mSubFolderId);
    			}
    		
    			if(IsReturn)
    			{
    				setResult(RESULT_OK);
    				finish();
    			}
    		}
    		else if(mInfoID==59) //No Item is selected
    		{
    			alertDialog = new AlertDialog.Builder(this).create();
            	alertDialog.setTitle("Item Type");
				alertDialog.setMessage("Please select Item Type.");
            	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
   	             public void onClick(DialogInterface dialog, int which) {
   	                     return;
   	             }
   	         	});
            	alertDialog.show();
    		}	
    	}
    }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        outState.putLong(information.SubFolderID, mSubFolderId);
        outState.putLong(information._ID, mInfoID);
        
    }
	public int getIndexFromElementID(Cursor c, Long ItemID) 
	{
		
		if (c.getCount() > 0) 
		{
		    while (c.moveToNext()) 
		    {
		    	
	            if(c.getLong(c.getColumnIndexOrThrow(information._ID))==ItemID) 
	            {
	                return c.getPosition();
	                
	            }
		    }
		   
		}
		
		return 0;
	     
	} 
	public int getIndexFromElementName(Cursor c, String ItemName) 
	{
		
		if (c.getCount() > 0) 
		{
		    while (c.moveToNext()) 
		    {
		    	String strInfoName =  c.getString(c.getColumnIndexOrThrow(information.InfoName));
	            if(strInfoName.equalsIgnoreCase(ItemName)) 
	            {
	                return c.getPosition();
	                
	            }
		    }
		   
		}
		
		return 0;
	     
	} 
	public void filldata()
	{
		if(mSubFolderId !=null)
		{
			s1 = (Spinner)findViewById(R.id.spinner1);
			
			if(action.equals(Information.ACTIVITY_EDITITEM))
			{
				if(mIntOLD==0)
				{
					infoCursor = mDbHelper.FetchAllInformationBySubFolderId(0);
				}
				else
				{
					infoCursor = mDbHelper.FetchAllInformationBySubFolderId(mSubFolderId);
				}
				
			}
			else
			{
				if(mIntOLD==0)
				{
					infoCursor = mDbHelper.FetchAllInformationBySubFolderId(0);
				}
				else
				{
					infoCursor = mDbHelper.FetchAllInformationBySubFolderId(mSubFolderId);
				}
				
			}
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				    this,
				    android.R.layout.simple_spinner_item,
				    infoCursor,
				    new String[] {information.InfoName},
				    new int[] {android.R.id.text1});

					adapter.setDropDownViewResource(
				    android.R.layout.simple_spinner_dropdown_item);
					
					s1.setAdapter(adapter);
					//s1.setSelection(infoCursor.getPosition());
					
					s1.setOnItemSelectedListener(new OnItemSelectedListener() {
	                    public void onItemSelected(AdapterView<?> parent, View v,
	                              int position, long id) 
	                    {
	                    	mItemName = infoCursor.getString(infoCursor.getColumnIndex(information.InfoName));
	                    	mItemType = infoCursor.getString(infoCursor.getColumnIndex(information.InfoDataType));
	                    	//mItemValue = infoCursor.getString(infoCursor.getColumnIndex(information.InfoValue));
	                    	//mInfoID = infoCursor.getLong(infoCursor.getColumnIndex(information._ID));
	                    	
	                    	SetTextBoxProperty();
	                    	
	                    }
	                    
	                    public void onNothingSelected(AdapterView<?> arg0) 
	                    {
	                    	
	                    }
	               });
					if(mInfoID>0)
					{
						
						if(mIntOLD==0)
						{
							s1.setSelection(getIndexFromElementName(infoCursor,mItemName));
						}
						else
						{
							s1.setSelection(getIndexFromElementID(infoCursor,mInfoID));
						}
						
					}
					else
					{
						s1.setSelection(0);
					}
					
	        
		}
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
            
        
        switch(requestCode)
        {
       
        	case ACTIVITY_CREATE_CUSTITEM:
        	if( resultCode == RESULT_OK)
        	{
        		//mSubFolderId = intent.getLongExtra(information.SubFolderID, mSubFolderId);
        		//mInfoID = intent.getLongExtra(information._ID);
        		//mItemName =  intent.getStringExtra(information.InfoName);
        		//mItemValue = intent.getStringExtra(information.InfoValue);
        		//mItemType = intent.getStringExtra(information.InfoDataType);
        		mInfoID = intent.getLongExtra("mItemID",0);
        		if(mIntOLD==0)
        		{
        			action = 5;
        		}
        		//s1.setSelection(getIndexFromElementID(infoCursor,intent.getLongExtra("mItemID",0)));
        		
        		break;
        	}
        	
        }
        populateFields();
        filldata();	
        if(ItemTextNumericDate.getVisibility()==0)
		{
			ItemTextNumericDate.setText("");
		}
		else
		{
			ItemMultiText.setText("");
		}
        
    }       
}
