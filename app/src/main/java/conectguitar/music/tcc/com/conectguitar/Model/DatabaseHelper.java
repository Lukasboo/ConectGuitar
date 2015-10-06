package conectguitar.music.tcc.com.conectguitar.Model;

/**
 * Created by lucas on 06/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "ConectGuitar.db";

    private static final String TABLE_NAME = "release";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_STATUS = "status";
    private static final String TABLE_CREATE = "create table release (id integer primary key not null , " +
            " description text not null, status integer not null );";

    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

        db.execSQL("insert into release (description, status) VALUES ('stage1', '1') " ); //id 1
        db.execSQL("insert into release (description, status) VALUES ('lesson1', '1') " );//id 2
        db.execSQL("insert into release (description, status) VALUES ('lesson2', '0') " );// id 3
        db.execSQL("insert into release (description, status) VALUES ('lesson3', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson4', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson5', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('stage2', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson6', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson7', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson8', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson9', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson10', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('stage3', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson11', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson12', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson13', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson14', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson15', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('stage4', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson16', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson17', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson18', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson19', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson20', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('stage5', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson21', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson22', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson23', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson24', '0') " );
        db.execSQL("insert into release (description, status) VALUES ('lesson25', '0') " );

    }

    public ArrayList<String> getReleases(){
        ArrayList<String> list=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        db.beginTransaction();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    list.add(description);
                }
            }
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }

    public String getStudentReleases(int students_IdRelease){

        db = this.getReadableDatabase();
        String selectQuery = "SELECT id, description  FROM " + TABLE_NAME + " WHERE id = " + students_IdRelease ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int a;
        String b;

        b = "not found";

        if (cursor.moveToFirst())
        {
            do{
                a = cursor.getInt(0);

                if(a==students_IdRelease)
                {
                    b = cursor.getString(1);
                    break;
                }

            }
            while(cursor.moveToNext());
        }

        return b;

    }

    public int searchIdRelease(String descriptionRelease){

        db = this.getReadableDatabase();
        String query = "select description , status, id from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a , b;
        int c;

        b = "not found";
        c = 0;

        if (cursor.moveToFirst())
        {
            do{
                a = cursor.getString(0);

                if(a.equals(descriptionRelease))
                {
                    b = cursor.getString(1);
                    c = cursor.getInt(2);
                    break;
                }

            }
            while(cursor.moveToNext());
        }

        return c;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

}
