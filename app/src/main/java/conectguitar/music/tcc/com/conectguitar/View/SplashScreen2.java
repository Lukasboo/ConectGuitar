package conectguitar.music.tcc.com.conectguitar.View;

import android.content.Intent;
import android.os.Bundle;

import conectguitar.music.tcc.com.conectguitar.R;

public class SplashScreen2 extends SplashScreen {

    //public MediaPlayer mediaPlayerS;
    SplashScreen splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //mediaPlayer = new MediaPlayer();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(5100);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    //splashScreen.mediaPlayerS.stop();
                    //splashScreen.mediaPlayerS.release();
                    mediaPlayerS.stop();
                    mediaPlayerS.release();
                    Intent intent = new Intent(SplashScreen2.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }

}
