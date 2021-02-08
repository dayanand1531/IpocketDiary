package com.softwaredepotonline.iPocketDiary;

import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class SubFolderEditor extends Activity implements OnClickListener
{
	GridView mGrid;
	Button mSaveFolder;
	private EditText mFolderName;
	private ImageView mFolderImage;
	private AlertDialog alertDialog;
	String ImageName;
	String SubFolderImage;
	private PocketDbAdapter mDbHelper;
	private Long mFolderId;
	private Long mSubFolderId;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mDbHelper = new PocketDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.folder_list);
		SubFolderImage = "defaultimage";
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
	    final TextView centertext = (TextView) findViewById(R.id.center_text);
	    centertext.setText("Create Sub Folder");
		mFolderId = savedInstanceState!=null ? savedInstanceState.getLong(subfolder.FolderID): null;
		Bundle extras = getIntent().getExtras();
		if(mFolderId == null)
		  {
			 
			  mFolderId = extras !=null? extras.getLong(subfolder.FolderID) : null;
		  }
		if(mSubFolderId == null)
		{
			mSubFolderId = extras !=null? extras.getLong(subfolder._ID) : null;
		}
		mFolderName = (EditText)findViewById(R.id.FolderName);
		mSaveFolder = (Button)findViewById(R.id.SaveFolder);
		populateFields();
		mGrid = (GridView) findViewById(R.id.myGrid);
	    mGrid.setAdapter(new AppsAdapter());
	    mGrid.setOnItemClickListener(new OnItemClickListener() 
	    {  
	    	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
            	
	    		int resID = getResources().getIdentifier(mIcons[position], "drawable", "com.softwaredepotonline.iPocketDiary");
            	mFolderImage.setImageResource(resID);
            	SubFolderImage = mIcons[position];
            	           	
            }
            
	    }
	    );
	    
	    mSaveFolder.setOnClickListener(this);
	}
	public void populateFields() 
	{
		
		
        if (mSubFolderId != null && mSubFolderId>0) 
        {
        	Cursor csubfolder = mDbHelper.FetchSubFolder(mSubFolderId);
        	startManagingCursor(csubfolder);
        	mFolderName.setText(csubfolder.getString(csubfolder.getColumnIndexOrThrow(subfolder.SubFolderName)));
        	SubFolderImage = csubfolder.getString(csubfolder.getColumnIndexOrThrow(subfolder.SubFolderImage));
        	int resID = getResources().getIdentifier(SubFolderImage, "drawable", "com.softwaredepotonline.iPocketDiary");
        	mFolderImage = (ImageView)findViewById(R.id.FolderImage);
    		mFolderImage.setImageResource(resID);
        	
        }
        else
        {
        	
        	mFolderName.setText("");
        	
        	int resID = getResources().getIdentifier("defaultimage", "drawable", "com.softwaredepotonline.iPocketDiary");
        	mFolderImage = (ImageView)findViewById(R.id.FolderImage);
    		mFolderImage.setImageResource(resID);
        }
        
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) 
	{
        super.onSaveInstanceState(outState);
        outState.putLong(subfolder.FolderID, mFolderId);
        outState.putLong(subfolder._ID, mSubFolderId);
    }
	public void onClick(View v)
	{
		if(v.getId()==R.id.SaveFolder)
		{
			String SubFolderName = mFolderName.getText().toString();
			if(SubFolderName.length()>1)
			{
				if(mSubFolderId==0)
				{
					long id = mDbHelper.CreateSubFolder(SubFolderName, SubFolderImage, mFolderId);
					if (id > 0) 
					{
						mSubFolderId = id;
		            }
				}
				else
				{
					mDbHelper.UpdateSubFolder(SubFolderName, SubFolderImage, mSubFolderId);
				}
				setResult(RESULT_OK);
				finish();
                
			}
			else
			{
				alertDialog = new AlertDialog.Builder(this).create();
            	alertDialog.setTitle("Sub Folder");
				alertDialog.setMessage("Please enter sub folder name.");
            	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
   	             public void onClick(DialogInterface dialog, int which) {
   	                     return;
   	             }
   	         	});
            	alertDialog.show();
			}
			
			
		}
	}
	private String[] mIcons={"address","agreements",
							"airlinerewardpoints","americalexpress",
							"americanexpress","animal","bank","bankaccountdetails",
							"books","callingcardinformation","car","carinfo","carinsurance","citibank",
							"computer","creditcarddetails","drivinglicense","ebay","emailaccount",
							"facebook","finances","flickr",
							"fun","game",
							"google","health","home","hotelreservationinformation","hotelrewardpoints",
							"importantpasswords","insurance","insurancepolicies","internetproviderinformation",
							"itunes","keys",
							"mail","mobilephone",
							"movie","music",
							"music_2","musicone","musictwo","mycar","myspace",
							"others","pancard","pcinternet",
							"person","personalinformation",
							"piechart","pin",
							"restaruent","safe",
							"security","settings","shoping","sports",
							"transport","travel","travelling",
							"twitter","video",
							"visa",
							"yahoo"
		};
	public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;

            if (convertView == null) {
                i = new ImageView(SubFolderEditor.this);
                //i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                i.setLayoutParams(new GridView.LayoutParams(40, 40));
                i.setAdjustViewBounds(false);
                i.setScaleType(ImageView.ScaleType.CENTER_CROP);
                i.setPadding(2, 2, 2, 2);
            } else {
                i = (ImageView) convertView;
            }
            
            int resID = getResources().getIdentifier(mIcons[position], "drawable", "com.softwaredepotonline.iPocketDiary");
            i.setImageResource(resID);
            //i.setImageResource(mIcons[position]);

            return i;
        }


        public final int getCount() 
        {
            return mIcons.length;
        }

        public final Object getItem(int position) 
        {
            return position;
        }

        public final long getItemId(int position) 
        {
            return position;
        }
    }
}
