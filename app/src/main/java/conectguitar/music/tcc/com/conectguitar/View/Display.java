package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class Display extends Activity {

    ImageButton btMedia;
    Button btnStudents;
    int id;
    int idteacher=0;
    int usertype=0;
    int idrelease=0;
    int student_id=0;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        btnStudents = (Button)findViewById(R.id.btnStudents);

        /*SharedPreferences globalVar = getSharedPreferences("globalVariables", MODE_PRIVATE);

        usertype = globalVar.getInt("usertype", 0);
        idteacher = globalVar.getInt("idteacher", 0);*/



        usertype = 0;
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        student_id = intent.getIntExtra("student_id", 0);
        usertype = intent.getIntExtra("usertype", 0);
        idrelease = intent.getIntExtra("idrelease", 0);
        idteacher = intent.getIntExtra("idteacher", 0);
        name = intent.getStringExtra("name");

        if(usertype==1) {
            btnStudents.setEnabled(true);
        } else {
            btnStudents.setEnabled(false);
        }


    }

    public void onButtonClick(View v){

        if(v.getId()==R.id.btMedia) {
            Intent i = new Intent(Display.this, Stage.class);
            i.putExtra("id", id);
            i.putExtra("student_id", student_id);
            i.putExtra("usertype", usertype);
            i.putExtra("idrelease", idrelease);
            i.putExtra("name", name);
            i.putExtra("idteacher", idteacher);
            startActivity(i);
        }

        if(v.getId()==R.id.btnStudents) {
            Intent i = new Intent(Display.this, Students.class);
            i.putExtra("usertype", usertype);
            i.putExtra("idrelease", idrelease);
            i.putExtra("name", name);
            i.putExtra("idteacher", idteacher);
            i.putExtra("id", id);
            startActivity(i);
        }

    }

}

