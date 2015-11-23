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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import conectguitar.music.tcc.com.conectguitar.Controller.ServiceHandler;
import conectguitar.music.tcc.com.conectguitar.Model.DatabaseHelper;
import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class Display extends AppCompatActivity {

    ImageButton guitarrista5;
    MediaPlayer mediaPlayer;
    DatabaseHelper helper = new DatabaseHelper(this);

    TextView students_Id, students_Name, students_Lesson, students_Filename, students_NStage, students_NLesson, students_ID;

    Button btnStudents;
    Button btnMedia;
    ImageButton btnInterrogation;
    private ProgressDialog pDialog;
    ListView lv;
    boolean isPlaying = false;
    private static final String TAG_USERS = "users";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ORIGINAL_FILENAME = "original_filename";
    private static final String TAG_FILENAME = "filename";
    private static final String TAG_STUDENT_ID = "student_id";
    private static final String TAG_STAGE = "stage";
    private static final String TAG_LESSON = "lesson";
    private static String downloadFilePath;
    private static String file_url = "http://conectguitarws-conectguitar.rhcloud.com/audiosconectguitar/";
    private int idteacher=0;
    private int studentsIdRelease=0;
    private static String url = "http://conectguitarws-conectguitar.rhcloud.com/fileentry/avaliation/";
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
    JSONArray users = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> studentsList;

    TextView txt1, txt2, txt3, txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        //int idteacher=0;
        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        idteacher = globalVar.getInt("idteacher", 0);*/

        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Melody MakerNotesOnly.ttf");
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Pleasewritemeasong.ttf");
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Raggedways Regular.otf");
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Wolfganger.otf");
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/BeautifulEveryTime.ttf");

        Intent intent = getIntent();
        usertype = 0;
        idteacher = intent.getIntExtra("idteacher", 0);
        id = intent.getIntExtra("id", 0);
        student_id = intent.getIntExtra("student_id", 0);
        usertype = intent.getIntExtra("usertype", 0);
        idrelease = intent.getIntExtra("idrelease", 0);
        name = intent.getStringExtra("name");

        lv = (ListView) findViewById(R.id.list);
        guitarrista5 = (ImageButton)findViewById(R.id.guitarrista5);
        btnStudents = (Button)findViewById(R.id.btnStudents);
        btnMedia = (Button)findViewById(R.id.btnMedia);
        btnInterrogation = (ImageButton)findViewById(R.id.btnInterrogation);
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt3 = (TextView)findViewById(R.id.txt3);
        txt4 = (TextView)findViewById(R.id.txt4);
        btnStudents.setTypeface(custom_font);
        btnMedia.setTypeface(custom_font);
        /*txt1.setTypeface(custom_font);
        txt2.setTypeface(custom_font);
        txt3.setTypeface(custom_font);*/
        txt4.setTypeface(custom_font);
        /*students_Id.setTypeface(custom_font);
        students_Name.setTypeface(custom_font);
        students_Lesson.setTypeface(custom_font);
        students_Filename.setTypeface(custom_font);
        students_NStage.setTypeface(custom_font);
        students_NLesson.setTypeface(custom_font);
        students_ID.setTypeface(custom_font);*/

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
                students_Id = (TextView) view.findViewById(R.id.students_Id);
                students_Name = (TextView) view.findViewById(R.id.students_name);
                students_Lesson = (TextView) view.findViewById(R.id.students_lesson);
                students_Filename = (TextView) view.findViewById(R.id.students_filename);
                students_NStage = (TextView) view.findViewById(R.id.students_nstage);
                students_NLesson = (TextView) view.findViewById(R.id.students_nlesson);
                students_ID = (TextView) view.findViewById(R.id.students_ID);

                audio_id = students_Id.getText().toString();
                students_id = students_ID.getText().toString();
                String studentsName = students_Name.getText().toString();
                studentsLesson = students_Lesson.getText().toString();
                original_filename = students_Lesson.getText().toString();
                filename = students_Filename.getText().toString();
                stage = students_NStage.getText().toString();
                lesson = students_NLesson.getText().toString();




                /*Intent objIndent = new Intent(getApplicationContext(), StudentsRelease.class);
                objIndent.putExtra("students_Id", Integer.parseInt(studentsId));
                objIndent.putExtra("students_Name", studentsName);
                objIndent.putExtra("students_Lesson", Integer.parseInt(studentsLesson));
                startActivity(objIndent);*/

                new DownloadFile().execute(file_url + filename);

                helper.insertAudioData(filename, original_filename, stage, lesson, students_id);


            }
        });

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

                intent = new Intent(Display.this, UpdateUser.class);
                intent.putExtra("student_id", student_id);
                startActivity(intent);

            case R.id.item2:


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Display.this);



                alertDialogBuilder.setTitle(this.getTitle() + " decision");

                alertDialogBuilder.setMessage("Deseja realmente deletar a conta?");

                // set positive button: Yes message

                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        new DeleteUserAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/" + student_id);

                        Intent i = new Intent(Display.this, MainActivity.class);
                        startActivity(i);


                    }

                });

                alertDialogBuilder.setNegativeButton("Não",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(getApplicationContext(),"Cancelado!",Toast.LENGTH_LONG).show();

                    }

                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                // show alert
                alertDialog.show();

                return true;

            case R.id.item3:

                intent = new Intent(Display.this, UpdatePassword.class);
                intent.putExtra("student_id", student_id);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Interrogation(View view) {

        //Toast interrogation = Toast.makeText(AudioAppActivity.this, "To record or listen to a recorded audio, click Record, only to hear the lesson click Listen!", Toast.LENGTH_LONG);
        //Toast interrogation = Toast.makeText(Display.this, "Para gravar ou ouvir um Audio, Clique em Gravar, para somente ouvir uma lição, clique eu Ouvir!", Toast.LENGTH_LONG);
        //interrogation.show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Display.this);
        alertDialogBuilder.setTitle(" ConectGuitar Tutorial");
        alertDialogBuilder.setMessage("Clique em Aluno para ver o seus alunos ou em Media para ouvir todas as aulas disponíveis. Clique em Media para ouvir todas as aulas disponíveis. Abaixo está a lista de audios recentes enviados por seus alunos. Após voce baixar o audio ele não irá mais aparecer aqui neste tela!");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

            }

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();

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
            Intent intent = new Intent(Display.this, Stage.class);
            intent.putExtra("id", id);
            intent.putExtra("student_id", student_id);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            intent.putExtra("idteacher", idteacher);
            startActivity(intent);
        }

        if(v.getId()==R.id.btnStudents) {
            Intent intent = new Intent(Display.this, Students.class);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            intent.putExtra("idteacher", idteacher);
            intent.putExtra("id", id);
            startActivity(intent);
        }

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Display.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url + id, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    users = jsonObj.getJSONArray(TAG_USERS);
                    studentsList = new ArrayList<HashMap<String, String>>();
                    // looping through All Contacts
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String original_filename = c.getString(TAG_ORIGINAL_FILENAME);
                        String filename = c.getString(TAG_FILENAME);
                        String student_id = c.getString(TAG_STUDENT_ID);
                        String stage = c.getString(TAG_STAGE);
                        String lesson = c.getString(TAG_LESSON);

                        //studentsList = new ArrayList<HashMap<String, String>>();

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_ORIGINAL_FILENAME, original_filename);
                        contact.put(TAG_FILENAME, filename);
                        contact.put(TAG_STUDENT_ID, student_id);
                        contact.put(TAG_STAGE, stage);
                        contact.put(TAG_LESSON, lesson);

                        // adding contact to contact list
                        studentsList.add(contact);
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

            ListAdapter adapter = new SimpleAdapter(Display.this, studentsList, R.layout.display_entry, new String[]{"id", "name", "original_filename", "filename", "stage", "lesson", "student_id"}, new int[]{R.id.students_Id, R.id.students_name, R.id.students_lesson, R.id.students_filename, R.id.students_nstage, R.id.students_nlesson, R.id.students_ID});
            lv.setAdapter(adapter);

        }

    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Display.this);
            pDialog.setMessage("Baixando Audio...");
            pDialog.setCancelable(false);
            pDialog.show();
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
                                Environment.DIRECTORY_MUSIC) + "/AudiosDownloads/" + filename);


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
            if (pDialog.isShowing())
                pDialog.dismiss();

            new AvaliatedAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/fileentry/" + audio_id);

            //helper.insertAudioData(filename, original_filename, stage, lesson, students_id);

            /*AudiosConectGuitar a = new AudiosConectGuitar();
            a.setStudent_Id(students_id);
            a.setStage(Integer.parseInt(stage));
            a.setLesson(Integer.parseInt(lesson));
            a.setFilename(filename);
            a.setOriginal_Filename(original_filename);*/


        }
    }

    //avaliation
    private class AvaliatedAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Display.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            //user = new User();
            //user.setIdRelease(newIdRelease);

            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPut httpPUT;
                httpPUT = new HttpPut("http://conectguitarws-conectguitar.rhcloud.com/fileentry/" + audio_id);
                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("avaliation", "1");

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

    private class DeleteUserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Display.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            URL url = null;
            try {
                url = new URL("http://conectguitarws-conectguitar.rhcloud.com/users/" + student_id);
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
                Toast.makeText(getBaseContext(), "Usuário deletado com sucesso!", Toast.LENGTH_LONG).show();
            //} else {
            //    Toast.makeText(getBaseContext(), "Erro ao deletar usuário!", Toast.LENGTH_LONG).show();
            //}
        }
    }

    public void solo1(View view) {

        if (isPlaying) {
            isPlaying = false;
            mediaPlayer.stop();
            mediaPlayer.release();
        } else {
            isPlaying = true;
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor = null;
            try {
                descriptor = Display.this.getAssets().openFd("solo1.mp3");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /*private class DownloadFile extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        /*@Override
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

                audioFilePath =
                        (Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + stage + "/" + "Lesson" + lesson + "/" + original_filename);
                //Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + stage + "/" + "Lesson" + lesson + "/" + "Lesson" + lesson + ".mp3");

                // Output stream to write file
                //OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                OutputStream output = new FileOutputStream(audioFilePath);
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
        /*protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
       /* @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }*/

}





