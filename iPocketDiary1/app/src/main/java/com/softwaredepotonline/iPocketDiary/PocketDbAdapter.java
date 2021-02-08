package com.softwaredepotonline.iPocketDiary;

import com.softwaredepotonline.iPocketDiary.PocketDiary.folder;
import com.softwaredepotonline.iPocketDiary.PocketDiary.subfolder;
import com.softwaredepotonline.iPocketDiary.PocketDiary.information;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.io.*;

public class PocketDbAdapter 
{
	 private static final String TAG = "PocketDbAdapter";
	 private static final String DATABASE_NAME ="pocketdiary";
	 private static final int DATABASE_VERSION = 2;
	 private static final String Folder_TABLE_NAME = "folder";
	 private static final String SUBFOlDER_TABLE_NAME = "subfolder";
	 private static final String INFORMATION_TABLE_NAME = "Information";
	 private final Context mCtx;
	 private DatabaseHelper mDbHelper;
	 private SQLiteDatabase mDb;
	 private static String DB_PATH = "/data/data/com.softwaredepotonline.iPocketDiary/databases/";
	
	 /**
	     * This class helps open, create, and upgrade the database file.
	 */
	 public class DatabaseHelper extends SQLiteOpenHelper 
	 {

	        DatabaseHelper(Context context) 
	        {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }
	        
	        /**
		     * Creates a empty database on the system and rewrites it with your own database.
		     * 
		     */
		    public void createDataBase() throws IOException
		    {
		 
		    	boolean dbExist = checkDataBase();
		 
		    	if(dbExist)
		    	{
		    		//do nothing - database already exist
		    	}
		    	else
		    	{
		 
		    		//By calling this method and empty database will be created into the default system path
		            //of your application so we are gonna be able to overwrite that database with our database.
		    		this.getWritableDatabase();
		 
		        	try 
		        	{
		 
		    			copyDataBase();
		 
		    		} 
		        	catch (IOException e) 
		        	{
		 
		        		throw new Error("Error copying database");
		        	}
		    	}
		 
		    }

		    /**
		     * Check if the database already exist to avoid re-copying the file each time you open the application.
		     * @return true if it exists, false if it doesn't
		     */
		    private boolean checkDataBase()
		    {
		 
		    	SQLiteDatabase checkDB = null;
		 
		    	try
		    	{
		    		String myPath = DB_PATH + DATABASE_NAME;
		    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		 
		    	}
		    	catch(SQLiteException e)
		    	{
		 
		    		//database does't exist yet.
		 
		    	}
		 
		    	if(checkDB != null)
		    	{
		 
		    		checkDB.close();
		 
		    	}
		 
		    	return checkDB != null ? true : false;
		    }
		    
		    /**
		     * Copies your database from your local assets-folder to the just created empty database in the
		     * system folder, from where it can be accessed and handled.
		     * This is done by transfering bytestream.
		     * */
		    private void copyDataBase() throws IOException
		    {
		 
		    	//Open your local db as the input stream
		    	InputStream myInput = mCtx.getAssets().open(DATABASE_NAME);
		 
		    	// Path to the just created empty db
		    	String outFileName = DB_PATH + DATABASE_NAME;
		 
		    	//Open the empty db as the output stream
		    	OutputStream myOutput = new FileOutputStream(outFileName);
		 
		    	//transfer bytes from the inputfile to the outputfile
		    	byte[] buffer = new byte[1024];
		    	int length;
		    	while ((length = myInput.read(buffer))>0){
		    		myOutput.write(buffer, 0, length);
		    	}
		 
		    	//Close the streams
		    	myOutput.flush();
		    	myOutput.close();
		    	myInput.close();
		    	
		    }
		 
		    public void openDataBase() throws SQLException
		    {
		    	 
		    	//Open the database
		        String myPath = DB_PATH + DATABASE_NAME;
		        mDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		       
		    }

