package com.softwaredepotonline.iPocketDiary;

import com.softwaredepotonline.iPocketDiary.PocketDiary.information;
import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Menu;
//import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class Information extends Activity implements OnClickListener
{
	public static final int ADD_ITEM = Menu.FIRST;
	public static final int DELETE_FOLDER = Menu.FIRST + 1;
	public static final int ACTIVITY_ADDNEWITEM=0;
	public static final int ACTIVITY_EDITITEM=1;
	public static final int ACTIVITY_EDITSUBFOLDER=2;
	private static final int DIALOG_YES_NO_LONG_MESSAGE = 2;
	private Long mSubFolderId;
	private TextView tvSubFolderName;
	private ImageView ivSubFolderImage;
	private Cursor folderCursor;
	private ListView lv;
	Button btnEditSubFolder;
	Button btnAddNewItem;
	Button btnDeleteSubFolder;
	Integer intOld;
	private PocketDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.info_main);
		lv = (ListView)findViewById(R.id.ListView01);
		btnEditSubFolder = (Button)findViewById(R.id.btnEditSubFolder);
		btnAddNewItem = (Button)findViewById(R.id.btnAddNewItem);
		btnDeleteSubFolder = (Button)findViewById(R.id.btnDeleteSubFolder);
		mDbHelper = new PocketDbAdapter(this);
		mDbHelper.open();
		Bundle extras = getIntent().getExtras();
		mSubFolderId = savedInstanceState!=null ? savedInstanceState.getLong(information.SubFolderID): null;
		
		if(mSubFolderId == null)
		{
			mSubFolderId = extras !=null? extras.getLong(information.SubFolderID) : null;
		}
		populateFields();
		fillData();
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView centertext = (TextView) findViewById(R.id.center_text);
	    centertext.setText("Information");
	    btnEditSubFolder.setOnClickListener(this);
	    btnDeleteSubFolder.setOnClickListener(this);
	    btnAddNewItem.setOnClickListener(this);
	    
	}
	 	@Override
	    protected Dialog onCreateDialog(int id) 
	 	{
	        switch (id) 
	        {
	        	case DIALOG_YES_NO_LONG_MESSAGE:
	        		return new AlertDialog.Builder(Information.this)
	                .setIcon(R.drawable.alert_dialog_icon)
	                .setTitle("Confirmation")
	                .setMessage("Are you sure you want to delete this folder?")
	                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) 
	                    {
	                    	mDbHelper.DeleteSubFolder(mSubFolderId);
	                    	mDbHelper.DeleteInfo(mSubFolderId);
	                    	setResult(RESULT_OK);
	        				finish();
	                        return;
	                        
	                    }
	                })
	                
	                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	                {
	                    public void onClick(DialogInterface dialog, int whichButton) {

	                        /* User clicked Cancel so do some stuff */
	                    }
	                })
	                .create();
	        }
	        return null;
	 }
	public void onClick(View v)
	{
		
		if(v.getId()==R.id.btnEditSubFolder)
		{
			Intent intent = new Intent(this, SubFolderEditor.class);
			intent.putExtra(subfolder._ID, mSubFolderId);
			startActivityForResult(intent,ACTIVITY_EDITSUBFOLDER);
			
		}
		if(v.getId()==R.id.btnAddNewItem)
		{
			Intent addNewItemIntent= new Intent(this,AddNewItem.class);
			addNewItemIntent.putExtra(information.SubFolderID, mSubFolderId);
			addNewItemIntent.putExtra(subfolder.intOld, intOld);
			addNewItemIntent.putExtra("action" , ACTIVITY_ADDNEWITEM);
			startActivityForResult(addNewItemIntent,ACTIVITY_ADDNEWITEM);
		}
		if(v.getId()==R.id.btnDeleteSubFolder)
		{
			showDialog(DIALOG_YES_NO_LONG_MESSAGE);
		}
	}
	private void fillData()
	{
		try
		{
			folderCursor = mDbHelper.FetchAllInformationBySubFolderId(mSubFolderId);
			startManagingCursor(folderCursor);
			if(folderCursor.getCount()<1)
			{
				Toast msg = Toast.makeText(this, "Click + to add New Item.", Toast.LENGTH_LONG);

				msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

				msg.show();
			
			}
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.info_layout,folderCursor, 
					new String[] {information.InfoName,information.InfoValue},
					new int[] { R.id.infoname, R.id.infovalue
					});
			//adapter.setViewBinder(new ShowViewBinder());
			lv.setAdapter(adapter); 
			
			lv.setOnItemClickListener(new OnItemClickListener() 
			{
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
			    {
			    	Cursor c = folderCursor;
			    	Intent editItemIntent = new Intent(Information.this, AddNewItem.class);
			    	editItemIntent.putExtra(information.SubFolderID , mSubFolderId);
			    	editItemIntent.putExtra(information._ID , c.getLong(c.getColumnIndexOrThrow(information._ID)));
			    	editItemIntent.putExtra(subfolder.intOld, intOld);
			    	editItemIntent.putExtra("action" , ACTIVITY_EDITITEM);
			    	startActivityForResult(editItemIntent,ACTIVITY_EDITITEM); 
			    }
	     	});

		}
		catch (Throwable er) 
		{
			Toast.makeText(this, "Error "+er.getMessage(), Toast.LENGTH_LONG).show();

		}
		
	}
	public void populateFields() 
	{
		ivSubFolderImage = (ImageView)findViewById(R.id.subFolderImage);
		tvSubFolderName = (TextView)findViewById(R.id.subFolderName);
		Cursor csubfolder = mDbHelper.FetchSubFolder(mSubFolderId);
		startManagingCursor(csubfolder);
		tvSubFolderName.setText(csubfolder.getString(csubfolder.getColumnIndexOrThrow(subfolder.SubFolderName)));
    	String SubFolderImage = csubfolder.getString(csubfolder.getColumnIndexOrThrow(subfolder.SubFolderImage));
    	int resID = getResources().getIdentifier(SubFolderImage, "drawable", "com.softwaredepotonline.iPocketDiary");
    	ivSubFolderImage.setImageResource(resID);
    	intOld = csubfolder.getInt(csubfolder.getColumnIndexOrThrow(subfolder.intOld));
		
	}
	
	
	 class ShowViewBinder implements SimpleCursorAdapter.ViewBinder 
	 {
		 boolean retval = false;
		 public boolean setViewValue(View view, Cursor cursor, int columnIndex) 
		 {
			 switch(view.getId()) 
			 {
			 	case R.id.infoname:
				 TextView tv1 = (TextView) view;
				 tv1.setText(cursor.getString(columnIndex));
				 retval = true;
                 break;

				 case R.id.infovalue:
				 TextView tv2 = (TextView) view;
				 tv2.setText(cursor.getString(columnIndex));
				 retval = true;
                 break;
			 }
			 return retval;

		 }
	 }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        outState.putLong(information.SubFolderID, mSubFolderId);
    }
	/*
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	//if(folderCursor.getCount()==0)
    	//{
    		menu.add(0,ADD_ITEM , 0, "Add New Item")
    		.setShortcut('0', 'a')
    		.setIcon(android.R.drawable.ic_menu_add);
    	//}
    	menu.add(0, DELETE_FOLDER , 0, "Delete Folder")
    	.setShortcut('1', 'd')
    	.setIcon(android.R.drawable.ic_menu_delete);
    	    	
    	return true;
    }
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	    	switch(item.getItemId())
	    	{
	    		case ADD_ITEM:
	    			Intent addNewItemIntent= new Intent(this,AddNewItem.class);
	    			addNewItemIntent.putExtra(information.SubFolderID, mSubFolderId);
	    			addNewItemIntent.putExtra("action" , ACTIVITY_ADDNEWITEM);
	    			startActivityForResult(addNewItemIntent,ACTIVITY_ADDNEWITEM);
	    			break;
	    		case DELETE_FOLDER:
	    			showDialog(DIALOG_YES_NO_LONG_MESSAGE);
	    			break;
	    		
	    	}
	    	return super.onOptionsItemSelected(item);
	    }
	 	*/
	   
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, 
	                                    Intent intent) {
	        super.onActivityResult(requestCode, resultCode, intent);
	        populateFields();
	        fillData();
	    }
}
