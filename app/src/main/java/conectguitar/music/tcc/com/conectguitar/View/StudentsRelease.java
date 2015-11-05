package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import conectguitar.music.tcc.com.conectguitar.Controller.AudiosConectGuitar;
import conectguitar.music.tcc.com.conectguitar.Controller.AudiosList;
import conectguitar.music.tcc.com.conectguitar.Controller.User;
import conectguitar.music.tcc.com.conectguitar.Model.DatabaseHelper;
import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class StudentsRelease extends Activity {

    User user;
    private static MediaPlayer mediaPlayer;

    Button btnSave;
    Button btnShowProgress;
    ListView lv;
    EditText edStudent, edRelease;
    Spinner spinner;
    TextView lessons_filename;
    TextView lessons_original_filename;
    TextView lessons_nstage;
    TextView lessons_nLesson;
    TextView lessons_students_Id;
    ImageButton stopButton;
    ImageButton playButton;
    private ProgressDialog pDialog;

    JSONArray lessons = null;
    ArrayList<HashMap<String, String>> lessonList;

    DatabaseHelper helper = new DatabaseHelper(this);
    AudiosList audio = new AudiosList(this);
    public static final int progress_bar_type = 0;
    private static String audioFilePath;
    private int students_Id=0, students_IdRelease=0, idrelease=0, newIdRelease=0;
    private String students_Name, descriptionRelease;

    String url;
    String filename;
    String stage;
    String lesson;
    String original_filename;
    String student_id;
    private static String downloadFilePath;
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_ORIGINAL_FILENAME = "original_filename";
    private static final String TAG_FILENAME = "filename";
    private static final String TAG_STAGE = "stage";
    private static final String TAG_LESSON = "lesson";
    private static final String TAG_STUDENT_ID = "student_id";

    String id;
    /*String original_filename;
    String filename;
    String stage;
    String lesson;*/


    private boolean isPlaying = false;

    //private static String file_url = "http://conectguitarws-conectguitar.rhcloud.com/fileentry/get/";
    private static String file_url = "http://conectguitarws-conectguitar.rhcloud.com/audiosconectguitar/";
    //private static String file_url = "http://localhost:8000/conectguitarws/audiosconectguitar/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_release);

        edStudent = (EditText)findViewById(R.id.edStudent);
        edRelease = (EditText)findViewById(R.id.edRelease);
        //spinner = (Spinner)findViewById(R.id.spinner);
        btnSave = (Button)findViewById(R.id.btnSave);

        Intent intent = getIntent();
        students_Id = intent.getIntExtra("students_Id", 0);
        students_Name = intent.getStringExtra("students_Name");
        students_IdRelease = intent.getIntExtra("students_IdRelease", 0);

        edStudent.setText("Aluno:" + students_Name);

        edRelease.setText(helper.getStudentReleases(students_IdRelease));

        helper.getReleases();
        ArrayList<String> listReleases=helper.getReleases();
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.spinner, R.id.txtSpinner, listReleases);
        spinner.setAdapter(adapter);

        url = "http://conectguitarws-conectguitar.rhcloud.com/fileentry/";

        lv = (ListView) findViewById(R.id.lessonList);
        //helper.getLessons(student_id);
        //DatabaseHelper repo = new PedidoRepo(this);

        ArrayList<HashMap<String, String>> lessonList = audio.getAudiosList(students_Id);
        //ArrayList<HashMap<String, String>> lessonList = helper.getLesson(String.valueOf(students_Id));
        //ArrayList<String> lessonList=helper.getLessons(String.valueOf(students_Id));
        ListAdapter lessonAdapter = new SimpleAdapter(StudentsRelease.this, lessonList, R.layout.lessons_entry, new String[]{"id", "original_filename", "filename", "stage", "lesson", "student_id"}, new int[]{R.id.lessons_Id, R.id.lessons_original_filename, R.id.lessons_filename, R.id.lessons_nstage, R.id.lessons_nLesson, R.id.lessons_students_Id});
        lv.setAdapter(lessonAdapter);
        //spinner=(Spinner)findViewById(R.id.spinner);
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.spinner, R.id.txtSpinner, listReleases);
        //adapter = new ArrayAdapter<String>(this, R.layout.spinner, R.id.txtSpinner, lessonList);
        //spinner.setAdapter(adapter);
        //playButton = (ImageButton) findViewById(R.id.playButton);
        //stopButton = (ImageButton) findViewById(R.id.stopButton);
        //new GetLessons().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                lessons_filename = (TextView) view.findViewById(R.id.lessons_filename);
                lessons_original_filename = (TextView) view.findViewById(R.id.lessons_original_filename);
                lessons_nstage = (TextView) view.findViewById(R.id.lessons_nstage);
                lessons_nLesson = (TextView) view.findViewById(R.id.lessons_nLesson);
                lessons_students_Id = (TextView) view.findViewById(R.id.lessons_students_Id);
                filename = lessons_filename.getText().toString();
                original_filename = lessons_original_filename.getText().toString();
                student_id = lessons_students_Id.getText().toString();
                stage = lessons_nstage.getText().toString();
                lesson = lessons_nLesson.getText().toString();
                //new DownloadFileFromURL().execute(file_url);
                //new HttpAsyncTask().execute(file_url + lessons_filename);
                //new DownloadFile().execute(file_url + filename);

                //helper.insertAudioData(filename, original_filename, stage, lesson, student_id);


                try {
                    playAudio(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public String releaseSpinner() {

        String descriptionRelease = spinner.getSelectedItem().toString();
        return descriptionRelease;
    }

    public void onClick(View view) {

        descriptionRelease = releaseSpinner();
        idrelease = helper.searchIdRelease(descriptionRelease);
        newIdRelease = helper.searchIdRelease(descriptionRelease);

        User c = new User();
        c.setId(students_Id);
        c.setIdRelease(newIdRelease);

        new HttpAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/" + students_Id);

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(StudentsRelease.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            user = new User();
            user.setIdRelease(newIdRelease);

            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPut httpPUT;
                httpPUT = new HttpPut("http://conectguitarws-conectguitar.rhcloud.com/teachers/" + students_Id);
                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("release_id", newIdRelease);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPUT.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPUT.setHeader("Accept", "application/json");
                httpPUT.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPUT);
                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // 10. convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return "EXITO!";


        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_LONG).show();

        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    /*private class GetLessons extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(StudentsRelease.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + students_Id, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    lessons = jsonObj.getJSONArray(TAG_USERS);
                    lessonList = new ArrayList<HashMap<String, String>>();
                    // looping through All Contacts
                    for (int i = 0; i < lessons.length(); i++) {
                        JSONObject c = lessons.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String original_filename = c.getString(TAG_ORIGINAL_FILENAME);
                        String filename = c.getString(TAG_FILENAME);
                        String stage = c.getString(TAG_STAGE);
                        String lesson = c.getString(TAG_LESSON);
                        String student_id = c.getString(TAG_STUDENT_ID);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_ORIGINAL_FILENAME, original_filename);
                        contact.put(TAG_FILENAME, filename);
                        contact.put(TAG_STAGE, stage);
                        contact.put(TAG_LESSON, lesson);
                        contact.put(TAG_STUDENT_ID, student_id);


                        // adding contact to contact list
                        lessonList.add(contact);
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

            //ListAdapter adapter = new SimpleAdapter(StudentsRelease.this, lessonList, R.layout.lessons_entry, new String[]{"id", "original_filename", "filename", "stage", "lesson", "playButton", "stopButton" }, new int[]{R.id.lessons_Id, R.id.lessons_original_filename, R.id.lessons_filename, R.id.lessons_nstage, R.id.lessons_nLesson, R.id.playButton, R.id.stopButton});
            /*ListAdapter adapter = new SimpleAdapter(StudentsRelease.this, lessonList, R.layout.lessons_entry, new String[]{"id", "original_filename", "filename", "stage", "lesson", "student_id"}, new int[]{R.id.lessons_Id, R.id.lessons_original_filename, R.id.lessons_filename, R.id.lessons_nstage, R.id.lessons_nLesson, R.id.lessons_students_Id});
            lv.setAdapter(adapter);

        }

    }*/

    private class DownloadFile extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                /*audioFilePath =
                        (Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + stage + "/" + "Lesson" + lesson + "/" + original_filename);*/
                                //Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + stage + "/" + "Lesson" + lesson + "/" + "Lesson" + lesson + ".mp3");

                downloadFilePath =
                        (Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_MUSIC) + "/AudiosDownloads/" + filename + ".mp3");


                // Output stream to write file
                //OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                //OutputStream output = new FileOutputStream(audioFilePath);
                OutputStream output = new FileOutputStream(downloadFilePath);
//                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + stage + "/" + "Lesson" + lesson);




                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            AudiosConectGuitar a = new AudiosConectGuitar();
            a.setStudent_Id(students_Id);
            a.setStage(Integer.parseInt(stage));
            a.setLesson(Integer.parseInt(lesson));
            a.setFilename(filename);
            a.setOriginal_Filename(original_filename);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

   /* public void playAudio (String filename) throws IOException
    {


        String url = "http://conectguitarws-conectguitar.rhcloud.com/audiosconectguitar/" + filename;
        final MediaPlayer mediaPlayer = new MediaPlayer();
        // Set type to streaming
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // Listen for if the audio file can't be prepared
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // ... react appropriately ...
                // The MediaPlayer has moved to the Error state, must be reset!
                return false;
            }
        });
        // Attach to when audio file is prepared for playing
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        // Set the data source to the remote URL
        mediaPlayer.setDataSource(url);
        // Trigger an async preparation which will file listener when completed
        mediaPlayer.prepareAsync();

        /*mediaPlayer = new MediaPlayer();
        AssetFileDescriptor descriptor = StudentsRelease.this.getAssets().openFd("Lesson" + nLesson + ".mp3");
        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
        descriptor.close();*/
        /*if (isPlaying) {
            isPlaying = false;
            mediaPlayer.stop();
            //mediaPlayer.release();

        }
        else {
            isPlaying = true;
            mediaPlayer = new MediaPlayer();
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource("http://conectguitarws-conectguitar.rhcloud.com/audiosconectguitar/" + filename);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }

    }*/

    public void playAudio (String filename) throws IOException
    {

         if (isPlaying) {
            isPlaying = false;
            mediaPlayer.stop();
            //mediaPlayer.release();
             Toast.makeText(getBaseContext(), "Parando...", Toast.LENGTH_LONG).show();

        } else {

            isPlaying = true;

            audioFilePath =
                    (Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC) + "/AudiosDownloads/" + filename);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(getBaseContext(), "Tocando...", Toast.LENGTH_LONG).show();

         }

    }


}

