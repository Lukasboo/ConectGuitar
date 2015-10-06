package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class Lesson extends Activity implements View.OnClickListener {

    Button btnLesson1, btnLesson2, btnLesson3, btnLesson4, btnLesson5;
    private int idrelease=0, nLesson=0, usertype=0, nstage=0, id=0, student_id;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson);

        usertype = 0;
        idrelease = 0;
        nstage = 0;
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        student_id = intent.getIntExtra("student_id", 0);
        nstage = intent.getIntExtra("nstage", 0);
        usertype = intent.getIntExtra("usertype", 0);
        idrelease = intent.getIntExtra("idrelease", 0);
        name = intent.getStringExtra("name");

        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        usertype = globalVar.getInt("usertype", 0);
        idrelease = globalVar.getInt("idrelease", 0);
        nstage = globalVar.getInt("nstage", 0);*/


        btnLesson1 = (Button)findViewById(R.id.btnLesson1);
        btnLesson1.setOnClickListener(this);

        //if(usertype==0){
        if (nstage==1) {
            if(idrelease>1){
                btnLesson1.setEnabled(true);
            } else {
                btnLesson1.setEnabled(false);
            }
        }

        if (nstage==2) {
            if(idrelease>6){
                btnLesson1.setEnabled(true);
            } else {
                btnLesson1.setEnabled(false);
            }
        }

        if (nstage==3) {
            if(idrelease>12){
                btnLesson1.setEnabled(true);
            } else {
                btnLesson1.setEnabled(false);
            }
        }

        if (nstage==4) {
            if(idrelease>18){
                btnLesson1.setEnabled(true);
            } else {
                btnLesson1.setEnabled(false);
            }
        }

        if (nstage==5) {
            if(idrelease>24){
                btnLesson1.setEnabled(true);
            } else {
                btnLesson1.setEnabled(false);
            }
        }

        //} else {
        //    btnLesson1.setEnabled(true);
        //}


        btnLesson2 = (Button)findViewById(R.id.btnLesson2);
        btnLesson2.setOnClickListener(this);

        if (nstage==1) {
            if(idrelease>2){
                btnLesson2.setEnabled(true);
            } else {
                btnLesson2.setEnabled(false);
            }
        }

        if (nstage==2) {
            if(idrelease>8){
                btnLesson2.setEnabled(true);
            } else {
                btnLesson2.setEnabled(false);
            }
        }

        if (nstage==3) {
            if(idrelease>14){
                btnLesson2.setEnabled(true);
            } else {
                btnLesson2.setEnabled(false);
            }
        }

        if (nstage==4) {
            if(idrelease>20){
                btnLesson2.setEnabled(true);
            } else {
                btnLesson2.setEnabled(false);
            }
        }

        if (nstage==5) {
            if(idrelease>26){
                btnLesson2.setEnabled(true);
            } else {
                btnLesson2.setEnabled(false);
            }
        }

        btnLesson3 = (Button)findViewById(R.id.btnLesson3);
        btnLesson3.setOnClickListener(this);

        if (nstage==1) {
            if(idrelease>3){
                btnLesson3.setEnabled(true);
            } else {
                btnLesson3.setEnabled(false);
            }
        }

        if (nstage==2) {
            if(idrelease>9){
                btnLesson3.setEnabled(true);
            } else {
                btnLesson3.setEnabled(false);
            }
        }

        if (nstage==3) {
            if(idrelease>15){
                btnLesson3.setEnabled(true);
            } else {
                btnLesson3.setEnabled(false);
            }
        }

        if (nstage==4) {
            if(idrelease>21){
                btnLesson3.setEnabled(true);
            } else {
                btnLesson3.setEnabled(false);
            }
        }

        if (nstage==5) {
            if(idrelease>27){
                btnLesson3.setEnabled(true);
            } else {
                btnLesson3.setEnabled(false);
            }
        }

        btnLesson4 = (Button)findViewById(R.id.btnLesson4);
        btnLesson4.setOnClickListener(this);
        //if(usertype==0){
        if (nstage==1) {
            if(idrelease>4){
                btnLesson4.setEnabled(true);
            } else {
                btnLesson4.setEnabled(false);
            }
        }

        if (nstage==2) {
            if(idrelease>10){
                btnLesson4.setEnabled(true);
            } else {
                btnLesson4.setEnabled(false);
            }
        }

        if (nstage==3) {
            if(idrelease>16){
                btnLesson4.setEnabled(true);
            } else {
                btnLesson4.setEnabled(false);
            }
        }

        if (nstage==4) {
            if(idrelease>22){
                btnLesson4.setEnabled(true);
            } else {
                btnLesson4.setEnabled(false);
            }
        }

        if (nstage==5) {
            if(idrelease>28){
                btnLesson4.setEnabled(true);
            } else {
                btnLesson4.setEnabled(false);
            }
        }

        btnLesson5 = (Button)findViewById(R.id.btnLesson5);
        btnLesson5.setOnClickListener(this);
        //if(usertype==0){
        if (nstage==1) {
            if(idrelease>4){
                btnLesson5.setEnabled(true);
            } else {
                btnLesson5.setEnabled(false);
            }
        }

        if (nstage==2) {
            if(idrelease>10){
                btnLesson5.setEnabled(true);
            } else {
                btnLesson5.setEnabled(false);
            }
        }

        if (nstage==3) {
            if(idrelease>16){
                btnLesson5.setEnabled(true);
            } else {
                btnLesson5.setEnabled(false);
            }
        }

        if (nstage==4) {
            if(idrelease>22){
                btnLesson5.setEnabled(true);
            } else {
                btnLesson5.setEnabled(false);
            }
        }

        if (nstage==5) {
            if(idrelease>28){
                btnLesson5.setEnabled(true);
            } else {
                btnLesson5.setEnabled(false);
            }
        }

        if(nstage==1) {
            btnLesson1.setText("Aula 1");
            btnLesson2.setText("Aula 2");
            btnLesson3.setText("Aula 3");
            btnLesson4.setText("Aula 4");
            btnLesson5.setText("Aula 5");
        }

        if(nstage==2) {
            btnLesson1.setText("Aula 6");
            btnLesson2.setText("Aula 7");
            btnLesson3.setText("Aula 8");
            btnLesson4.setText("Aula 9");
            btnLesson5.setText("Aula 10");
        }

        if(nstage==3) {
            btnLesson1.setText("Aula 11");
            btnLesson2.setText("Aula 12");
            btnLesson3.setText("Aula 13");
            btnLesson4.setText("Aula 14");
            btnLesson5.setText("Aula 15");
        }

        if(nstage==4) {
            btnLesson1.setText("Aula 16");
            btnLesson2.setText("Aula 17");
            btnLesson3.setText("Aula 18");
            btnLesson4.setText("Aula 19");
            btnLesson5.setText("Aula 20");
        }

        if(nstage==5) {
            btnLesson1.setText("Aula 21");
            btnLesson2.setText("Aula 22");
            btnLesson3.setText("Aula 23");
            btnLesson4.setText("Aula 24");
            btnLesson5.setText("Aula 25");
        }


        /*if(nstage==1) {
            btnLesson1.setText("Lesson 1");
            btnLesson2.setText("Lesson 2");
            btnLesson3.setText("Lesson 3");
            btnLesson4.setText("Lesson 4");
            btnLesson5.setText("Lesson 5");
        }

        if(nstage==2) {
            btnLesson1.setText("Lesson 6");
            btnLesson2.setText("Lesson 7");
            btnLesson3.setText("Lesson 8");
            btnLesson4.setText("Lesson 9");
            btnLesson5.setText("Lesson 10");
        }

        if(nstage==3) {
            btnLesson1.setText("Lesson 11");
            btnLesson2.setText("Lesson 12");
            btnLesson3.setText("Lesson 13");
            btnLesson4.setText("Lesson 14");
            btnLesson5.setText("Lesson 15");
        }

        if(nstage==4) {
            btnLesson1.setText("Lesson 16");
            btnLesson2.setText("Lesson 17");
            btnLesson3.setText("Lesson 18");
            btnLesson4.setText("Lesson 19");
            btnLesson5.setText("Lesson 20");
        }

        if(nstage==5) {
            btnLesson1.setText("Lesson 21");
            btnLesson2.setText("Lesson 22");
            btnLesson3.setText("Lesson 23");
            btnLesson4.setText("Lesson 24");
            btnLesson5.setText("Lesson 25");
        }*/


    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, AudioAppActivity.class);
        //SharedPreferences.Editor editor = getSharedPreferences("globalVariables", MODE_PRIVATE).edit();

        if (v == findViewById(R.id.btnLesson1)) {

            if (nstage==1) {
                intent.putExtra("nLesson", 1);
                //editor.putInt("nLesson", 1);
            }
            if (nstage==2) {
                intent.putExtra("nLesson", 6);
                //editor.putInt("nLesson", 6);
            }
            if (nstage==3) {
                intent.putExtra("nLesson", 11);
                //editor.putInt("nLesson", 11);
            }
            if (nstage==4) {
                intent.putExtra("nLesson", 16);
                //editor.putInt("nLesson", 16);
            }
            if (nstage==5) {
                intent.putExtra("nLesson", 21);
                //editor.putInt("nLesson", 21);
            }

        }

        if (v == findViewById(R.id.btnLesson2)){

            if (nstage==1) {
                intent.putExtra("nLesson", 2);
                //editor.putInt("nLesson", 2);
            }
            if (nstage==2) {
                intent.putExtra("nLesson", 7);
                //editor.putInt("nLesson", 7);
            }
            if (nstage==3) {
                intent.putExtra("nLesson", 12);
                //editor.putInt("nLesson", 12);
            }
            if (nstage==4) {
                intent.putExtra("nLesson", 17);
                //editor.putInt("nLesson", 17);
            }
            if (nstage==5) {
                intent.putExtra("nLesson", 22);
                //editor.putInt("nLesson", 22);
            }

        }

        if (v == findViewById(R.id.btnLesson3)){

            if (nstage==1) {
                intent.putExtra("nLesson", 3);
                //editor.putInt("nLesson", 3);
            }
            if (nstage==2) {
                intent.putExtra("nLesson", 8);
                //editor.putInt("nLesson", 8);
            }
            if (nstage==3) {
                intent.putExtra("nLesson", 13);
                //editor.putInt("nLesson", 13);
            }
            if (nstage==4) {
                intent.putExtra("nLesson", 18);
                //editor.putInt("nLesson", 18);
            }
            if (nstage==5) {
                intent.putExtra("nLesson", 23);
                //editor.putInt("nLesson", 23);
            }

        }

        if (v == findViewById(R.id.btnLesson4)){

            if (nstage==1) {
                intent.putExtra("nLesson", 4);
                //editor.putInt("nLesson", 4);
            }
            if (nstage==2) {
                intent.putExtra("nLesson", 9);
                //editor.putInt("nLesson", 9);
            }
            if (nstage==3) {
                intent.putExtra("nLesson", 14);
                //editor.putInt("nLesson", 14);
            }
            if (nstage==4) {
                intent.putExtra("nLesson", 19);
                //editor.putInt("nLesson", 19);
            }
            if (nstage==5) {
                intent.putExtra("nLesson", 24);
                //editor.putInt("nLesson", 24);
            }

        }

        if (v == findViewById(R.id.btnLesson5)){

            if (nstage==1) {
                intent.putExtra("nLesson", 5);
                //editor.putInt("nLesson", 5);
            }
            if (nstage==2) {
                intent.putExtra("nLesson", 10);
                //editor.putInt("nLesson", 10);
            }
            if (nstage==3) {
                intent.putExtra("nLesson", 15);
                //editor.putInt("nLesson", 15);
            }
            if (nstage==4) {
                intent.putExtra("nLesson", 20);
                //editor.putInt("nLesson", 20);
            }
            if (nstage==5) {
                intent.putExtra("nLesson", 25);
                //editor.putInt("nLesson", 25);
            }

        }

        intent.putExtra("id", id);
        intent.putExtra("student_id", student_id);
        intent.putExtra("usertype", usertype);
        intent.putExtra("idrelease", idrelease);
        intent.putExtra("nstage", nstage);
        intent.putExtra("name", name);

        startActivity(intent);

    }
}
