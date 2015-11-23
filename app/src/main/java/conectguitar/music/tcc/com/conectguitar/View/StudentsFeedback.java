package conectguitar.music.tcc.com.conectguitar.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import conectguitar.music.tcc.com.conectguitar.Controller.ServiceHandler;
import conectguitar.music.tcc.com.conectguitar.Model.DatabaseHelper;
import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 20/11/15.
 */

    public class StudentsFeedback extends AppCompatActivity {
        //http://conectguitarws-conectguitar.rhcloud.com/feedbacks/20

        DatabaseHelper helper = new DatabaseHelper(this);

        TextView feedback_Id, students_Id, students_Name, students_Lesson, students_Filename, students_NStage, students_NLesson, students_ID;
        ImageButton guitarrista6;
        Button btnStudents;
        Button btnMedia;
        private ProgressDialog pDialog;
        ListView lv;

        private static final String TAG_FEEDBACKS = "feedbacks";
        private static final String TAG_ID = "id";
        private static final String TAG_ORIGINAL_FILENAME = "original_filename";
        private static final String TAG_MESSAGE = "message";
        private static String downloadFilePath;
        private int idteacher=0;
        private int studentsIdRelease=0;
        private static String url = "http://conectguitarws-conectguitar.rhcloud.com/feedbacks/";
        int id;
        int usertype=0;
        int idrelease=0;
        int student_id=0;
        String name;
        String audio_id;
        String students_id;
        String studentsLesson;
        String original_filename;
        String filename;
        String stage;
        String lesson;
        private int forinit=0, forend=0;
        //private static String url = "http://localhost:8000/fileentry/avaliation/";
        // users JSONArray
        JSONArray feedbacks = null;
        // Hashmap for ListView
        ArrayList<HashMap<String, String>> feedbackList;

        TextView txt1, txt2, txt3, txt4;

        String feedback_id;
        boolean isPlaying = false;
        MediaPlayer mediaPlayer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.students_feedback);
            //int idteacher=0;
        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        idteacher = globalVar.getInt("idteacher", 0);*/

            //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Melody MakerNotesOnly.ttf");
            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Wolfganger.otf");

            Intent intent = getIntent();
            usertype = 0;
            idteacher = intent.getIntExtra("idteacher", 0);
            id = intent.getIntExtra("id", 0);
            student_id = intent.getIntExtra("student_id", 0);
            usertype = intent.getIntExtra("usertype", 0);
            idrelease = intent.getIntExtra("idrelease", 0);
            name = intent.getStringExtra("name");

            lv = (ListView) findViewById(R.id.list);
            guitarrista6 = (ImageButton)findViewById(R.id.guitarrista6);
            btnStudents = (Button)findViewById(R.id.btnStudents);
            btnMedia = (Button)findViewById(R.id.btnMedia);
            /*txt1 = (TextView)findViewById(R.id.txt1);
            txt2 = (TextView)findViewById(R.id.txt2);*/
            txt3 = (TextView)findViewById(R.id.txt3);
            btnMedia.setTypeface(custom_font);
            /*txt1.setTypeface(custom_font);
            txt2.setTypeface(custom_font);*/
            txt3.setTypeface(custom_font);


        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);

        usertype = globalVar.getInt("usertype", 0);
        idteacher = globalVar.getInt("idteacher", 0);*/

            getAlbumStorageDownloadDir("AudiosDownloads");
            getAlbumStorageDir("ConectGuitarAudios");
            getAlbumStorageSubDir("Stage");
            getAlbumStorageSubsubDir("Lesson");

            new GetContacts().execute();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*students_Id = (TextView) view.findViewById(R.id.students_Id);
                    students_Name = (TextView) view.findViewById(R.id.students_name);
                    students_Lesson = (TextView) view.findViewById(R.id.students_lesson);
                    students_Filename = (TextView) view.findViewById(R.id.students_filename);
                    students_NStage = (TextView) view.findViewById(R.id.students_nstage);
                    students_NLesson = (TextView) view.findViewById(R.id.students_nlesson);
                    students_ID = (TextView) view.findViewById(R.id.students_ID);*/
                    feedback_Id = (TextView) view.findViewById(R.id.feedback_Id);

                    /*audio_id = students_Id.getText().toString();
                    students_id = students_ID.getText().toString();
                    String studentsName = students_Name.getText().toString();
                    studentsLesson = students_Lesson.getText().toString();
                    original_filename = students_Lesson.getText().toString();
                    filename = students_Filename.getText().toString();
                    stage = students_NStage.getText().toString();
                    lesson = students_NLesson.getText().toString();*/
                    feedback_id = feedback_Id.getText().toString();

                    new DeleteUserAsyncTask().execute(url + feedback_id);


                /*Intent objIndent = new Intent(getApplicationContext(), StudentsRelease.class);
                objIndent.putExtra("students_Id", Integer.parseInt(studentsId));
                objIndent.putExtra("students_Name", studentsName);
                objIndent.putExtra("students_Lesson", Integer.parseInt(studentsLesson));
                startActivity(objIndent);*/

                    //new DownloadFile().execute(file_url + filename);

                    //helper.insertAudioData(filename, original_filename, stage, lesson, students_id);


                }
            });

        }

    public void Interrogation(View view) {

        //Toast interrogation = Toast.makeText(AudioAppActivity.this, "To record or listen to a recorded audio, click Record, only to hear the lesson click Listen!", Toast.LENGTH_LONG);
        //Toast interrogation = Toast.makeText(Display.this, "Para gravar ou ouvir um Audio, Clique em Gravar, para somente ouvir uma lição, clique eu Ouvir!", Toast.LENGTH_LONG);
        //interrogation.show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentsFeedback.this);
        alertDialogBuilder.setTitle(" ConectGuitar Tutorial");
        alertDialogBuilder.setMessage("Aqui estão os feedbacks do seu professor em relação aos exercícios enviados a ele, clique no feedback desejado para exclui-lo!");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

            }

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();

    }

        public void solo2(View view) {

            if (isPlaying) {
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();
            } else {
                isPlaying = true;
                mediaPlayer = new MediaPlayer();
                AssetFileDescriptor descriptor = null;
                try {
                    descriptor = StudentsFeedback.this.getAssets().openFd("solo2.mp3");
                    mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                    descriptor.close();
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            Intent intent;

            switch (item.getItemId()) {
                case R.id.item1:

                    intent = new Intent(StudentsFeedback.this, UpdateUser.class);
                    intent.putExtra("student_id", student_id);
                    startActivity(intent);

                case R.id.item2:


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentsFeedback.this);



                    alertDialogBuilder.setTitle(this.getTitle() + " decision");

                    alertDialogBuilder.setMessage("Deseja realmente deletar a conta?");

                    // set positive button: Yes message

                    alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            new DeleteUserAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/" + student_id);

                            Intent i = new Intent(StudentsFeedback.this, MainActivity.class);
                            startActivity(i);


                        }

                    });

                    alertDialogBuilder.setNegativeButton("Não",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            Toast.makeText(getApplicationContext(), "Cancelado!", Toast.LENGTH_LONG).show();

                        }

                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show alert
                    alertDialog.show();

                    return true;

                case R.id.item3:

                    intent = new Intent(StudentsFeedback.this, UpdatePassword.class);
                    intent.putExtra("student_id", student_id);
                    startActivity(intent);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        public File getAlbumStorageDownloadDir(String albumName) {

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC), albumName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }

        public File getAlbumStorageDir(String albumName) {

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC), albumName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }

        public void getAlbumStorageSubDir(String SubalbumName) {

            for (int i = 1; i < 6; i++) {

                File folder = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" , SubalbumName + i  );
                // Get the directory for the user's public pictures directory.

                if (!folder.exists()) {
                    folder.mkdirs();
                }

            }

        }

        public void getAlbumStorageSubsubDir(String SubsubalbumName) {

            forinit = 1;
            forend = 6;

            for (int i = 1; i < 6; i++) {

                for (int y = forinit; y < forend; y++) {
                    File folder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + i + "/" , SubsubalbumName + y  );
                    // Get the directory for the user's public pictures directory.

                    if (!folder.exists()) {
                        folder.mkdir();


                        //File file = new File(getExternalFilesDir(null), folder);
                        try {
                            InputStream in = getAssets().open(SubsubalbumName + y + ".mp3");
                            OutputStream out = new FileOutputStream(folder + "/" + SubsubalbumName + y + ".mp3");
                            byte[] buffer = new byte[1024];
                            int read = in.read(buffer);
                            while (read != -1) {
                                out.write(buffer, 0, read);
                                read = in.read(buffer);
                            }
                            out.close();
                            in.close();
                        } catch (IOException e) {
                            //Log.e(e);
                        }


//Now copy is to os. I'd recommend using Apache Commons IO
                        //org.apache.commons.io.IOUtils.copy(is, os));


                    }
                    //return folder;

                }

                forinit = forend;
                forend = forinit + 5;
            }
        }

        public void onButtonClick(View v){

            if(v.getId()==R.id.btnMedia) {
                Intent i = new Intent(StudentsFeedback.this, Stage.class);
                i.putExtra("id", id);
                i.putExtra("student_id", student_id);
                i.putExtra("usertype", usertype);
                i.putExtra("idrelease", idrelease);
                i.putExtra("name", name);
                i.putExtra("idteacher", idteacher);
                startActivity(i);
            }

        }

        private class GetContacts extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(StudentsFeedback.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url + student_id, ServiceHandler.GET);

                Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        feedbacks = jsonObj.getJSONArray(TAG_FEEDBACKS);
                        feedbackList = new ArrayList<HashMap<String, String>>();
                        // looping through All Contacts
                        for (int i = 0; i < feedbacks.length(); i++) {
                            JSONObject c = feedbacks.getJSONObject(i);

                            String id = c.getString(TAG_ID);
                            String original_filename = c.getString(TAG_ORIGINAL_FILENAME);
                            String message = c.getString(TAG_MESSAGE);

                            //studentsList = new ArrayList<HashMap<String, String>>();

                            // tmp hashmap for single contact
                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put(TAG_ID, id);
                            contact.put(TAG_ORIGINAL_FILENAME, original_filename);
                            contact.put(TAG_MESSAGE, message);

                            // adding contact to contact list
                            feedbackList.add(contact);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
                /**
                 * Updating parsed JSON data into ListView
                 * */

                ListAdapter adapter = new SimpleAdapter(StudentsFeedback.this, feedbackList, R.layout.students_feedback_entry, new String[]{"id", "original_filename", "message"}, new int[]{R.id.feedback_Id, R.id.students_original_filename, R.id.students_message});
                lv.setAdapter(adapter);

            }

        }

        private class DeleteUserAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(StudentsFeedback.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... urls) {

                URL url = null;
                try {
                    url = new URL("http://conectguitarws-conectguitar.rhcloud.com/feedbacks/" + feedback_id);
                } catch (MalformedURLException exception) {
                    exception.printStackTrace();
                }
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    httpURLConnection.setRequestMethod("DELETE");
                    System.out.println(httpURLConnection.getResponseCode());
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }

                return null;

            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

                if (pDialog.isShowing())
                    pDialog.dismiss();

                //if(result.equals("EXITO!")) {
                Toast.makeText(getBaseContext(), "Feedback deletado com sucesso!", Toast.LENGTH_LONG).show();
                //} else {
                //    Toast.makeText(getBaseContext(), "Erro ao deletar usuário!", Toast.LENGTH_LONG).show();
                //}



            }
        }

    }





