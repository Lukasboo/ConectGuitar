package conectguitar.music.tcc.com.conectguitar.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import conectguitar.music.tcc.com.conectguitar.Model.DatabaseHelper;

/**
 * Created by lucas on 01/09/15.
 */
public class AudiosList {

    private DatabaseHelper dbHelper;

    public AudiosList(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    private ContentValues fillData(AudiosConectGuitar audios) {
        ContentValues values = new ContentValues();
        values.put(AudiosConectGuitar.COLUMN_ID,audios.getId());
        values.put(AudiosConectGuitar.COLUMN_STUDENT_ID,audios.getStudent_Id());
        values.put(AudiosConectGuitar.COLUMN_STAGE, audios.getStage());
        values.put(AudiosConectGuitar.COLUMN_LESSON, audios.getLesson());
        values.put(AudiosConectGuitar.COLUMN_FILENAME, audios.getFilename());
        values.put(AudiosConectGuitar.COLUMN_ORIGINAL_FILENAME, audios.getOriginal_Filename());
        return values;
    }





    public ArrayList<HashMap<String, String>> getAudiosList(int IdStudent) {

        SQLiteDatabase db = dbHelper.getReadableDatabase(); String selectQuery = "SELECT " +
                AudiosConectGuitar.COLUMN_ID + "," + AudiosConectGuitar.COLUMN_STUDENT_ID +  "," + AudiosConectGuitar.COLUMN_STAGE + "," + AudiosConectGuitar.COLUMN_LESSON + "," + AudiosConectGuitar.COLUMN_FILENAME + "," + AudiosConectGuitar.COLUMN_ORIGINAL_FILENAME +
                " FROM AudiosConectGuitar WHERE " + AudiosConectGuitar.COLUMN_STUDENT_ID + " = " + IdStudent;
        ArrayList<HashMap<String, String>> audiosList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> audios = new HashMap<String, String>();
                audios.put("id", cursor.getString(cursor.getColumnIndex(AudiosConectGuitar.COLUMN_ID)));
                audios.put("student_id", cursor.getString(cursor.getColumnIndex(AudiosConectGuitar.COLUMN_STUDENT_ID)));
                audios.put("stage", cursor.getString(cursor.getColumnIndex(AudiosConectGuitar.COLUMN_STAGE)));
                audios.put("lesson", cursor.getString(cursor.getColumnIndex(AudiosConectGuitar.COLUMN_LESSON)));
                audios.put("filename", cursor.getString(cursor.getColumnIndex(AudiosConectGuitar.COLUMN_FILENAME)));
                audios.put("original_filename", cursor.getString(cursor.getColumnIndex(AudiosConectGuitar.COLUMN_ORIGINAL_FILENAME)));
                audiosList.add(audios);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return audiosList;
    }
    
}
