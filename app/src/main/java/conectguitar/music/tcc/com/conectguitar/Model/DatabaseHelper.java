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

    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "ConectGuitar.db";

    private static final String TABLE_NAME = "release";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_STATUS = "status";
    private static final String TABLE_CREATE = "create table release (id integer primary key not null , " +
            " description text not null, status integer not null );";

    private static final String TABLE_NAME2 = "audiosconectguitar";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STAGE = "stage";
    public static final String COLUMN_LESSON = "lesson";
    public static final String COLUMN_FILENAME = "filename";
    public static final String COLUMN_ORIGINAL_FILENAME = "original_filename";

    private static final String TABLE_CREATE2 = "create table audiosconectguitar (id integer primary key not null , " +
            " student_id integer not null, stage integer not null, lesson integer not null, filename text not null, original_filename text not null);";

    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE2);

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

    public void insertAudioData(String filename, String original_filename, String stage, String lesson, String students_id){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from audiosconectguitar";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_STUDENT_ID, Integer.parseInt(students_id));
        values.put(COLUMN_STAGE, Integer.parseInt(stage));
        values.put(COLUMN_LESSON, Integer.parseInt(lesson));
        values.put(COLUMN_FILENAME, filename);
        values.put(COLUMN_ORIGINAL_FILENAME, original_filename);

        db.insert(TABLE_NAME2 , null , values);

    }

    /*public ArrayList<HashMap<String, String>> getLessons() {

        SQLiteDatabase db = dbHelper.getReadableDatabase(); String selectQuery = "SELECT " +
                Pedido.COLUNA_ID + "," + Pedido.COLUNA_MESA + "," + Pedido.COLUNA_GARCOM + "," + Pedido.COLUNA_STATUS +
                " FROM " + dbHelper.TABELA_PEDIDO;
        ArrayList<HashMap<String, String>> pedidoList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> pedido = new HashMap<String, String>();
                pedido.put("id", cursor.getString(cursor.getColumnIndex(Pedido.COLUNA_ID)));
                pedido.put("mesa", cursor.getString(cursor.getColumnIndex(Pedido.COLUNA_MESA)));
                pedido.put("garcom", cursor.getString(cursor.getColumnIndex(Pedido.COLUNA_GARCOM)));
                pedido.put("status", cursor.getString(cursor.getColumnIndex(Pedido.COLUNA_STATUS)));
                pedidoList.add(pedido);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pedidoList;
    }*/


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        db.execSQL(query);
        db.execSQL(query2);
        this.onCreate(db);
    }

}
