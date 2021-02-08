package com.softwaredepotonline.iPocketDiary;




import com.softwaredepotonline.iPocketDiary.PocketDiary.folder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FolderEditor extends Activity implements OnClickListener
{ 
	private PocketDbAdapter mDbHelper;
	private Long mFolderId;
   	GridView mGrid;
	Button mSaveFolder;
	private EditText mFolderName;
	private ImageView mFolderImage;
	String FolderImage;
	private AlertDialog alertDialog;
	String ImageName;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mDbHelper = new PocketDbAdapter(this);
		mDbHelper.open();
		FolderImage = "defaultimage";
		setContentView(R.layout.folder_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
	    final TextView centertext = (TextView) findViewById(R.id.center_text);
	    centertext.setText("Create Folder");
		int resID = getResources().getIdentifier("defaultimage", "drawable", "com.softwaredepotonline.iPocketDiary");
		mFolderImage = (ImageView)findViewById(R.id.FolderImage);
		mFolderImage.setImageResource(resID);
       	mFolderName = (EditText)findViewById(R.id.FolderName);
		
		mSaveFolder = (Button)findViewById(R.id.SaveFolder);
		
		mGrid = (GridView) findViewById(R.id.myGrid);
	    mGrid.setAdapter(new AppsAdapter());
	    mGrid.setOnItemClickListener(new OnItemClickListener() 
	    {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
            	//FolderIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_36);
            	int resID = getResources().getIdentifier(mIcons[position], "drawable", "com.softwaredepotonline.iPocketDiary");
            	mFolderImage.setImageResource(resID);
            	FolderImage = mIcons[position];
            	//mFolderImage.ge
            	//ImageName = getResourceNameFromClassByID(R.drawable.class,mThumbIds[position]);
            }
            
	    }
	    );
	    mSaveFolder.setOnClickListener(this);
	}
	public void onClick(View v)
	{
		if(v.getId()==R.id.SaveFolder)
		{
			String FolderName = mFolderName.getText().toString();
			//String FolderImage = "bank";
			if(FolderName.length()>1)
			{
				if(mFolderId==null)
				{
					long id = mDbHelper.CreateFolder(FolderName, FolderImage);
					if (id > 0) 
					{
						mFolderId = id;
		            }
				}
				
				setResult(RESULT_OK);
				finish();
                return;
			}
			else
			{
				alertDialog = new AlertDialog.Builder(this).create();
            	alertDialog.setTitle("Folder");
				alertDialog.setMessage("Please enter folder name.");
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
        outState.putLong(folder._ID, mFolderId);
    }
	private String[] mIcons={"address","agreements","airlinerewardpoints","americalexpress",
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
        public AppsAdapter() 
        {
        	
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;

            if (convertView == null) 
            {
                i = new ImageView(FolderEditor.this);
                //i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                i.setLayoutParams(new GridView.LayoutParams(40, 40));
                i.setAdjustViewBounds(false);
                i.setScaleType(ImageView.ScaleType.CENTER_CROP);
                i.setPadding(2, 2, 2, 2);
            } 
            else 
            {
                i = (ImageView) convertView;
            }
            int resID = getResources().getIdentifier(mIcons[position], "drawable", "com.softwaredepotonline.iPocketDiary");
            i.setImageResource(resID);

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
