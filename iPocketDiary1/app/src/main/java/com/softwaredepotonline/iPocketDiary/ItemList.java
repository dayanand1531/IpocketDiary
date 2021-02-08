package com.softwaredepotonline.iPocketDiary;

		import com.softwaredepotonline.iPocketDiary.PocketDiary.folder;
		import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;
		import android.app.Activity;
		import android.os.Bundle;
		import android.view.LayoutInflater;
		import android.view.Menu;
		import android.view.View;
		import android.view.ViewGroup;
		import android.view.Window;
		import android.view.View.OnClickListener;
		import android.widget.AdapterView;
		import android.widget.Button;
		import android.widget.CursorAdapter;
		import android.widget.ImageView;
		import android.widget.ListView;
		import android.widget.TextView;
		import android.widget.Toast;
		import android.content.Context;
		import android.content.Intent;
		import android.database.Cursor;
		import android.widget.AdapterView.OnItemClickListener;

public class ItemList extends Activity implements OnClickListener
{
	public static final int HELP_ID = Menu.FIRST;
	public static final int ADD_CATEGORY = Menu.FIRST + 1;
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_CREATE_SUBFOLDER=1;
	private ListView lv;
	private PocketDbAdapter mDbHelper;
	private Cursor folderCursor;
	Button menu_help;
	Button btnAddNewFolder;
	/** Called when the activity is first created. */
	@Override

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		setContentView(R.layout.folder_list_main);
		lv = (ListView)findViewById(R.id.lvfoldermain);
		mDbHelper = new PocketDbAdapter(this);
		mDbHelper.open();
		fillData();
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView centertext = (TextView) findViewById(R.id.center_text);
		centertext.setText("Pocket Diary");
		menu_help = (Button)findViewById(R.id.menu_help);
		btnAddNewFolder= (Button)findViewById(R.id.btnAddNewFolder);
		menu_help.setOnClickListener(this);
		btnAddNewFolder.setOnClickListener(this);
	}
	public void onClick(View v)
	{

		if(v.getId()==R.id.menu_help)
		{
			Intent changePassIntent= new Intent(this,Help.class);
			startActivity(changePassIntent);
		}
		else if(v.getId()==R.id.btnAddNewFolder)
		{
			Intent folderEditorIntent= new Intent(this,FolderEditor.class);
			startActivityForResult(folderEditorIntent,ACTIVITY_CREATE);
		}
	}
	private void fillData()
	{
		try
		{
			folderCursor = mDbHelper.FetchAllFolders();
			startManagingCursor(folderCursor);
			lv.setAdapter(new EfficientAdapter(this,folderCursor));
			lv.setOnItemClickListener(new OnItemClickListener()
			{
				public void onItemClick(AdapterView<?> parent, View v, int position, long id)
				{
					Intent SubFolderListIntent = new Intent(ItemList.this, SubItemList.class);
					SubFolderListIntent.putExtra(subfolder.FolderID, id);
					startActivityForResult(SubFolderListIntent,ACTIVITY_CREATE_SUBFOLDER);
				}
			});
		}
		catch (Throwable er)
		{
			Toast.makeText(this, "Error "+er.getMessage(), Toast.LENGTH_LONG).show();

		}

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
			int resID = getResources().getIdentifier(cursor.getString(cursor.getColumnIndex(folder.FolderImage)), "drawable", "com.softwaredepotonline.iPocketDiary");
			ImageView s = (ImageView) view.findViewById(R.id.FolderIcon);
			s.setImageResource(resID);
			TextView t = (TextView) view.findViewById(R.id.sFolderName);
			t.setText(cursor.getString(cursor.getColumnIndex(folder.FolderName)));


		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			final View view = mInflater.inflate(R.layout.folderlist_item, parent, false);

			return view;

		}


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME))
        {
        	Toast msg = Toast.makeText(this, "Home Button is pressed.", Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();

        }
        return super.onKeyDown(keyCode, event);
    }*/

}
