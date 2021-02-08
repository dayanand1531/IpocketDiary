package com.softwaredepotonline.iPocketDiary;
import com.softwaredepotonline.iPocketDiary.PocketDiary.folder;
import com.softwaredepotonline.iPocketDiary.PocketDiary.information;
import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SubItemList extends Activity implements OnClickListener
{
	
	public static final int ADD_SUBCATEGORY = Menu.FIRST;
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_CREATE_INFO=1;
	private Long mFolderId;
	private PocketDbAdapter mDbHelper;
	private Cursor subfolderCursor;
	private ListView lv;
	String FolderName;
	Button btnAddNewSubFolder;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		  setContentView(R.layout.subfoldelist_main);
		  lv = (ListView)findViewById(R.id.lvSubFolderMain);
		  mDbHelper = new PocketDbAdapter(this);
		  mDbHelper.open();
		  
		  setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		  
		  mFolderId = savedInstanceState!=null ? savedInstanceState.getLong(subfolder.FolderID): null;
		  if(mFolderId == null)
		  {
			  Bundle extras = getIntent().getExtras();
			  mFolderId = extras !=null? extras.getLong(subfolder.FolderID) : null;
		  }
		  fillData();
		  getSubFolderName();
		  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
	      final TextView centertext = (TextView) findViewById(R.id.center_text);
	      centertext.setText(FolderName);  
	      btnAddNewSubFolder = (Button)findViewById(R.id.btnAddNewSubFolder);
	      btnAddNewSubFolder.setOnClickListener(this);
		  		  		  
	}
	public void onClick(View v)
	{
		
		if(v.getId()==R.id.btnAddNewSubFolder)
		{
			Intent SubFolderEditorIntent= new Intent(this,SubFolderEditor.class);
			SubFolderEditorIntent.putExtra(subfolder.FolderID, mFolderId);
			startActivityForResult(SubFolderEditorIntent, ACTIVITY_CREATE);
		}
		
	}
	private void getSubFolderName()
	{
		Cursor folderName =  mDbHelper.FetchFolderName(mFolderId);
		FolderName = folderName.getString(folderName.getColumnIndexOrThrow(folder.FolderName));
	}
	private void fillData()
	{
		try
		{
			if(mFolderId !=null)
			{
				subfolderCursor = mDbHelper.FetchAllSubFoldersByFolderId(mFolderId);
				if(subfolderCursor.getCount()<1)
				{
					Toast msg = Toast.makeText(this, "Click + to add new Sub Folder.", Toast.LENGTH_LONG);

					msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

					msg.show();

					
				}
				startManagingCursor(subfolderCursor);
				lv.setAdapter(new EfficientAdapter(this,subfolderCursor));
				lv.setOnItemClickListener(new OnItemClickListener() 
				{
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
				    {
						Cursor c = subfolderCursor;
				    	Intent informationIntent = new Intent(SubItemList.this, Information.class);
				    	informationIntent.putExtra(information.SubFolderID, id);
				    	informationIntent.putExtra(subfolder.SubFolderName, c.getString(c.getColumnIndexOrThrow(subfolder.SubFolderName)));
				    	informationIntent.putExtra(subfolder.SubFolderImage, c.getString(c.getColumnIndexOrThrow(subfolder.SubFolderImage)));
				    	startActivityForResult(informationIntent,ACTIVITY_CREATE_INFO);
				    }
				});
			}
			
			
		}
		catch (Throwable er) 
		{
			Toast.makeText(this, "Error "+er.getMessage(), Toast.LENGTH_LONG).show();

		}
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        outState.putLong(subfolder.FolderID, mFolderId);
    }
	private class EfficientAdapter extends CursorAdapter
	{
		
    	private final LayoutInflater mInflater;
    	
    	public EfficientAdapter(Context context, Cursor cursor)
    	{
    		super(context,cursor,true);
    		mInflater = LayoutInflater.from(context);
    		   		
    	}

    	@Override
		public void bindView(View view, Context context, Cursor cursor) 
		{
			// TODO Auto-generated method stub
    		int resID = getResources().getIdentifier(cursor.getString(cursor.getColumnIndex(subfolder.SubFolderImage)), "drawable", "com.softwaredepotonline.iPocketDiary");
    		ImageView s = (ImageView) view.findViewById(R.id.FolderIcon);
    		s.setImageResource(resID);
    		TextView t = (TextView) view.findViewById(R.id.sFolderName);
    		t.setText(cursor.getString(cursor.getColumnIndex(subfolder.SubFolderName)));
    		
    		
		}

		@Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) 
        {
			// TODO Auto-generated method stub
    		final View view = mInflater.inflate(R.layout.subfolderlist_item, parent, false);

			return view;

        }
		
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}


