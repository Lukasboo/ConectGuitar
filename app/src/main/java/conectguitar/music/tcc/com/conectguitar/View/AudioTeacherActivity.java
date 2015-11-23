package conectguitar.music.tcc.com.conectguitar.View;

/**
 * Created by lucas on 06/10/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import conectguitar.music.tcc.com.conectguitar.Controller.Feedback;
import conectguitar.music.tcc.com.conectguitar.R;

public class AudioTeacherActivity extends Activity implements View.OnClickListener {

    private static MediaPlayer mediaPlayer;
    private static MediaRecorder mediaRecorder;
    private Handler durationHandler = new Handler();

    private static String audioFilePath;
    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private boolean isRecording = false;
    String url;

    EditText edLesson, edStudent, edfeedback;
    CheckBox chkRecord, chkListen;
    ImageButton btnInterrogation;
    Button btnSendFeedback;


    private static ImageButton stopButton;
    private static ImageButton playButton;
    private static ImageButton recordButton;
    private ProgressDialog pDialog;
    private SeekBar seekbar;
    public TextView songName, duration, txt1;


    int serverResponseCode = 0;
    private int stage =0, lesson =0, forinit=0, forend=0, student_id=0;
    private String name, filename, original_filename;

    ProgressDialog dialog = null;
    private static final int SELECT_AUDIO = 2;
    String selectedPath = "";

    Feedback feedback;
    String feedbackstr;
    String messagestr;
    String data_completa;
    //String data_completastr;

    String downloadFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_teacher);


        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Melody MakerNotesOnly.ttf");
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Wolfganger.otf");

        Intent intent = getIntent();
        student_id =intent.getIntExtra("student_id", 0);
        //stage =intent.getIntExtra("stage", 0);
        //lesson = intent.getIntExtra("lesson", 0);
        filename = intent.getStringExtra("filename");
        original_filename = intent.getStringExtra("original_filename");
        name = getIntent().getStringExtra("name");

        url = "http://conectguitarws-conectguitar.rhcloud.com/feedbacks";


        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        name = globalVar.getString("name", null);*/
        /*nstage = globalVar.getInt("nstage", 0);
        nLesson = globalVar.getInt("nLesson", 0);
        username = globalVar.getString("name", null);*/

        txt1 = (TextView)findViewById(R.id.txt1);
        edLesson = (EditText)findViewById(R.id.edLesson);
        edStudent = (EditText)findViewById(R.id.edStudent);
        btnSendFeedback = (Button)findViewById(R.id.btnSendFeedback);

        edStudent.setText("Aluno: " + name);
        edLesson.setText("Etapa " + stage + " - Lição n. " + lesson);

        btnInterrogation = (ImageButton)findViewById(R.id.btnInterrogation);
        edfeedback= (EditText)findViewById(R.id.edfeedback);
        txt1.setTypeface(custom_font);
        edLesson.setTypeface(custom_font);
        edStudent.setTypeface(custom_font);
        btnSendFeedback.setTypeface(custom_font);

        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedbackstr = edfeedback.getText().toString();
                //getDate();
                new SendFeedbackAsyncTask().execute(url);


            }
        });


        }



    @Override
    protected void onStart() {
        super.onStart();

        playButton = (ImageButton) findViewById(R.id.playButton);
        stopButton = (ImageButton) findViewById(R.id.stopButton);

        downloadFilePath =
                (Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC) + "/AudiosDownloads/" + filename);


    }

    /*public String getDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm-dd-MM-yyyy");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        data_completa = dateFormat.format(data_atual);

        return data_completa;

    }*/

    public static String POST(String url, Feedback feedback){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("student_id", feedback.getStudent_Id());
            jsonObject.accumulate("message", feedback.getMessage());
            //jsonObject.accumulate("stage", feedback.getStage());
            //jsonObject.accumulate("lesson", feedback.getLesson());
            //jsonObject.accumulate("created_at", feedback.getCreated_at());
            //jsonObject.accumulate("updated_at", feedback.getUpdated_at());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

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

        // 11. return result
        return result;
    }

    private class SendFeedbackAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            feedback = new Feedback();
            feedback.setStudent_Id(student_id);
            feedback.setMessage(feedbackstr);
            feedback.setOriginal_Filename(original_filename);
            //feedback.setStage(stage);
            //feedback.setLesson(lesson);
            //feedback.setCreated_at(data_completa);
            //feedback.setUpdated_at(data_completa);

            return POST(urls[0],feedback);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Feedback enviado com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void stopClicked (View view)
    {

            mediaPlayer.pause();
            playButton.setEnabled(true);
            stopButton.setEnabled(false);

    }

    public void playAudio (View view) throws IOException
    {

        //chooseListenRec();

        playButton.setEnabled(false);
        stopButton.setEnabled(true);
        stopButton.setImageResource(R.drawable.pausebutton);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(downloadFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setMax((int) finalTime);
        seekbar.setProgress((int) timeElapsed);
        timeElapsed = mediaPlayer.getCurrentPosition();
        durationHandler.postDelayed(updateSeekBarTime, 100);

    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {

            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();
            //set seekbar progress
            seekbar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);

        }
    };

    public void rewind(View view) {

        //check if we can go forward at forwardTime seconds before song endes
        if ((timeElapsed + forwardTime) <= finalTime) {

            timeElapsed = timeElapsed + forwardTime;
            //seek to the exact second of the track
            mediaPlayer.seekTo((int) timeElapsed);

        }
    }

    // go backwards at backwardTime seconds
    public void forward(View view) {

        //check if we can go back at backwardTime seconds after song starts
        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;
            //seek to the exact second of the track
            mediaPlayer.seekTo((int) timeElapsed);

        }
    }

    public void Interrogation(View view) {

        //Toast interrogation = Toast.makeText(AudioAppActivity.this, "To record or listen to a recorded audio, click Record, only to hear the lesson click Listen!", Toast.LENGTH_LONG);
        //Toast interrogation = Toast.makeText(Display.this, "Para gravar ou ouvir um Audio, Clique em Gravar, para somente ouvir uma lição, clique eu Ouvir!", Toast.LENGTH_LONG);
        //interrogation.show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioTeacherActivity.this);
        alertDialogBuilder.setTitle("ConectGuitar Tutorial");
        alertDialogBuilder.setMessage("Ouça o audio do seu aluno, caso não aprove, envie o feedback a ele com o motivo dele não ser aprovado!");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

            }

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    @Override
    public void onClick(View v) {

    }



}


