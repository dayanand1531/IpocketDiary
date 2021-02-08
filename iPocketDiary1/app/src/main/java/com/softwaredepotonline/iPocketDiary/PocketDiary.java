package com.softwaredepotonline.iPocketDiary;
import android.provider.BaseColumns;


public final class PocketDiary 
{
	// This class cannot be instantiated
    private PocketDiary() {}
    /**
     * Folder Table
     */
    public static final class folder implements BaseColumns {
        // This class cannot be instantiated
        private folder() {}
        
    /**
     * The default sort order for this table
     */
    public static final String DEFAULT_SORT_ORDER = "modified DESC";

    /**
     * The name of the folder
     * <P>Type: TEXT</P>
     */
    public static final String FolderName = "FolderName";

    /**
     * The name of the folder Image
     * <P>Type: TEXT</P>
     */
    public static final String FolderImage = "FolderImage";

    /**
     * The timestamp for when the folder was created
     * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
     */
    public static final String CREATED_DATE = "created";

    /**
     * The timestamp for when the folder was last modified
     * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
     */
    public static final String MODIFIED_DATE = "modified";
}
    /**
     * Sub Folder Table
     */
    public static final class subfolder implements BaseColumns {
        // This class cannot be instantiated
        private subfolder() {}
    /**
     * The default sort order for this table
     */
    public static final String DEFAULT_SORT_ORDER = "modified DESC";

    /**
     * The name of the folder
     * <P>Type: TEXT</P>
     */
    public static final String SubFolderName = "SubFolderName";

    /**
     * The name of the folder Image
     * <P>Type: TEXT</P>
     */
    public static final String SubFolderImage = "SubFolderImage";
    /**
     * The name of the folder image
     * <P>Type: TEXT</P>
     */
    public static final String FolderID = "FolderID";
     /**
     * The timestamp for when the folder was created
     * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
     */
    public static final String intOld = "intOld";
    public static final String CREATED_DATE = "created";

    /**
     * The timestamp for when the folder was last modified
     * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
     */
    public static final String MODIFIED_DATE = "modified";
}
    /**
     * Information Table
    */
    public static final class information implements BaseColumns
    {
    	// This class cannot be instantiated
        private information() {}
        
        public static final String SubFolderID = "SubFolderID";
        
        public static final String InfoName = "InfoName";
        
        public static final String InfoDataType = "InfoDataType";
        
        public static final String InfoValue = "InfoValue";
            
        public static final String CREATED_DATE = "created";
        
        public static final String MODIFIED_DATE = "modified";
        
        public static final String ITEM_NUMERIC = "0";
        
        public static final String ITEM_TEXT = "1";
        
        public static final String ITEM_MULTITEXT = "2";
        
        public static final String ITEM_DATE = "3";
        
    }
    
}
