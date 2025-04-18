# Sqlite Revision .

-------------

- **The SQLiteOpenHelper**

  • Create a subclass of **SQLiteOpenHelper** implementing **onCreate(SQLiteDatabase)**, **onUpgrade(SQLiteDatabase, int, int)**

  • This class takes care of opening the database if it exists, creating it if it does not, and upgrading it as necessary.

- **onCreate():** Called when the database is created for the first time. Creation of tables and initial data inside the tables should be put here. 

- **onUpgrade():** Called when the database needs to be upgraded. Use this method to drop tables, add tables, or do anything else it needs to upgrade to the new schema version. 

  - If you add new columns you can use ALTER TABLE to insert them into a live table. 
  - If you rename or remove columns you can use ALTER TABLE to rename the old table, then create the new table and then populate the new table with the contents of the old table.

-  SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks. 

- Database names must be unique within an application, not across all applications. 

- public void execSQL(String sql) 

  - Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data. 
  - Multiple statements separated by semicolons are not supported. 
  - If the SQL string is invalid, throws an SQLException

- SQLHelper.java :

````java
class SQLHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "sqldatabase.db";
    private static final String TABLE_NAME = "SQLTABLE";
    private static final String UID = "_id";
    private static final String NAME = "Name";
    private static final int DATABASE_VERSION = 1;

    VivzHelper(Context context)
    {
        super (context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    public void onCreate(SQLiteDatabase db)
    {
        try{
            db.execSQL( "CREATE TABLE "+TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255));" );
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE "+TABLE_NAME+" IF EXISTS VIVZTABLE");
    }
}
````

- MainActivity.java :

  ````java
  package slidenerd.sqlrev;
  
  import android.os.Bundle;
  
  public class MainActivity extends Activity {
      SQLHelper sqlhelper;
  
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          sqlhelper = new SQLHelper(this);
      }
  }
  ````

  - **Note** : SQLite is efficient because it avoids the database creation overhead by creating the database only the first time someone tries accessing it. By initializing SQLHelper, we have not yet accessed the database, to access the database object that represents the physical database file stored on our device, call the **getWritableDatabase()** method and that’ll trigger the other lifecycle methods of **SQLiteOpenHelper**

  - ````java
    package slidenerd.sqlrev;
    
    import android.os.Bundle;
    
    public class MainActivity extends Activity {
        SQLHelper sqlhelper;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            sqlhelper = new SQLHelper(this);
            
            SQLiteDatabase sqLiteDatabase = sqlhelper.getWritableDatabase();
            // After That the onCreate() method is called.
            // And you can find the database on the files 
        }
    }
    ````

    --------------------------

  - ## SQLite Database Insert Statement:

    - **ContentValues contentValues=new ContentValues();**
      For every column specify the name of column as KEY
      And the data to be put as value
      contentValues.put(VivzHelper.NAME, "Vivz");
    - **long id=db.insert(VivzHelper.TABLE_NAME, null, contentValues);**
      TableName, NullColumnHack, ContentValues object
      id=row ID of inserted row or -1 if the operation fails
    - Important Note :
      - ![image-20250418154358248](/home/awadin/snap/typora/96/.config/Typora/typora-user-images/image-20250418154358248.png)

- 