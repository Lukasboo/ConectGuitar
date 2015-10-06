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
public class Stage extends Activity implements View.OnClickListener  {

    private int usertype=0, nstage=0, idrelease=0, id=0, student_id=0;
    private String name;
    Button btnStage1, btnStage2, btnStage3, btnStage4, btnStage5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage);

        usertype = 0;
        idrelease = 0;
        nstage = 0;
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        student_id = intent.getIntExtra("student_id", 0);
        usertype = intent.getIntExtra("usertype", 0);
        idrelease = intent.getIntExtra("idrelease", 0);
        name = intent.getStringExtra("name");

        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);
        usertype = globalVar.getInt("usertype", 0);
        idrelease = globalVar.getInt("idrelease", 0);*/

        btnStage1 = (Button)findViewById(R.id.btnStage1);
        btnStage1.setOnClickListener(this);

        btnStage2 = (Button)findViewById(R.id.btnStage2);
        btnStage2.setOnClickListener(this);
        if(usertype==0){
            if(idrelease<7){
                btnStage2.setEnabled(false);
            }
        } else {
            btnStage2.setEnabled(true);
        }

        btnStage3 = (Button)findViewById(R.id.btnStage3);
        btnStage3.setOnClickListener(this);
        if(usertype==0){
            if(idrelease<13){
                btnStage3.setEnabled(false);
            }
        } else {
            btnStage3.setEnabled(true);
        }

        btnStage4 = (Button)findViewById(R.id.btnStage4);
        btnStage4.setOnClickListener(this);
        if(usertype==0){
            if(idrelease<19){
                btnStage4.setEnabled(false);
            }
        } else {
            btnStage4.setEnabled(true);
        }

        btnStage5 = (Button)findViewById(R.id.btnStage5);
        btnStage5.setOnClickListener(this);
        if(usertype==0){
            if(idrelease<25){
                btnStage5.setEnabled(false);
            }
        } else {
            btnStage5.setEnabled(true);
        }

    }

    @Override
    public void onClick(View v) {

        // SharedPreferences.Editor editor = getSharedPreferences("globalVariables", MODE_PRIVATE).edit();
        Intent intent = new Intent(this, Lesson.class);

        if (v == findViewById(R.id.btnStage1)) {
            nstage=1;
            //ntent intent = new Intent(this, Lesson.class);
            //editor.putInt("nstage", 1);
            intent.putExtra("id", id);
            intent.putExtra("student_id", student_id);
            intent.putExtra("nstage", 1);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            //startActivity(intent);
        }

        if (v == findViewById(R.id.btnStage2)) {
            nstage=2;
            //Intent intent = new Intent(this, Lesson.class);
            //editor.putInt("nstage", 2);
            intent.putExtra("id", id);
            intent.putExtra("student_id", student_id);
            intent.putExtra("nstage", 2);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            //startActivity(intent);
        }

        if (v == findViewById(R.id.btnStage3)) {
            nstage=3;
            //Intent intent = new Intent(this, Lesson.class);
            //editor.putInt("nstage", 3);
            intent.putExtra("id", id);
            intent.putExtra("student_id", student_id);
            intent.putExtra("nstage", 3);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            //startActivity(intent);
        }

        if (v == findViewById(R.id.btnStage4)) {
            nstage=4;
            //Intent intent = new Intent(this, Lesson.class);
            //editor.putInt("nstage", 4);
            intent.putExtra("id", id);
            intent.putExtra("student_id", student_id);
            intent.putExtra("nstage", 4);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            //startActivity(intent);
        }

        if (v == findViewById(R.id.btnStage5)) {
            nstage=5;
            //Intent intent = new Intent(this, Lesson.class);
            //editor.putInt("nstage", 5);
            intent.putExtra("id", id);
            intent.putExtra("student_id", student_id);intent.putExtra("nstage", 5);
            intent.putExtra("usertype", usertype);
            intent.putExtra("idrelease", idrelease);
            intent.putExtra("name", name);
            //startActivity(intent);
        }

        //editor.commit();
        startActivity(intent);

    }

}

