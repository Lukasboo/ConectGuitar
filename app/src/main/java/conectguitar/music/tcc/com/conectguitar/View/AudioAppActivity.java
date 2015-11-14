package conectguitar.music.tcc.com.conectguitar.View;

/**
 * Created by lucas on 06/10/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import conectguitar.music.tcc.com.conectguitar.R;

public class AudioAppActivity extends Activity implements View.OnClickListener {

    private static MediaPlayer mediaPlayer;
    private static MediaRecorder mediaRecorder;
    private Handler durationHandler = new Handler();

    private static String audioFilePath;
    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private boolean isRecording = false;

    EditText edLesson, edStudent;
    CheckBox chkRecord, chkListen;
    ImageButton btnInterrogation;
    Button btnSendAudio;

    private static ImageButton stopButton;
    private static ImageButton playButton;
    private static ImageButton recordButton;
    private ProgressDialog pDialog;
    private SeekBar seekbar;
    public TextView songName, duration;


    int serverResponseCode = 0;
    private int nstage =0, nLesson =0, forinit=0, forend=0, student_id=0;
    private String username, name;

    ProgressDialog dialog = null;
    private static final int SELECT_AUDIO = 2;
    String selectedPath = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_app);


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        username = null;
        nLesson = 0;
        Intent intent = getIntent();
        student_id =intent.getIntExtra("student_id", 0);
        nstage =intent.getIntExtra("nstage", 0);
        nLesson = intent.getIntExtra("nLesson", 0);
        name = intent.getStringExtra("name");
        //name = getIntent().getStringExtra("name");

        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        name = globalVar.getString("name", null);*/
        /*nstage = globalVar.getInt("nstage", 0);
        nLesson = globalVar.getInt("nLesson", 0);
        username = globalVar.getString("name", null);*/

        edLesson = (EditText)findViewById(R.id.edLesson);
        edStudent = (EditText)findViewById(R.id.edStudent);
        btnSendAudio = (Button)findViewById(R.id.btnSendAudio);

        edStudent.setText("Student: " + username);
        edLesson.setText("Stage " + nstage + " - Lesson Numb. " + nLesson);

        chkRecord = (CheckBox)findViewById(R.id.chkRecord);
        chkListen = (CheckBox)findViewById(R.id.chkListen);
        btnInterrogation = (ImageButton)findViewById(R.id.btnInterrogation);



        btnSendAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendAudioAsyncTask().execute();
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        recordButton = (ImageButton) findViewById(R.id.recordButton);
        playButton = (ImageButton) findViewById(R.id.playButton);
        stopButton = (ImageButton) findViewById(R.id.stopButton);

        if (!hasMicrophone())
        {
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            //playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }

        //getAlbumStoragePublicDir("Lesson");
        getAlbumStorageDownloadDir("AudiosDownloads");
        getAlbumStorageDir("ConectGuitarAudios");
        getAlbumStorageSubDir("Stage");
        getAlbumStorageSubsubDir("Lesson");


        audioFilePath =
                (Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + nstage + "/" + "Lesson" + nLesson + "/" + "Lesson" + nLesson + ".mp3");




        /*audioFilePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()
                       + "/sample_song.3gp";*/
        //+ "/myaudio.3gp";


        //mediaPlayer = MediaPlayer.create(this, R.raw.sample_song);
        //finalTime = mediaPlayer.getDuration();

    }

    //SEND AUDIO, FUNCIONA MAS PROVAVELMENTE CORROMPE O AUDIO
    private class SendAudioAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AudioAppActivity.this);
            pDialog.setMessage("Uploading Audio...Wait for it...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {


            String upLoadServerUri = "http://conectguitarws-conectguitar.rhcloud.com/fileentry/add/" + student_id + "/" + nstage + "/" + nLesson ;
            //String upLoadServerUri = "http://http://localhost:8000/conectguitarws/fileentry/add/" + student_id + "/" + nstage + "/" + nLesson ;
            String fileName = audioFilePath;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(audioFilePath);

            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File Does not exist");
                //return 0;
            }
            try { // open a URL connection
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("filefield", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"filefield\";filename=\""+ fileName + "\"" + lineEnd);
                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AudioAppActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                if (pDialog.isShowing())
                    pDialog.dismiss();
                ex.printStackTrace();
                Toast.makeText(AudioAppActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {

                if (pDialog.isShowing())
                    pDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(AudioAppActivity.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload Exception", "Exception : " + e.getMessage(), e);

            }

            if (pDialog.isShowing())
                pDialog.dismiss();

            return null;
        }


        protected void onPostExecute(Void serverResponseCode) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(getBaseContext(), "Dados enviados com sucesso!", Toast.LENGTH_LONG).show();

        }

    }

    

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
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

    public void recordAudio (View view) throws IOException
    {
        //chooseListenRec();

        isRecording = true;
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        recordButton.setImageResource(R.drawable.recordingbutton);
        stopButton.setImageResource(R.drawable.stopbuttonn);

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
        Toast recordmsg = Toast.makeText(AudioAppActivity.this, "Gravando...", Toast.LENGTH_LONG);
        recordmsg.show();
    }


    public void stopClicked (View view)
    {

        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        if (isRecording)  //Stop
        {
            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            recordButton.setImageResource(R.drawable.recordbutton);
            recordButton.setEnabled(true);
            stopButton.setImageResource(R.drawable.pausebutton);
            isRecording = false;

            Toast mediamsg = Toast.makeText(AudioAppActivity.this, "Recorded!", Toast.LENGTH_SHORT);
            mediamsg.show();

        } else { //Pause
            mediaPlayer.pause();
            playButton.setEnabled(true);
            recordButton.setEnabled(true);
            stopButton.setEnabled(true);
        }
    }

    public void playAudio (View view) throws IOException
    {

        //chooseListenRec();

        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);
        stopButton.setImageResource(R.drawable.pausebutton);

        /*AssetFileDescriptor audioFileDescriptor;
        audioFileDescriptor = getAssets().openFd("Lesson" + nLesson + ".mp3");
        mediaPlayer.setDataSource(audioFileDescriptor.getFileDescriptor(),
                audioFileDescriptor.getStartOffset(), audioFileDescriptor.getLength());*/

        if(chkListen.isChecked()) {

            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor = AudioAppActivity.this.getAssets().openFd("Lesson" + nLesson + ".mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
            descriptor.close();

        } else {
            audioFilePath =
                    (Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + nstage + "/" + "Lesson" + nLesson + "/" + "Lesson" + nLesson + ".mp3");

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);

        }




        //mediaPlayer = new MediaPlayer();
        //mediaPlayer.setDataSource(audioFilePath);
        //mediaPlayer.setDataSource();
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

    /*public void chooseListenRec() {

        if(chkListen.isChecked()) {
            /*audioFilePath =
                    (Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + nstage + "/" + "Lesson" + nLesson + "/" + "Lesson" + nLesson + ".mp3");*/

            //R.raw.sample_song


        /*} else {
            audioFilePath =
                    (Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_MUSIC) + "/ConectGuitarAudios/" + "Stage" + nstage + "/" + "Lesson" + nLesson + "/" + "RecLesson" + nLesson + ".mp3");
        }

    }*/

    public void listenCheckBox(View view) {

        chkListen.setChecked(true);
        chkRecord.setChecked(false);
        recordButton.setEnabled(false);

    }

    public void recordCheckBox(View view) {

        chkRecord.setChecked(true);
        chkListen.setChecked(false);
        recordButton.setEnabled(true);
    }

    public void Interrogation(View view) {

        //Toast interrogation = Toast.makeText(AudioAppActivity.this, "To record or listen to a recorded audio, click Record, only to hear the lesson click Listen!", Toast.LENGTH_LONG);
        Toast interrogation = Toast.makeText(AudioAppActivity.this, "Para gravar ou ouvir um Audio, Clique em Gravar, para somente ouvir uma lição, clique eu Ouvir!", Toast.LENGTH_LONG);
        interrogation.show();
    }


    @Override
    public void onClick(View v) {

    }



}