		    @Override
		    public synchronized void close() 
		    {
		    	if(mDb != null)
		    		mDb.close();
		    	super.close();
		    }
		    
	        @Override
	        public void onCreate(SQLiteDatabase db) 
	        {
	        	        	
	        }
	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	        {
	        	
	        	
	        	Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS folder");
	            onCreate(db);
	        }
	        
	        
	 }
	 
	    
	    


	 /**
	     * Constructor - takes the context to allow the database to be
	     * opened/created
	     * 
	     * @param ctx the Context within which to work
	     */
	    public PocketDbAdapter(Context ctx) 
	    {
	        this.mCtx = ctx;
	    }

	    /**
	     * Open the notes database. If it cannot be opened, try to create a new
	     * instance of the database. If it cannot be created, throw an exception to
	     * signal the failure
	     * 
	     * @return this (self reference, allowing this to be chained in an
	     *         initialization call)
	     * @throws SQLException if the database could be neither opened or created
	     */
	    public PocketDbAdapter open() throws SQLException 
	    {
	        mDbHelper = new DatabaseHelper(mCtx);
	        try 
	        { 
     			mDbHelper.createDataBase(); 
     		} 
	        catch (IOException ioe) 
		 	{ 
	        	System.out.print("Unable to create database"); //throw new Error("Unable to create database"); 
		 	}
	        
	        try 
	        {
	           	mDbHelper.openDataBase();
	     
	     	}
	        catch(SQLException sqle)
	        {
	       		throw sqle;
	      	}

	        
	        return this;
	    }
	    	    
	    public void close() 
	    {
	        mDbHelper.close();
	    }
	    
