package conectguitar.music.tcc.com.conectguitar.View;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.IOException;

import conectguitar.music.tcc.com.conectguitar.R;

public class SplashScreen extends Activity {

    public MediaPlayer mediaPlayerS = new MediaPlayer();
    AssetFileDescriptor descriptor = null;
    WebView myWebView;
    ImageView imglogotype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://media.giphy.com/media/XR0XVPSxuPliE/giphy.gif");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        imglogotype = (ImageView) findViewById(R.id.imglogotype);


        try {
            descriptor = SplashScreen.this.getAssets().openFd("solo3splash.mp3");
            mediaPlayerS.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayerS.prepare();
            mediaPlayerS.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread timerThread = new Thread() {
            public void run() {

                //int i;
                //for(i=0;i<2;i++) {

                try {
                    sleep(5100);
                            /*if(i==1) {
                            myWebView.setVisibility(View.GONE);
                            imglogotype.setImageResource(R.drawable.splash2);
                            imglogotype.getLayoutParams().width = 300;
                            imglogotype.getLayoutParams().height = 500;*/
                            /*mediaPlayerS.stop();
                            mediaPlayerS.release();*/
                    //}

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);

                }


            }
        };
        timerThread.start();

    }


}