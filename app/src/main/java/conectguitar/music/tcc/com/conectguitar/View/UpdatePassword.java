package conectguitar.music.tcc.com.conectguitar.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import conectguitar.music.tcc.com.conectguitar.Controller.User;
import conectguitar.music.tcc.com.conectguitar.R;

/**
 * Created by lucas on 19/11/15.
 */
public class UpdatePassword extends Activity {

    User user;
    int student_id=0;
    ProgressDialog pDialog;
    private int newPassword =0;
    EditText pass1;
    EditText pass2;
    Button btnUpdatePassword;
    String pass1str;
    String pass2str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);

        Button btnUpdatePassword = (Button)findViewById(R.id.btnUpdatePassword);

        Intent intent = getIntent();
        student_id = intent.getIntExtra("student_id", 0);

    }

    public void onClick(View view) {

        EditText pass1 = (EditText)findViewById(R.id.TFpass1);
        EditText pass2 = (EditText)findViewById(R.id.TFpass2);

        pass1str = pass1.getText().toString();
        pass2str = pass2.getText().toString();

        if(!pass1str.equals(pass2str)){

            Toast pass = Toast.makeText(UpdatePassword.this , "As senhas estão com valores diferentes!" , Toast.LENGTH_SHORT);
            pass.show();

        }
        else{

            if(pass1str.equals("") || pass2str.equals("")) {
                Toast msg = Toast.makeText(UpdatePassword.this , "Valor Incorreto!!!" , Toast.LENGTH_SHORT);
                msg.show();
            }
            else {

                new NewReleaseAsyncTask().execute("http://conectguitarws-conectguitar.rhcloud.com/users/updatepassword/" + student_id);
                //new InsertUserAsyncTask().execute("http://localhost:8000/users");

                //Toast salvo = Toast.makeText(UpdatePassword.this, "Senha atualizada com sucesso!" , Toast.LENGTH_SHORT);
                //salvo.show();


            }

        }




    }

    private class NewReleaseAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(UpdatePassword.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPut httpPUT;
                httpPUT = new HttpPut("http://conectguitarws-conectguitar.rhcloud.com/users/updatepassword/" + student_id);
                String json = "";
                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("password", pass1str);

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

            Toast.makeText(getBaseContext(), "Senha atualizada com sucesso!", Toast.LENGTH_LONG).show();

            finish();

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
}