	    public long CreateFolder(String FolderName,String FolderImage)
	    {
	    	//Long now = Long.valueOf(System.currentTimeMillis());
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put(folder.FolderName, FolderName);
	    	initialValues.put(folder.FolderImage, FolderImage);
	    	//initialValues.put(folder.CREATED_DATE, now);
	    	//initialValues.put(folder.MODIFIED_DATE, now);
	    	return mDb.insert(Folder_TABLE_NAME, null, initialValues);
	    	
	    }
	    public long CreateSubFolder(String SubFolderName,String SubFolderImage, Long FolderId)
	    {
	    	
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put(subfolder.SubFolderName, SubFolderName);
	    	initialValues.put(subfolder.SubFolderImage, SubFolderImage);
	    	initialValues.put(subfolder.intOld, 0);
	    	initialValues.put(subfolder.FolderID, FolderId);
	    	
	    	return mDb.insert(SUBFOlDER_TABLE_NAME, null, initialValues);
	    }
	    public long CreateInformation(String InfoDataType,String InfoName, long SubFolderID)
	    {
	    	
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put(information.InfoDataType, InfoDataType);
	    	initialValues.put(information.InfoName, InfoName);
	    	initialValues.put(information.SubFolderID, SubFolderID);
	    	
	    	return mDb.insert(INFORMATION_TABLE_NAME, null, initialValues);
	    	
	    }
	    public boolean AddNewItem(String InfoDataType,String InfoName, String InfoValue, Long SubFolderID)
	    {
	    	
	    	ContentValues initialValues = new ContentValues();
	    	initialValues.put(information.InfoDataType, InfoDataType);
	    	initialValues.put(information.InfoName, InfoName);
	    	initialValues.put(information.InfoValue, InfoValue);
	    	initialValues.put(information.SubFolderID, SubFolderID);
	    	return mDb.insert(INFORMATION_TABLE_NAME, null, initialValues)>0;
	    	
	    }
	    public boolean UpdateItem(String InfoDataType,String InfoName, String InfoValue,Long ItemID)
	    {
	    	
	    	ContentValues args = new ContentValues();
	    	args.put(information.InfoDataType, InfoDataType);
	    	args.put(information.InfoName, InfoName);
	    	args.put(information.InfoValue, InfoValue);
	    	return mDb.update(INFORMATION_TABLE_NAME, args, information._ID + "=" + ItemID, null) > 0;
	    	
	    }
	    public boolean UpdateSubFolder(String SubFolderName,String SubFolderImage, Long SubFolderId)
	    {	    	
	    	ContentValues args = new ContentValues();
	    	args.put(subfolder.SubFolderName, SubFolderName);
	    	args.put(subfolder.SubFolderImage, SubFolderImage);
	    	return mDb.update(SUBFOlDER_TABLE_NAME, args, subfolder._ID + "=" + SubFolderId, null) > 0;
	    	
	    }
	    public boolean DeleteSubFolder(long SubFolderId)
	    {
	    	return mDb.delete(SUBFOlDER_TABLE_NAME, subfolder._ID + "=" + SubFolderId, null) > 0;
	    	
	    }
	    public boolean DeleteInfo(long SubFolderId)
	    {
	    	return mDb.delete(INFORMATION_TABLE_NAME, information.SubFolderID + "=" + SubFolderId, null) > 0;
	    	
	    }
	    public boolean DeleteInformation(long InfoId)
	    {
	    	return mDb.delete(INFORMATION_TABLE_NAME, information._ID + "=" + InfoId, null) > 0;
	    	
	    }
	    public Cursor FetchAllFolders()
	    {
	    	return mDb.query(Folder_TABLE_NAME, new String[] {folder._ID, folder.FolderName,folder.FolderImage
	                }, null, null, null, null, null);
	    }
	    public Cursor FetchFolderName(long FolderId)
	    {
	    	Cursor mCursor =  mDb.query(Folder_TABLE_NAME, new String[] {folder.FolderName
	                }, folder._ID + "=" + FolderId, null, null, null, null, null);
	    	if (mCursor != null) 
	    	{
	    		mCursor.moveToFirst();
	    	}
	    	return mCursor;
	    }
	    public Cursor FetchAllSubFoldersByFolderId(long FolderId)throws SQLException
	    {
	    	Cursor mCursor = mDb.query(true,SUBFOlDER_TABLE_NAME, 
            			new String[] {subfolder._ID,subfolder.SubFolderName, subfolder.SubFolderImage, subfolder.FolderID}, 
                		subfolder.FolderID + "=" + FolderId, null,
                        null, null, null, null);
	    	if (mCursor != null) 
	    	{
	    		mCursor.moveToFirst();
	    	}
	    	return mCursor;
	    }
	    public Cursor FetchSubFolder(long SubFolderId)throws SQLException
	    {
	    	Cursor mCursor = mDb.query(true,SUBFOlDER_TABLE_NAME, 
            			new String[] {subfolder.SubFolderName, subfolder.SubFolderImage, subfolder.intOld}, 
                		subfolder._ID + "=" + SubFolderId, null,
                        null, null, null, null);
	    	if (mCursor != null) 
	    	{
	    		mCursor.moveToFirst();
	    	}
	    	return mCursor;
	    }
	    
	    public Cursor FetchAllInformationBySubFolderId(long SubFolderId)throws SQLException
	    {
	    	Cursor mCursor = mDb.query(true, INFORMATION_TABLE_NAME, new String[]{information._ID,information.InfoName, information.InfoValue,information.InfoDataType}
	    					,information.SubFolderID +"=" + SubFolderId, null, null, null, null, null);
	    	if(mCursor!=null)
	    	{
	    		mCursor.moveToNext();
	    	}
	    	
	    	return mCursor;
	    }
	    public Cursor FetchInformation(long infoID)throws SQLException
	    {
	    	Cursor mCursor = mDb.query(true, INFORMATION_TABLE_NAME, new String[]{information.InfoName, information.InfoValue,information.InfoDataType}
	    					,information._ID +"=" + infoID, null, null, null, null, null);
	    	if(mCursor!=null)
	    	{
	    		mCursor.moveToNext();
	    	}
	    	
	    	return mCursor;
	    }
	   
}
