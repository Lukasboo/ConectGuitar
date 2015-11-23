package conectguitar.music.tcc.com.conectguitar.View;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 06/10/15.
 */
public class Stage extends Activity implements View.OnClickListener  {

    private int usertype=0, nstage=0, idrelease=0, id=0, student_id=0;
    private String name;
    Button btnStage1, btnStage2, btnStage3, btnStage4, btnStage5;
    ProgressDialog pDialog;

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

        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Melody MakerNotesOnly.ttf");
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Wolfganger.otf");

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

        btnStage1.setTypeface(custom_font);
        btnStage2.setTypeface(custom_font);
        btnStage3.setTypeface(custom_font);
        btnStage4.setTypeface(custom_font);
        btnStage5.setTypeface(custom_font);

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

                intent = new Intent(Stage.this, UpdateUser.class);
                intent.putExtra("student_id", student_id);
                startActivity(intent);

            case R.id.item2:


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Stage.this);



                alertDialogBuilder.setTitle(this.getTitle() + " decision");

                alertDialogBuilder.setMessage("Deseja realmente deletar a conta?");

                // set positive button: Yes message

                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        new DeleteUserAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/" + student_id);

                        Intent i = new Intent(Stage.this, MainActivity.class);
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

                intent = new Intent(Stage.this, UpdatePassword.class);
                intent.putExtra("student_id", student_id);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DeleteUserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Stage.this);
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

